package com.example.saycv.stockartifact.service;

import com.example.saycv.stockartifact.model.Portfolio;
import com.example.saycv.stockartifact.model.Stock;
import com.example.saycv.stockartifact.model.StockDetail;
import com.thoughtworks.xstream.XStream;

import java.io.Reader;
import java.util.List;

public class PortfolioSerializer {
    public static String toXML(List<Portfolio> portfolios) {
        XStream xstream = getXStream();
        return xstream.toXML(portfolios);
    }

    @SuppressWarnings("unchecked")
    public static List<Portfolio> fromXML(String xml) {
        XStream xstream = getXStream();
        return (List<Portfolio>) xstream.fromXML(xml);
    }

    @SuppressWarnings("unchecked")
    public static List<Portfolio> fromXML(Reader xml) {
        XStream xstream = getXStream();
        return (List<Portfolio>) xstream.fromXML(xml);
    }

    private static XStream getXStream() {
        XStream xstream = new XStream();
        xstream.alias("sk", Stock.class);
        xstream.alias("po", Portfolio.class);
        xstream.alias("sd", StockDetail.class);
        xstream.aliasField("q", StockDetail.class, "quote");
        xstream.aliasField("n", StockDetail.class, "name");
        xstream.aliasField("su", StockDetail.class, "sourceUrl");
        xstream.aliasField("v", StockDetail.class, "volume");
        xstream.aliasField("p", StockDetail.class, "price");
        xstream.aliasField("cp", StockDetail.class, "changePrice");
        xstream.aliasField("cpp", StockDetail.class, "changePricePercent");
        xstream.aliasField("h", StockDetail.class, "dayHigh");
        xstream.aliasField("l", StockDetail.class, "dayLow");
        xstream.aliasField("ud", StockDetail.class, "updatedAt");
        return xstream;
    }
}