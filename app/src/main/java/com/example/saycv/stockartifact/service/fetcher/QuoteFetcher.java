package com.example.saycv.stockartifact.service.fetcher;

import com.example.saycv.stockartifact.model.StockDetail;
import com.example.saycv.stockartifact.service.exception.DownloadException;
import com.example.saycv.stockartifact.service.exception.ParseException;

import org.apache.http.client.HttpClient;

public interface QuoteFetcher {
    HttpClient getClient();

    String getUrl(String quote);

    StockDetail fetch(String quote) throws DownloadException, ParseException;
}
