package com.mall.vo;

import com.mall.constants.ResultEnum;
import lombok.Data;

@Data
public class JsonResult<T> {

    private int code;
    private String msg;
    private T data;

    public static JsonResult success(){
        JsonResult result = new JsonResult();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getReason());
        return result;
    }

    public static <T> JsonResult success(T data){
        JsonResult result = new JsonResult();
        result.setCode(0);
        result.setMsg("success");
        result.setData(data);
        return result;
    }

    public static JsonResult fail(ResultEnum res){
        JsonResult result = new JsonResult();
        result.setCode(res.getCode());
        result.setMsg(res.getReason());
        return result;

    }
}
