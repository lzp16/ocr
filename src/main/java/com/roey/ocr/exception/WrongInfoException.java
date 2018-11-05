package com.roey.ocr.exception;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/11/2 14:16
 **/
public class WrongInfoException extends RuntimeException {

    private int code;
    private String msg;

    public WrongInfoException() {
        super();
    }

    public WrongInfoException(String msg) {
        super(msg);
    }

    public WrongInfoException(Throwable cause) {
        super(cause);
    }

    public WrongInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongInfoException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public WrongInfoException(int code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
        this.msg = String.format(msgFormat, args);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static WrongInfoException newInstance(String msg) {
        return new WrongInfoException(msg);
    }

    public static WrongInfoException newInstance(int code, String msg) {
        return new WrongInfoException(code, msg);
    }

    public static WrongInfoException newInstance(int code, String msgFormat, Object... args) {
        return new WrongInfoException(code, msgFormat, args);
    }

}
