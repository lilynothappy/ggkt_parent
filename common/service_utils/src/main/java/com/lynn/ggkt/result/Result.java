package com.lynn.ggkt.result;

import lombok.Data;

//统一返回结果类
@Data
public class Result<T> {
    private Integer code;//状态码

    private String message;//返回状态信息

    private T data;//返回数据

    public Result() {
    }

    //成功方法
    public static<T> Result<T> ok(T data){
        Result<T> result = new Result<>();
        if(data != null){
            result.setData(data);
        }
        result.setCode(20000);
        result.setMessage("成功");
        return result;
    }

    //失败方法
    /*public static<T> Result<T> fail(T data){
        Result<T> result = new Result<>();
        if(data != null){
            result.setData(data);
        }
        result.setCode(201);
        result.setMessage("失败");
        return result;
    }*/

    public static<T> Result<T> fail(){
        Result<T> result = new Result<>();
        result.setCode(20001);
        result.setMessage("失败");
        return result;
    }

    //为了可以改变状态吗与信息
    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }
}
