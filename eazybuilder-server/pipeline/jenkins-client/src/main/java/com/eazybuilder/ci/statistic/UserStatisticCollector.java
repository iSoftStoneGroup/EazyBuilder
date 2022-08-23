package com.eazybuilder.ci.statistic;

import java.util.Date;
import java.util.List;


import com.eazybuilder.ci.entity.UserActivityStatistic;

public interface UserStatisticCollector {


	/**
	 * 收集用户的日活动统计信息
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<UserActivityStatistic> collectUserActivities(Date date) throws Exception;
	
}
