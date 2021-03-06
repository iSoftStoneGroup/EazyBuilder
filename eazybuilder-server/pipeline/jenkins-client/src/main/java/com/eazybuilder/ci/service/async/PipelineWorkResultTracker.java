package com.eazybuilder.ci.service.async;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import com.eazybuilder.ci.util.HttpUtil;
import freemarker.template.*;
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
    private static Logger logger = LoggerFactory.getLogger(PipelineWorkResultTracker.class);


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
    //??????????????????????????????
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
        //?????????????????????????????????????????????????????????????????????????????????gitlab?????????
        if (StringUtils.isNotBlank(redmineUser)) {
            user = new User();
            if (!redmineUser.contains("@eazybuilder.com")) {
                user.setEmail(redmineUser + "@eazybuilder.com");
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
        //????????????
        metrics.add(new Metric(MetricType.vulner_blocker,
                report.getSummary().getRows().get(1)[1],
                pipeLine));
        metrics.add(new Metric(MetricType.vulner_critical,
                report.getSummary().getRows().get(1)[2],
                pipeLine));
        metrics.add(new Metric(MetricType.vulner_major,
                report.getSummary().getRows().get(1)[3],
                pipeLine));
        //????????????
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
        logger.info("jenkins???????????????????????????");
        if (pipeLine == null) {
            logger.info("Pipeline??????");
            return result;
        }
        try {
            String consoleText = updateAndAttachPipline(result);
            //?????????????????????????????????
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
            logger.info("ci??????????????????{}", net.sf.json.JSONObject.fromObject(dockerImage));
            dockerImageService.save(dockerImage);
            //???redmine???????????????????????????????????????
            dockerImageTag = url + dockerImageTag;
            saveDockerDigest(consoleText, result.getPipeline());
//            genK8sDeployFile(result, consoleText);
            //???????????????????????????????????????????????????????????????
            judgeNpmInstall(result,consoleText);
            messageBuilder.append("??????:").append(result.getProject().getDescription()).append(result.getProject().getName()).append("\n");
            String pipelineUid = pipeLine.getId();
            codeReport.put("devopsEventType", "codeReport");

            codeReport.put("pipelineUid", pipelineUid);
            codeReport.put("stages", pipeLine.getStages());
            if (pipeLine.getStatus() == Status.SUCCESS || pipeLine.getStatus() == Status.UNSTABLE) {
                logger.info("ci??????sonarqube??????????????????");
                loadReport(result);
                Map<String,Report> typedReport = result.getTypedReport();
                logger.info("ci??????sonarqube?????????????????? {}", JSONObject.toJSONString(typedReport));
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
                    logger.info("??????????????????????????????????????????????????????????????????????????????");
                }
            } else {
                result.failed();
                messageBuilder.append("??????????????????").append("\n");
                logger.info("???????????????ci????????????????????????");
                sendMail(result, null);
                logger.info("???????????????ci????????????????????????");
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
        		logger.info("????????????????????????????????????:{}",codeReport);
    			sendRabbitMq.sendMsg(codeReport.toString());
        	}else {
        		logger.warn("?????????????????????????????????????????????????????????????????????");
        	}
            pipelineServiceImpl.save(pipeLine);
            logger.info("?????????????????????redmine");
            try {
                callbackRedmine(pipeLine, messageBuilder, result);
            } catch (Exception e) {
                logger.error("??????rabbitmq?????????????????????{}-{}",e.getMessage(),e);
            }
        }
    }

    private StringBuilder genEmailMsg(Map<String, Report> typedReport) {
        StringBuilder messageBuilder = new StringBuilder();
        if (typedReport.containsKey("sonar")) {

            messageBuilder.append("??????????????????").append("\n");
            messageBuilder.append("????????????????????????").append("\n");
            messageBuilder.append("    ?????? ").append(" ?????? ").append(" ?????? ").append("\n");
            messageBuilder.append("BUG???");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(0)[1]).append("  ");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(0)[2]).append("  ");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(0)[3]).append("\n");
            messageBuilder.append("?????????");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(1)[1]).append("  ");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(1)[2]).append("  ");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(1)[3]).append("\n");
            messageBuilder.append("?????????");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(2)[1]).append("  ");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(2)[2]).append("  ");
            messageBuilder.append(typedReport.get("sonar").getSummary().getRows().get(2)[3]).append("\n");

            messageBuilder.append("??????????????????????????????????????? ").append(typedReport.get("sonar").getLink()).append("\n");
        } else {
            messageBuilder.append(JSONObject.toJSONString(typedReport));

        }
        return messageBuilder;
    }

    private void sendMail(PipelineExecuteResult result, Map<String, Report> typedReport) throws Exception {
        if (!sendMail) {
            logger.info("ci?????????????????????");
            return;
        }
        if (result.isSuccess()||result.getSuccess().equals("WAIT_AUTO_TEST_RESULT")) {
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
        //????????????
        List<Metric> metrics = Lists.newArrayList();
        Project project = result.getProject();
        long start = System.currentTimeMillis();

        if (project.getProfile().isSkipCloneCode()) {
            logger.warn("??????????????????????????????????????????????????????mvn????????????????????????????????????");
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
                //??????????????????sonarkey(????????????)?????????sonarkey?????????(CI?????????sonar project key????????????)
                if (project.getSonarKey() == null || !report.getSonarKey().equals(project.getSonarKey())) {
                    project.setSonarKey(report.getSonarKey());
                    //update project (set sonar project key)
                    projectService.save(project);
                }
            }
            typedReport.put(report.getType().name(), report);
            /**
             * ????????????????????????????????????????????????????????????
             */
             List<Metric> metricsResults =  loadMetricFromReport(result.getPipeline(), report);
            metrics.addAll(metricsResults);
            logger.info("#################metrics??????????????????"+JSONArray.toJSON(metrics).toString());
        }
        //???????????????????????????java????????????????????????????????????????????????????????????????????????????????????
        if(result.getPipeline().getStatus()==Status.SUCCESS) {
            if (result.getPipeline().getProject().getProjectType() == ProjectType.java || result.getPipeline().getProject().getProjectType() == ProjectType.gradle|| result.getPipeline().getProject().getProjectType() == ProjectType.net) {
                boolean checkResult = checkMetric(metrics, guardMap, result.getPipeline().isNpmInstal());
                logger.info("???????????????????????????????????????????????????{}", checkResult);
                if(checkResult) {
                    result.getPipeline().setStatusGuard(StatusGuard.SUCCESS);
                }else{
                    result.getPipeline().setStatusGuard(StatusGuard.FAILED);
                }
            } else {
                logger.info("?????????????????????????????????????????????");
                result.getPipeline().setStatusGuard(StatusGuard.NOT_EXECUTED);
            }
        }else{
            logger.info("?????????????????????,?????????????????????");
            result.getPipeline().setStatusGuard(StatusGuard.FAILED);
        }
        logger.info("#################metrics??????????????????"+JSONArray.toJSON(metrics).toString());
        saveMetric(project, metrics);
        pipelineServiceImpl.getLastBuild(project.getId()).setMetricList(metrics);
        result.setTypedReport(typedReport);
    }

    /**
     *  ????????????????????????????????????????????????????????????????????????????????????????????????????????????java??????
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
            logger.info("???????????????????????????????????????????????????????????????");
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
                            //flase?????????
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
            //????????????????????????0??????????????????????????????????????????
            if(null!=newUnitTestCoverageMetric&&!newUnitTestCoverageRateFlag&&newUncoveredLines>0){
                newUnitTestCoverageMetric.setAssertSucceed(false);
            }else if(null!=newUnitTestCoverageMetric){
                newUnitTestCoverageMetric.setAssertSucceed(true);
            }
        }
        if(null==checkMetrics) {
        	return true;
        }
        logger.info("#################metrics??????????????????"+JSONArray.toJSON(checkMetrics).toString());
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
     * ????????????????????????????????????????????????????????????
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
        //?????????????????????????????????????????????????????????mq?????????redmine??????
        sendK8sDeploy(consoleText, result.getPipeline());
        //??????????????????yaml??????????????????????????????obs????????????
//		removeK8sYaml(result.getProject());
    }

    private void judgeNpmInstall(PipelineExecuteResult result, String consoleText) {
        if (consoleText.contains("<artifactId>frontend-maven-plugin</artifactId>")||consoleText.contains("npm install")) {
            logger.info("????????????????????????????????????<artifactId>docker-maven-plugin</artifactId>???npm install?????????????????????????????????");
            result.getPipeline().setNpmInstal(true);
        }else{
            result.getPipeline().setNpmInstal(false);
        }
//        pipelineServiceImpl.save(result.getPipeline());
    }

    /**
     * ?????? ?????????????????????????????????
     *
     * @param result
     * @return jenkins????????????
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
            logger.error("??????obs????????????????????????????????????????????????????????????:{}", e.getMessage());

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
            //???????????????profile
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
            stringBuilder.append("??????:" + stage.getName() + " ????????????: " + stage.getDurationMillis() / 1000 + "???" + " ???????????????" + stage.getStatus() + "\n");
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
                //??????????????????????????????????????? ?????????????????????????????????????????????????????????
                if (message.contains("error")) {
                    message = "k8s???????????????????????????";
                } else if (message.contains("created")) {
                    message = "k8s???????????????????????????";
                } else if (message.contains("unchanged")) {
                    message = "k8s??????????????????????????????";
                }
                stringBuilder.append("\n" + message);

            } catch (Exception e) {
                logger.error("sendK8sDeploy.error: " + e, e);
            }
        }

    }

    private void callbackRedmine(Pipeline pipeline, StringBuilder stringBuilder, PipelineExecuteResult result) {

        stringBuilder.append("\n" + "CI????????????????????? " + ciHost + "/ci/resources/" + pipeline.getLogId());
        JSONObject jsonData = new JSONObject();
        EventType devopsEventType = EventType.AUTO_DEPLOYMENT;
        jsonData.put("imageVersion", dockerImageTag);
        jsonData.put("subProjectIdentifier", pipeline.getProject().getName());
        jsonData.put("testSwitch", pipelineProfile.getTestSwitch());

        jsonData.put("projectId", pipeline.getProject().getPiplineEventProjectId());
        //????????????
        jsonData.put("projectType", pipeline.getProject().getProjectType());
        //??????id?????????upms?????????????????????
        jsonData.put("teamId", pipeline.getProject().getTeam().getGroupId().toString());
        //ci ???????????????id
        jsonData.put("ciTeamId", pipeline.getProject().getTeam().getId());
        jsonData.put("teamName", pipeline.getProject().getTeam().getName());
        jsonData.put("gitPath", pipeline.getProject().getScm().getUrl());
        jsonData.put("stages", pipeline.getStages());
        jsonData.put("profileType", profileType);

        logger.info("????????????????????????{}", pipeline.getStatus().name());
        if (pipelineProfile.getTestSwitch() != AutoTestSwitch.CLOSED && pipeline.getStatus() == Status.SUCCESS) {
            logger.info("????????????????????????????????????????????????????????????");
            //?????????????????????
            jsonData.put("status", Status.WAIT_AUTO_TEST_RESULT.name());
            //????????????????????????????????????????????????????????????dtp??????????????????
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

        //????????????id??? dtp??????????????????ci?????????????????? ???????????????
        jsonData.put("pipelineHistoryId", pipeline.getId());

        //?????????????????? ??????????????????
        jsonData.put("pipelineProfile",JSONObject.toJSON(pipelineProfile));
        //????????????
        jsonData.put("devopsEventType", devopsEventType);
        logger.info("????????????:{},????????????:{}", profileType, devopsEventType);
        jsonData.put("message", "CI??????????????????,?????????CI??????????????????????????????" + stringBuilder.toString());
        if (StringUtils.isNotBlank(redmineUser) && StringUtils.isNotBlank(redmineCode)) {
            logger.info("redmineUser?????????:{}", redmineUser);
            if(redmineUser.indexOf("@")>-1) {
                jsonData.put("userName", redmineUser);
            }else{
                jsonData.put("userName", redmineUser+"@eazybuilder.com");
            }
            jsonData.put("code", redmineCode);
        } else {
            if(user!=null) {
                logger.info("redmineUser??????,?????????????????????????????????:{}", user.getEmail());
                jsonData.put("userName", user.getEmail());
                logger.info("????????????user:{}", jsonData.getString("userName"));
            }else{
                jsonData.put("userName", "xxx@eamil.com");
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
                logger.info("??????????????????????????????????????????????????????????????????????????????");
                jsonData.put("code","0");
            }
            logger.info("????????????code???:{}", jsonData.getString("code"));
        }
        logger.info("CI?????????????????????????????????: {},", jsonData);
        pipeline.setEmail(jsonData.getString("userName"));
        //?????????????????? ??????????????????
        jsonData.put("pipelineHistory",JSONObject.toJSON(pipeline));
        sendRabbitMq.sendMsg(jsonData.toString());
        pipelineServiceImpl.save(pipeline);
        if (pipelineProfile.getTestSwitch() != AutoTestSwitch.CLOSED) {
            logger.info("CI??????????????????????????????????????????????????????????????????");
            DingtalkWebHookUtil.sendDingtalkMsgBymq(devopsEventType.getName(),
                    "CI???????????????????????????????????????????????????????????????????????????" + jsonData.toString(),
                    pipelineExecuteService.getDingtalkSecret(), pipelineExecuteService.getAccessToken(),
                    sendRabbitMq,MsgProfileType.monitoringDtpTestRun);
        }
    }




    private void renderFailedMailAndSend(String name, Project project, Pipeline pipeLine)
            throws Exception {
        //????????????
        Template mailTemplate = configuration.getTemplate("mail-failed.ftl");

        Map<String, Object> context = this.reportModel;
        context.put("project", project);
        context.put("pipeline", pipeLine);
        context.put("buildUrl", ciHost + "ci/resources/" + pipeLine.getLogId());


        String mailHtml = FreeMarkerTemplateUtils.processTemplateIntoString(mailTemplate, context);
        //????????????
//		String subject= defaultSubject+project.getDescription()+"("+project.getName()+")??????"+pipeLine.getStatus();
        mailSender.sendMail(getReceiverMailList(project), getCCMailList(project), mailHtml,
                project.getDescription() + "(" + project.getName() + ")??????" + pipeLine.getStatus(),MsgProfileType.pipelineFail,project.getTeam().getCode());
    }

    private void renderMailAndSend(String name, Project project, Pipeline pipeLine,
                                   Map<String, Report> typedReport) throws Exception {
        //????????????
        Template mailTemplate = configuration.getTemplate("mail.ftl");
        Map<String, Object> context = this.reportModel;
        context.put("project", project);
        context.put("pipeline", pipeLine);
        context.put("reports", typedReport);

        context.put("buildUrl", ciHost + "ci/resources/" + pipeLine.getLogId());
//        logger.info("ci?????????????????????{}", net.sf.json.JSONObject.fromObject(context));
        String mailHtml = FreeMarkerTemplateUtils.processTemplateIntoString(mailTemplate, context);
        //????????????
        logger.info("ci?????????????????? {}", mailHtml);

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
