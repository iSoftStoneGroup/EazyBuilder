package com.eazybuilder.ci.aspect;


import com.eazybuilder.ci.exception.CIException;
import com.eazybuilder.ci.util.JsonMapper;
import com.eazybuilder.ci.vo.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHander {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(CIException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public  RestResponse<String> handleWebException(CIException e) {
		logger.error("抛出异常", e,e);
		 RestResponse<String> responce = new RestResponse<String>(e.getMessage());
		 if(e.getCode() != null){
             responce.setCode(e.getCode());
         }
        return responce;
    }

    @ExceptionHandler(org.springframework.orm.ObjectOptimisticLockingFailureException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public  RestResponse<String> handleWebException(org.springframework.orm.ObjectOptimisticLockingFailureException e) {
        logger.error("抛出异常", e,e);
        RestResponse<String> responce = new RestResponse<String>("页面数据过期，请更新页面后保存");
        return responce;
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResponse<String> handleException( Exception e) {
        logger.error("抛出异常", e,e);
        RestResponse<String> responce = new RestResponse<String>(e.getMessage());
        return responce;
    }

    @ExceptionHandler({TransactionSystemException.class})
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResponse<String> handleException( TransactionSystemException e) {
        logger.error("抛出异常", e.getCause().getCause().getMessage());
        RestResponse<String> responce = new RestResponse<String>(e.getMessage());
        return responce;
    }




    /**
     * spring boot默认参数错误异常，统一处理
     * @param e
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public RestResponse<String> handleValidateException(MethodArgumentNotValidException e) {
    	logger.error("抛出异常", e,e);
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            errorMessages.add(error.getDefaultMessage());
        }
        String msg = JsonMapper.nonEmptyMapper().toJson(errorMessages);
        RestResponse<String> responce = new RestResponse<String>(msg);
        return responce;
    }
}