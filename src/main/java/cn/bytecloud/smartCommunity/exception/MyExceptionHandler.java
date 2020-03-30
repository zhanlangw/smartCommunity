package cn.bytecloud.smartCommunity.exception;

import cn.bytecloud.smartCommunity.base.dto.APIResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class MyExceptionHandler {
    public void handleException(Exception ex) {
        ex.printStackTrace();
        log.info("------------------------------------------------------------------------------------------------------------------------------------------");
        log.info("【异常类型】：" + ex);
        StackTraceElement[] stackTrace = ex.getStackTrace();
        log.info("【异常类名：】：" + stackTrace[0].getClassName());
        log.info("【方法名：】：" + stackTrace[0].getMethodName());
        log.info("【异常行数：】：" + stackTrace[0].getLineNumber());
        if (stackTrace.length > 1) {
            log.info("【异常类名：】：" + stackTrace[1].getClassName());
            log.info("【异常行数：】：" + stackTrace[1].getLineNumber());
            log.info("【异常行数：】：" + stackTrace[1].getLineNumber());
        }
        log.info("------------------------------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * 其他异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public APIResult showException(Exception ex) {
        handleException(ex);
        return APIResult.failure(ErrorCode.FAILURE).setMessage("请求失败");
    }

    /**
     * 权限异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(AuthorizationException.class)
    public APIResult showShiroException(Exception ex) {
        handleException(ex);
        return APIResult.failure(ErrorCode.AUTHORIZATION).setMessage("权限异常");
    }

//    @ExceptionHandler(UnauthorizedException.class)
//    public APIResult showUnauthorizedException(exception ex) {
////        ex.printStackTrace();
//        log.info("【错误信息】  " + ex.toString());
//        return APIResult.failure(ErrorCode.AUTHORIZATION).setMessage("权限异常");
//    }

    /**
     * 打自定义异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ByteException.class)
    public APIResult showByteException(ByteException ex) {
        handleException(ex);
        log.info("【错误信息】  " + ex.getMessage());
        return APIResult.failure(ex.getStatus()).setMessage(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public APIResult showByteException(IllegalArgumentException ex) {
        handleException(ex);
        log.info("【错误信息】  " + ex.getMessage());
        return APIResult.failure(ErrorCode.PARAMETER).setMessage(ex.getMessage());
    }

    /**
     * 参数异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public APIResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        handleException(ex);
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String message = fieldError.getField() + fieldError.getDefaultMessage();
        log.info("【错误信息】  " + message);
        return APIResult.failure(ErrorCode.PARAMETER_ERROR).setMessage(message);
    }

    @ExceptionHandler(value = BindException.class)
    public APIResult showBindException(BindException ex) {
        handleException(ex);
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String message = fieldError.getField() + fieldError.getDefaultMessage();
        log.info("【错误信息】  " + message);
        return APIResult.failure(ErrorCode.PARAMETER_ERROR).setMessage(message);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public APIResult showBindException(MissingServletRequestParameterException ex) {
        handleException(ex);
        String name = ex.getParameterName();
        String message = name + "不能为空";
        log.info("【错误信息】  " + message);
        return APIResult.failure(ErrorCode.NULL_PARAMETER).setMessage(message);
    }


    @ExceptionHandler(value = AuthenticationException.class)
    public APIResult showAuthenticationException(AuthenticationException ex) {
        handleException(ex);
        String message = "用户名或密码错误";
        log.info("【错误信息】  " + message);
        return APIResult.failure(ErrorCode.USERNAME_OR_PASSWORD).setMessage("用户名或密码错误");
    }
}
