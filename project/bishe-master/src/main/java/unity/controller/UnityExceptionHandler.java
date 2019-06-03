package unity.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * 全局异常捕捉
 */
@ControllerAdvice
public class UnityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(UnityExceptionHandler.class);
    @ExceptionHandler(value = Exception.class)
    public String javaExceptionHandler(Exception ex) {

        logger.error("捕获到Exception异常", ex);
        //异常日志入库
        return "error";
    }
}


