package com.example.saycv.stockartifact.model;

import java.math.BigDecimal;
import java.util.Calendar;

public class Radar {
    private String time;
    private String code;
    private String name;

    private String type;
    private String price;
    private String volume;


    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getVolume() {
        return volume;
    }
    public void setVolume(String volume) {
        this.volume = volume;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(
                "Index [time=%s, code=%s, name=%s, price=%s, type=%s, volume=%s]",
                time, code, name, price, type, volume );
    }

}
