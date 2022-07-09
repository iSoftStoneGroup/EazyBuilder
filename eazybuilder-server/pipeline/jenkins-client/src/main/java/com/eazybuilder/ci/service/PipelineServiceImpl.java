package com.eazybuilder.ci.service;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.constant.AutoTestSwitch;
import com.eazybuilder.ci.controller.vo.*;
import com.eazybuilder.ci.dto.ProjectLastBuildInfo;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.entity.devops.IssuesStatus;
import com.eazybuilder.ci.entity.devops.Online;
import com.eazybuilder.ci.entity.devops.Release;
import com.eazybuilder.ci.entity.devops.ReleaseProject;
import com.eazybuilder.ci.entity.report.Report;
import com.eazybuilder.ci.entity.report.ResourceItem;
import com.eazybuilder.ci.entity.report.Stage;
import com.eazybuilder.ci.entity.report.Status;
import com.eazybuilder.ci.event.EventBusSupport;
import com.eazybuilder.ci.jenkins.JenkinsPipelineEnv;
import com.eazybuilder.ci.jenkins.JenkinsPipelineService;
import com.eazybuilder.ci.mail.MailSenderHelper;
import com.eazybuilder.ci.rabbitMq.SendRabbitMq;
import com.eazybuilder.ci.repository.FileResourceDao;
import com.eazybuilder.ci.repository.PipelineDao;
import com.eazybuilder.ci.service.async.PipelineExecuteResult;
import com.eazybuilder.ci.service.async.PipelineWorkResultTracker;
import com.eazybuilder.ci.storage.ResourceStorageService;
import com.eazybuilder.ci.util.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;



@Service

public class PipelineServiceImpl extends AbstractCommonServiceImpl<PipelineDao, Pipeline>
        implements CommonService<Pipeline> {

    private static Logger logger = LoggerFactory.getLogger(PipelineServiceImpl.class);


    public static  final String SUBLOG_BEGIN_SIGN = "========%s start========";

    public static  final String SUBLOG_END_SIGN = "========%s end========";


    private static final String LAST_SIGN = "Declarative: Post Actions";

    @Resource
    FileResourceDao fileResourceDao;


    @Resource
    ReleaseProjectService releaseProjectService;

    @Autowired
    CIPackageService ciPackageService;

    @Resource
    ReleaseService releaseService;

    @Resource
    OnlineService onlineService;

    @Resource
    DockerImageService dockerImageService;
    @Resource
    SendRabbitMq sendRabbitMq;
    @Autowired
    ProjectService projectService;

    @Autowired
    PipelineProfileService profileService;

    @Autowired
    ResourceStorageService storageService;

    @Autowired
    TeamServiceImpl teamServiceImpl;

    @Autowired
    TeamResourceService teamResourceService;

    @Autowired
    MetricService metricService;

    @Resource
    PipelineBuildService pipelineBuildService;


    @Autowired
    Configuration configuration;

    @Value("${base.url}")
    String baseUrl;

    @Value("${ci.hosts}")
    String ciHost;

    @Value("${ci.k8s-folder}")
    String ciK8s;

    @Value("${ci.scm.user}")
    String scmUser;
    @Value("${ci.scm.password}")
    String scmPass;

    @Autowired
    MailSenderHelper mailSender;

    @Autowired
    EventBusSupport eventBus;

    @Autowired
    SystemPropertyService configService;

    @Autowired
    HostInfoService hostService;

    @Autowired
    DockerDigestService dockerDigestService;


    @Autowired
    PipelineExecuteService pipelineExecuteService;

//	ExecutorService threadPool=Executors.newFixedThreadPool(5);
    /**
     * 1、corePoolSize线程池的核心线程数
     * 2、maximumPoolSize能容纳的最大线程数
     * 3、keepAliveTime空闲线程存活时间
     * 4、unit 存活的时间单位
     * 5、workQueue 存放提交但未执行任务的队列
     * 6、threadFactory 创建线程的工厂类
     * 7、handler 等待队列满后的拒绝策略
     */
    ExecutorService threadPool = new ThreadPoolExecutor(20, 30,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(15),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    Map<String, ThreadPoolExecutor> projectPipelineMap = new ConcurrentHashMap<String, ThreadPoolExecutor>();

    @Autowired
    RedissonClient client;

    @Autowired
    JenkinsPipelineEnv env;

    Map<String, List<Report>> reportCache;

    Map<String, ProjectLastBuildInfo> lastBuildInfo;

    Random r = new Random();

    private String dockerImageTag;

    public static final String JENKINSURL_TRUE = ",";

    public String getDockerImageTag() {
        if (StringUtils.isNotBlank(dockerImageTag)) {
            return dockerImageTag;
        } else {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
            return df.format(new Date());
        }
    }

    @Autowired
    private EntityManager entityManager;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void initReportCache() {
        reportCache = client.getMap("pipeline-callback-cache");
        lastBuildInfo = client.getMap("project-last-build-cache");
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public void updateBuildStatus(Project prj, Status status) {
        ProjectLastBuildInfo lastBuild = getLastBuildRecord(prj);
        lastBuild.setBuildStatus(status);
        lastBuild.setLastUpdate(new Date());
        lastBuildInfo.put(prj.getId(), lastBuild);
    }

    public void updateBuildStatus(Project prj, Status status, String logId) {
        ProjectLastBuildInfo lastBuild = getLastBuildRecord(prj);
        lastBuild.setBuildStatus(status);
        lastBuild.setLastUpdate(new Date());
        lastBuild.setLastLogId(logId);
        lastBuildInfo.put(prj.getId(), lastBuild);
    }


    public void updateProjectMetrcs(Project prj, List<Metric> metrics) {
        if (metrics != null) {
            ProjectLastBuildInfo lastBuild = getLastBuildRecord(prj);
            metrics.forEach(metric -> {
                if (metric.getType() != null) {
                    lastBuild.getMetrics().put(metric.getType(), metric);
                }
            });
            lastBuild.setLastUpdate(new Date());
            lastBuildInfo.put(prj.getId(), lastBuild);
        }
    }

    public ProjectLastBuildInfo getLastBuildRecord(Project prj) {
//        ProjectLastBuildInfo lastBuild = lastBuildInfo.get(prj.getId());
        ProjectLastBuildInfo lastBuild = null;
        if (lastBuild == null||!lastBuild.getName().equals(prj.getName())) {
            lastBuild = ProjectLastBuildInfo.build(prj);
            lastBuild.setJenkinsUrl(env.getJenkinsUrl());
            lastBuild.setSonarUrl(env.getSonarUrl());
            lastBuild.setBuildStatus(Status.NOT_EXECUTED);
        }
        lastBuild.setSonarKey(prj.getSonarKey());
        lastBuild.setProjectType(prj.getProjectType());


        return lastBuild;
    }

    @PreDestroy
    public void closePool() {
        threadPool.shutdown();
    }

    /**
     * 根据项目ID，分页查询历史构建信息(已完成的，不包含进行中的)
     *
     * @param projectId
     * @param pageable
     * @return
     */
    public Page<Pipeline> pageQueryByProjectId(String projectId, Pageable pageable) {
        return dao.findAll(QPipeline.pipeline.project.id.eq(projectId), pageable);
    }

    /**
     * 根据项目ID，查询所有历史构建信息(已完成的，不包含进行中的)
     *
     * @param projectId
     * @return
     */
    public Iterable<Pipeline> findAllByProjectId(String projectId) {
        return dao.findAll(QPipeline.pipeline.project.id.eq(projectId));
    }

    protected JenkinsPipelineService createJenkinsService(Project project, String dockerImageTag) {
        Iterable<TeamResource> trs = teamResourceService.findAll(
                QTeamResource.teamResource.teamId.eq(project.getTeam().getId()));
        try {
            if (trs != null && trs.iterator() != null && trs.iterator().hasNext()) {
                TeamResource tr = trs.iterator().next();
                String jenkinsUrl = tr.getJenkinsUrl();
                if (StringUtils.isNotBlank(jenkinsUrl)) {
                    if (jenkinsUrl.contains(JENKINSURL_TRUE)) {
                        jenkinsUrl = choiseRandom(jenkinsUrl.split(JENKINSURL_TRUE));
                    }
                } else {
                    jenkinsUrl = env.getJenkinsUrl();
                }
                String sonarUrl = tr.getSonarUrl();
                if (StringUtils.isNotBlank(sonarUrl)) {
                    if (sonarUrl.contains(JENKINSURL_TRUE)) {
                        sonarUrl = choiseRandom(sonarUrl.split(JENKINSURL_TRUE));
                    }
                } else {
                    sonarUrl = env.getSonarUrl();
                }

                return new JenkinsPipelineService(jenkinsUrl, sonarUrl, configuration, tr.isJenkinsK8sSupport(), env, dockerImageTag);
            } else {
                //use default jenkins server & sonar server
                return new JenkinsPipelineService(env.getJenkinsUrl(), env.getSonarUrl(), configuration, env.isK8sSupport(), env, dockerImageTag);
            }
        } catch (Exception e) {
            logger.error("error create jenkins pipeline service", e);
            throw new RuntimeException(e);
        }
    }

    private String choiseRandom(String[] split) {
        return split[r.nextInt(split.length)];
    }

    /**
     * 获取工程最新的构建状态
     *
     * @param projectId
     * @return
     */
    public ProjectLastBuildInfo getLastBuild(String projectId) {
        return lastBuildInfo.get(projectId);
    }

    /**
     * 实时获取指定项目的流水线构建信息(包括正在构建的情况)
     *
     * @param projectName
     * @return
     */
    public Pipeline getLastPipeline(String projectName) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        Project project = projectService.findByName(projectName);
        ProjectLastBuildInfo lastBuild = lastBuildInfo.get(project.getId());
        JenkinsPipelineService jenkinsService = null;
        if (lastBuild == null) {
            jenkinsService = createJenkinsService(project, df.format(new Date()));
        } else {
            try {
                //使用最后一次的构建服务器地址
                jenkinsService = new JenkinsPipelineService(lastBuild.getJenkinsUrl(),
                        lastBuild.getSonarUrl(), configuration, false, env, df.format(new Date()));
                Pipeline pipeline = jenkinsService.getLastPipeline(project.getJobName());
                pipeline.setProject(project);
                return pipeline;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
    
    public ProjectLastBuildInfo getProjectLastBuildInfoByProjectId(String projectId) {
    	 Project project = projectService.findOne(projectId);
    	 ProjectLastBuildInfo lastBuild = lastBuildInfo.get(project.getId());
    	 
    	 return lastBuild;
    }

    public Pipeline getLastPipelineByProjectId(String projectId) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        Project project = projectService.findOne(projectId);
        ProjectLastBuildInfo lastBuild = lastBuildInfo.get(project.getId());
        JenkinsPipelineService jenkinsService = null;
        List<Pipeline> pipelineList = null;
        if (lastBuild == null) {
            jenkinsService = createJenkinsService(project, df.format(new Date()));
        } else {
            try {
                //使用最后一次的构建服务器地址
                jenkinsService = new JenkinsPipelineService(lastBuild.getJenkinsUrl(),
                        lastBuild.getSonarUrl(), configuration, false, env, df.format(new Date()));
                Pipeline pipeline = jenkinsService.getLastPipeline(project.getJobName());
                pipeline.setProject(project);
                return pipeline;
            } catch (Exception e) {
                //throw new RuntimeException(e);
                pipelineList = (List<Pipeline>) dao.findAll(QPipeline.pipeline.project.id.eq(projectId),QPipeline.pipeline.endTimeMillis.desc());
                if(null!=pipelineList&&pipelineList.size()>0){
                    Pipeline pipeline = pipelineList.get(0);
                    pipeline.setProject(project);
                    return pipeline;
                }else{
                    return null;
                }
            }
        }
        return null;

    }

    public void triggerPipeline(Map<Project, List<ProjectBuildVo>> projectProfileMap) throws Exception {
        for (Map.Entry<Project, List<ProjectBuildVo>> entry : projectProfileMap.entrySet()) {
            Project project = entry.getKey();
            List<ProjectBuildVo> pipelines = entry.getValue();
            //通过渲染freemarker脚本，在jenkins中执行相关操作。
            try {
                for (ProjectBuildVo projectBuildVo : pipelines) {
                    logger.info("gitlab代码提交/分支合并触发流水线 {}-{}", project.getName(), project.getImageTag());
                    Pipeline pipeline = triggerPipeline(project.getId(), projectBuildVo);
                    logger.info("保存的触发类型：{}",pipeline.getPipelineType());
                }
            } catch (Exception e) {
                logger.error("gitlab代码提交/分支合并触发的流水线出现异常" + e.getMessage(), e);
            }
        }
    }
    public Pipeline triggerPipeline(Project project,String dockerImageTag,ProjectBuildVo buildParam) throws Exception {
         return triggerPipeline(project, null,dockerImageTag,buildParam);
    }

    /**
     * 触发流水线（成功提交即返回，任务将异步完成）
     *
     * @param projectId
     * @throws Exception
     */
    public Pipeline triggerPipeline(String projectId, ProjectBuildVo buildParam) throws Exception {

        Project project = projectService.findOne(projectId);
        if (project == null) {
            throw new IllegalArgumentException("项目ID:" + projectId + "不存在");
        }
        if (StringUtils.isNotBlank(buildParam.getProfile())) {
            PipelineProfile profile = profileService.findOne(buildParam.getProfile());
            if (profile != null) {
                project.setProfile(profile);
            }
        } else if (project.getDefaultProfile() != null) {
            //使用默认的profile
            project.setProfile(project.getDefaultProfile());
        }
        if (StringUtils.isNotBlank(buildParam.getNameSpace())) {
            project.setNameSpace(buildParam.getNameSpace());
        }
        if (StringUtils.isNotBlank(buildParam.getArriveTagName())) {
            project.getScm().setArriveTagName(buildParam.getArriveTagName());
        }
        if (null != (buildParam.getReleaseDate())) {
            project.setReleaseDate(buildParam.getReleaseDate());
        }
        if (StringUtils.isNotBlank(buildParam.getRolloutVersion())) {
            project.setRolloutVersion(buildParam.getRolloutVersion());
        }
        if (StringUtils.isNotBlank(buildParam.getDbPassword())) {
            project.setDbPassword(buildParam.getDbPassword());
        }
        if (StringUtils.isNotBlank(buildParam.getDbUserName())) {
            project.setDbUserName(buildParam.getDbUserName());
        }
        if (StringUtils.isNotBlank(buildParam.getDbUrl())) {
            project.setDbUrl(buildParam.getDbUrl());
        }
        if (StringUtils.isNotBlank(buildParam.getGitlabApiUrl())) {
            project.setGitlabApiUrl(buildParam.getGitlabApiUrl());
        }
        if (StringUtils.isNotBlank(buildParam.getDockerImageTag())) {
            project.setDockerImageTag(buildParam.getDockerImageTag());
        }
        if (StringUtils.isNotBlank(buildParam.getReleaseDockerVersion())) {
            project.setDockerImageTag(buildParam.getReleaseDockerVersion());
        }
        if (StringUtils.isNotBlank(buildParam.getCreteBranchVersion())) {
            project.setBranchVersion(buildParam.getCreteBranchVersion());
        }
        if (StringUtils.isNotBlank(buildParam.getCreateTagVersion())) {
            project.setImageTag(buildParam.getReleaseDockerVersion());
            project.setTagVersion(buildParam.getCreateTagVersion());
        }
        if (StringUtils.isNotBlank(buildParam.getCreateTagDetail())) {
            project.setTagDetail(buildParam.getCreateTagDetail());
        }
        if (StringUtils.isNotBlank(buildParam.getRedmineCode())) {
            project.setRedmineCode(buildParam.getRedmineCode());
        }
        if (StringUtils.isNotBlank(buildParam.getRedmineUser())) {
            project.setRedmineUser(buildParam.getRedmineUser());
        }
        //在这里将vo的数据存到数据库 方便后续重新执行的时候使用。
        Pipeline pipeline = triggerPipeline(project, buildParam.getCreateTagVersion() != null ? buildParam.getCreateTagVersion() : "",buildParam);
        savePipelineBuild(pipeline,buildParam);
        return pipeline;
    }

    private void savePipelineBuild(Pipeline pipeline, ProjectBuildVo buildParam) {
        try {
            logger.info("将Pipeline和ProjectBuildVo转为PipelineBuild");
            PipelineBuild pipelineBuild = new PipelineBuild();
            BeanUtils.copyProperties(buildParam,pipelineBuild);
            pipelineBuild.setPipelineHistoryId(pipeline.getId());
            pipelineBuildService.save(pipelineBuild);
        } catch (BeansException e) {
            e.printStackTrace();
            logger.error("保存PipelineBuild出现异常：{}-{}",e.getMessage(),e);
        }

    }
    /**
     * 使用工程当前指定的profile，执行工程流水线
     */
    public Pipeline triggerPipeline(Project project, GitSystemPushEvent event,String dockerImageTag,ProjectBuildVo buildParam) throws Exception {
        if (project.getProfile() != null && project.getProfile().isDeployWar() && project.getProfile().getDeployInfo() != null) {
            try {
                String hostId = project.getProfile().getDeployInfo().getHostId();
                HostInfo host = hostService.findOne(hostId);
                if (host != null) {
                    project.getProfile().getDeployInfo().setHost(host);
                }
            } catch (Exception e) {
                logger.error("config error skip deploy war", e);
                project.getProfile().setDeployWar(false);
            }
        } else if (project.getDefaultProfile() != null && project.getProfile() == null) {
            project.setProfile(project.getDefaultProfile());
        }
        JenkinsPipelineService jenkinsService = createJenkinsService(project, dockerImageTag);
        //uid用于后续收集汇总本次构建报告
        String uid = UUID.randomUUID().toString();
        //project.setJobName(project.getName()+"-"+uid);
        project.setJobName(project.getName());

        //save pipeline first
        Pipeline ppl = new Pipeline();
        ppl.setId(uid);
        ppl.setProject(project);
        ppl.setProjectName(project.getName() + "/" + project.getDescription());
        ppl.setRunBranch(project.getScm().getArriveTagName());
        ppl.setRolloutVersion(project.getRolloutVersion());
        ppl.setReleaseDate(project.getReleaseDate());
        //设置构建过程
        ppl.setProfileName(project.getProfile().getName());
        ppl.setProfileId(project.getProfile().getId());

        //命名空间
        ppl.setNameSpace(project.getProfile().getNameSpace());

        if(StringUtils.isNotBlank(project.getScm().getArriveTagName())) {
            ppl.setTargetBranch(project.getScm().getArriveTagName());
        }else if(StringUtils.isNotBlank(project.getScm().getTagName())){
            ppl.setTargetBranch(project.getScm().getTagName());
        }else{
            ppl.setSourceBranch("master");
        }

        //触发类型
        if(buildParam.getPipelineType()!=null) {
            ppl.setPipelineType(buildParam.getPipelineType());
            if(buildParam.getPipelineType()==PipelineType.merge){
                ppl.setSourceBranch(buildParam.getSourceBranch());
                ppl.setTargetBranch(buildParam.getTargetBranch());
            }
            if(StringUtils.isNotBlank(buildParam.getCreateTagVersion())) {
                if (buildParam.getPipelineType() == PipelineType.online || buildParam.getPipelineType() == PipelineType.release) {
                    ppl.setPipelineVersion(buildParam.getCreateTagVersion());
                }
            }
        }
//        ppl.setAutotestResult("等待dtp平台回传测试报告");
        if(project.getProfile().getTestSwitch()!= AutoTestSwitch.CLOSED) {
            ppl.setDtpTask(true);
        }
        save(ppl);
        logger.info("执行流水线之前将project转为vo");
        //在这里new一个新的projectVo对象，后面流水线用vo代替project。
        PipelineBuildVo pipelineBuildVo = new PipelineBuildVo();
        BeanUtils.copyProperties(project,pipelineBuildVo);
        createK8sYaml(pipelineBuildVo);
        sqlTakeMin(pipelineBuildVo);
        User user = AuthUtils.getCurrentUser();
        ExecutorService projectPipelinePool = projectPipelineMap.get(project.getId());
        if(null== projectPipelinePool){
            projectPipelinePool = new ThreadPoolExecutor(0, 1,
                    30L, TimeUnit.MINUTES,
                    new LinkedBlockingQueue<>(15),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.AbortPolicy());
            projectPipelineMap.put(project.getId(), (ThreadPoolExecutor) projectPipelinePool);
        }

        logger.info("#######################任务数量："+String.valueOf(((ThreadPoolExecutor) projectPipelinePool).getActiveCount()));
        projectPipelinePool.submit(()->{
            logger.info("###########################uid"+uid);
            try{
                String name = jenkinsService.createPipeLine(pipelineBuildVo, uid, true);
                logger.info("pipe line :{} created. uid:{}", name, uid);

                cacheBuildInfo(project, jenkinsService, uid);
                jenkinsService.runPipeLine(name);



                Map<String, Object> reportModel = Maps.newHashMap();
                if (event != null) {
                    reportModel.put("event", event);
                }

                //异步跟踪执行结果
                PipelineWorkResultTracker asyncTracker = new PipelineWorkResultTracker(
                        user,
                        project.getProfile(),
                        dockerImageTag,
                        dockerImageService,
                        dockerDigestService,
                        pipelineBuildVo.getRedmineCode(),
                        pipelineBuildVo.getRedmineUser(),
                        ciK8s,
                        ciHost,
                        sendRabbitMq, projectService,
                        profileService,
                        jenkinsService,
                        this,
                        metricService,
                        configuration,
                        configService.getValue("base.url", baseUrl),
                        mailSender,
                        eventBus,
                        reportModel,
                        storageService,pipelineExecuteService);

                asyncTracker.setTrackInfo(project.getId(), project.getProfile() == null ? null : project.getProfile().getId(), name, uid);
                //send mail individually
                //执行完成立即发送邮件(按项目配置的组别)
                asyncTracker.setSendMail(true);
                Future future = threadPool.submit(asyncTracker);
                future.get(30L,TimeUnit.MINUTES);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
            logger.info("#################任务执行完成:"+project.getName()+"-"+uid);

        });


        return ppl;
    }

    private void sqlTakeMin(PipelineBuildVo pipelineBuildVo) throws IOException, SQLException {
        if(pipelineBuildVo.getProfile().isSqlTakeMin()){
            logger.info("开启了sql脱敏，准备执行脱敏脚本");
            FileResource fileResource = fileResourceDao.findById(pipelineBuildVo.getProfile().getSqlTakeMinUrl()).orElse(null);
            sqlTakeMinJdbc(pipelineBuildVo.getDbUrl(),pipelineBuildVo.getDbUserName(),pipelineBuildVo.getDbPassword(),new String(fileResource.getData(), StandardCharsets.UTF_8));
        }
    }
    public static List<String> sqlTakeMinJdbc(String url, String userName, String password,String sql) throws SQLException {

        Connection conn = null;

        PreparedStatement pstmt  = null;

        try{
            conn = JDBCUtil.getConnection(url, userName, password);

            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            List<String> resultList = new ArrayList<String>();

            return resultList;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            conn.close();
        }
        return null;
    }
    /**
     * 创建k8s部署文件，并保存文件到obs。
     */
    public void createK8sYaml(Project project) throws IOException, TemplateException {
        Template template = null;
        //先删除之前的
        //删除磁盘中的yaml文件。后续查看可以在obs中查看。
//		removeK8sYaml(project);
        //渲染k8s yaml文件。
        if (project.getDeployType() == DeployType.k8s && !(project.getProfile().isSkipDeploy()) &&(!project.getProfile().isRestartDeploy())&&(!project.getProfile().isRollout())&&(!project.getProfile().isAssignYaml())) {
            for (DeployConfig deployConfig : project.getDeployConfigList()) {
                //判断域名里是否包含 ‘/’ 路径 有的话截取出来填充到 k8s 的path属性种
                if (StringUtils.isNotBlank(deployConfig.getIngressHost()) && deployConfig.getIngressHost().contains("/")) {
                    String ingressHost = deployConfig.getIngressHost();
                    deployConfig.setIngressPath(ingressHost.substring(ingressHost.indexOf("/")));
                    deployConfig.setIngressHost(ingressHost.substring(0, ingressHost.indexOf("/")));
                }
                deployConfig.setTag(getDockerImageTag());
                logger.info("镜像中imagetag标签为:{}", deployConfig.getTag());
                
               if(project.getProfile().isInitDeploy()) {
            	   logger.info("使用初始化容器的方式部署，采用专用模板kubectl-init-yaml.ftl:{}", project.getProfile().isInitDeploy());
            	   template = configuration.getTemplate("kubectl-init-yaml.ftl"); 
               }else {
            	   template = configuration.getTemplate("kubectl-yaml.ftl");  
               }
                
                //将文件存入obs中
                Map<String, Object> params = Maps.newHashMap();
                params.put("deployConfig", deployConfig);
                params.put("project", project);
                params.put("profile", project.getProfile());
                
                
//        		params.put("jenkinsDataPath",env.getJenkinsDataPath());
//        		params.put("jenkinsLimitMeory",env.getJenkinsLimitMeory());
//        		params.put("jenkinsNetworkHost",env.getJenkinsNetworkHost());
//        		params.put("jenkinsRequestMeory",env.getJenkinsRequestMeory());
//        		params.put("jenkinsPathType",env.getJenkinsPathType());
//        		params.put("jenkinsMavenUrl",env.getJenkinsMavenUrl());
//        		params.put("jenkinsBuildNode",env.getJenkinsBuildNode());
//        		params.put("jenkinsTeamGitlabUrl",env.getJenkinsTeamGitlabUrl());
//        		params.put("jenkinsTeamGitlabHost",env.getJenkinsTeamGitlabHost());
                params=env.initEnvParams(params);
        		
                
                String yamlData = FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
                String url = ciK8s + project.getName() + "/" + deployConfig.getName() + System.currentTimeMillis() + ".yaml";
                //将文件存入本地
                logger.info("k8s 部署文件保存到本地中：" + url);
                writeBytesToFile(yamlData, url);
                ResourceItem resource = new ResourceItem();
                resource.setName(deployConfig.getName() + System.currentTimeMillis() + ".yaml");
                resource.setData(yamlData.getBytes(Charsets.UTF_8));
                String resourceId = storageService.save(resource);
                deployConfig.setYamlId(resourceId);
                logger.info("k8s 部署文件保存到obs中：" + resourceId);
            }
        }else {
        	 logger.info("不需要生成yaml部署文件：{}" ,com.alibaba.fastjson.JSONObject.toJSONString(project.getProfile()));
        }

    }


    public void writeBytesToFile(String yamlData, String url) throws IOException {
        // 指定路径如果没有则创建并添加
        File file = new File(url);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        OutputStream out = new FileOutputStream(file);
        InputStream is = new ByteArrayInputStream(yamlData.getBytes());
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = is.read(buff)) != -1) {
            out.write(buff, 0, len);
        }
        is.close();
        out.close();
    }


    /**
     * 当前的构建信息写入缓存
     *
     * @param project
     * @param jenkinsService
     */
    private void cacheBuildInfo(Project project, JenkinsPipelineService jenkinsService, String pipelineId) {
        ProjectLastBuildInfo lastBuild = ProjectLastBuildInfo.build(project);
        lastBuild.setLastBuild(new Date());
        lastBuild.setLastPipelineId(pipelineId);
        lastBuild.setJenkinsUrl(jenkinsService.getServerUrl());
        lastBuild.setSonarUrl(jenkinsService.getSonarUrl());
        User currentUser = AuthUtils.getCurrentUser();
        if (currentUser != null) {
            lastBuild.setTriggerUserName(currentUser.getName());
            lastBuild.setTriggerUserId(currentUser.getId());
        } else {
            lastBuild.setTriggerUserName("Git钩子");
        }
        if (project.getProfile() != null) {
            lastBuild.setProfileId(project.getProfile().getId());
            lastBuild.setProfileName(project.getProfile().getName());
        }
        lastBuild.setBuildStatus(Status.IN_PROGRESS);
        lastBuildInfo.put(project.getId(), lastBuild);
    }






    public void triggerBatchPipeline (BuildJob jobInfo) throws Exception {
            if (jobInfo.isOnLine() && StringUtils.isNotBlank(jobInfo.getOnLineId())) {
                Online online = onlineService.findOne(jobInfo.getOnLineId());
                online.setBatchStatus(com.eazybuilder.ci.entity.devops.Status.SUCCESS);
                Release release = releaseService.findOne(online.getReleaseId());
                sendRabbitMq.sendReleaseStatusMq(release.getIssuesId(), IssuesStatus.finished);
            }
            triggerBatchPipeline(jobInfo, null);
        }


        /**
         * 触发批量流水线任务,发送汇总邮件
         *
         * @param jobInfo,event
         * @throws Exception
         */
        public void triggerBatchPipeline (BuildJob jobInfo, GitEvent event) throws Exception {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
            ExecutorCompletionService<PipelineExecuteResult> ecs = new ExecutorCompletionService<>(threadPool);

            AtomicBoolean pipelinePoolError=new AtomicBoolean(Boolean.FALSE);
            Online online = onlineService.findOne(jobInfo.getOnLineId());
            for (Project prj : jobInfo.getProjects()) {
                Pipeline ppl = new Pipeline();
                ppl.setJobId(jobInfo.getId());
                if (jobInfo.isOnLine() && StringUtils.isNotBlank(jobInfo.getOnLineId())) {
                    ppl.setTargetBranch(online.getImageTag().toLowerCase());
                }else{
                    if (StringUtils.isNotBlank(prj.getScm().getArriveTagName())) {
                        ppl.setTargetBranch(prj.getScm().getArriveTagName());
                    } else if (StringUtils.isNotBlank(prj.getScm().getTagName())) {
                        ppl.setTargetBranch(prj.getScm().getTagName());
                    } else {
                        ppl.setTargetBranch("master");
                    }
                }
                if (StringUtils.isNotBlank(jobInfo.getProfileId())) {
                    PipelineProfile profile = profileService.findOne(jobInfo.getProfileId());
                    if (profile != null) {
                        prj.setProfile(profile);
                        ppl.setProfileName(profile.getName());
                        ppl.setProfileId(profile.getId());
                    }
                } else {
                    //使用默认的profile
                    prj.setProfile(prj.getDefaultProfile());
                }
                if (null != AuthUtils.getCurrentUser()) {
                    ppl.setPipelineType(PipelineType.manualJob);
                } else {
                    ppl.setPipelineType(PipelineType.job);
                }
                if (jobInfo.isOnLine() && StringUtils.isNotBlank(jobInfo.getOnLineId())) {
                    ppl.setPipelineType(PipelineType.online);
                    ppl.setPipelineVersion(online.getOnLineImageTag());
                }
                Project project = getCreateJenkinsProject(prj, jobInfo, event);
                JenkinsPipelineService jenkinsService = createJenkinsService(project, df.format(new Date()));
                String uid = UUID.randomUUID().toString();
                //project.setJobName(project.getName()+"-"+uid);
                project.setJobName(project.getName());

                //save pipeline first
                ppl.setId(uid);
                ppl.setProject(project);

                //冗余字段
                ppl.setProjectName(project.getName() + "/" + project.getDescription());
                if (project.getProfile().getTestSwitch() != AutoTestSwitch.CLOSED) {
                    ppl.setDtpTask(true);
                }

                save(ppl);
                //异步跟踪执行结果
                User user = AuthUtils.getCurrentUser();
                ExecutorService projectPipelinePool = projectPipelineMap.get(project.getId());
                if(null== projectPipelinePool){
                    projectPipelinePool = new ThreadPoolExecutor(0, 1,
                            30L, TimeUnit.MINUTES,
                            new LinkedBlockingQueue<>(15),
                            Executors.defaultThreadFactory(),
                            new ThreadPoolExecutor.AbortPolicy());
                    projectPipelineMap.put(project.getId(), (ThreadPoolExecutor) projectPipelinePool);
                }

                logger.info("#######################任务数量："+String.valueOf(((ThreadPoolExecutor) projectPipelinePool).getActiveCount()));
                projectPipelinePool.submit(()->{
                    logger.info("####################uuid:"+uid+"开始执行");
                    Future future = null;
                    try {
                        String name = jenkinsService.createPipeLine(project, uid, true);
                        logger.info("pipe line :{} created", name);
                        cacheBuildInfo(project, jenkinsService, uid);
                        jenkinsService.runPipeLine(name);
                        PipelineWorkResultTracker asyncTracker = new PipelineWorkResultTracker(
                                user,
                                project.getProfile(),
                                df.format(new Date()),
                                dockerImageService,
                                dockerDigestService,
                                null,
                                null,
                                ciK8s,
                                ciHost,
                                sendRabbitMq,
                                projectService,
                                profileService,
                                jenkinsService,
                                this,
                                metricService,
                                configuration,
                                configService.getValue("base.url", baseUrl),
                                mailSender,
                                eventBus,
                                Maps.newHashMap(),
                                storageService, pipelineExecuteService);
                        asyncTracker.setTrackInfo(prj.getId(), jobInfo.getProfileId(), name, uid);
                        //send mail individually
                        //执行完成立即发送邮件(按项目配置的组别)
                        asyncTracker.setSendMail(false);
                        future = ecs.submit(asyncTracker);
                    }catch (Exception e){
                        logger.error(e.getMessage(),e);
                        pipelinePoolError.set(Boolean.TRUE);
                    }

                    try {
                        future.get(30L,TimeUnit.MINUTES);
                    } catch (Exception e) {
                        logger.error(e.getMessage(),e);
                    }
                    logger.info("流水线运行完成："+project.getName()+uid);
                });

            }
            int done = 0;
            boolean errors = false;
            List<PipelineExecuteResult> results = Lists.newArrayList();
            boolean hasBuildError = false;
            while (done < jobInfo.getProjects().size() && !errors && !pipelinePoolError.get()) {
                //blocks if none available
                Future<PipelineExecuteResult> resultFuture = ecs.take();
                try {
                    PipelineExecuteResult result = resultFuture.get();
                    if (!"true".equalsIgnoreCase(result.getSuccess())) {
                        hasBuildError = true;
                    }
                    results.add(result);
                    done++;
                } catch (Exception e) {
                    errors = true;
                }
            }
            if (!results.isEmpty()) {
                Map<String, Object> model = Maps.newHashMap();
                if (event != null) {
                    model.put("event", event);
                }
                if (jobInfo.isSendMailOnFail()) {
                    //send mail notify only when build failed or job failed
                    if (hasBuildError || errors) {
                        renderBatchMailAndSend(results, jobInfo, model);
                    }
                } else {
                    renderBatchMailAndSend(results, jobInfo, model);
                }
            }
        }

        private Project getCreateJenkinsProject (Project project, BuildJob jobInfo, GitEvent event){
            if (project == null) {
                throw new IllegalArgumentException("项目ID:" + project.getId() + "不存在");
            }
            if (project.getProfile() != null && project.getProfile().isDeployWar() && project.getProfile().getDeployInfo() != null) {
                try {
                    String hostId = project.getProfile().getDeployInfo().getHostId();
                    HostInfo host = hostService.findOne(hostId);
                    if (host != null) {
                        project.getProfile().getDeployInfo().setHost(host);
                    }
                } catch (Exception e) {
                    logger.error("config error skip deploy war", e);
                    project.getProfile().setDeployWar(false);
                }
            }
            if (event != null && event.getRef() != null) {
                logger.info("set Build Ref to:{}", event.getRef());
                project.getScm().setTagName(event.getRef());
            }
            return project;
        }

        private void renderBatchMailAndSend (List < PipelineExecuteResult > results, BuildJob
        jobInfo, Map < String, Object > model){
            try {
                model.put("job", jobInfo);
                model.put("results", results);
                model.put("buildUrl", HttpUtil.getRootUrl(configService.getValue("base.url", baseUrl)) + "/jenkins/job/");
                Template mailTemplate = configuration.getTemplate("mail-batch.ftl");
                logger.info("----BATCH RENDER MODEL-----");
                logger.info(JsonMapper.nonDefaultMapper().toJson(model));
                try {
                    String mailHtml = FreeMarkerTemplateUtils.processTemplateIntoString(mailTemplate, model);
                    Team team = teamServiceImpl.findOne(jobInfo.getTeamId());
                    //发送邮件
                    mailSender.sendMail(getReceiver(team), getCc(team), mailHtml, jobInfo.getName(),MsgProfileType.pipelineFail,team.getCode());
                } catch (Exception e) {
                    logger.info("failed to render mail template with model:");
                    logger.info(JsonMapper.nonDefaultMapper().toJson(model));
                    e.printStackTrace();
                }
                if (jobInfo.isNotifyDingtalk() && jobInfo.getDingtalkWebHook() != null) {
                    try {
                        Template dingtalkTemplate = configuration.getTemplate("dingtalk-batch.ftl");
                        String markdownMsg = FreeMarkerTemplateUtils.processTemplateIntoString(dingtalkTemplate, model);
                        DingtalkWebHookUtil.sendDingtalkMsg("流水线通知", markdownMsg, jobInfo.getDingtalkWebHook().getSecret(), jobInfo.getDingtalkWebHook().getUrl());
                    } catch (Exception e) {
                        logger.info("failed to render dingtalk template with model:");
                        logger.info(JsonMapper.nonDefaultMapper().toJson(model));
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String[] getReceiver (Team team){
            //trigger by manual
            User user = AuthUtils.getCurrentUser();
            if (user != null) {
                return new String[]{user.getEmail()};
            }
            return mailSender.getReceiverMailList(team);
        }

        private String[] getCc (Team team){
            //trigger by manual
            User user = AuthUtils.getCurrentUser();
            if (user != null) {
                return new String[]{user.getEmail()};
            }
            return mailSender.getCCMailList(team);
        }

        @Override
        public Page<Pipeline> pageSearch (Pageable pageable, String searchText){
            BooleanExpression condition = null;
            UserVo currentUser = AuthUtils.getCurrentUser();
            if (currentUser != null
                    && !currentUser.isAuditReader()) {
                condition = QPipeline.pipeline.project.team.in(currentUser.getTeamList());
            }

            if (StringUtils.isNotBlank(searchText)) {
                if (condition != null) {
                    condition = condition.and(QPipeline.pipeline.project.name.like("%" + searchText + "%")
                            .or(QPipeline.pipeline.project.description.like("%" + searchText + "%")));
                } else {
                    condition = QPipeline.pipeline.project.name.like("%" + searchText + "%")
                            .or(QPipeline.pipeline.project.description.like("%" + searchText + "%"));
                }
            }
            return condition == null ? dao.findAll(pageable) : dao.findAll(condition, pageable);
        }

        public Page<Pipeline> pageSearch (Pageable pageable, Collection < String > projectIds, String
        projectName, String sourceBranch, String targetBranch, String profileName, Date date){
            BooleanExpression condition = null;
            UserVo currentUser = AuthUtils.getCurrentUser();
            if (currentUser != null && !currentUser.isAuditReader()) {
                condition = QPipeline.pipeline.project.team.in(currentUser.getTeamList());
            }

            if (projectIds != null && projectIds.size() > 0) {
                condition = QPipeline.pipeline.project.id.in(projectIds);
            }

            if (date != null) {
                Date startDate = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
                Date endDate = DateUtils.truncate(DateUtils.addDays(date, 1), Calendar.DAY_OF_MONTH);
                if (condition != null) {
                    condition = condition.and(QPipeline.pipeline.startTimeMillis.goe(startDate.getTime())
                            .and(QPipeline.pipeline.startTimeMillis.lt(endDate.getTime())));
                } else {
                    condition = QPipeline.pipeline.startTimeMillis.goe(startDate.getTime())
                            .and(QPipeline.pipeline.startTimeMillis.lt(endDate.getTime()));
                }
            }


            if (StringUtils.isNotBlank(projectName)) {
                if (condition != null) {
                    condition = condition.and(QPipeline.pipeline.projectName.like("%" + projectName + "%"));
                } else {
                    condition = QPipeline.pipeline.projectName.like("%" + projectName + "%");
                }
            }
            if (StringUtils.isNotBlank(sourceBranch)) {
                if (condition != null) {
                    condition = condition.and(QPipeline.pipeline.sourceBranch.like("%" + sourceBranch + "%"));
                } else {
                    condition = QPipeline.pipeline.sourceBranch.like("%" + sourceBranch + "%");
                }
            }
            if (StringUtils.isNotBlank(targetBranch)) {
                if (condition != null) {
                    condition = condition.and(QPipeline.pipeline.targetBranch.like("%" + targetBranch + "%"));
                } else {
                    condition = QPipeline.pipeline.targetBranch.like("%" + targetBranch + "%");
                }
            }
            if (StringUtils.isNotBlank(profileName)) {
                if (condition != null) {
                    condition = condition.and(QPipeline.pipeline.profileName.like("%" + profileName + "%"));
                } else {
                    condition = QPipeline.pipeline.profileName.like("%" + profileName + "%");
                }
            }
            return condition == null ? dao.findAll(pageable) : dao.findAll(condition, pageable);
        }
        public List<Pipeline> getPipelineByGitPath(List<String> gitPathList) throws Exception {
            Set<String> projectIdSet = new HashSet<String>();
            if (null != gitPathList && gitPathList.size() > 0) {
                for (String gitlabPath : gitPathList) {
                    List<Project> projectsByUrl = projectService.findByScmUrl(gitlabPath);
                    if (null != projectsByUrl && projectsByUrl.size() > 0) {
                        for (Project project : projectsByUrl) {
                            projectIdSet.add(project.getId());
                        }
                    }
                }
            }
            if (projectIdSet.size() <= 0) {
                return null;
            } else {
                return (List<Pipeline>) findLastedPipeline(projectIdSet, "master");
            }
        }
        public Iterable<Pipeline> findLastedPipeline (Collection < String > projectIds, String targetBranch){

            List<Long> timeList = queryFactory.select(QPipeline.pipeline.endTimeMillis.max())
                    .from(QPipeline.pipeline)
                    .where(QPipeline.pipeline.targetBranch.eq(targetBranch)
                            .and(QPipeline.pipeline.project.id.in(projectIds)))
                    .groupBy(QPipeline.pipeline.project.id).fetch();
            return dao.findAll(QPipeline.pipeline.endTimeMillis.in(timeList)
                    .and(QPipeline.pipeline.project.id.in(projectIds))
                    .and(QPipeline.pipeline.targetBranch.eq(targetBranch))
                    .and(QPipeline.pipeline.endTimeMillis.in(timeList)));
        }

        public Iterable<Pipeline> findPipelineByProjectIdAndTargetBranch (String projectId, Release release) {
            BooleanExpression booleanExpression = (QPipeline.pipeline.project.id.eq(projectId))
                    .and(QPipeline.pipeline.pipelineType.notIn(PipelineType.online))
                    .and(QPipeline.pipeline.dtpReports.isNotEmpty());
            if (StringUtils.isNotBlank(release.getImageTag())) {
                ReleaseProject releaseProject = releaseProjectService.findByReleaseIdAndProjectId(release.getId(), projectId);
                booleanExpression = booleanExpression.and(QPipeline.pipeline.targetBranch.eq(release.getImageTag().toLowerCase()).or(QPipeline.pipeline.pipelineVersion.eq(releaseProject.getCreateTagVersion())));
            } else {
                logger.info("提测表里的分支号或者镜像号为空，查询测试报告时不拼装分支条件");
            }
            return dao.findAll(booleanExpression, Sort.by(Sort.Direction.DESC, "endTimeMillis"));
        }
        public List<Stage> findStageByPipelineId (String id){
            return dao.findById(id).orElse(null).getStages();
        }

        public List<DtpReport> findDtpReportByPipelineId (String id){
            return dao.findById(id).orElse(null).getDtpReports();
        }
        public List<Pipeline> findByJobId (String jobId) {
            return (List<Pipeline>)dao.findAll(QPipeline.pipeline.jobId.eq(jobId));
        }
        public Map<String, List<Report>> getReportCache () {
            return reportCache;
        }

        public void removeK8sYaml (Project project){
            deleteAnyone(ciK8s + project.getName() + "/");
        }

        /**
         * 判断指定的文件或文件夹删除是否成功
         *
         * @param fileName 文件或文件夹的路径
         * @return true or false 成功返回true，失败返回false
         */
        public static boolean deleteAnyone (String fileName){
            File file = new File(fileName);
            if (!file.exists()) {
                logger.info("文件" + fileName + "不存在，删除失败！");
                return false;
            } else {
                if (file.isFile()) {
                    return deleteFile(fileName);
                } else {
                    return deleteDir(fileName);
                }
            }
        }

        /**
         * 判断指定的文件删除是否成功
         *
         * @param fileName 文件路径
         * @return true or false 成功返回true，失败返回false
         */
        public static boolean deleteFile (String fileName){
            //根据指定的文件名创建File对象
            File file = new File(fileName);
            //要删除的文件存在且是文件
            if (file.exists() && file.isFile()) {
                if (file.delete()) {
                    logger.info("文件" + fileName + "删除成功！");
                    return true;
                } else {
                    logger.info("文件" + fileName + "删除失败！");
                    return false;
                }
            } else {
                logger.info("文件" + fileName + "不存在，删除失败！");
                return false;
            }
        }

        /**
         * 删除指定的目录以及目录下的所有子文件
         *
         * @param dirName is 目录路径
         * @return true or false 成功返回true，失败返回false
         */
        public static boolean deleteDir (String dirName){

            //dirName不以分隔符结尾则自动添加分隔符
            if (dirName.endsWith(File.separator)) {
                dirName = dirName + File.separator;
            }
            //根据指定的文件名创建File对象
            File file = new File(dirName);

            //目录不存在或者
            if (!file.exists() || (!file.isDirectory())) {
                logger.info("目录删除失败" + dirName + "目录不存在！");
                return false;
            }
            //列出源文件下所有文件，包括子目录
            File[] fileArrays = file.listFiles();
            //将源文件下的所有文件逐个删除
            for (int i = 0; i < fileArrays.length; i++) {
                deleteAnyone(fileArrays[i].getAbsolutePath());
            }
            //删除当前目录
            if (file.delete()) {
                logger.info("目录" + dirName + "删除成功！");
            }
            return true;

        }


     public String subStageLogByName(String consoleText,Stage stage){
         String starSign = String.format(SUBLOG_BEGIN_SIGN,stage.getName());
         int startIndex =  consoleText.indexOf(starSign);
         String endSign;
         if(!stage.isSuccess() && startIndex>0){
             endSign = String.format(SUBLOG_BEGIN_SIGN,LAST_SIGN);
         }else {
             endSign = String.format(SUBLOG_END_SIGN,stage.getName());
         }
         int endIndex = consoleText.indexOf(endSign);
         if(startIndex > 0 && endIndex> 0){
             return stage.isSuccess() ?  consoleText.substring(startIndex,endIndex + endSign.length()) : consoleText.substring(startIndex,endIndex);
         }
         return null;
     }

}
