package com.example.articleManagement.controller.exception;

import com.example.articleManagement.entity.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result HandlerException(Exception e) {
        e.printStackTrace();
        System.out.println("错误信息："+(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : ""));
        return Result.error("操作失败");
    }

}
