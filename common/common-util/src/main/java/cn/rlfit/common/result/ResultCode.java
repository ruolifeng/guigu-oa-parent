package cn.rlfit.common.result;

import lombok.Getter;

/**
 * @description: some desc
 * @author: sunjianrong
 * @email: sunruolifeng@gmail.com
 * @date: 22/01/2024 12:06 PM
 */

@Getter
public enum ResultCode {
    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(2012, "服务异常"),
    DATA_ERROR(204, "数据异常"),
    LOGIN_ERROR(208, "未登录"),
    PERMISSION(209, "没有权限");

    private Integer code;
    private String message;

    private ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
