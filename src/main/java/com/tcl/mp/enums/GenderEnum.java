package com.tcl.mp.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author li
 * @version 1.0
 * @date 2020/8/24 17:13
 */
public enum GenderEnum {

    /**
     * 性别
     */
    MALE(1, "male"),
    FEMALE(0, "female"),
    UNDEFINED(-1, "undefined")
    ;

    @EnumValue
    private final Integer code;
    private final String msg;

    GenderEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
