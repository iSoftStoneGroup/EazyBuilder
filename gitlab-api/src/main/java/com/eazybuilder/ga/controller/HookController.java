package com.eazybuilder.ga.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.utils.StringUtils;
import com.eazybuilder.ga.component.DingTalkComponent;
import com.eazybuilder.ga.component.GitlabApiComponent;
import com.eazybuilder.ga.constant.GuardType;
import com.eazybuilder.ga.constant.MsgProfileType;
import com.eazybuilder.ga.constant.PriorityConstant;
import com.eazybuilder.ga.constant.ProfileType;
import com.eazybuilder.ga.fegin.DemandFeginService;
import com.eazybuilder.ga.fegin.vo.CheckCodeVo;
import com.eazybuilder.ga.fegin.vo.UpdateNacosRequestVo;
import com.eazybuilder.ga.pojo.*;
import com.eazybuilder.ga.pojo.cache.BranchCache;
import com.eazybuilder.ga.pojo.rule.GroupRulePojo;
import com.eazybuilder.ga.pojo.rule.GuardPojo;
import com.eazybuilder.ga.service.AddNoteService;
import com.eazybuilder.ga.service.CIPackageService;
import com.eazybuilder.ga.service.NacosService;
import io.swagger.annotations.ApiOperation;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/hook")
@CrossOrigin
public class HookController {

    private static Logger logger = LoggerFactory.getLogger(HookController.class);

    @Autowired
    private AddNoteService addNoteService;

    @Autowired
    private CIPackageService ciPackageService;

    @Autowired
    private DemandFeginService demandFeginService;

    @Autowired
    private NacosService nacosService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${gitLab.base_url}")
    private String baseUrl;

    @Value("${redmine.base_url}")
    private String redmineUrl;

    @Value("${sql.checkMessage}")
    private String checkMessage;

    @Value("${sql.checkPriority}")
    private String checkPriority;

    @Autowired
    private DingTalkComponent dingTalkComponent;

    @Autowired
    private GitlabApiComponent gitlabApiComponent;

    @ApiOperation(value = "SQL流水线检查")
    @PostMapping("/parseSqlPipeline")
    
    public Boolean parseSqlPipeline(@RequestBody SqlCheckPojo data){
        byte[] sqlByte = Base64.getDecoder().decode(Pattern.compile("\t|\r|\n| ").matcher(data.getBaseSqlFile()).replaceAll(""));
        logger.info(new String(sqlByte));
        String sqlString = new String(sqlByte);
        String[] checkMessageArray = checkMessage.split(",");
        String[] checkPriorityArray = checkPriority.split(",");
        String[] sqlArray = sqlString.split("\n");
        String note="";
        String shellNote = "";
        String groupName = data.getGroupName();
        byte[] fullPathByte = Base64.getDecoder().decode(Pattern.compile("\t|\r|\n| ").matcher(data.getFullPath()).replaceAll(""));
        String fullPath = new String(fullPathByte);
        if(null!=sqlArray&&sqlArray.length>0){
            for(String sql:sqlArray){
                if(null!=checkMessageArray&&checkMessageArray.length>0){
                    for(int i=0;i<checkMessageArray.length;i++){
                        String checkStr = checkMessageArray[i];
                        String priorityStr = "";
                        if(null!=checkPriorityArray&&checkPriorityArray.length>i){
                            Integer priority = Integer.valueOf(checkPriorityArray[i]);
                            if(priority.equals(PriorityConstant.BLOCKER.getPriority())){
                                priorityStr=PriorityConstant.BLOCKER.getName();
                            }else if(priority.equals(PriorityConstant.CRITICAL.getPriority())){
                                priorityStr=PriorityConstant.CRITICAL.getName();
                            }else if(priority.equals(PriorityConstant.MAJOR.getPriority())){
                                priorityStr=PriorityConstant.MAJOR.getName();
                            }else if(priority.equals(PriorityConstant.MINOR.getPriority())){
                                priorityStr=PriorityConstant.MINOR.getName();
                            }else if(priority.equals(PriorityConstant.INFO.getPriority())){
                                priorityStr=PriorityConstant.INFO.getName();
                            }
                        }else{
                            priorityStr=PriorityConstant.MAJOR.getName();
                        }
                        if(sql.contains(checkStr.toLowerCase())||sql.contains(checkStr.toUpperCase())){
                            shellNote+="文件"+fullPath+"存在包含"+checkStr+"的敏感SQL,敏感度级别为"+priorityStr+"\n";
                            note+="- 文件"+fullPath+"存在包含"+checkStr+"的敏感SQL:"+sql+",敏感度级别为"+priorityStr+"，请审核人员注意\n";
                            break;
                        }
                    }
                }
            }
        }
        if(!"".equals(shellNote)){
            sendDingTalk(groupName,note,null,MsgProfileType.monitoringSql);
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }

    }

    @ApiOperation(value = "解析SQL检查")
    @PostMapping("/parseSql")
    
    public String parseSql(@RequestBody SqlCheckPojo data){
        byte[] sqlByte = Base64.getDecoder().decode(Pattern.compile("\t|\r|\n| ").matcher(data.getBaseSqlFile()).replaceAll(""));
        logger.info(new String(sqlByte));
        String sqlString = new String(sqlByte);
        String[] checkMessageArray = checkMessage.split(",");
        String[] checkPriorityArray = checkPriority.split(",");
        String[] sqlArray = sqlString.split("\n");
        String note="";
        String shellNote = "";
        String fileName = data.getFileName();
        String groupName = data.getGroupName();
        String branchName = data.getBranchName();
        byte[] fullPathByte = Base64.getDecoder().decode(Pattern.compile("\t|\r|\n| ").matcher(data.getFullPath()).replaceAll(""));
        String fullPath = new String(fullPathByte);
        if(null!=sqlArray&&sqlArray.length>0){
            for(String sql:sqlArray){
                if(null!=checkMessageArray&&checkMessageArray.length>0){
                    for(int i=0;i<checkMessageArray.length;i++){
                        String checkStr = checkMessageArray[i];
                        String priorityStr = "";
                        if(null!=checkPriorityArray&&checkPriorityArray.length>i){
                            Integer priority = Integer.valueOf(checkPriorityArray[i]);
                            if(priority.equals(PriorityConstant.BLOCKER.getPriority())){
                                priorityStr=PriorityConstant.BLOCKER.getName();
                            }else if(priority.equals(PriorityConstant.CRITICAL.getPriority())){
                                priorityStr=PriorityConstant.CRITICAL.getName();
                            }else if(priority.equals(PriorityConstant.MAJOR.getPriority())){
                                priorityStr=PriorityConstant.MAJOR.getName();
                            }else if(priority.equals(PriorityConstant.MINOR.getPriority())){
                                priorityStr=PriorityConstant.MINOR.getName();
                            }else if(priority.equals(PriorityConstant.INFO.getPriority())){
                                priorityStr=PriorityConstant.INFO.getName();
                            }
                        }else{
                            priorityStr=PriorityConstant.MAJOR.getName();
                        }
                        if(sql.contains(checkStr.toLowerCase())||sql.contains(checkStr.toUpperCase())){
                            shellNote+="文件"+fullPath+"存在包含"+checkStr+"的敏感SQL,敏感度级别为"+priorityStr+"\n";
                            note+="- 文件"+fullPath+"存在包含"+checkStr+"的敏感SQL:"+sql+",敏感度级别为"+priorityStr+"，请审核人员注意\n";
                            break;
                        }
                    }
                }
            }
        }
        if(!"".equals(shellNote)){
            String patternStr = "(bugfix|feature)[-]([0-9]+)";
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(branchName);
            try {
                if (matcher.find()) {
                    String arr[] = branchName.split("-");
                    if(arr.length==2){
                        demandFeginService.addNote(Integer.valueOf(arr[1]),note);
                    }
                    BranchCache cache = new BranchCache();
                    cache.setBranchName(branchName);
                    cache.setSqlCheckMessage(note);
                    redisTemplate.opsForHash().put("gitlab-branch-cache", branchName, JSONObject.toJSONString(cache));
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            sendDingTalk(groupName,note,null,MsgProfileType.monitoringSql);
            return Result.no().message(shellNote).toString();
        }else{
            return Result.ok().message("文件"+fileName+"不包含敏感sql").toString();
        }

    }
    

    private void sendDingTalk(String groupName, String message, List<String> emailList, MsgProfileType msgProfileType) {
        if (redisTemplate.opsForHash().entries(groupName).containsKey(groupName)) {
            Object value = redisTemplate.opsForHash().entries(groupName).get(groupName);
            DingTalkHookPojo dingTalkHookPojo = JSONObject.parseObject(String.valueOf(value), DingTalkHookPojo.class);
            dingTalkComponent.sendDingPrivateMessageMQ("gitlab 提醒", message,
                    emailList, msgProfileType,"private");
        }
    }    @ApiOperation(value = "解析代码检查xml")
    @PostMapping("/parseXml")
    
    public String parseXml(@RequestBody ParseXmlPojo data){
        try {

            byte[] xml = Base64.getDecoder().decode(Pattern.compile("\t|\r|\n| ").matcher(data.getBaseXml()).replaceAll(""));
            logger.info(new String(xml));
            Document document = DocumentHelper.parseText(new String(xml, "utf-8"));
            Element rootElement = document.getRootElement();
            Iterator iterator = rootElement.elementIterator();
            String p3cResult="";

            Integer blockerCount=0;
            Integer criticalCount=0;
            Integer majorCount=0;
            Integer minorCount=0;
            Integer infoCount=0;

            Boolean successFlag = Boolean.TRUE;


            while (iterator.hasNext()){
                Element element = (Element) iterator.next();

                Iterator subIterator = element.elementIterator();
                while (subIterator.hasNext()){
                    Element subElement = (Element) subIterator.next();
                    if("violation".equals(subElement.getName())){
                        List<Attribute> subAttributes = subElement.attributes();
                        String beginline="";
                        Integer priority=0;
                        String packageStr="";
                        String classStr="";
                        for (Attribute subAttribute : subAttributes) {
                            if("package".equals(subAttribute.getName())){
                                packageStr=subAttribute.getValue();
                            }
                            if("class".equals(subAttribute.getName())){
                                classStr=subAttribute.getValue();
                            }
                            if("beginline".equals(subAttribute.getName())){
                                beginline=subAttribute.getValue();
                            }
                            if("priority".equals(subAttribute.getName())){
                                priority=Integer.valueOf(subAttribute.getValue());
                            }
                            if(priority.equals(PriorityConstant.BLOCKER.getPriority())){
                                blockerCount++;
                            }else if(priority.equals(PriorityConstant.CRITICAL.getPriority())){
                                criticalCount++;
                            }else if(priority.equals(PriorityConstant.MAJOR.getPriority())){
                                majorCount++;
                            }else if(priority.equals(PriorityConstant.MINOR.getPriority())){
                                minorCount++;
                            }else if(priority.equals(PriorityConstant.INFO.getPriority())){
                                infoCount++;
                            }

                        }
                        p3cResult=p3cResult+packageStr;
                        p3cResult=p3cResult+"."+classStr;
                        p3cResult=p3cResult+":"+beginline;
                        p3cResult=p3cResult+subElement.getStringValue().replace(" ", "");
                        p3cResult=p3cResult+"[优先级]:"+priority+"\n";
                    }
                }
            }

            Integer thresholdMin=null;
            Integer thresholdMax=null;
            logger.info("#######################"+data.getGroup());
            if (redisTemplate.opsForHash().entries("gitlabMessageCache").containsKey(data.getGroup())) {
                Object value = redisTemplate.opsForHash().entries("gitlabMessageCache").get(data.getGroup());
                GroupRulePojo groupRule = JSONObject.parseObject(String.valueOf(value), GroupRulePojo.class);
                logger.info("#####################"+groupRule);
                List<GuardPojo> guardList = groupRule.getGuards();
                if(null!=guardList&&guardList.size()>0){
                    for(int i=0;i<guardList.size();i++){
                        GuardPojo guard = guardList.get(i);
                        if(GuardType.code_smell_blocker.getName().equals(guard.getGuardType())){
                            thresholdMax=guard.getThresholdMax();
                            thresholdMin=guard.getThresholdMin();
                        }
                    }
                }
            }
            String thresholdMsg = "";
            if(thresholdMax!=null){
                if(blockerCount>=thresholdMax){
                    successFlag = Boolean.FALSE;
                    thresholdMsg="严重程度阻断数目大于"+thresholdMax+"先修改代码";
                }
            }

            p3cResult=p3cResult+PriorityConstant.BLOCKER.getName()+":"+blockerCount
                    +PriorityConstant.CRITICAL.getName()+":"+criticalCount
                    +PriorityConstant.MAJOR.getName()+":"+majorCount
                    +PriorityConstant.MINOR.getName()+":"+minorCount
                    +PriorityConstant.INFO.getName()+":"+infoCount
                    +"\n"+thresholdMsg+"\n";
            if(successFlag){
                return Result.ok().message(p3cResult).toString();
            }else{
                return Result.no().message(p3cResult).toString();
            }

        } catch (DocumentException | UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            return Result.no().message("xml解析失败").toString();
        }
    }

    @ApiOperation(value = "校验分支名")
    @PostMapping("/checkBranch")
    
    public String checkBranch(@RequestBody CheckBranchPojo data){
        String branchName = data.getBranchName();

        String patternStr = "(bugfix|feature)[-]([0-9]+)";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(branchName);
        try {
            if (matcher.find()) {
                return Result.ok().message("分支校验通过").toString();
            }
        }catch (Exception e){
            return Result.no().message("非法分支").toString();
        }
        return Result.no().message("非法分支").toString();
    }

    @ApiOperation(value = "接受preReceive传输信息")
    @PostMapping("/preReceive")
    public String preReceive(@RequestBody PreReceivePojo data) {  	
 
       return checkCommitMsg(data);

    
    }
    @ApiOperation(value = "客户端钩子校验")
    @PostMapping("/checkCommit") 
    public String checkCommit(HttpServletRequest request) throws UnsupportedEncodingException, IOException {  
    	BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "GBK"));
        String str = "";
        String wholeStr = "";
        while ((str = reader.readLine()) != null) {//一行一行的读取body体里面的内容；
            wholeStr += str;
        }
        logger.info("入参:{}",wholeStr);
        PreReceivePojo data=JSONObject.parseObject(wholeStr, PreReceivePojo.class);
        return checkCommitMsg(data);
    }
    
    private String  checkCommitMsg(PreReceivePojo data) {
        String userName = data.getUserName();
        String notes = data.getNotes();
           
        
        String groupName=data.getGroupName();
        List<String> emailList = new ArrayList<String>();
        List<String> userEmailList = gitlabApiComponent.getUserEmail(data.getUserName());
        if(null!=userEmailList&&userEmailList.size()>0){
            emailList.addAll(userEmailList);
        }
        logger.info("注释详情：{},组名:{},用户邮箱:{}",notes,groupName,emailList);

        //1.校验参数
        if (StringUtils.isBlank(userName)||StringUtils.isBlank(notes)){
        	sendDingTalk(groupName,"输入参数不可为空",null,MsgProfileType.push);
            return Result.no().message("输入参数不可为空").toString();
        }
        Integer code = 0;
        String pushType = "";

        
        //0.字符串处理，去除去除字符串中的空格、换行等转义字符
        String newNote = Pattern.compile("\t|\r|\n| ").matcher(notes).replaceAll("");
        //1.校验注释长度，大于20个字节
        if (newNote.length() < 20) {
        	sendDingTalk(groupName,"注释长度，小于20个字节",emailList,MsgProfileType.push);
            return Result.no().message("注释长度，小于20个字节").toString();
        }
        //2.代码提交注释规范
        String pattern = "(\\[ADD\\]|\\[REM\\]|\\[IMP\\]|\\[FIX\\]|\\[REF\\])(BUGFIX|FEATURE|HOTFIX)[-]([0-9]+).+";
        boolean isMatch = Pattern.matches(pattern, newNote);
        String branchName = "";
        if (isMatch) {
            Pattern r = Pattern.compile(pattern);
            // 现在创建 matcher 对象
            Matcher m = r.matcher(newNote);
            if (m.find()) {
                code = Integer.valueOf(m.group(3));
                pushType = String.valueOf(m.group(2));
                branchName = pushType.toLowerCase()+"-"+code;

                String branchPatternStr = "(bugfix|feature|hotfix)[-]([0-9]+)";
                Pattern branchPattern = Pattern.compile(branchPatternStr);
                Matcher branchMatcher = branchPattern.matcher(data.getBranchName());
                if (branchMatcher.find()) {
                    if(!branchName.equals(data.getBranchName().toLowerCase())){
                    	sendDingTalk(groupName,"当前分支名称为"+data.getBranchName()+",提交注释与分支名称不匹配",emailList,MsgProfileType.push);
                        return Result.no().message("当前分支名称为"+data.getBranchName()+",提交注释与分支名称不匹配").toString();
                    }
                }else{
                	sendDingTalk(groupName,"当前分支名称为"+data.getBranchName()+",格式非法（bugfix-数字|feature-数字）",emailList,MsgProfileType.push);
                    return Result.no().message("当前分支名称为"+data.getBranchName()+",格式非法（bugfix-数字|feature-数字）").toString();
                }

            } else {
            	sendDingTalk(groupName,"注释格式是：[ADD/REM/IMP/FIX/REF] FEATURE/BUGFIX-id(开发任务id)+描述（不允许数字开头）。例如：[ADD] BUGFIX-129 20字符20字符20字符",emailList,MsgProfileType.push);
                return Result.no()
                        .message("注释格式是：[ADD/REM/IMP/FIX/REF] FEATURE/BUGFIX-id(开发任务id)+描述（不允许数字开头）。例如：[ADD] BUGFIX-129 20字符20字符20字符")
                        .toString();
            }
        } else {
        	sendDingTalk(groupName,"注释格式是：[ADD/REM/IMP/FIX/REF] FEATURE/BUGFIX-id(开发任务id)+描述（不允许数字开头）。例如：[ADD] BUGFIX-129 20字符20字符20字符",emailList,MsgProfileType.push);
             return Result.no()
                    .message("注释格式是：[ADD/REM/IMP/FIX/REF] FEATURE/BUGFIX-id(开发任务id)+描述（不允许数字开头）。例如：[ADD] BUGFIX-129 20字符20字符20字符")
                    .toString();
        }

        String commitUrl = baseUrl+data.getProjectPath()+"/-/commit/"+data.getCommit();
        notes = notes + "\n" + commitUrl;
        String gitPathAddr = baseUrl+data.getProjectPath()+".git";
        CheckCodeVo result = demandFeginService.checkCode(gitPathAddr, userName, code, notes);
        
        logger.info("########################查看结果"+result.getMessage());
         //3.结果判断
        if (result.getFlag()){
            return Result.ok().message("已成功push代码\n"+redmineUrl+"issues/"+code).toString();
        }else{
        	sendDingTalk(groupName,result.getMessage(),emailList,MsgProfileType.push);
            return Result.no().message(result.getMessage()).toString();
        }
    }
    
    


    @ApiOperation(value = "发布配置")
    @PostMapping("/releaseConfigPipeline")
    
    public String releaseConfigPipeline(@RequestBody ReleaseConfigPipelinePojo data){
        String fileName = data.getFileName();
        byte[] fileContentByte = Base64.getDecoder().decode(Pattern.compile("\t|\r|\n| ").matcher(data.getFileContentBase()).replaceAll(""));
        String fileContent = new String(fileContentByte);
        String group = "DEFAULT_GROUP";

        UpdateNacosRequestVo updateNacosRequestVo = new UpdateNacosRequestVo();
        updateNacosRequestVo.setContent(fileContentByte);
        updateNacosRequestVo.setFileName(fileName);

        demandFeginService.updateNacos(updateNacosRequestVo);

        String result = nacosService.releaseConfig(data.getNamespace(), fileName, group, fileContent);

        if ("".equals(result)){
            return Result.ok().toString();
        }else{
            return Result.no().message(result).toString();
        }
    }

    @ApiOperation(value = "发布配置")
    @PostMapping("/releaseConfig")
    
    public String releaseConfig(@RequestBody ReleaseConfigPojo data){
        String fileName = data.getFileName();
        String branchName = data.getBranchName();
        byte[] fileContentByte = Base64.getDecoder().decode(Pattern.compile("\t|\r|\n| ").matcher(data.getFileContentBase()).replaceAll(""));
        String fileContent = new String(fileContentByte);
        String namespace = data.getGroupName();
        String group = "DEFAULT_GROUP";

        String[] branchArr = branchName.split("-");
        if(null==branchArr||branchArr.length<2){
            return Result.no().message("提交分支错误，不能更新nacos文件").toString();
        }
        String code = branchArr[1];

        UpdateNacosRequestVo updateNacosRequestVo = new UpdateNacosRequestVo();
        updateNacosRequestVo.setContent(fileContentByte);
        updateNacosRequestVo.setUserName(data.getUserName());
        updateNacosRequestVo.setCode(Integer.valueOf(code));
        updateNacosRequestVo.setFileName(fileName);

        demandFeginService.updateNacos(updateNacosRequestVo);

        String result = nacosService.releaseConfig(namespace, fileName, group, fileContent);

        List<String> emailList = new ArrayList<String>();
        List<String> userEmailList = gitlabApiComponent.getUserEmail(data.getUserName());
        if(null!=userEmailList&&userEmailList.size()>0){
            emailList.addAll(userEmailList);
        }

        if ("".equals(result)){
            sendDingTalk(data.getGroupName(),data.getUserName()+"提交配置文件"+fileName+",已更新到nacos环境上。",emailList,MsgProfileType.configEdit);
            return Result.ok().toString();
        }else{
            return Result.no().message(result).toString();
        }
    }

    @ApiOperation(value = "触发ci打包")
    @PostMapping("/ciPackage")
    
    public String ciPackage(@RequestBody PostReceivePojo data) {
        Long totalTime = System.currentTimeMillis();

        String userName = data.getUserName();
        String branchName = data.getBranchName();
        String[] projectPath = data.getProjectPath().split("/");
        String projectName = projectPath[projectPath.length-1];
        String notes = data.getNotes();
        String code = "";
        String codePhrase="";

        if(branchName.toLowerCase().contains("test")||branchName.toLowerCase().contains("master")){
            return Result.no().message("不发送").toString();
        }

        if (StringUtils.isBlank(userName)||StringUtils.isBlank(branchName)||StringUtils.isBlank(projectName)){
            return Result.no().message("输入参数不可为空").toString();
        }

        String patternStr = "[A-Za-z]*[-|#]([0-9]+)";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(notes);
        try{
            if (matcher.find()) {
                codePhrase = matcher.group(0);
                String arr[];
                if(codePhrase.contains("-")){
                    arr = codePhrase.split("-");
                }else if(codePhrase.contains("#")){
                    arr = codePhrase.split("#");
                }else{
                    return Result.no().message("需求号解析错误").toString();
                }
                if(arr.length>0){
                    code = arr[arr.length-1];
                }else{
                    return Result.no().message("需求号解析错误").toString();
                }
            }else{
                return Result.no().message("需求号解析错误").toString();
            }
        }catch (Exception e) {
            e.printStackTrace();
            return Result.no().message("需求号解析错误").toString();
        }
        String topCode = String.valueOf(demandFeginService.getTopIssueId(Integer.valueOf(code)));
        HashMap<String,String> customFields = demandFeginService.getIssueCustomFieldById(Integer.valueOf(code));
        //customFields.replace("配置文件内容","");
        CIPackagePojo pojo = new CIPackagePojo();
        pojo.setCode(code);
        pojo.setImageTag(codePhrase);
        pojo.setProjectName(projectName);
        pojo.setUserName(userName);
        pojo.setTopCode(topCode);
        pojo.setTagName(branchName);
        pojo.setGitPath(baseUrl+data.getProjectPath()+".git");
        pojo.setCustomFields(customFields);

        pojo.setTargetBranchName(branchName);

        pojo.setProfileType(ProfileType.push);

        ciPackageService.sendCIPackage(pojo);

        logger.info("########################共执行了"+(System.currentTimeMillis()-totalTime)+"ms");
        return Result.ok().message("已将构建任务发送到CI平台").toString();
    }


}
