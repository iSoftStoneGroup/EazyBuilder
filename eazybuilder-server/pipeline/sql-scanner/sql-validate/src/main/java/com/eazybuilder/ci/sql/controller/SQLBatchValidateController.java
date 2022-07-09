package com.eazybuilder.ci.sql.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.eazybuilder.ci.sql.vo.JobStatus;
import com.eazybuilder.ci.sql.vo.ProjectSQLBatchInfo;
import com.eazybuilder.ci.sql.vo.ReportModel;
import com.eazybuilder.ci.sql.vo.SQLSourceResult;
import com.eazybuilder.ci.sql.vo.SQLValidateResult;
import com.eazybuilder.ci.sql.vo.SqlCount;
import com.eazybuilder.ci.sql.vo.SqlResultModel;

@RestController
@RequestMapping("/batch")
public class SQLBatchValidateController {
	Cache<String,Future<List<SQLSourceResult>>> JOB_FUTURE_CACHE=CacheBuilder.newBuilder()
			.maximumSize(10000)
			.expireAfterWrite(30, TimeUnit.MINUTES).build();
	
	ExecutorService threadPool=Executors.newCachedThreadPool();
	
	@Autowired
	SQLValidateController validateApi;
	
	@PreDestroy
	public void clearPool() {
		threadPool.shutdown();
	}

	@PostMapping
	public String submitCheckJob(@RequestBody ProjectSQLBatchInfo sqlBatch) {
		String jobId=UUID.randomUUID().toString();
		Future<List<SQLSourceResult>> future=threadPool.submit(new Callable<List<SQLSourceResult>>() {
			@Override
			public List<SQLSourceResult> call() throws Exception {
				List<SQLSourceResult> checkResult=Lists.newLinkedList();
				sqlBatch.getSqlSources().forEach(sqlSource->{
					Map<String,SQLValidateResult> resultMap=validateApi.validateMySQL5(sqlSource.getSqlMap());
					SQLSourceResult result=new SQLSourceResult(sqlSource);
					result.setResultMap(resultMap);
					checkResult.add(result);
				});
				return checkResult;
			}
		});
		JOB_FUTURE_CACHE.put(jobId, future);
		return jobId;
	}
	
	@GetMapping("/status")
	public JobStatus getJobStatus(@RequestParam("jobId")String checkId) {
		Future<?> future=JOB_FUTURE_CACHE.getIfPresent(checkId);
		if(future==null) {
			return null;
		}
		System.out.println("task:"+checkId+",isDone="+future.isDone()+",isCancelled:"+future.isCancelled());
		if(future.isDone()) {
			return JobStatus.DONE;
		}
		return JobStatus.RUNNING;
	}
	
	@GetMapping
	public ReportModel getJobResult(@RequestParam("jobId")String checkId) throws InterruptedException, ExecutionException{
		Future<List<SQLSourceResult> > future=JOB_FUTURE_CACHE.getIfPresent(checkId);
		if(future==null||!future.isDone())
			return null;
		List<SQLSourceResult> result = future.get();
		JOB_FUTURE_CACHE.invalidate(checkId);
		return buildReportModel(result);
	}
	
	private ReportModel buildReportModel(List<SQLSourceResult> sourceResult) {
		ReportModel model=new ReportModel();
		AtomicInteger totalSql=new AtomicInteger(0);
		AtomicInteger totalImcompatible=new AtomicInteger(0);
		sourceResult.forEach(sr->{
			SqlCount sourceCount=new SqlCount();
			AtomicInteger totalSourceImcompatible=new AtomicInteger(0);
			sourceCount.setTotal(sr.getSqlMap().size());
			sourceCount.setSource(sr.getSource());
			//计算总SQL
			totalSql.addAndGet(sr.getSqlMap().size());
			
			List<SqlResultModel> sourceDetail=new ArrayList<>();
			
			sr.getResultMap().entrySet().forEach(entry->{
				SQLValidateResult sqlValidateResult=entry.getValue();
				if(!sqlValidateResult.isSuccess()) {
					//计算总违例数
					totalImcompatible.incrementAndGet();
					//源文件内违例数
					totalSourceImcompatible.incrementAndGet();
					
					SqlResultModel srm=new SqlResultModel();
					String sqlId=entry.getKey();
					srm.setSqlId(sqlId);
					srm.setSql(sr.getSqlMap().get(sqlId));
					srm.setValidatorName(sqlValidateResult.getValidatorName());
					srm.setCode(sqlValidateResult.getCode());
					srm.setMsg(sqlValidateResult.getMsg());
					sourceDetail.add(srm);
				}
			});
			sourceCount.setImcompatible(totalSourceImcompatible.get());
			//源文件摘要统计
			if(sourceCount.getImcompatible()>0) {
				model.getSummary().add(sourceCount);
				model.getDetail().put(sr.getSource(), sourceDetail);
			}
		});
		Collections.sort(model.getSummary());
		model.setTotalSql(totalSql.get());
		model.setTotalFound(totalImcompatible.get());
		return model;
	}
}
