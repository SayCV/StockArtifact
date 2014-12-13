package com.example.saycv.stockartifact.model;

import java.math.BigDecimal;
import java.util.Calendar;

public class Index {
    private String name;
    private String code;
    private String time;

    /*--> */
    private BigDecimal value;
    private BigDecimal change;
    private BigDecimal changePercent;
    private Calendar updatedAt;
    /* <--*/

    private String lastDayClose;
    private String lastDayHigh;
    private String lastDayLow;
    private String dayHigh;
    private String dayLow;
    private String dayOpen;
    private String dayClose;
    private String dayVolume;
    private String dayMoney;
    private String dayRange;
    private String dayRangePercent;
    private String daySwing;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BigDecimal getChange() {
        return change;
    }
    public void setChange(BigDecimal change) {
        this.change = change;
    }

    public BigDecimal getChangePercent() {
        return changePercent;
    }
    public void setChangePercent(BigDecimal changePercent) {
        this.changePercent = changePercent;
    }

    public BigDecimal getValue() {
        return value;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getLastDayClose() {
        return lastDayClose;
    }

    public void setLastDayClose(String lastDayClose) {
        this.lastDayClose = lastDayClose;
    }

    public String getLastDayHigh() {
        return lastDayHigh;
    }

    public void setLastDayHigh(String lastDayHigh) {
        this.lastDayHigh = lastDayHigh;
    }

    public String getLastDayLow() {
        return lastDayLow;
    }

    public void setLastDayLow(String lastDayLow) {
        this.lastDayLow = lastDayLow;
    }

    public String getDayHigh() {
        return dayHigh;
    }

    public void setDayHigh(String dayHigh) {
        this.dayHigh = dayHigh;
    }

    public String getDayLow() {
        return dayLow;
    }

    public void setDayLow(String dayLow) {
        this.dayLow = dayLow;
    }

    public String getDayOpen() {
        return dayOpen;
    }

    public void setDayOpen(String dayOpen) {
        this.dayOpen = dayOpen;
    }

    public String getDayClose() {
        return dayClose;
    }

    public void setDayClose(String dayClose) {
        this.dayClose = dayClose;
    }

    public String getDayVolume() {
        return dayVolume;
    }

    public void setDayVolume(String dayVolume) {
        this.dayVolume = dayVolume;
    }

    public String getDayMoney() {
        return dayMoney;
    }

    public void setDayMoney(String dayMoney) {
        this.dayMoney = dayMoney;
    }

    public String getDayRange() {
        return dayRange;
    }

    public void setDayRange(String dayRange) {
        this.dayRange = dayRange;
    }

    public String getDayRangePercent() {
        return dayRangePercent;
    }

    public void setDayRangePercent(String dayRangePercent) {
        this.dayRangePercent = dayRangePercent;
    }

    public String getDaySwing() {
        return daySwing;
    }

    public void setDaySwing(String daySwing) {
        this.daySwing = daySwing;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(
                "Index [name=%s, code=%s, time=%s, " +
                        "lastDayClose=%s, lastDayHigh=%s, lastDayLow=%s, " +
                        "dayHigh=%s, dayLow=%s, dayRange=%s, " +
                        "dayOpen=%s, dayClose=%s, dayVolume=%s, " +
                        "dayRangePercent=%s, daySwing=%s" +
                        "]",
                name, code, time, time,
                lastDayClose, lastDayHigh, lastDayLow,
                dayHigh, dayLow, dayRange,
                dayOpen, dayClose, dayVolume,
                dayRangePercent, daySwing);
    }

    /**
     * @return the updatedAt
     */
    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt the updatedAt to set
     */
    public void setUpdatedAt(Calendar updatedAt) {
        this.updatedAt = updatedAt;
    }


}
