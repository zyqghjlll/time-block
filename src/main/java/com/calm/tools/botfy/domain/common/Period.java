package com.calm.tools.botfy.domain.common;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.calm.tools.botfy.utils.CommonUtil;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * the time-limited interval
 *
 * @author zyq
 * @date 2022/11/17 11:45
 */
@Getter
public class Period {
    /**
     * 起始时间
     */
    private Date startTime;
    /**
     * 截止时间
     */
    private Date endTime;

    /**
     * Generate a date range
     */
    public Period() {
        // 默认当前时间前一秒至当前时间区间
        this.newPeriod(DateUtil.offsetSecond(CommonUtil.getDate(), -1), CommonUtil.getDate());
    }

    /**
     * Generate a date range based on the specified year
     *
     * @param year
     */
    public Period(int year) {
        Date date = DateUtil.parse(year + "-01-01 00:00:00");
        this.newPeriod(DateUtil.beginOfYear(date), DateUtil.endOfYear(date));
    }

    /**
     * Generate a date range based on the specified year and month
     *
     * @param year
     * @param month
     */
    public Period(int year, int month) {
        if (month <= 0 || month > 12) {
            throw new RuntimeException("12 months of a year");
        }
        Date date = DateUtil.parseDate(year + "-" + month + "-" + "01");
        this.newPeriod(DateUtil.beginOfMonth(date), DateUtil.endOfMonth(date));
    }

    /**
     * Generate a date range based on the specified start-time and end-time
     *
     * @param startTime
     * @param endTime
     */
    public Period(Date startTime, Date endTime) {
        this.newPeriod(startTime, endTime);
    }

    private void newPeriod(Date startTime, Date endTime) {
        String message = verify(startTime, endTime);
        if (ObjectUtil.isEmpty(message)) {
            this.startTime = CommonUtil.formatDate(startTime);
            this.endTime = CommonUtil.formatDate(endTime);
        } else {
            throw new RuntimeException(message);
        }
    }

    /**
     * 验证起止时间是否符合时间区间的定义
     *
     * @param startTime
     * @param endTime
     * @return 验证通过则返回空(字符串) 验证失败则返回提示信息(字符串)
     */
    public static String verify(Date startTime, Date endTime) {
        if (ObjectUtil.isEmpty(startTime)) {
            return "日期区间错误：起始时间不能为空";
        }
        if (ObjectUtil.isEmpty(endTime)) {
            return "日期区间错误：截止时间不能为空";
        }
        if (startTime.getTime() >= endTime.getTime()) {
            return "日期区间错误：起始时间应小于且不等于截止时间 格式[yyyy-MM-dd HH:mm:ss.SSS]";
        }
        return "";
    }

    public boolean isFullMonth() {
        boolean result = false;
        if (this.startTime.equals(DateUtil.beginOfMonth(this.startTime)) && this.endTime.equals(DateUtil.beginOfMonth(this.endTime))) {
            result = true;
        }
        return result;
    }

    /**
     * 根据月份分割  todo 尝试重构，简化逻辑
     *
     * @param periodEnum 周期类型：年，月，日
     * @param isExtreme  是否极致分割，物理周期分割 true:极致分割  false:非极致分割，合并连续周期
     * @return
     */
    public List<Period> splitByType(PeriodEnum periodEnum, boolean isExtreme) {
        List<Period> result = new ArrayList<>();

        // 周期起始时间
        Date periodStart = getPeriodStart(this.startTime, periodEnum);
        Date periodEnd = getPeriodEnd(this.endTime, periodEnum);

        if (!this.isCrossPeriod(this, periodEnum)) {
            result.add(new Period(this.startTime, this.endTime));
        } else {
            // 连续满周期区间
            Date fullPeriodStart = periodStart;
            Date fullPeriodEnd = periodEnd;
            if (this.startTime.getTime() > periodStart.getTime()) {
                fullPeriodStart = this.offsetPeriod(periodStart, periodEnum, 1);
                result.add(new Period(this.startTime, this.getPeriodEnd(this.startTime, periodEnum)));
            }
            if (this.endTime.getTime() < periodEnd.getTime()) {
                fullPeriodEnd = this.offsetPeriod(periodEnd, periodEnum, -1);
            }

            Date start = null;
            Date end = null;
            for (Date i = fullPeriodStart; i.getTime() <= fullPeriodEnd.getTime(); i = this.offsetPeriod(i, periodEnum, 1)) {
                Date iStart = this.getPeriodStart(i, periodEnum);
                Date iEnd = this.getPeriodEnd(i, periodEnum);
                if (isExtreme) {
                    result.add(new Period(iStart, iEnd));
                } else {
                    if (i.getTime() == fullPeriodStart.getTime()) {
                        start = iStart;
                    }
                    if (iEnd.getTime() == fullPeriodEnd.getTime()) {
                        end = iEnd;
                    }
                    if (ObjectUtil.isNotEmpty(start) && ObjectUtil.isNotEmpty(end)) {
                        result.add(new Period(start, end));
                    }
                }
            }

            if (this.endTime.getTime() > this.getPeriodStart(this.endTime, periodEnum).getTime() && this.endTime.getTime() < periodEnd.getTime()) {
                result.add(new Period(this.getPeriodStart(this.endTime, periodEnum), this.endTime));
            }
        }
        return result;
    }

    /**
     * 获取指定类型的阶段开始时间
     *
     * @param date
     * @param periodEnum
     * @return
     */
    private Date getPeriodStart(Date date, PeriodEnum periodEnum) {
        Date result = null;
        switch (periodEnum) {
            case YEAR:
                result = DateUtil.beginOfYear(date);
                break;
            case MONTH:
                result = DateUtil.beginOfMonth(date);
                break;
            case DAY:
                result = DateUtil.beginOfDay(date);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 获取指定类型的阶段截止时间
     *
     * @param date
     * @param periodEnum
     * @return
     */
    private Date getPeriodEnd(Date date, PeriodEnum periodEnum) {
        Date result = null;
        switch (periodEnum) {
            case YEAR:
                result = DateUtil.endOfYear(date);
                break;
            case MONTH:
                result = DateUtil.endOfMonth(date);
                break;
            case DAY:
                result = DateUtil.endOfDay(date);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 获取指定类型的阶段截止时间
     *
     * @param date
     * @param periodEnum
     * @return
     */
    private Date offsetPeriod(Date date, PeriodEnum periodEnum, int offset) {
        Date result = null;
        switch (periodEnum) {
            case YEAR:
                result = DateUtil.offsetMonth(date, offset * 12);
                break;
            case MONTH:
                result = DateUtil.offsetMonth(date, offset);
                break;
            case DAY:
                result = DateUtil.offsetDay(date, offset);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 判断是否跨越物理周期  年月日等物理周期
     *
     * @param period
     * @param periodEnum
     * @return true 跨越周期；false 未跨越周期
     */
    private boolean isCrossPeriod(Period period, PeriodEnum periodEnum) {
        boolean result = false;

        // 周期起始时间
        Date periodStart = getPeriodStart(period.startTime, periodEnum);
        // 按周期起始时间开始算，周期结束时间应该是多少
        Date periodEnd = getPeriodEnd(periodStart, periodEnum);
        if (period.endTime.getTime() > periodEnd.getTime()) {
            result = true;
        }
        return result;
    }

    /**
     * 对比区间是否相同
     *
     * @param period
     * @return
     */
    protected boolean compareTo(Period period) {
        boolean result = false;
        if (this.startTime.getTime() == period.startTime.getTime() && this.endTime.getTime() == period.endTime.getTime()) {
            result = true;
        }
        return result;
    }

    /**
     * 日期区间与指定日期关系：日期区间居于指定日期左侧则表示小于，返回-1
     *
     * @param date
     * @return 返回结果：区间判断为左开右闭格式 <br/>
     * 日期区间居于指定日期左侧则表示小于，返回-1；<br/>
     * 日期区间居于指定日期右侧则表示大于于，返回1；<br/>
     * 日期区间包含指定日期则表示包含，返回0; <br/>
     */
    public int compareTo(Date date) {
        int result = 0;
        if (this.endTime.getTime() < date.getTime()) {
            result = -1;
        }
        if (this.startTime.getTime() < date.getTime() && this.endTime.getTime() >= date.getTime()) {
            result = 0;
        }
        if (this.startTime.getTime() > date.getTime()) {
            result = 1;
        }
        return result;
    }

    /**
     * 获取区间之间所差天数 按短日期计算
     *
     * @return
     */
//    public int diffDay() {
//        DateTime st = DateUtil.beginOfDay(this.startTime);
//        DateTime et = DateUtil.endOfDay(this.endTime);
//        long timeDiff = et.getTime() - st.getTime();
//        DivCalculation calculation = new DivCalculation(0, RoundingMode.CEILING);
//        BigDecimal result = calculation.getResult(new BigDecimal(timeDiff), new BigDecimal(1000 * 60 * 60 * 24));
//
//        return result.intValue();
//    }

    /**
     * 判断日期区间是否包含指定日期
     *
     * @param date
     * @return
     */
    public boolean contain(Date date) {
        boolean result = false;
        if (this.startTime.getTime() <= date.getTime() && this.endTime.getTime() >= date.getTime()) {
            result = true;
        }
        return result;
    }
}
