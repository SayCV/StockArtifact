package com.example.saycv.stockartifact.service.fetcher;

import android.content.Context;

public class IndexesFetcherFactory {
    public static IndexesFetcher getIndexesFetcher(Context context) {
        return new  QQIndexesFetcher(context);
    }
}
