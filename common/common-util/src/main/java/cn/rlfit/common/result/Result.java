package cn.rlfit.common.result;

import lombok.Data;

/**
 * @description: some desc
 * @author: sunjianrong
 * @email: sunruolifeng@gmail.com
 * @date: 22/01/2024 12:11 PM
 */

@Data
public class Result<T> {
    private Integer coed; //操作的状态码
    private String message; //是否成功
    private T data; //查询出来的具体数据

    //    构造私有化
    private Result() {
    }

    //    封装返回的数据
    public static <T> Result<T> build(T body, ResultCode resultCode) {
        Result<T> result = new Result<>();
        if (body != null) result.setData(body);
        result.setCoed(resultCode.getCode());
        result.setMessage(resultCode.getMessage());
        return result;
    }

    //    成功
    public static <T> Result<T> ok() {
        return build(null, ResultCode.SUCCESS);
    }

    public static <T> Result<T> ok(T data) {
        return build(data, ResultCode.SUCCESS);
    }

    //    失败
    public static <T> Result<T> fail() {
        return build(null, ResultCode.FAIL);
    }

    public static <T> Result<T> fail(T data) {
        return build(data, ResultCode.FAIL);
    }

    //  自己手动扩展
    public Result<T> message(String msg) {
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer coed) {
        this.setCoed(coed);
        return this;
    }
}
