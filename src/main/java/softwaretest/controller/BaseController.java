package softwaretest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import softwaretest.error.*;
import softwaretest.response.CommonReturnType;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/* 这个注解是指这个类是处理其他controller抛出的异常 */
@ControllerAdvice
public class BaseController {
    /* 定义exceptionhandler解决未被controller层吸收的exception */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex) {
        return this.generateResponse(ex);
    }

    /* 处理用户注册失败的情况 */
    @ExceptionHandler(RegisterFailException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public Object handleUnauthorizedException(HttpServletRequest request, RegisterFailException ex) {
        return this.generateResponse(ex);
    }

    /* 处理用户不存在的情况 */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Object handleNotFoundException(HttpServletRequest request, NotFoundException ex) {
        return this.generateResponse(ex);
    }

    /* 处理用户登录失败的情况 */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Object handleUnauthorizedException(HttpServletRequest request, UnauthorizedException ex) {
        return this.generateResponse(ex);
    }

    /* 处理用户未登录的情况 */
    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Object handleNotLoginException(HttpServletRequest request, NotLoginException ex) {
        return this.generateResponse(ex);
    }

    /* 处理用户注册失败的情况 */
    @ExceptionHandler(ModifyFailException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Object handleModifyFailException(HttpServletRequest request, ModifyFailException ex) {
        return this.generateResponse(ex);
    }

    private Object generateResponse(Exception ex) {
        Map<String, Object> responseData = new HashMap<>();
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errCode", businessException.getErrCode());
            responseData.put("errMsg", businessException.getErrMsg());
        } else {
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }
        return CommonReturnType.create(responseData, "fail");
    }
}
