package com.example.saycv.stockartifact.service.fetcher;

import android.content.Context;

public class RadarFetcherFactory {
    public static RadarFetcher getRadarFetcher(Context context) {
        return new QQRadarFetcher(context);
    }
}
