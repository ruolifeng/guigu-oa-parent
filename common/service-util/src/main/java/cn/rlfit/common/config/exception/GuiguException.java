package cn.rlfit.common.config.exception;

import cn.rlfit.common.result.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @description: some desc
 * @author: sunjianrong
 * @email: sunruolifeng@gmail.com
 * @date: 22/01/2024 4:41 PM
 */
@Data
@AllArgsConstructor
public class GuiguException extends RuntimeException{
    private Integer code; //状态码
    private String msg; //描述信息

    public GuiguException(ResultCode resultCode){
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.msg = resultCode.getMessage();
    }
}
