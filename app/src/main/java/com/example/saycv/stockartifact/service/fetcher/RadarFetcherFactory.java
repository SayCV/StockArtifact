package com.example.saycv.stockartifact.service.fetcher;

public class RadarFetcherFactory {
    public static RadarFetcher getRadarFetcher() {
        return new QQRadarFetcher();
    }
}
