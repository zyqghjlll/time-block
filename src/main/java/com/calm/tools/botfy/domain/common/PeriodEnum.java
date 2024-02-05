package com.calm.tools.botfy.domain.common;

import lombok.Getter;

/**
 * @author zyq
 * @date 2022/12/23 9:56
 */
public enum PeriodEnum {
    /**
     * YEAR 年
     */
    YEAR("YEAR", "年"),
    /**
     * MONTH 月
     */
    MONTH("MONTH", "月"),
    /**
     * DAY 天
     */
    DAY("DAY", "天"),
    ;

    @Getter
    private String code;
    @Getter
    private String value;

    PeriodEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

}
