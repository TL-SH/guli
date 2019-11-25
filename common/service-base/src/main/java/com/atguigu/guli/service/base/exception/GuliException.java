package com.atguigu.guli.service.base.exception;

import com.atguigu.guli.common.base.result.ResultCodeEnum;
import lombok.Data;

/**
 * @author tanglei
 */
@Data
public class GuliException extends RuntimeException {

    private Integer code;

    /**
     * 接受状态码和信息
     * @param message
     * @param code
     */
    public GuliException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 接受枚举类
     * @param resultCodeEnum
     */
    public GuliException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}