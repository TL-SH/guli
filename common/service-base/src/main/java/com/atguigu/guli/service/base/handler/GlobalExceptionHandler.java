package com.atguigu.guli.service.base.handler;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.ExceptionUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author tanglei
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 全局异常
     * @param e
     * @return
     */
    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException e){
        log.error(ExceptionUtils.getMessage(e));
        return R.error().message(e.getMessage()).code(e.getCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        // e.printStackTrace();
        // log.error(e.getMessage());
        // 将日志堆栈信息输出到文件
        log.error(ExceptionUtils.getMessage(e));
        return R.error();
    }

    /**
     * 处理sql异常
     * @param e
     * @return
     */
    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseBody
    public R error(BadSqlGrammarException e){
        // e.printStackTrace();
        // log.error(e.getMessage());
        log.error(ResultCodeEnum.BAD_SQL_GRAMMAR.toString());
        // 将日志堆栈信息输出到文件
        log.error(ExceptionUtils.getMessage(e));
        return R.setResult(ResultCodeEnum.BAD_SQL_GRAMMAR);
    }

    /**
     * 处理json格式转化错误
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public R error(HttpMessageNotReadableException e){
        // e.printStackTrace();
        // log.error(e.getMessage());
        log.error(ResultCodeEnum.JSON_PARSE_ERROR.toString());
        // 将日志堆栈信息输出到文件
        log.error(ExceptionUtils.getMessage(e));
        return R.setResult(ResultCodeEnum.JSON_PARSE_ERROR);
    }





}
