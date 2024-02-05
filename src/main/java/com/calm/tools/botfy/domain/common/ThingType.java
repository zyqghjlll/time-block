package com.calm.tools.botfy.domain.common;

import lombok.Getter;

public enum ThingType {
    /**
     * YEAR 年
     */
    YEAR("YEAR", "习惯"),
    /**
     * MONTH 月
     */
    MONTH("MONTH", "临时"),

    ;

    @Getter
    private String code;
    @Getter
    private String value;

    ThingType(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
