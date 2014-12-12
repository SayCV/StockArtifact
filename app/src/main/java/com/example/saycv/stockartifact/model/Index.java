package com.example.saycv.stockartifact.model;

import java.math.BigDecimal;
import java.util.Calendar;

public class Index {
    private String name;

    private BigDecimal value;
    private BigDecimal change;
    private BigDecimal changePercent;
    private Calendar updatedAt;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the change
     */
    public BigDecimal getChange() {
        return change;
    }

    /**
     * @param change the change to set
     */
    public void setChange(BigDecimal change) {
        this.change = change;
    }

    /**
     * @return the changePercent
     */
    public BigDecimal getChangePercent() {
        return changePercent;
    }

    /**
     * @param changePercent the changePercent to set
     */
    public void setChangePercent(BigDecimal changePercent) {
        this.changePercent = changePercent;
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(
                "Index [name=%s, value=%s, change=%s, changePercent=%s]", name,
                value, change, changePercent);
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
