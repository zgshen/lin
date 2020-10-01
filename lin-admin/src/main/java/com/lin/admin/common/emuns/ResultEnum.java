package com.lin.admin.common.emuns;

/**
 * 统一处理结果枚举
 */
public enum ResultEnum {

    /**
     * 成功
     */
    SUCCESS(0, "成功"),

    /**
     * 数据库异常
     */
    DATABASE_ERROR(202, "数据库异常"),

    /**
     * 操作失败
     */
    FAIL(205, "操作失败"),

    /**
     * 数据已存在
     */
    SUCCESS_IS_HAVE(208, "数据已存在"),

    /**
     * 没有结果
     */
    NOT_DATA(911, "没有结果"),

    /**
     * 没有登录
     */
    NOT_LOGIN(600, "没有登录"),

    /**
     * 发生异常
     */
    EXCEPTION(401, "发生异常"),

    /**
     * 系统错误
     */
    SYS_ERROR(402, "系统错误"),

    /**
     * 未授权
     */
    UN_AUTHORIZATION(403, "未授权"),

    /**
     * 登陆超时
     */
    LOGIN_TIMEOUT(403, "超时请重新登陆"),

    /**
     * 参数错误
     */
    PARAMS_ERROR(405, "参数错误"),

    /**
     * 不支持或已经废弃
     */
    NOT_SUPPORTED(410, "不支持或已经废弃"),

    /**
     * 签名错误
     */
    INVALID_SIGN(443, "无效的签名"),

    /**
     * AuthCode错误
     */
    INVALID_AUTHCODE(444, "无效的AuthCode"),

    /**
     * 太频繁的调用
     */
    TOO_FREQUENT(445, "太频繁的调用"),

    /**
     * 未知的错误
     */
    UNKNOWN_ERROR(500, "服务器错误，请联系管理员"),

    /**
     * 未设置方法
     */
    NOT_METHOD(4004, "未设置方法"),

    /**
     * 验证码为空
     */
    NOT_CAPTCHA(446, "验证码为空"),
    /**
     * 验证码已过期
     */
    NOT_REDIS_CAPTCHA(447, "验证码已过期"),
    /**
     * 验证码输入错误
     */
    ERROR_CAPTCHA(448, "验证码输入错误");

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}