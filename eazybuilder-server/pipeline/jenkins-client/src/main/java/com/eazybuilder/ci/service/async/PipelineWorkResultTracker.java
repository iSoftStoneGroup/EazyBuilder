package com.eazybuilder.ci.service.async;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ci.config.LoadConfigYML;
import com.eazybuilder.ci.constant.AutoTestSwitch;
import com.eazybuilder.ci.constant.MetricType;
import com.eazybuilder.ci.constant.RoleEnum;
import com.eazybuilder.ci.constant.SendMailSwitch;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.entity.devops.DockerImage;
import com.eazybuilder.ci.entity.devops.EventType;
import com.eazybuilder.ci.entity.docker.DockerDigest;
import com.eazybuilder.ci.entity.report.*;
import com.eazybuilder.ci.event.EventBusSupport;
import com.eazybuilder.ci.jenkins.JenkinsPipelineService;
import com.eazybuilder.ci.mail.MailSenderHelper;
import com.eazybuilder.ci.rabbitMq.SendRabbitMq;
import com.eazybuilder.ci.service.*;
import com.eazybuilder.ci.storage.ResourceStorageService;
import com.eazybuilder.ci.util.DingtalkWebHookUtil;
import com.eazybuilder.ci.util.FreemakerUtils;
import com.eazybuilder.ci.util.HttpUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class PipelineWorkResultTracker implements Callable<PipelineExecuteResult> {


    private static String dingTalkPipeline = "### 流水线执行结果\n" +
            "\n" +
            "\n" +
            "- 项目名称： ${result.project.name}(${result.project.description})；\n" +
            "\n" +
            "- 构建结果： ${result.pipeline.status}；\n" +
            "\n" +
            "- 构建过程：  ${result.pipeline.profileName}；\n" +
            "\n" +
            "- 触发类型：  ${result.pipeline.pipelineType}；\n" +
            "\n" +
            "- 操作分支:     ${editBranch}；\n" +
            "\n" +
            "- 查看日志:   ${logUrl}；\n" ;

    private static Logger logger = LoggerFactory.getLogger(PipelineWorkResultTracker.class);

    private static Properties properties = new LoadConfigYML().getConfigProperties();

    private DockerImageService dockerImageService;
    private ProjectService projectService;
    private PipelineProfileService profileService;
    private JenkinsPipelineService jenkinsService;
    private PipelineServiceImpl pipelineServiceImpl;
    private ResourceStorageService storageService;
    private MetricService metricService;
    private Configuration configuration;
    private String baseUrl;
    private String rootUrl;
    private MailSenderHelper mailSender;
    private SendRabbitMq sendRabbitMq;
    private String dockerImageTag;
    private String profileType;
    private String ciHost;
    private String projectId;
    private String profileId;
    private String name;
    private PipelineProfile pipelineProfile;

    private String redmineCode;
    private String redmineUser;

    private boolean sendMail = false;

    private EventBusSupport eventBus;
    //触发任务的用户，可空
    private User user;

    private String uid;

    private Map<String, Object> reportModel;

    private DockerDigestService dockerDigestService;



    PipelineExecuteService pipelineExecuteService;


    public PipelineWorkResultTracker(User user,PipelineProfile pipelineProfile, String dockerImageTag, DockerImageService dockerImageService, DockerDigestService dockerDigestService,
                                     String redmineCode, String redmineUser, String profileType, String ciHost, SendRabbitMq sendRabbitMq, ProjectService projectService, PipelineProfileService profileService,
                                     JenkinsPipelineService jenkinsService, PipelineServiceImpl pipelineServiceImpl, MetricService metricService, Configuration configuration, String baseUrl,
                                     MailSenderHelper mailSender, EventBusSupport eventBus, Map<String, Object> reportModel, ResourceStorageService storageService,PipelineExecuteService pipelineExecuteService) {
        super();
        this.pipelineProfile = pipelineProfile;
        this.dockerImageTag = dockerImageTag;
        this.dockerImageService = dockerImageService;
        this.redmineCode = redmineCode;
        this.redmineUser = redmineUser;
        this.profileType = profileType;
        this.ciHost = ciHost;
        this.sendRabbitMq = sendRabbitMq;
        this.projectService = projectService;
        this.profileService = profileService;
        this.jenkinsService = jenkinsService;
        this.pipelineServiceImpl = pipelineServiceImpl;
        this.metricService = metricService;
        this.configuration = configuration;
        this.baseUrl = baseUrl;
        this.rootUrl = HttpUtil.getRootUrl(baseUrl);

        this.mailSender = mailSender;
        this.eventBus = eventBus;
        this.reportModel = reportModel;
        this.storageService = storageService;
        this.dockerDigestService = dockerDigestService;
        this.pipelineExecuteService = pipelineExecuteService;
        //在这里做一次判断，看此次任务是由客户在页面触发的，还是gitlab触发的
        if (StringUtils.isNotBlank(redmineUser)) {
            user = new User();
            if (!redmineUser.contains(properties.getProperty("email.suffix"))) {
                user.setEmail(redmineUser + properties.getProperty("email.suffix"));
            } else {
                user.setEmail(redmineUser);
            }
        } else {
            this.user = user;
        }
    }



    public void setTrackInfo(String projectId, String profileId, String name, String uid) {
        this.projectId = projectId;
        this.profileId = profileId;
        this.name = name;
        this.uid = uid;
    }

    private void fillSonarMetric(Pipeline pipeLine, List<Metric> metrics, Report report) {
        //bug
        metrics.add(new Metric(MetricType.bug_blocker,
                report.getSummary().getRows().get(0)[1],
                pipeLine));
        metrics.add(new Metric(MetricType.bug_critical,
                report.getSummary().getRows().get(0)[2],
                pipeLine));
        metrics.add(new Metric(MetricType.bug_major,
                report.getSummary().getRows().get(0)[3],
                pipeLine));
        //安全漏洞
        metrics.add(new Metric(MetricType.vulner_blocker,
                report.getSummary().getRows().get(1)[1],
                pipeLine));
        metrics.add(new Metric(MetricType.vulner_critical,
                report.getSummary().getRows().get(1)[2],
                pipeLine));
        metrics.add(new Metric(MetricType.vulner_major,
                report.getSummary().getRows().get(1)[3],
                pipeLine));
        //编码规范
        metrics.add(new Metric(MetricType.code_smell_blocker,
                report.getSummary().getRows().get(2)[1],
                pipeLine));
        metrics.add(new Metric(MetricType.code_smell_critical,
                report.getSummary().getRows().get(2)[2],
                pipeLine));
        metrics.add(new Metric(MetricType.code_smell_major,
                report.getSummary().getRows().get(2)[3],
                pipeLine));
    }

    @Override
    public PipelineExecuteResult call() {
        PipelineExecuteResult result = initResult();
        Pipeline pipeLine = result.getPipeline();
        Project project = result.getProject();
        StringBuilder messageBuilder = new StringBuilder();
        JSONObject codeReport= new JSONObject();;
        logger.info("jenkins流水线执行结果处理");
        if (pipeLine == null) {
            logger.info("Pipeline为空");
            return result;
        }
        try {
            String consoleText = updateAndAttachPipline(result);
            //保存镜像版本到数据库中
            DockerImage dockerImage = new DockerImage();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dockerImage.setCreateDate(df.parse(df.format(new Date())));
            dockerImage.setImageTag(dockerImageTag);
            //dockerImage.setProjectName(result.getProject().getName().split("-")[0]);
            dockerImage.setProjectName(result.getProject().getName());
            dockerImage.setProjectId(result.getProject().getId());
            String url = "";
            logger.info("project:{}", JSONObject.toJSONString(project));
            if (project.getDeployConfigList() != null && project.getDeployConfigList().size() > 0) {
                url = project.getRegistry().getUrl() + "/" + project.getImageSchema() + "/" + project.getDeployConfigList().get(0).getName() + "/";
            } else {
                url = project.getRegistry().getUrl() + "/" + project.getImageSchema() + "/" + project.getName() + "/";
            }
            dockerImage.setPullUrl(url + dockerImageTag);
//			docker pull core.eazybuilder.com/devops/harbor-support:v1
            logger.info("ci保存镜像版本{}", net.sf.json.JSONObject.fromObject(dockerImage));
            dockerImageService.save(dockerImage);
            //给redmine推送镜像时，推的是完整路径
            dockerImageTag = url + dockerImageTag;
            saveDockerDigest(consoleText, result.getPipeline());
//            genK8sDeployFile(result, consoleText);
            //前端工程门禁判断逻辑中移除单元测试相关门禁
            judgeNpmInstall(result,consoleText);
            messageBuilder.append("项目:").append(result.getProject().getDescription()).append(result.getProject().getName()).append("\n");
            String pipelineUid = pipeLine.getId();
            codeReport.put("devopsEventType", "codeReport");

            codeReport.put("pipelineUid", pipelineUid);
            codeReport.put("stages", pipeLine.getStages());
            if (pipeLine.getStatus() == Status.SUCCESS || pipeLine.getStatus() == Status.UNSTABLE) {
                logger.info("ci读取sonarqube测试报告开始");
                loadReport(result);
                Map<String,Report> typedReport = result.getTypedReport();
                logger.info("ci读取sonarqube测试报告结束 {}", JSONObject.toJSONString(typedReport));
                if (null!=typedReport&&!typedReport.isEmpty()) {
                	List<Report> reports = new ArrayList<Report>();
					for(String key:typedReport.keySet()){
						reports.add(typedReport.get(key));
					}
					codeReport.put("reports", new JSONArray());
					//codeReport.put("stages", new JSONArray());
					codeReport.getJSONArray("reports").addAll(reports);
					//List<Stage> list=pipelineServiceImpl.findStageByPipelineId(pipelineUid);
					//codeReport.getJSONArray("stages").addAll(list);
					pipelineServiceImpl.getReportCache().put(pipelineUid, reports);
                    messageBuilder.append(genEmailMsg(typedReport));
                    Status status = pipeLine.getStatus();
                    pipeLine.setStatus(status);
                    pipelineServiceImpl.updateBuildStatus(project,status);
                }
                result.setSuccess(pipeLine.getStatus().name());
                if(pipelineProfile.getSendMailSwitch()== SendMailSwitch.TOTAL) {
                    sendMail(result, typedReport);
                }else{
                    logger.info("构建过程中没有开启全量邮件通知，本次流水线不发送邮件");
                }
            } else {
                result.failed();
                messageBuilder.append("项目构建失败").append("\n");
                logger.info("构建失败，ci开始发送通知邮件");
                sendMail(result, null);
                logger.info("构建失败，ci结束发送通知邮件");
            }
            if(pipeLine.isNpmInstal()) {
                codeReport.put("projectType", ProjectType.npm);
            }else{
                codeReport.put("projectType", project.getProjectType());
            }
            codeReport.put("status",pipeLine.getStatus().name());
            if(pipeLine.getStatusGuard()!=null) {
                codeReport.put("statusGuard", pipeLine.getStatusGuard());
            }
            //fire pipeLine done event
            eventBus.postEvent(pipeLine);
            return result;
        } catch (Exception e) {
            logger.error("failed to collect pipeline report", e);
            result.failed();
            return result;
        } finally {
        	if(null!=codeReport) {
        		logger.info("发送代码质量报告到广播中:{}",codeReport);
    			sendRabbitMq.sendMsg(codeReport.toString());
        	}else {
        		logger.warn("流水线中不包含代码质量信息，不发送代码质量报告");
        	}
            pipelineServiceImpl.save(pipeLine);
            logger.info("准备发送消息给redmine");
            try {
                callbackRedmine(pipeLine, messageBuilder, result);
            } catch (Exception e) {
                logger.error("拼装rabbitmq数据出现异常：{}-{}",e.getMessage(),e);
            }
            try {
                sendDingTalkPipeline(result);
            } catch (Exception e) {
                logger.error("发送流水线执行结果出现异常：{}-{}",e.getMessage(),e);
            }
        }
    }

    private void sendDingTalkPipeline(PipelineExecuteResult results) {
        Map<String,Object> model = new HashMap<>();
        model.put("result", results);
        model.put("logUrl", ciHost + "/ci/resources/" + results.getPipeline().getLogId());
        String sourceBranch = results.getPipeline().getSourceBranch();
        String targetBranch = results.getPipeline().getTargetBranch();
        if(StringUtils.isBlank(targetBranch)&&StringUtils.isBlank(sourceBranch)){
            model.put("editBranch","--");
        }
        if(StringUtils.isNotBlank(sourceBranch)&&StringUtils.isBlank(targetBranch)){
            model.put("editBranch",sourceBranch);
        }
        if(StringUtils.isNotBlank(targetBranch)&&StringUtils.isBlank(sourceBranch)){
            model.put("editBranch",targetBranch);
        }

        if(StringUtils.isNotBlank(sourceBranch)&&StringUtils.isNotBlank(targetBranch)){
            model.put("editBranch",sourceBranch+"-->"+targetBranch);
        }
//        Template dingtalkTemplate = configuration.getTemplate("dingtalk-pipeline.ftl");
        String markdownMsg = FreemakerUtils.generateByModelAndTemplate(model, dingTalkPipeline);
        String[] receiverMailList = getReceiverMailList(results.getProject());
        DingtalkWebHookUtil.sendDingtalkPrivateMsgBymq("流水线执行结果", markdownMsg, results.getProject().getTeam().getCode(),
                Arrays.asList(receiverMailList),MsgProfileType.pipelineFail,sendRabbitMq);
    }

    private StringBuilder genEmailMsg(Map<String, Report> typedReport) {
        StringBuilder messageBuilder = new StringBuilder();
        if (typedReport.containsKey("sonar")) {

            messageBuilder.append("项目构建成功").append("\n");
            messageBuilder.append("代码质量汇总报告").append("\n");
            messageBuilder.append("    阻断 ").append(" 严重 ").append(" 主要 ").append("\n");
            messageBuilder.append("BUG：");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(0)[1]).append("  ");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(0)[2]).append("  ");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(0)[3]).append("\n");
            messageBuilder.append("漏洞：");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(1)[1]).append("  ");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(1)[2]).append("  ");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(1)[3]).append("\n");
            messageBuilder.append("规范：");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(2)[1]).append("  ");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(2)[2]).append("  ");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(2)[3]).append("\n");

            messageBuilder.append("详细代码质量报告链接地址： ").append(typedReport.get("sonar").getLink()).append("\n");
        } else {
            messageBuilder.append(JSONObject.toJSONString(typedReport));

        }
        return messageBuilder;
    }

    private void sendMail(PipelineExecuteResult result, Map<String, Report> typedReport) throws Exception {
        if (!sendMail) {
            logger.info("ci未开启发送邮件");
            return;
        }
        if (StringUtils.equals(result.getSuccess(),Boolean.toString(true))||result.getSuccess().equals("WAIT_AUTO_TEST_RESULT")) {
            renderMailAndSend(name, result.getProject(), result.getPipeline(), typedReport);
        } else {
            renderFailedMailAndSend(name, result.getProject(), result.getPipeline());
        }
    }

    public PipelineExecuteResult initResult() {
        PipelineExecuteResult result = new PipelineExecuteResult();
        Project project = projectService.findOne(projectId);
        result.setProject(project);
        readProfile(result.getProject());
        Pipeline pipeLine = pipelineServiceImpl.findOne(uid);
        result.setPipeline(pipeLine);
        if (pipeLine == null) {
            logger.warn("cannot attach pipeline & get build result");
            result.unknow();
            pipelineServiceImpl.updateBuildStatus(result.getProject(), Status.ABORTED);
        }
        return result;
    }

    private void loadReport(PipelineExecuteResult result) throws InterruptedException, IOException, TemplateException {
        //收集报告
        List<Metric> metrics = Lists.newArrayList();
        Project project = result.getProject();
        long start = System.currentTimeMillis();

        if (project.getProfile().isSkipCloneCode()) {
            logger.warn("构建过程，跳过了下载代码，不执行任何mvn相关命令，不需要收集报告");
            return;
        }

        while (!pipelineServiceImpl.getReportCache().containsKey(uid)) {
            logger.info("waiting for report..,{}",uid);
            if (System.currentTimeMillis() - start > 30 * 60 * 1000) {
                throw new RuntimeException("waiting pipeline report timeout");
            }
            Thread.sleep(5 * 1000);
        }
        List<Report> reports = pipelineServiceImpl.getReportCache().remove(uid);
        final Map<String, Guard> guardMap = pipelineExecuteService.findTeamGuard(project.getTeam());
        Map<String, Report> typedReport = Maps.newHashMap();
        for (Report report : reports) {
            if (report.getSonarKey() != null) {
                //如果工程没有sonarkey(首次扫描)，或者sonarkey有变更(CI调整了sonar project key标识规则)
                if (project.getSonarKey() == null || !report.getSonarKey().equals(project.getSonarKey())) {
                    project.setSonarKey(report.getSonarKey());
                    //update project (set sonar project key)
                    projectService.save(project);
                }
            }
            typedReport.put(report.getType().name(), report);
            /**
             * 依据流水线返回结果信息，更新质量统计信息
             */
             List<Metric> metricsResults =  loadMetricFromReport(result.getPipeline(), report);
            metrics.addAll(metricsResults);
            logger.info("#################metrics第一次日志："+JSONArray.toJSON(metrics).toString());
        }
        //判断项目类型是否为java类型，如果是的话在走详细判断，如果不是直接设置为门禁成功
        if(result.getPipeline().getStatus()==Status.SUCCESS) {
            if (result.getPipeline().getProject().getProjectType() == ProjectType.java || result.getPipeline().getProject().getProjectType() == ProjectType.gradle|| result.getPipeline().getProject().getProjectType() == ProjectType.net) {
                boolean checkResult = checkMetric(metrics, guardMap, result.getPipeline().isNpmInstal());
                logger.info("流水线执行完成触发的门禁判定结果：{}", checkResult);
                if(checkResult) {
                    result.getPipeline().setStatusGuard(StatusGuard.SUCCESS);
                }else{
                    result.getPipeline().setStatusGuard(StatusGuard.FAILED);
                }
            } else {
                logger.info("流水线执行完成没有触发门禁判定");
                result.getPipeline().setStatusGuard(StatusGuard.NOT_EXECUTED);
            }
        }else{
            logger.info("流水线执行失败,门禁判定为失败");
            result.getPipeline().setStatusGuard(StatusGuard.FAILED);
        }
        logger.info("#################metrics第三次日志："+JSONArray.toJSON(metrics).toString());
        saveMetric(project, metrics);
        pipelineServiceImpl.getLastBuild(project.getId()).setMetricList(metrics);
        result.setTypedReport(typedReport);
    }

    /**
     *  判断门禁是否通过。前端工程门禁判断逻辑中移除单元测试相关门禁、暂时只判断java项目
     * @param metrics
     * @return
     */
    private boolean checkMetric(List<Metric> metrics, final Map<String, Guard> guardMap,boolean isNpmInstall) {
        List<String> thresholdTypeNames = getTthresholdTypeEnumNames();
        if(isNpmInstall){
            thresholdTypeNames.clear();
            thresholdTypeNames.add(ThresholdType.bug_blocker.name());
            thresholdTypeNames.add(ThresholdType.vulner_blocker.name());
            thresholdTypeNames.add(ThresholdType.code_smell_blocker.name());
            logger.info("前端工程门禁判断逻辑中移除单元测试相关门禁");
        }
        List<Metric> checkMetrics =  metrics.stream().filter(metric->thresholdTypeNames.contains(metric.getType().name())).collect(Collectors.toList());
        Boolean newUnitTestCoverageRateFlag = true;
        Integer newUncoveredLines=0;
        Metric newUnitTestCoverageMetric = null;
        if(MapUtils.isNotEmpty(guardMap) && CollectionUtils.isNotEmpty(checkMetrics)){
            for(Metric metric: checkMetrics){
                Guard checkGuard = guardMap.get(metric.getType().name());
                if(MetricType.new_uncovered_lines.equals(metric.getType())){
                    newUncoveredLines = Integer.parseInt(metric.getVal());
                    metric.setAssertSucceed(true);
                } else if(checkGuard!=null){
                    metric.setThresholdVal(checkGuard.getThresholdMax());
                    if(MetricType.new_unit_test_coverage_rate.equals(metric.getType())){
                        if(metric.compareWitchThresholdVal(checkGuard.getThresholdMax())){
                            metric.setAssertSucceed(metric.compareWitchThresholdVal(checkGuard.getThresholdMax()));
                        }else{
                            //flase不通过
                            newUnitTestCoverageRateFlag=false;
                            newUnitTestCoverageMetric=metric;
                        }
                    }else{
                        metric.setAssertSucceed(metric.compareWitchThresholdVal(checkGuard.getThresholdMax()));
                    }

                }else{
                    metric.setAssertSucceed(true);
                }
            }
            //如果新增行数大于0并且未通过新增代码覆盖率门禁
            if(null!=newUnitTestCoverageMetric&&!newUnitTestCoverageRateFlag&&newUncoveredLines>0){
                newUnitTestCoverageMetric.setAssertSucceed(false);
            }else if(null!=newUnitTestCoverageMetric){
                newUnitTestCoverageMetric.setAssertSucceed(true);
            }
        }
        if(null==checkMetrics) {
        	return true;
        }
        logger.info("#################metrics第二次日志："+JSONArray.toJSON(checkMetrics).toString());
        return checkMetrics.stream().map(Metric::isAssertSucceed).reduce(Boolean::logicalAnd).orElse(null);
    }

    private List<String> getTthresholdTypeEnumNames() {
        return EnumUtils.getEnumList(ThresholdType.class).stream().map(thresholdType->thresholdType.name()).collect(Collectors.toList());
    }

    private void saveMetric(Project project, List<Metric> metrics) {
        try {
            pipelineServiceImpl.updateProjectMetrcs(project, metrics);
            metricService.save(metrics);
        } catch (Exception e) {
            logger.error("save metric error:", e);
        }
    }

    /**
     * 依据流水线返回结果信息，更新质量统计信息
     *
     * @param pipeLine
     * @param report
     */
    private List loadMetricFromReport(Pipeline pipeLine, Report report) {
        List<Metric> metrics = Lists.newArrayList();
        logger.info("report={}", report.toString());
        try {
            switch (report.getType()) {
                case junit:
					/*metrics.add(new Metric(MetricType.unit_test_success_rate,
							report.getSummary().getDataMap().get("Success Rate")[0],
							pipeLine,report.getAttachmentId(),report.getLink()));*/
                    break;
                case jacoco:
					/*metrics.add(new Metric(MetricType.unit_test_coverage_rate,
							report.getSummary().getDataMap().get("Cov.")[0],
							pipeLine,report.getAttachmentId(),report.getLink()));*/
                    break;
                case sonar:
                    fillSonarMetric(pipeLine, metrics, report);
                    break;
                case project_analysis:
                    metrics.add(new Metric(MetricType.code_line,
                            report.getSummary().getRows().get(0)[0],
                            pipeLine));
                    metrics.add(new Metric(MetricType.unit_test_coverage_rate,
                            report.getSummary().getRows().get(0)[1],
                            pipeLine));
                    metrics.add(new Metric(MetricType.unit_test_success_rate,
                            report.getSummary().getRows().get(0)[2],
                            pipeLine));
                    metrics.add(new Metric(MetricType.new_lines,
                            report.getSummary().getRows().get(0)[3],
                            pipeLine));
                    metrics.add(new Metric(MetricType.technology_debt,
                            report.getSummary().getRows().get(0)[4],
                            pipeLine));
                    metrics.add(new Metric(MetricType.new_bugs,
                            report.getSummary().getRows().get(0)[5],
                            pipeLine));
                    metrics.add(new Metric(MetricType.new_vulnerabilities,
                            report.getSummary().getRows().get(0)[6],
                            pipeLine));
                    metrics.add(new Metric(MetricType.new_code_smells,
                            report.getSummary().getRows().get(0)[7],
                            pipeLine));
                    metrics.add(new Metric(MetricType.new_technical_debt,
                            report.getSummary().getRows().get(0)[8],
                            pipeLine));
                    String newUnit = report.getSummary().getRows().get(0)[9];
                    if(newUnit.contains(".")) {
                        newUnit = newUnit.substring(0,newUnit.indexOf("."));
                    }
                    metrics.add(new Metric(MetricType.new_unit_test_coverage_rate,
                            newUnit,
                            pipeLine));
                    metrics.add(new Metric(MetricType.new_uncovered_lines,
                            report.getSummary().getRows().get(0)[10],
                            pipeLine));
                    break;
                case dependency_check:
                    metrics.add(new Metric(MetricType.dc_high, report.getSummary().getDataMap().get("High")[0], pipeLine, report.getAttachmentId(), report.getLink()));
                    metrics.add(new Metric(MetricType.dc_medium, report.getSummary().getDataMap().get("Medium")[0], pipeLine, report.getAttachmentId(), report.getLink()));
                    break;
                case sql_scan:
                    metrics.add(new Metric(MetricType.sql_imcompatible, report.getSummary().getDataMap().get("imcompatible")[0], pipeLine, report.getAttachmentId(), report.getLink()));
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error("collect metric error:", e);
        }
        return metrics;
    }


    private void genK8sDeployFile(PipelineExecuteResult result, String consoleText) {
        //判断日志中是否有部署结果，有的话则通过mq发送到redmine中。
        sendK8sDeploy(consoleText, result.getPipeline());
        //删除磁盘中的yaml文件。后续查看可以在obs中查看。
//		removeK8sYaml(result.getProject());
    }

    private void judgeNpmInstall(PipelineExecuteResult result, String consoleText) {
        if (consoleText.contains("<artifactId>frontend-maven-plugin</artifactId>")||consoleText.contains("npm install")) {
            logger.info("流水线日志中存在关键字：<artifactId>docker-maven-plugin</artifactId>、npm install。判断项目为前端项目，");
            result.getPipeline().setNpmInstal(true);
        }else{
            result.getPipeline().setNpmInstal(false);
        }
//        pipelineServiceImpl.save(result.getPipeline());
    }

    /**
     * 更新 构架流水线历史记录信息
     *
     * @param result
     * @return jenkins日志信息
     * @throws IOException
     */
    private String updateAndAttachPipline(PipelineExecuteResult result) throws IOException {
        //fetch jenkins log
        String consoleText;

        Pipeline jenkinsPipeLine = jenkinsService.attachPipeLine(name);
        if (jenkinsPipeLine.getStages() != null) {
            jenkinsPipeLine.getStages().forEach(stage -> stage.setId(UUID.randomUUID().toString()));
        }
        Pipeline pipeLine = result.getPipeline();
        pipeLine.setStages(jenkinsPipeLine.getStages());
        pipeLine.setScmVersion(jenkinsPipeLine.getScmVersion());
        pipeLine.setDurationMillis(jenkinsPipeLine.getDurationMillis());
        pipeLine.setEndTimeMillis(jenkinsPipeLine.getEndTimeMillis());
        pipeLine.setName(jenkinsPipeLine.getName());
        pipeLine.setStartTimeMillis(jenkinsPipeLine.getStartTimeMillis());
        pipeLine.setStatus(jenkinsPipeLine.getStatus());

        consoleText = jenkinsService.getLastPipelineConsoleText(name);

        ResourceItem resource = new ResourceItem();
        resource.setName("console.txt");
        resource.setData(consoleText.getBytes(Charsets.UTF_8));
        String resourceId = "";
        try {
            resourceId = storageService.save(resource);
            pipeLine.setLogId(resourceId);
        } catch (Exception e) {
            pipeLine.setLogId(System.currentTimeMillis() + "");
            logger.error("华为obs服务器存储出现问题，捕捉异常，不中断程序:{}", e.getMessage());

        }


        //--------------------------------------------
        pipelineServiceImpl.updateBuildStatus(result.getProject(), jenkinsPipeLine.getStatus(), resourceId);
        return consoleText;


    }

    private void readProfile(Project project) {
        if (StringUtils.isNotBlank(profileId)) {
            PipelineProfile profile = profileService.findOne(profileId);
            if (profile != null) {
                project.setProfile(profile);
            }
        } else if (project.getDefaultProfile() != null) {
            //使用默认的profile
            project.setProfile(project.getDefaultProfile());
        }
    }

    private void saveDockerDigest(String consoleText, Pipeline pipeline) {
        List<Stage> stages = pipeline.getStages();
        Boolean dockerBuild = Boolean.FALSE;
        for (Stage stage : stages) {
            if (stage.getName().equals("build docker image")) {
                dockerBuild = Boolean.TRUE;
            }
        }
        if(dockerBuild){
            String[] logList = consoleText.split("\n");
            Set<String> digestSet = new LinkedHashSet<String>();
            String tag="";
            if(null!=logList&&logList.length>0){
                for (String logStr:logList){
                    int strStartIndex = logStr.indexOf("digest: ");
                    int strEndIndex = logStr.indexOf(" size:");
                    int urlStartIndex = logStr.indexOf("The push refers to repository [");
                    int urlEndIndex = logStr.indexOf("]");
                    int tagStartIndex = logStr.indexOf("iss devops docker image tag are: ");
                    if(tagStartIndex >= 0) {
                        String[] tagArr = logStr.substring(tagStartIndex + new String("iss devops docker image tag are: ").length()).split(",");
                        tag = tagArr[0];
                    }
                    if(urlStartIndex>=0&&urlEndIndex>=0&&urlStartIndex<urlEndIndex) {
                        String dockerUrl = logStr.substring(urlStartIndex + new String("The push refers to repository [").length(),urlEndIndex);
                        digestSet.add(dockerUrl);
                    }
                    if(strStartIndex>=0&&strEndIndex>=0&&strStartIndex<strEndIndex){
                        String digest = logStr.substring(strStartIndex + new String("digest: ").length(), strEndIndex);
                        digestSet.add(digest);
                    }
                }
            }

            if(null!=digestSet && digestSet.size()>0){
                String url = "";
                String namespace="";
                String imageName="";
                for(String digest : digestSet){
                    String[] digestArr = digest.split("/");
                    if(digestArr.length==3) {
                        url = "https://"+digestArr[0]+"/harbor/projects/"+digestArr[1]+"/repositories/"+digestArr[2]+"/artifacts/";
                        namespace = digestArr[1];
                        imageName = digestArr[2];
                    }else if(digestArr.length==1){
                        url+=digestArr[0];

                        DockerDigest dockerDigest = new DockerDigest();
                        dockerDigest.setDigest(digest);
                        dockerDigest.setPipelineId(pipeline.getId());
                        dockerDigest.setProjectId(pipeline.getProject().getId());
                        dockerDigest.setProjectName(pipeline.getProjectName());
                        dockerDigest.setUrl(url);
                        dockerDigest.setImageName(imageName);
                        dockerDigest.setNamespace(namespace);
                        dockerDigest.setTag(tag);
                        logger.info(url);
                        dockerDigestService.save(dockerDigest);
                        url="";
                        namespace="";
                        imageName="";
                    }
                }
            }

        }
    }

    private void sendK8sDeploy(String consoleText, Pipeline pipeline) {
        List<Stage> stages = pipeline.getStages();
        StringBuilder stringBuilder = new StringBuilder();
        JSONObject jsonData = new JSONObject();
        boolean sonarqube = false;
        for (Stage stage : stages) {
            stringBuilder.append("步骤:" + stage.getName() + " 执行耗时: " + stage.getDurationMillis() / 1000 + "秒" + " 执行状态：" + stage.getStatus() + "\n");
            if (stage.getName().equals("sonar scan")) {
                sonarqube = true;
            }
        }
        if (consoleText.contains("begin kubectl apply -f") && consoleText.contains("end kubectl apply -f")) {
            try (BufferedReader br = new BufferedReader(new StringReader(consoleText))) {
                boolean sub = false;
                StringBuilder data = new StringBuilder();
                String codeLine = "";
                while ((codeLine = br.readLine()) != null) {
                    if (codeLine.contains("end kubectl apply -f")) {
                        break;
                    }
                    if (sub) {
                        data.append(codeLine).append("\n");
                    }
                    if (codeLine.contains("begin kubectl apply -f")) {
                        sub = true;
                    }
                }
                String message = data.toString();
                //成功分为全部成功，未改变。 失败分为全部失败，部分失败。其它：未知
                if (message.contains("error")) {
                    message = "k8s自动部署结果：失败";
                } else if (message.contains("created")) {
                    message = "k8s自动部署结果：成功";
                } else if (message.contains("unchanged")) {
                    message = "k8s自动部署结果：未改变";
                }
                stringBuilder.append("\n" + message);

            } catch (Exception e) {
                logger.error("sendK8sDeploy.error: " + e, e);
            }
        }

    }

    private void callbackRedmine(Pipeline pipeline, StringBuilder stringBuilder, PipelineExecuteResult result) {

        stringBuilder.append("\n" + "CI平台详细日志： " + ciHost + "/ci/resources/" + pipeline.getLogId());
        JSONObject jsonData = new JSONObject();
        EventType devopsEventType = EventType.AUTO_DEPLOYMENT;
        jsonData.put("imageVersion", dockerImageTag);
        jsonData.put("subProjectIdentifier", pipeline.getProject().getName());
        jsonData.put("testSwitch", pipelineProfile.getTestSwitch());

        jsonData.put("projectId", pipeline.getProject().getPiplineEventProjectId());
        //项目类型
        jsonData.put("projectType", pipeline.getProject().getProjectType());
        //这个id可以与upms里的项目组关联
        jsonData.put("teamId", pipeline.getProject().getTeam().getGroupId().toString());
        //ci 项目组表的id
        jsonData.put("ciTeamId", pipeline.getProject().getTeam().getId());
        jsonData.put("teamName", pipeline.getProject().getTeam().getName());
        jsonData.put("gitPath", pipeline.getProject().getScm().getUrl());
        jsonData.put("stages", pipeline.getStages());
        jsonData.put("profileType", profileType);

        logger.info("流水线执行状态：{}", pipeline.getStatus().name());
        if (pipelineProfile.getTestSwitch() != AutoTestSwitch.CLOSED && pipeline.getStatus() == Status.SUCCESS) {
            logger.info("流水线运行状态成功，并且开启了自动化测试");
            //流水线运行状态
            jsonData.put("status", Status.WAIT_AUTO_TEST_RESULT.name());
            //构建过程中包含自动化测试，自动部署后需要dtp开启联调测试
            jsonData.put("nameSpace", pipelineProfile.getNameSpace());
            if (pipeline.getProject().getDeployConfigList() != null && !pipeline.getProject().getDeployConfigList().isEmpty()) {
                if (pipeline.getProject().getDeployConfigList().get(0) != null &&
                        StringUtils.isNotBlank(pipeline.getProject().getDeployConfigList().get(0).getIngressHost())) {
                    jsonData.put("ingressHost", pipeline.getProject().getDeployConfigList().get(0).getIngressHost());
                }
            }
            jsonData.put("nextstep", "autotest");
            devopsEventType = EventType.TRIGGER_AUTO_TEST;
        } else if (result.getPipeline().getStatusGuard() != StatusGuard.SUCCESS) {
            jsonData.put("status", Status.ASSERT_WARNRULE_FAILED.name());
        }

        //历史记录id， dtp回传该字段，ci更新指定记录 的测试报告
        jsonData.put("pipelineHistoryId", pipeline.getId());

        //构建过程对象 度量平台使用
        jsonData.put("pipelineProfile",JSONObject.toJSON(pipelineProfile));
        //消息类型
        jsonData.put("devopsEventType", devopsEventType);
        logger.info("构建类型:{},消息作用:{}", profileType, devopsEventType);
        jsonData.put("message", "CI平台完成构建,可登录CI平台，查看构建详情！" + stringBuilder.toString());
        if (StringUtils.isNotBlank(redmineUser) && StringUtils.isNotBlank(redmineCode)) {
            logger.info("redmineUser不为空:{}", redmineUser);
            if(redmineUser.indexOf("@")>-1) {
                jsonData.put("userName", redmineUser);
            }else{
                jsonData.put("userName", redmineUser+ properties.getProperty("email.suffix"));
            }
            jsonData.put("code", redmineCode);
        } else {
            if(user!=null) {
                logger.info("redmineUser为空,特殊处理，通过邮箱获取:{}", user.getEmail());
                jsonData.put("userName", user.getEmail());
                logger.info("转换后的user:{}", jsonData.getString("userName"));
            }else{
                jsonData.put("userName", "d-fesci"+ properties.getProperty("email.suffix"));
            }
            String tagName= "";
            if(StringUtils.isNotBlank(pipeline.getTargetBranch())) {
                tagName= pipeline.getTargetBranch();
            }else if(StringUtils.isNotBlank(pipeline.getSourceBranch())){
                tagName= pipeline.getSourceBranch();
            }
            if(tagName.indexOf("-")>-1) {
                jsonData.put("code", StringUtils.isEmpty(tagName) ? null : tagName.split("-")[1]);
            }else {
                logger.info("提测或者上线流程，批量打包，无法获取单个的开发任务号");
                jsonData.put("code","0");
            }
            logger.info("转换后的code为:{}", jsonData.getString("code"));
        }
        logger.info("CI构建完成，准备发送消息: {},", jsonData);
        pipeline.setEmail(jsonData.getString("userName"));
        //历史记录对象 度量平台使用
        jsonData.put("pipelineHistory",JSONObject.toJSON(pipeline));
        sendRabbitMq.sendMsg(jsonData.toString());
        pipelineServiceImpl.save(pipeline);
        if (pipelineProfile.getTestSwitch() != AutoTestSwitch.CLOSED) {
            try {
                int testDelayTime = pipelineProfile.getTestDelayTime() == null ? 3 : pipelineProfile.getTestDelayTime();
                Thread.sleep(testDelayTime * 60 * 1000);
                logger.info("CI消息发送完成，本消息触发自动化测试，进行监控");
                DingtalkWebHookUtil.sendDingtalkGroupMsgBymq(devopsEventType.getName(),
                        "CI完成自动构建，发送通知给自动化测试平台，开启监控：" + jsonData.toString(),
                        pipelineExecuteService.getDingtalkSecret(), pipelineExecuteService.getAccessToken(),
                        sendRabbitMq,MsgProfileType.monitoringDtpTestRun);
            } catch (InterruptedException e) {
               logger.error("发起自动化测试休眠中断",e);
            }
        }
    }




    private void renderFailedMailAndSend(String name, Project project, Pipeline pipeLine)
            throws Exception {
        //渲染邮件
        Template mailTemplate = configuration.getTemplate("mail-failed.ftl");

        Map<String, Object> context = this.reportModel;
        context.put("project", project);
        context.put("pipeline", pipeLine);
        context.put("buildUrl", ciHost + "ci/resources/" + pipeLine.getLogId());


        String mailHtml = FreeMarkerTemplateUtils.processTemplateIntoString(mailTemplate, context);
        //发送邮件
//		String subject= defaultSubject+project.getDescription()+"("+project.getName()+")构建"+pipeLine.getStatus();
        mailSender.sendMail(getReceiverMailList(project), getCCMailList(project), mailHtml,
                project.getDescription() + "(" + project.getName() + ")构建" + pipeLine.getStatus(),MsgProfileType.pipelineFail,project.getTeam().getCode());
    }

    private void renderMailAndSend(String name, Project project, Pipeline pipeLine,
                                   Map<String, Report> typedReport) throws Exception {
        //渲染邮件
        Template mailTemplate = configuration.getTemplate("mail.ftl");
        Map<String, Object> context = this.reportModel;
        context.put("project", project);
        context.put("pipeline", pipeLine);
        context.put("reports", typedReport);

        context.put("buildUrl", ciHost + "ci/resources/" + pipeLine.getLogId());
//        logger.info("ci渲染邮件数据：{}", net.sf.json.JSONObject.fromObject(context));
        String mailHtml = FreeMarkerTemplateUtils.processTemplateIntoString(mailTemplate, context);
        //发送邮件
        logger.info("ci发送邮件内容 {}", mailHtml);

        mailSender.sendMail(getReceiverMailList(project), getCCMailList(project), mailHtml, null,MsgProfileType.pipelineFail,project.getTeam().getCode());
    }


    private String[] getReceiverMailList(Project project) {
        if (user != null) {
            return new String[]{user.getEmail()};
        }
        if (project == null || project.getTeam() == null || project.getTeam().getMembers() == null) {
            return null;
        }
        List<String> emails = Lists.newArrayList();
        project.getTeam().getMembers().forEach(member -> {
            List<Role> roles = member.getRoles();
            if (!Role.existRole(roles, RoleEnum.admin)) {
                emails.add(member.getEmail());
            }
        });

        return emails.toArray(new String[0]);
    }

    private String[] getCCMailList(Project project) {
        if (user != null) {
            return new String[]{user.getEmail()};
        }
        if (project == null || project.getTeam() == null || project.getTeam().getMembers() == null) {
            return null;
        }
        List<String> emails = Lists.newArrayList();
        project.getTeam().getMembers().forEach(member -> {
            List<Role> roles = member.getRoles();
            if (Role.existRole(roles, RoleEnum.admin) || Role.existRole(roles, RoleEnum.teamleader)) {
                emails.add(member.getEmail());
            }
        });

        return emails.toArray(new String[0]);
    }


    public boolean isSendMail() {
        return sendMail;
    }


    public void setSendMail(boolean sendMail) {
        this.sendMail = sendMail;
    }


    public String getRootUrl() {
        return rootUrl;
    }


    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }
}
