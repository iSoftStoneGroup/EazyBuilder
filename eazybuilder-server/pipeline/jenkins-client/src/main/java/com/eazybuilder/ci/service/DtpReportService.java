package com.eazybuilder.ci.service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.DtpReport;
import com.eazybuilder.ci.entity.Pipeline;
import com.eazybuilder.ci.entity.report.Status;
import com.eazybuilder.ci.entity.report.StatusGuard;
import com.eazybuilder.ci.repository.DtpReportDao;
import com.alibaba.fastjson.JSONObject ;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DtpReportService extends AbstractCommonServiceImpl<DtpReportDao, DtpReport> implements CommonService<DtpReport> {

    private static Logger logger = LoggerFactory.getLogger(DtpReportService.class);

    @Autowired
    PipelineServiceImpl pipelineService;

    /**
     * 将mq监听到的dtp测试报告，转为实体类。
     * @param jsonObject
     * @return
     */
    @Transactional
    public String updateDtpRerport(JSONObject jsonObject) {
    	
    	StringBuilder msg=new StringBuilder();

        String status = jsonObject.getString("status");
        DtpReport dtpReport = getDtpReport(jsonObject);
        logger.info("将自动化测试报告转为实体类：{}",dtpReport.toString());
        Pipeline pipeline = pipelineService.findOne(dtpReport.getPipelineHistoryId());
        logger.info("根据自动化测试回传过来的pipelineHistoryId查到的历史记录：{}",pipeline.toString());
        dtpReport.setProjectName(pipeline.getProject().getName());
        dtpReport.setProjectId(pipeline.getProject().getId());
        msg.append("工程名称[").append(pipeline.getProject().getName()).append("],");
        
//        Release release = releaseService.findByPipelineId(pipeline);
        if(jsonObject.containsKey("resultDetail")&&jsonObject.getJSONObject("resultDetail")!=null) {
            logger.info("保存自动化测试报告");
            dao.save(dtpReport);
            if (pipeline.getDtpReports()==null) {
                List<DtpReport> dtpReportList = new ArrayList<>();
                dtpReportList.add(dtpReport);
                pipeline.setDtpReports(dtpReportList);
            } else {
                pipeline.getDtpReports().add(dtpReport);
            }
        }else {
        	msg.append("自动化测试返回的测试结果，不包含resultDetail节点，");
        }

        //有流水线在运行中。
        if(dtpReport.isEnd()){
            //这里只判断流水线状态
            switch(status){
                case "SUCCESS":
                	msg.append("自动化测试成功，");
                    //自动化测试门禁结果
                    Optional<Boolean> isSucceedOption  =pipeline.getDtpReports().stream().map(DtpReport::isSucceed).reduce(Boolean::logicalAnd);
                    boolean testSucceed = isSucceedOption.isPresent() ? isSucceedOption.get() : true;

                    //如果自动化测试门禁成功、并且之前门禁不是失败 就将结果置为成功
                    if(testSucceed){
                        if(pipeline.getStatusGuard()!=StatusGuard.FAILED){
                            pipeline.setStatusGuard(StatusGuard.SUCCESS);
                            msg.append("代码质量也符合要求，将流水线整体状态置为成功，");
                        }
                    }else {
                        pipeline.setStatusGuard(StatusGuard.FAILED);
                        msg.append("代码质量不符合门禁要求，将流水线整体状态置为失败，");
                    }
                    msg.append("流水线整体运行状态置为成功，");
                    pipeline.setStatus(Status.SUCCESS);
                    break;
                case "FAILD":
                	msg.append("自动化测试失败，流水线整体运行状态置为失败");
                    pipeline.setStatus(Status.FAILED);
                    // 异常信息存在哪里呢?
                    break;
                default:break;
            }

        }else{
            /**
             *   流水线状态设定为成功， 门禁判定设定为未执行还是 保持不变？
             */
            //这里的门禁装填应该怎么改
            pipeline.setStatus(Status.IN_PROGRESS);
            //存疑 这里需要更新门禁状态吗？
            if(StringUtils.equals(status,"BEGIN")){
                //这里应该如何修改 包括前端提示自动化测试开始，以及执行测试计划数量
                logger.info("接收到自动化测试平台开始的消息,总共执行自动化测试计划数量：{}",jsonObject.getIntValue("executePlanCount"));
            }
        }
//        releaseService.save(release);
        pipelineService.save(pipeline);
        pipelineService.updateBuildStatus(pipeline.getProject(),pipeline.getStatus());
        
        return msg.toString();
    }

    private DtpReport getDtpReport(JSONObject jsonObject) {
        logger.info("开始将json转成dtpReport");
        DtpReport dtpReport = new DtpReport();
        if(jsonObject.containsKey("pipelineHistoryId")){
            dtpReport.setPipelineHistoryId(jsonObject.getString("pipelineHistoryId"));
        }
        if(jsonObject.containsKey("end")){
            dtpReport.setEnd(jsonObject.getBoolean("end"));
        }
        if(jsonObject.containsKey("code")){
            dtpReport.setCode(jsonObject.getString("code"));
        }
        if(jsonObject.containsKey("gitUrl")){
            dtpReport.setGitUrl(jsonObject.getString("gitUrl"));
        }
        if(jsonObject.containsKey("message")){
            dtpReport.setMessage(jsonObject.getString("message"));
        }
        if(jsonObject.containsKey("resultDetail")&&jsonObject.getJSONObject("resultDetail")!=null) {
            JSONObject resultDetail = jsonObject.getJSONObject("resultDetail");
            if (resultDetail.containsKey("createTime")) {
                dtpReport.setCreateTime(resultDetail.getLong("createTime"));
            }
            if (resultDetail.containsKey("elapsedTime")) {
                dtpReport.setElapsedTime(resultDetail.getLong("elapsedTime"));
            }
            if (resultDetail.containsKey("succeed")) {
                dtpReport.setSucceed(resultDetail.getBoolean("succeed"));
            }
            if (resultDetail.containsKey("reportUrl")) {
                dtpReport.setReportUrl(resultDetail.getString("reportUrl"));
            }
            if (resultDetail.containsKey("testType")) {
                dtpReport.setTestType(resultDetail.getString("testType"));
            }
            if (resultDetail.containsKey("reportUrl")) {
                dtpReport.setReportUrl(resultDetail.getString("reportUrl"));
            }
            if (resultDetail.containsKey("name")) {
                dtpReport.setName(resultDetail.getString("name"));
            }
            if (resultDetail.containsKey("totalPass")) {

                dtpReport.setTotalPass(resultDetail.getIntValue("totalPass"));
            }
            if (resultDetail.containsKey("totalFail")) {
                dtpReport.setTotalFail(resultDetail.getIntValue("totalFail"));
            }
            if (resultDetail.containsKey("expectRun")) {
                dtpReport.setExpectRun(resultDetail.getIntValue("expectRun"));
            }
            if (resultDetail.containsKey("envExceptionFailed")) {
                dtpReport.setEnvExceptionFailed(resultDetail.getIntValue("envExceptionFailed"));
            }
        }
        return dtpReport;
    }

}
