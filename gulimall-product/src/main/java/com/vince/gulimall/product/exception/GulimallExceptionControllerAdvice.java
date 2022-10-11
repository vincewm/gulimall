package com.vince.gulimall.product.exception;

import com.vince.common.exception.BizCodeEnum;
import com.vince.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.vince.gulimall.product.controller")
@Slf4j
public class GulimallExceptionControllerAdvice {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e){
//        log.info("错误信息"+e.getMessage()+"\n错误类型"+e.getFieldError());
        BindingResult bindingResult = e.getBindingResult();
        Map<String,String> errorMap=new HashMap<>();
        bindingResult.getFieldErrors().forEach(item->
            errorMap.put(item.getField(),item.getDefaultMessage())
        );
        return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(), BizCodeEnum.VALID_EXCEPTION.getMsg()).put("data",errorMap);
    }
    @ExceptionHandler(value = Throwable.class)  //Exception和Error继承自Throwable
    public R handleUnKnownException(Throwable throwable){
        log.error("未知错误：{}",throwable);
        return R.error(BizCodeEnum.UNKNOW_EXEPTION.getCode(), BizCodeEnum.UNKNOW_EXEPTION.getMsg());
    }
}
