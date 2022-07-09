package com.eazybuilder.ci.util;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

@Component
public class AsyncTaskTracker {

	ExecutorService threadpool=Executors.newCachedThreadPool();
	
	Cache<String,TaskTracker> taskExecuteResultCache=CacheBuilder.newBuilder()
			.expireAfterWrite(60, TimeUnit.MINUTES).build();
	
	
	public static abstract class TaskTracker<T> implements Callable<T>{
		Runnable task;
		protected LinkedBlockingQueue<String> logQueue=Queues.newLinkedBlockingQueue();
		boolean done=false;
		T  result;
		public abstract T doRun();
		
		public boolean isDone() {
			return done;
		}
		
		public String getLog(){
			List<String> logs=Lists.newArrayList();
			logQueue.drainTo(logs);
			return StringUtils.join(logs, "\n");
		}

		@Override
		public T call() throws Exception {
			try{
				this.result=doRun();
				return result;
			}finally{
				done=true;
			}
		}

		public T getResult() {
			return result;
		}
		
	}
	
	
	@PreDestroy
	public void closePool(){
		threadpool.shutdown();
	}
	
	public String submitTask(TaskTracker task){
		Future<?> future=threadpool.submit(task);
		String taskUid=UUID.randomUUID().toString();
		taskExecuteResultCache.put(taskUid, task);
		return taskUid;
	}
	
	public Map<String,Object> getExecuteProgress(String taskUid){
		Map<String,Object> result=Maps.newHashMap();
		TaskTracker task=taskExecuteResultCache.getIfPresent(taskUid);
		if(task!=null){
			result.put("done", task.isDone());
			String log=task.getLog();
			if(task.isDone()){
				result.put("projects",task.getResult());
				taskExecuteResultCache.invalidate(taskUid);
			}
			result.put("log",log);
			return result;
		}
		result.put("done", true);
		return result;
	}
	
	
	
}
