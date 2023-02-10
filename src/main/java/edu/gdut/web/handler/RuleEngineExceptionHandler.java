package edu.gdut.web.handler;

import edu.gdut.web.controller.ResponseDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 *
 * @author Trust
 */
@Slf4j
@ControllerAdvice
public class RuleEngineExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseDate handler(Exception e) {
        log.error(e.getMessage());
        return ResponseDate.failure().message(e.getMessage());
    }
}
