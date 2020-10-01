package com.lin.admin.common.utils;

import com.lin.admin.common.emuns.ResultEnum;
import com.lin.admin.common.model.Result;

/**
 * 统一返回结果工具
 */
public class ResultUtil {

    /**
     * 自定义返回状态
     * @param code
     * @param msg
     * @param object
     * @return
     */
    public static Result renderCustom(Integer code, String msg, Object object) {
        Result result = new Result(code, msg);
        result.setData(object);
        return result;
    }

    /**
     * 自定义返回状态
     * @param resultEnum 结果枚举
     * @param object 返回对象
     * @return
     */
    public static Result renderCustom(ResultEnum resultEnum, Object object) {
        return renderCustom(resultEnum.getCode(), resultEnum.getMsg(), object);
    }

    /**
     * 返回成功
     * @param code
     * @param msg
     * @return
     */
    public static Result renderSuccess(Integer code, String msg) {
        return renderCustom(code, msg, "");
    }

    /**
     * 返回成功
     * @param object
     * @return
     */
    public static Result renderSuccess(Object object) {
        return renderCustom(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), object);
    }

    /**
     *
     * 返回成功
     * @return
     */
    public static Result renderSuccess() {
        return renderSuccess(null);
    }

    /**
     * 返回失败
     * @param resultEnum
     * @return
     */
    public static Result renderFail(ResultEnum resultEnum) {
        return renderCustom(resultEnum, null);
    }

    /**
     * 返回失败
     * @param code
     * @param msg
     * @return
     */
    public static Result renderFail(Integer code, String msg) {
        return renderCustom(code, msg, null);
    }

    /**
     * 返回失败
     * @param object
     * @return
     */
    public static Result renderFail(Object object) {
        return renderCustom(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getMsg(), object);
    }

    /**
     * 返回失败
     * @return
     */
    public static Result renderFail() {
        return renderFail(null);
    }
}
