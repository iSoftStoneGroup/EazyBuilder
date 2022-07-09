package com.eazybuilder.ci.job;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eazybuilder.ci.entity.QScmStatisticJob;
import com.eazybuilder.ci.entity.QScmStatisticJobRecord;
import com.eazybuilder.ci.entity.QUserActivityStatistic;
import com.eazybuilder.ci.entity.ScmStatisticJob;
import com.eazybuilder.ci.entity.ScmStatisticJobRecord;
import com.eazybuilder.ci.entity.ScmType;
import com.eazybuilder.ci.entity.UserActivityStatistic;
import com.eazybuilder.ci.service.ScmStatisticJobRecordService;
import com.eazybuilder.ci.service.ScmStatisticJobService;
import com.eazybuilder.ci.service.UserActivityStatisticService;
import com.eazybuilder.ci.statistic.gitlab.GitLabStatisticCollector;
import com.querydsl.core.types.dsl.BooleanExpression;

import cn.hutool.core.date.DateUtil;

@Component
public class DevActivitiyAnalysisJob{
	private static Logger logger=LoggerFactory.getLogger(DevActivitiyAnalysisJob.class);
	
	@Autowired
	ScmStatisticJobService  jobService;
	
	@Autowired
	UserActivityStatisticService userActivityService;
	
	@Autowired
	ScmStatisticJobRecordService logService;
	
	@Scheduled(cron="0 0 0 * * ?")
	public void doDailyAnalysis() {
		Iterable<ScmStatisticJob> jobs=jobService.findAll(QScmStatisticJob.scmStatisticJob.enable.eq(true));
		if(jobs!=null) {
			jobs.forEach(job->{
				try {
					executeJob(job);
				} catch (Exception e) {
					logger.error("execute job failed",e);
				}
				
			});
		}
	}
	
	public void executeJob(ScmStatisticJob job) {
		//导入昨日数据
		collect(job,DateUtil.yesterday());
		//检查和重试历史失败记录
		checkFailedRecord(job);
	}

	public void checkFailedRecord(ScmStatisticJob job) {
		Iterable<ScmStatisticJobRecord> records=logService.findAll(
				QScmStatisticJobRecord.scmStatisticJobRecord.job.id.eq(job.getId())
				.and(QScmStatisticJobRecord.scmStatisticJobRecord.success.eq(false)));
		
		records.forEach(record->{
			logger.info("retry failed record:{} for job:{} at day:{}:",record.getId(),record.getJob().getId(),record.getDay());
			collect(job, DateUtil.parse(record.getDay(),"yyyy-MM-dd"));
		});
	}

	@Transactional
	public void collect(ScmStatisticJob job,Date date) {
		logger.info("collecting statistic for repo:{} during {}",job.getRepoUrl(),date);
		if(ScmType.git==job.getRepoType()) {
			try(GitLabStatisticCollector collector=new GitLabStatisticCollector(job.getRepoUrl(),
					job.getAccessToken())){
				List<UserActivityStatistic> activities=collector.collectUserActivities(date);
				logger.info(activities.toString());
				activities.forEach(activity->{
					saveIfNotPresent(activity);
				});
				writeLog(job, date, true, null);
			}catch(Exception e) {
				logger.error("collect statistic failed for job:"+job.getId(),e);
				writeLog(job, date, false, e.getMessage());
			}
		}
	}
	
	private void saveIfNotPresent(UserActivityStatistic activity) {
		BooleanExpression condition= activity.getEmail()==null?
				QUserActivityStatistic.userActivityStatistic.userName.eq(activity.getUserName()):QUserActivityStatistic.userActivityStatistic.email.eq(activity.getEmail());
		Iterable<UserActivityStatistic>  records=userActivityService.findAll(
				condition
				.and(QUserActivityStatistic.userActivityStatistic.projectName.eq(activity.getProjectName()))
				.and(QUserActivityStatistic.userActivityStatistic.groupName.eq(activity.getGroupName()))
				.and(QUserActivityStatistic.userActivityStatistic.day.eq(activity.getDay())));
		if(records!=null&&records.iterator().hasNext()) {
			//already exist skip
			logger.info("user {} activity for day {}, project:{} already exist", activity.getEmail(),activity.getDay(),
					activity.getGroupName() + "/" + activity.getProjectName());
		}else {
			userActivityService.save(activity);
		}
		
	}

	public void writeLog(ScmStatisticJob job,Date date,boolean success,String errMsg) {
		String day=DateUtil.format(date, "yyyy-MM-dd");
		
		Iterable<ScmStatisticJobRecord> records=logService.findAll(
				QScmStatisticJobRecord.scmStatisticJobRecord.job.id.eq(job.getId())
				.and(QScmStatisticJobRecord.scmStatisticJobRecord.day.eq(day)));
		if(records!=null&&records.iterator().hasNext()) {
			//update status
			ScmStatisticJobRecord record=records.iterator().next();
			record.setSuccess(success);
			record.setErrMsg(errMsg);
			logService.save(record);
		}else {
//			insert record
			ScmStatisticJobRecord record=new ScmStatisticJobRecord();
			record.setJob(job);
			record.setSuccess(success);
			record.setDay(day);
			record.setErrMsg(errMsg);
			logService.save(record);
		}
		
	}
}
