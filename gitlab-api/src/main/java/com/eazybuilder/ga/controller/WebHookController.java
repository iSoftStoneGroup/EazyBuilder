package com.eazybuilder.ga.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ga.component.DingTalkComponent;
import com.eazybuilder.ga.component.GitlabApiComponent;
import com.eazybuilder.ga.constant.*;
import com.eazybuilder.ga.fegin.DemandFeginService;
import com.eazybuilder.ga.fegin.vo.HotfixVo;
import com.eazybuilder.ga.pojo.CIPackagePojo;
import com.eazybuilder.ga.pojo.DingTalkHookPojo;
import com.eazybuilder.ga.pojo.Result;
import com.eazybuilder.ga.pojo.cache.BranchCache;
import com.eazybuilder.ga.pojo.codeScan.CodeReport;
import com.eazybuilder.ga.pojo.codeScan.ScanData;
import com.eazybuilder.ga.pojo.codeScan.ScanTag;
import com.eazybuilder.ga.pojo.codeScan.Stage;
import com.eazybuilder.ga.pojo.merge.Assignees;
import com.eazybuilder.ga.pojo.merge.GLWHRootInfo;
import com.eazybuilder.ga.pojo.merge.Object_attributes;
import com.eazybuilder.ga.pojo.rule.GroupRulePojo;
import com.eazybuilder.ga.pojo.rule.GuardPojo;
import com.eazybuilder.ga.service.CIPackageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ga.pojo.gitlab.GitSystemMergeEvent;
import com.eazybuilder.ga.service.GitlabService;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * gitlab systemhook
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/wh")
public class WebHookController {
	private static Logger logger = LoggerFactory.getLogger(WebHookController.class);
	private static final String X_GITLAB_TOKEN = "X-Gitlab-Token";
	private static final String CI_TOKEN_DEFINE = "ci.gitlab.token";
	private static final String X_Gitlab_Event = "X-Gitlab-Event";

	private static final String MERGE_ACTION = "merge";
	private static final String MERGE_STATE = "merged";

	private static final String OPEN_ACTION = "open";
	private static final String OPEN_STATE = "opened";

	private static final String CLOSE_ACTION = "close";
	private static final String CLOSE_STATE = "closed";

	private static final String webhook = "https://oapi.dingtalk.com/robot/send?access_token=xxx";
	private static final String sign = "xxx";
	
    @Autowired
    GitlabService gitlabService;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private DemandFeginService demandFeginService;
	@Autowired
	private CIPackageService ciPackageService;
	@Autowired
	private DingTalkComponent dingTalkComponent;
	@Autowired
	GitlabApiComponent gitlabApiComponent;

	@PostMapping("/mr")
	public void systemTrigger(@RequestBody GLWHRootInfo event, HttpServletRequest request,
							  HttpServletResponse response) {

		logger.info("receive system hook event from {},eventType:{},event:{}", request.getRemoteAddr(),
				request.getHeader(X_Gitlab_Event), event.getObject_kind());
		
		logger.info("headers:{}",request.getHeaderNames());

		logger.info("接收到gitlab的回调数据：{}", event);

		Object_attributes object_attributes = null;
		String action = "";
		String state = "";
		String projectUrl = "";
		String branchName = "";
		String pathWithNamespace = "";
		String groupName = "";

		try{
			object_attributes = event.getObject_attributes();
			action = object_attributes.getAction();
			state = object_attributes.getState();
			projectUrl = event.getProject().getHttp_url();
			branchName = event.getObject_attributes().getSource_branch();
			pathWithNamespace = event.getProject().getPath_with_namespace();
			groupName = pathWithNamespace.split("/")[0];
		}catch (Exception e){
			logger.info("数据信息不全，解析失败。");
			return;
		}

		List<GuardPojo> guardList = null;
		if (redisTemplate.opsForHash().entries("gitlabMessageCache").containsKey(groupName.toLowerCase())) {
			Object ruleValue = redisTemplate.opsForHash().entries("gitlabMessageCache").get(groupName.toLowerCase());
			GroupRulePojo groupRule = JSONObject.parseObject(String.valueOf(ruleValue), GroupRulePojo.class);
			logger.info("#####################"+groupRule);
			guardList = groupRule.getGuards();
		}

		if(OPEN_ACTION.equals(action) && OPEN_STATE.equals(state)){

			String body="";
			String head="- "+event.getUser().getName()+"申请合并\n";
			String headReject="- "+event.getUser().getName()+"申请合并被拒绝\n";
			String unitTestResult="";
			Boolean unitFlag=Boolean.TRUE;

			String authorName = event.getUser().getName();
			List<Assignees> assignees = event.getAssignees();
			List<String> emailList = new ArrayList<String>();
			List<String> authorEmailList = gitlabApiComponent.getUserEmail(authorName);
			if(null!=authorEmailList&&authorEmailList.size()>0){
				emailList.addAll(authorEmailList);
			}
			if(null!=assignees&&assignees.size()>0){
				head+="该任务指派给";
				for(Assignees assigneesObject:assignees){
					String assigneeName = assigneesObject.getUsername();
					List<String> assigneeEmailList = gitlabApiComponent.getUserEmail(assigneeName);
					if(null!=assigneeEmailList&&assigneeEmailList.size()>0){
						emailList.addAll(assigneeEmailList);
					}
					head+=assigneeName+";";
				}
				head+="审核\n";
			}

			head +="- 合并地址："+event.getObject_attributes().getUrl()+"\n";
			headReject +="- 合并地址："+event.getObject_attributes().getUrl()+"\n";
			headReject +="- 原因是\n";
			if (redisTemplate.opsForHash().entries(projectUrl+":"+branchName).containsKey(projectUrl+"-"+branchName)) {
				Object value = redisTemplate.opsForHash().entries(projectUrl+":"+branchName).get(projectUrl+"-"+branchName);
				CodeReport codeReport = JSONObject.parseObject(String.valueOf(value), CodeReport.class);
				String projectType = codeReport.getProjectType();
				String pipelineStatus = codeReport.getPipelineStatus();
				if("FAILED".equals(pipelineStatus)){
					body = "- 读取扫描报告失败\n";
					body+="- 原因为ci流水线执行失败,该任务自动关闭\n";
					sendScanTag(projectUrl,branchName,event.getProject().getId(),event.getObject_attributes().getIid(),groupName);
					gitlabService.createMRDiscussion(event.getProject().getId(),event.getObject_attributes().getIid(), body);
					gitlabService.updateMRState(event.getProject().getId(),event.getObject_attributes().getIid(),"close");
					sendDingTalk(groupName.toLowerCase(),headReject+body,emailList,MsgProfileType.mergeApply);
					return;
				}
				List<ScanData> scanDataList = codeReport.getScanDataList();
				HashMap<String,String> projectAnalysis = codeReport.getProjectAnalysis();
				List<Stage> stageList = codeReport.getStageList();
				if(null!=scanDataList&& scanDataList.size()>0) {
					for(int i=0;i<scanDataList.size();i++){
						ScanData scanData = scanDataList.get(i);
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
				if("".equals(body)){
					body = "- 读取扫描报告失败\n";
					body+="- 原因可能为ci流水线未完成。\n";
					sendScanTag(projectUrl,branchName,event.getProject().getId(),event.getObject_attributes().getIid(),groupName);
					gitlabService.createMRDiscussion(event.getProject().getId(),event.getObject_attributes().getIid(), body);
				}else{
					body+="- [查看详情]("+codeReport.getLink()+")";
					gitlabService.createMRDiscussion(event.getProject().getId(),event.getObject_attributes().getIid(), body);
					redisTemplate.opsForHash().entries(projectUrl+":"+branchName).remove(projectUrl+"-"+branchName);
				}
				if(!"".equals(unitTestResult)){
					gitlabService.createMRDiscussion(event.getProject().getId(),event.getObject_attributes().getIid(), unitTestResult);
				}
			}else{
				body = "- 读取扫描报告失败\n";
				body+="- 原因可能为打包失败或ci流水线未完成。\n";

				gitlabService.createMRDiscussion(event.getProject().getId(),event.getObject_attributes().getIid(), body);
			}
			sendScanTag(projectUrl,branchName,event.getProject().getId(),event.getObject_attributes().getIid(),groupName);

			if(!"".equals(unitTestResult)&&!unitFlag){
				gitlabService.updateMRState(event.getProject().getId(),event.getObject_attributes().getIid(),"close");
				sendDingTalk(groupName.toLowerCase(),headReject+unitTestResult,emailList,MsgProfileType.mergeApply);
			}else{
				sendDingTalk(groupName.toLowerCase(),head+body,emailList,MsgProfileType.mergeApply);
			}

		}else if (MERGE_ACTION.equals(action) && MERGE_STATE.equals(state)) {
			String authorName = event.getObject_attributes().getLast_commit().getAuthor().getName();
			String assignName = event.getUser().getName();
			String targetBranchName = event.getObject_attributes().getTarget_branch();
			String message=""+authorName
					+"提交合并请求："+event.getObject_attributes().getTitle()
					+"已由"+assignName+"合并通过。\n";
			if(branchName.toLowerCase().contains("hotfix")){
				message=event.getObject_attributes().getTitle()+"问题紧急修复，该任务已由"
						+assignName+"合并通过，代码已经合并到master分支，请本月待上线的任务重新从master合并最新代码";
				HotfixVo hotfixVo = new HotfixVo();
				String[] codeArr = branchName.split("-");
				if(codeArr.length==2){
					hotfixVo.setCode(codeArr[codeArr.length-1]);
					hotfixVo.setSubject("["+branchName+"]hotfix代码更新");
					hotfixVo.setDescription("紧急任务"+branchName+"代码已经合并到master分支，请本月待上线的任务重新从master合并最新代码\n");
					hotfixVo.setAssigneeName(assignName);
					demandFeginService.createHotfixIssue(hotfixVo);
				}

			}
			if(targetBranchName.toLowerCase().contains("test")){
				gitlabService.createMR(event.getProject().getId(),targetBranchName,"master","分支"+targetBranchName+"合并到master，"+event.getObject_attributes().getTitle());
			}
			List<String> emailList = new ArrayList<String>();
			List<String> authorEmailList = gitlabApiComponent.getUserEmail(authorName);
			if(null!=authorEmailList&&authorEmailList.size()>0){
				emailList.addAll(authorEmailList);
			}
			List<String> assignEmailList = gitlabApiComponent.getUserEmail(assignName);
			if(null!=assignEmailList&&assignEmailList.size()>0){
				emailList.addAll(assignEmailList);
			}

			sendDingTalk(groupName.toLowerCase(),message,emailList,MsgProfileType.mergePass);
			if(redisTemplate.opsForHash().entries("gitlab-branch-cache").containsKey(branchName)){
				redisTemplate.opsForHash().entries("gitlab-branch-cache").remove(branchName);
			}
			logger.info("合并成功，触发CI流水线");
			this.triggerCI(event);
		}else if (CLOSE_ACTION.equals(action) && CLOSE_STATE.equals(state)) {
			String authorName = event.getObject_attributes().getLast_commit().getAuthor().getName();
			String assignName = event.getUser().getName();
			String message=""+authorName
					+"提交合并请求："+event.getObject_attributes().getTitle()
					+"已由"+assignName+"关闭。\n";
			message+="- 查看详情："+event.getObject_attributes().getUrl()+"\n";
			List<String> emailList = new ArrayList<String>();
			List<String> authorEmailList = gitlabApiComponent.getUserEmail(authorName);
			if(null!=authorEmailList&&authorEmailList.size()>0){
				emailList.addAll(authorEmailList);
			}
			List<String> assignEmailList = gitlabApiComponent.getUserEmail(assignName);
			if(null!=assignEmailList&&assignEmailList.size()>0){
				emailList.addAll(assignEmailList);
			}
			sendDingTalk(groupName.toLowerCase(),message,emailList, MsgProfileType.mergeRefused);
		}
	}

	private void sendDingTalk(String groupName, String message, List<String> emailList, MsgProfileType msgProfileType) {
		if (redisTemplate.opsForHash().entries(groupName).containsKey(groupName)) {
			Object value = redisTemplate.opsForHash().entries(groupName).get(groupName);
			DingTalkHookPojo dingTalkHookPojo = JSONObject.parseObject(String.valueOf(value), DingTalkHookPojo.class);
			dingTalkComponent.sendDingPrivateMessageMQ("gitlab merge", message,
					emailList, msgProfileType, "private");
		} else {
			//dingTalkComponent.sendDingMessageMQ("gitlab merge", message, sign, webhook, null);
		}
	}

	private String triggerCI(GLWHRootInfo data) {
		Long totalTime = System.currentTimeMillis();

		String sourceBranch = data.getObject_attributes().getSource_branch();
		String code=null;
		String topCode=null;
		HashMap<String, String> customFields=null;
		if(sourceBranch.toLowerCase().contains("hotfix")||sourceBranch.toLowerCase().contains("bugfix")||sourceBranch.toLowerCase().contains("feature")){
			code = sourceBranch.split("-")[1];
			topCode = String.valueOf(demandFeginService.getTopIssueId(Integer.valueOf(code)));
			customFields = demandFeginService.getIssueCustomFieldById(Integer.valueOf(code));
			demandFeginService.addNote(Integer.valueOf(code), data.getObject_attributes().getLast_commit().getMessage());
		}


		CIPackagePojo pojo = new CIPackagePojo();
		pojo.setCode(code);
		pojo.setImageTag("");
		pojo.setProjectName(data.getProject().getName());
		pojo.setUserName(data.getObject_attributes().getLast_commit().getAuthor().getName());
		pojo.setTopCode(topCode);
		pojo.setTagName(sourceBranch);
		pojo.setGitPath(data.getProject().getHttp_url());
		pojo.setCustomFields(customFields);
		pojo.setTargetBranchName(data.getObject_attributes().getTarget_branch());
		pojo.setSourceBranchName(sourceBranch);
		pojo.setAssigneeName(data.getUser().getName());
		pojo.setAuthorName(data.getObject_attributes().getLast_commit().getAuthor().getName());

		pojo.setProfileType(ProfileType.merge);

		logger.info("发送构建消息给CI:{}", JSONObject.toJSONString(pojo));

		ciPackageService.sendCIPackage(pojo);

		logger.info("########################共执行了{}秒", (System.currentTimeMillis() - totalTime)/1000);
		return Result.ok().message("已将构建任务发送到CI平台").toString();
	}

	private void sendScanTag(String projectUrl, String branchName, Integer projectId, Integer mergeId, String groupName){
		ScanTag tag = new ScanTag();
		tag.setProjectId(projectId);
		tag.setMergeId(mergeId);
		tag.setGroupName(groupName);
		redisTemplate.opsForHash().put(projectUrl+":"+branchName+":tag", projectUrl+"-"+branchName, JSONObject.toJSONString(tag));
		redisTemplate.expire(projectUrl+":"+branchName+":tag", 5, TimeUnit.DAYS);
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
