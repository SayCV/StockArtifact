package com.example.saycv.stockartifact.model;

import java.math.BigDecimal;
import java.util.Calendar;

public class Radar {
    private String name;
    private String code;
    private String type;
    
    private BigDecimal price;
    private BigDecimal volume;
    private Calendar updatedAt;
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the volume
     */
    public BigDecimal getVolume() {
        return volume;
    }

    /**
     * @param volume
     *            the change to set
     */
    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the value to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(
                "Index [time=%s, code=%s, name=%s, price=%s, type=%s, volume=%s]",
                updatedAt, code, name, price, type, volume );
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
