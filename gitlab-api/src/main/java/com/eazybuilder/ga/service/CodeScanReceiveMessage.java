package com.eazybuilder.ga.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ga.constant.GuardType;
import com.eazybuilder.ga.constant.PriorityConstant;
import com.eazybuilder.ga.constant.ProjectType;
import com.eazybuilder.ga.pojo.cache.BranchCache;
import com.eazybuilder.ga.pojo.codeScan.CodeReport;
import com.eazybuilder.ga.pojo.codeScan.ScanData;
import com.eazybuilder.ga.pojo.codeScan.ScanTag;
import com.eazybuilder.ga.pojo.codeScan.Stage;
import com.eazybuilder.ga.pojo.rule.GroupRulePojo;
import com.eazybuilder.ga.pojo.rule.GuardPojo;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class CodeScanReceiveMessage {

    private static Logger logger = LoggerFactory.getLogger(CodeScanReceiveMessage.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${message.pipelineBroadcastExchange}")
    public String pipelineBroadcastExchange;

    @Autowired
    GitlabService gitlabService;

    @Bean
    public PipelineBroadcast pipelineBroadcast() {
        return new PipelineBroadcast(pipelineBroadcastExchange);
    }


    @RabbitListener(bindings = {
            @QueueBinding(value = @org.springframework.amqp.rabbit.annotation.Queue(value = "gitlab.sonarScan.queue"), exchange = @Exchange(type = ExchangeTypes.FANOUT, name = "#{pipelineBroadcast.exchange}")) })
    public void broadcastMsg(Message message, Channel channel) throws IOException {

        MessageHeaders headers = message.getHeaders();
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = "";
        if (null != headers.get("spring_returned_message_correlation")) {
            msgId = headers.get("spring_returned_message_correlation").toString();
        }else{
            msgId = headers.get("id").toString();
        }

        logger.info("消费者收到广播消息了：{},msgId:{},tag:{}", message.getPayload(), msgId, tag);

        // 当redis中存在此消息ID时,说明此消息已让消费过(用于幂等性的处理)
        if (redisTemplate.opsForHash().entries("gitlabMessageCache").containsKey(msgId)) {
            logger.info("[{}]消息已消费过", msgId);
            channel.basicAck(tag, false); // 手动确认消息已消费
            return;
        }

        logger.info("[{}]广播消息没有被消费过，继续后续流程", msgId);

        try{
            // 当redis中存在此消息ID时,说明此消息已让消费过(用于幂等性的处理)
            if (redisTemplate.opsForHash().entries("gitlabMessageCache").containsKey(msgId)) {
                logger.info("[{}]消息已消费过", msgId);
                channel.basicAck(tag, false);
            }

            logger.info("[{}]消息没有被消费过，继续后续流程,Payload type:{}", msgId,message.getPayload().getClass());

            JSONObject json;
            if (message.getPayload() instanceof String) {
                json = JSONObject.parseObject(String.valueOf(message.getPayload()));
            }else {
                json = (JSONObject) JSONObject.toJSON(message.getPayload());
            }
            logger.info("[{}]json", json);

            String devopsEventType = json.getString("devopsEventType");
            if(devopsEventType.equals("codeReport")){
                String projectType = json.getString("projectType");
                String pipelineStatus = json.getString("status");
                JSONArray reports = json.getJSONArray("reports");
                CodeReport codeReport = new CodeReport();
                codeReport.setProjectType(projectType);
                codeReport.setPipelineStatus(pipelineStatus);
                codeReport.setParseFlag(Boolean.FALSE);
                for(int i=0;i<reports.size();i++){
                    JSONObject report = reports.getJSONObject(i);
                    if("sonar".equals(report.getString("type"))){
                        String projectUrl = report.getString("projectUrl");
                        String branchName = report.getString("projectTagName");
                        String link = report.getString("link");
                        codeReport.setParseFlag(Boolean.TRUE);
                        codeReport.setProjectUrl(projectUrl);
                        codeReport.setLink(link);
                        codeReport.setBranchName(branchName);
                        JSONArray rows = report.getJSONObject("summary").getJSONArray("rows");
                        List<ScanData> scanDataList = new ArrayList<ScanData>();
                        for(int j=0;j<rows.size();j++){
                            JSONArray rowData = rows.getJSONArray(j);
                            ScanData scanData = new ScanData();
                            scanData.setType(rowData.getString(0));
                            scanData.setBlockerCount(rowData.getInteger(PriorityConstant.BLOCKER.getPriority()));
                            scanData.setCriticalCount(rowData.getInteger(PriorityConstant.CRITICAL.getPriority()));
                            scanData.setMajorCount(rowData.getInteger(PriorityConstant.MAJOR.getPriority()));
                            scanData.setMinorCount(rowData.getInteger(PriorityConstant.MINOR.getPriority()));
                            scanData.setInfoCount(rowData.getInteger(PriorityConstant.INFO.getPriority()));
                            scanDataList.add(scanData);
                        }
                        codeReport.setScanDataList(scanDataList);

                    }
                    if("project_analysis".equals(report.getString("type"))){
                        JSONArray analysisHeaders = report.getJSONObject("summary").getJSONArray("headers");
                        JSONArray rows = report.getJSONObject("summary").getJSONArray("rows");
                        JSONArray rowData = rows.getJSONArray(0);
                        HashMap<String,String> projectAnalysis = new HashMap<String,String>();
                        for(int j=0;j<analysisHeaders.size();j++){
                            if(j==1||j==2){
                                projectAnalysis.put(analysisHeaders.getString(j),rowData.getString(j)+"%");
                            }else{
                                projectAnalysis.put(analysisHeaders.getString(j),rowData.getString(j));
                            }
                            logger.info("key={},value={}",analysisHeaders.getString(j),rowData.getString(j)+"%");
                        }
                        codeReport.setProjectAnalysis(projectAnalysis);
                    }
                }
                JSONArray stages = json.getJSONArray("stages");
                List<Stage> stageList = new ArrayList<Stage>();
                for(int i=0;i<stages.size();i++) {
                    JSONObject stageJSONObject = stages.getJSONObject(i);
                    Stage stage = JSON.toJavaObject(stageJSONObject, Stage.class);
                    stageList.add(stage);
                }
                codeReport.setStageList(stageList);
                logger.info("codeReport={}",codeReport.toString());
                if(codeReport.getParseFlag()){
                    String projectUrl = codeReport.getProjectUrl();
                    String branchName = codeReport.getBranchName();
                    redisTemplate.opsForHash().put(projectUrl+":"+branchName, projectUrl+"-"+branchName, JSONObject.toJSONString(codeReport));

                    if (redisTemplate.opsForHash().entries(projectUrl+":"+branchName+":tag").containsKey(projectUrl+"-"+branchName)) {

                        Object value = redisTemplate.opsForHash().entries(projectUrl+":"+branchName+":tag").get(projectUrl+"-"+branchName);
                        ScanTag scanTag = JSONObject.parseObject(String.valueOf(value), ScanTag.class);

                        String body="";
                        String unitTestResult="";
                        Boolean unitFlag=Boolean.TRUE;

                        List<ScanData> scanDataList = codeReport.getScanDataList();
                        HashMap<String,String> projectAnalysis = codeReport.getProjectAnalysis();
                        if("FAILED".equals(pipelineStatus)){
                            body = "- 读取扫描报告失败\n";
                            body+="- 原因为ci流水线执行失败,该任务自动关闭\n";
                            gitlabService.createMRDiscussion(scanTag.getProjectId(),scanTag.getMergeId(), body);
                            gitlabService.updateMRState(scanTag.getProjectId(),scanTag.getMergeId(),"close");
                            return;
                        }


                        List<GuardPojo> guardList = null;
                        if (redisTemplate.opsForHash().entries("gitlabMessageCache").containsKey(scanTag.getGroupName().toLowerCase())) {
                            Object ruleValue = redisTemplate.opsForHash().entries("gitlabMessageCache").get(scanTag.getGroupName().toLowerCase());
                            GroupRulePojo groupRule = JSONObject.parseObject(String.valueOf(ruleValue), GroupRulePojo.class);
                            logger.info("#####################"+groupRule);
                            guardList = groupRule.getGuards();
                        }

                        if(null!=scanDataList&& scanDataList.size()>0) {
                            for(int t=0;t<scanDataList.size();t++){
                                ScanData scanData = scanDataList.get(t);
                                body+="- "+scanData.getType()+" 阻断："+scanData.getBlockerCount()
                                        +" 严重："+scanData.getCriticalCount()
                                        +" 主要："+scanData.getMajorCount()
                                        +" 次要："+scanData.getMinorCount()
                                        +" 提示："+scanData.getInfoCount()+"\n";

                                if("BUG".equals(scanData.getType())&&checkCode(projectType)){
                                    GuardPojo guard = checkGuardType(GuardType.bug_blocker,guardList);
                                    if(null!=guard){
                                        unitTestResult=unitTestResult+"- 当前BUG阻断数为"+scanData.getBlockerCount()
                                                +"  系统设定门禁值为"+guard.getThresholdMax();
                                        if(scanData.getBlockerCount()>guard.getThresholdMax()){
                                            unitTestResult=unitTestResult+"，不通过\n";
                                            unitFlag=Boolean.FALSE;
                                        }else{
                                            unitTestResult=unitTestResult+"，通过\n";
                                        }
                                    }
                                }else if("安全漏洞".equals(scanData.getType())&&checkCode(projectType)){
                                    GuardPojo guard = checkGuardType(GuardType.vulner_blocker,guardList);
                                    if(null!=guard) {
                                        unitTestResult = unitTestResult + "- 当前安全漏洞阻断数为" + scanData.getBlockerCount()
                                                + "  系统设定门禁值为" + guard.getThresholdMax();
                                        if (scanData.getBlockerCount() > guard.getThresholdMax()) {
                                            unitTestResult=unitTestResult+"，不通过\n";
                                            unitFlag = Boolean.FALSE;
                                        }else{
                                            unitTestResult=unitTestResult+"，通过\n";
                                        }
                                    }
                                }else if("编码规范/坏味道".equals(scanData.getType())&&checkCode(projectType)){
                                    GuardPojo guard = checkGuardType(GuardType.code_smell_blocker,guardList);
                                    if(null!=guard) {
                                        unitTestResult = unitTestResult + "- 当前坏味道阻断数为" + scanData.getBlockerCount()
                                                + "  系统设定门禁值为" + guard.getThresholdMax();
                                        if (scanData.getBlockerCount() > guard.getThresholdMax()) {
                                            unitTestResult=unitTestResult+"，不通过\n";
                                            unitFlag = Boolean.FALSE;
                                        }else{
                                            unitTestResult=unitTestResult+"，通过\n";
                                        }
                                    }
                                }
                            }
                            body+="- [查看详情]("+codeReport.getLink()+")\n";

                        }

                        if(null!=projectAnalysis&&projectAnalysis.size()>0){
                            Iterator<Map.Entry<String, String>> entries = projectAnalysis.entrySet().iterator();
                            Boolean newUnitTestCoverageRateFlag = true;
                            Integer newUncoveredLines=0;
                            String newUnitTestCoverageMessage="";
                            while (entries.hasNext()) {
                                Map.Entry<String, String> entry = entries.next();
                                if("单元测试成功率".equals(entry.getKey())&&checkUnit(projectType)){
                                    GuardPojo guard = checkGuardType(GuardType.unit_test_success_rate,guardList);
                                    if(null!=guard) {
                                        unitTestResult = unitTestResult + "- 当前单元测试成功率为" + entry.getValue()
                                                + "  系统设定门禁值为" + guard.getThresholdMax();
                                        if (Double.parseDouble(entry.getValue().replaceAll("%", "")) < guard.getThresholdMax()) {
                                            unitTestResult=unitTestResult+"，不通过\n";
                                            unitFlag = Boolean.FALSE;
                                        }else{
                                            unitTestResult=unitTestResult+"，通过\n";
                                        }
                                    }
                                }else if("单元测试覆盖率".equals(entry.getKey())&&checkUnit(projectType)){
                                    GuardPojo guard = checkGuardType(GuardType.unit_test_coverage_rate,guardList);
                                    if(null!=guard) {
                                        unitTestResult = unitTestResult + "- 当前单元测试覆盖率为" + entry.getValue()
                                                + "  系统设定门禁值为" + guard.getThresholdMax();
                                        if (Double.parseDouble(entry.getValue().replaceAll("%", "")) < guard.getThresholdMax()) {
                                            unitTestResult=unitTestResult+"，不通过\n";
                                            unitFlag = Boolean.FALSE;
                                        }else{
                                            unitTestResult=unitTestResult+"，通过\n";
                                        }
                                    }
                                }else if("新增单元测试覆盖率".equals(entry.getKey())&&checkUnit(projectType)){
                                    GuardPojo guard = checkGuardType(GuardType.new_unit_test_coverage_rate,guardList);
                                    if(null!=guard) {
                                        newUnitTestCoverageMessage = "- 当前新增单元测试覆盖率为" + entry.getValue()
                                                + "  系统设定门禁值为" + guard.getThresholdMax();
                                        if (Double.parseDouble(entry.getValue().replaceAll("%", "")) < guard.getThresholdMax()) {
                                            newUnitTestCoverageRateFlag = Boolean.FALSE;
                                        }
                                    }
                                }else if("新增未覆盖代码行数".equals(entry.getKey())&&checkUnit(projectType)){
                                    newUncoveredLines = Integer.parseInt(entry.getValue());
                                }
                                body+="- "+entry.getKey()+":"+entry.getValue()+"\n";
                            }
                            if (newUncoveredLines>0&&!newUnitTestCoverageRateFlag){
                                if(StringUtils.isNotBlank(newUnitTestCoverageMessage)){
                                    unitTestResult=unitTestResult+newUnitTestCoverageMessage+"  新增单元测试未覆盖行数"+newUncoveredLines+"，不通过\n";
                                }
                                unitFlag = Boolean.FALSE;
                            }else{
                                if(StringUtils.isNotBlank(newUnitTestCoverageMessage)){
                                    unitTestResult=unitTestResult+newUnitTestCoverageMessage+"  新增单元测试未覆盖行数"+newUncoveredLines+"，通过\n";
                                }
                            }
                        }
                        if(!unitFlag){
                            unitTestResult=unitTestResult+"- 已被系统强制拒绝，请相关人员查看。\n";
                        }
                        if(redisTemplate.opsForHash().entries("gitlab-branch-cache").containsKey(branchName)){
                            Object cache = redisTemplate.opsForHash().entries("gitlab-branch-cache").get(branchName);
                            BranchCache branchCache = JSONObject.parseObject(String.valueOf(cache), BranchCache.class);
                            body+=branchCache.getSqlCheckMessage();
                        }
                        if(null!=stageList&&stageList.size()>0){
                            for(Stage stage:stageList){
                                body+="- "+stage.getName()+"执行";
                                if(stage.getStatus().equals("SUCCESS")){
                                    body+="成功";
                                }else{
                                    body+="失败";
                                }
                                body+="共持续"+stage.getDurationMillis()/1000+"s\n";
                            }
                        }

                        if(!"".equals(body)){
                            gitlabService.createMRDiscussion(scanTag.getProjectId(),scanTag.getMergeId(), body);
                        }
                        if(!"".equals(unitTestResult)){
                            gitlabService.createMRDiscussion(scanTag.getProjectId(),scanTag.getMergeId(), unitTestResult);
                            if(!unitFlag){
                                gitlabService.updateMRState(scanTag.getProjectId(),scanTag.getMergeId(),"close");
                            }
                        }

                    }
                    redisTemplate.expire(projectUrl+":"+branchName, 5, TimeUnit.DAYS);
                }
            }

            redisTemplate.opsForHash().put("gitlabMessageCache", msgId, msgId);
            channel.basicAck(tag, false);
        }catch (Exception e){
            e.printStackTrace();
            channel.basicAck(tag, false);
        }
    }

    class PipelineBroadcast {
        private final String exchange;

        PipelineBroadcast(String exchange) {
            this.exchange = exchange;
        }

        public String getExchange() {
            return this.exchange;
        }
    }

    private GuardPojo checkGuardType(GuardType guardType,List<GuardPojo> guardList){
        if(null!=guardList){
            for(GuardPojo guard:guardList){
                if(guardType.getName().equals(guard.getGuardType())){
                    return guard;
                }
            }
        }
        return null;
    }

    private Boolean checkCode(String projectType){
        if(ProjectType.java.getName().equals(projectType)
                ||ProjectType.gradle.getName().equals(projectType)
                ||ProjectType.npm.getName().equals(projectType)
                ||ProjectType.net.getName().equals(projectType)){
            return true;
        }else{
            return false;
        }
    }

    private Boolean checkUnit(String projectType){
        if(ProjectType.java.getName().equals(projectType)
                ||ProjectType.gradle.getName().equals(projectType)){
            return true;
        }else{
            return false;
        }
    }

}
