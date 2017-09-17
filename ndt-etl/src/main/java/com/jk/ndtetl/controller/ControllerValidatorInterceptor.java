package com.jk.ndtetl.controller;


import com.jk.ndtetl.Constant;
import com.jk.ndtetl.util.WebUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: ControllerValidatorInterceptor
 * @Description: 后台验证错误信息统一处理
 * @author fangwei
 * @date 2017年5月22日 下午5:05:37
 *
 */
@Aspect
@Component
public class ControllerValidatorInterceptor {
    private static final String MESSAGE = "客户端输入有误，请确保输入正确";

    /**
     * 
     * @Description: 处理controller下面的类
     * @author fangwei
     * @date 2017年5月22日 下午5:05:56
     * @param pjp
     * @param bindingResult
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.jk.ndtetl.controller.*Controller.*(..)) && args(..,bindingResult)")
    public Object doAround1(ProceedingJoinPoint pjp, BindingResult bindingResult) throws Throwable {
        Object retVal = handError(pjp, bindingResult);
        return retVal;
    }

    /**
     * 
     * @Description: 处理controller子包下面的类
     * @author fangwei
     * @date 2017年5月22日 下午5:06:15
     * @param pjp
     * @param bindingResult
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.jk.ndtetl.controller.*.*Controller.*(..)) && args(..,bindingResult)")
    public Object doAround2(ProceedingJoinPoint pjp, BindingResult bindingResult) throws Throwable {
        Object retVal = handError(pjp, bindingResult);
        return retVal;
    }

    private Object handError(ProceedingJoinPoint pjp, BindingResult bindingResult) throws Throwable {
        Object retVal;
        if (bindingResult.hasErrors()) {
            Map<String, Object> map = new HashMap<String, Object>();
            Map<String, Object> errors = new HashMap<String, Object>();
            Map<String, Object> fieldMap = new HashMap<String, Object>();
            map.put("message", MESSAGE);
            if (bindingResult.getErrorCount()>0) {
                List<ObjectError>  errorList=bindingResult.getAllErrors();
                map.put("message", errorList.get(0).getDefaultMessage());
            }
            map.put("errors", errors);
            errors.put(bindingResult.getObjectName(), fieldMap);
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                fieldMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            retVal = map;
            HttpServletResponse response = WebUtils.getResponse();
            response.setStatus(Constant.FAILED_400);
        } else {
            retVal = pjp.proceed();
        }
        return retVal;
    }
}