package top.nanguomm.fox.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.nanguomm.fox.domain.Result;
import top.nanguomm.fox.exception.CustomException;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public Result<Object> customExceptionHandler(CustomException e) {
        log.error("出现异常----------------------->:\n{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 其他异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result<Object> globalHandler(Exception e) {
        log.error("出现异常----------------------->:{}", e.getMessage());

        if(e.getCause() instanceof SQLIntegrityConstraintViolationException) {
            return Result.error("名称重复");
        }

        if (e.getCause() instanceof IOException) {
            return Result.error("io异常");
        }

        return Result.error("服务器繁忙");
    }
}
