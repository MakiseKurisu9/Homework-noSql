package com.example.articleManagement.anno;

import com.example.articleManagement.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented//api文档中出现
@Constraint(
        validatedBy = {StateValidation.class}
)//谁给其提供校验规则
@Target(ElementType.FIELD)//元注解 用在哪些属性
@Retention(RetentionPolicy.RUNTIME)//元注解 哪些阶段保留

public @interface State {
    //提供校验失败的信息
    String message() default "state参数的值只能是已发布或者草稿";
    //制定分组
    Class<?>[] groups() default {};
    //负载 获取state注解的负载信息
    Class<? extends Payload>[] payload() default {};

}
