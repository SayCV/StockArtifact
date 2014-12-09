package com.example.saycv.stockartifact.service.fetcher;

import org.apache.http.client.HttpClient;

import com.example.saycv.stockartifact.model.StockDetail;
import com.example.saycv.stockartifact.service.exception.DownloadException;
import com.example.saycv.stockartifact.service.exception.ParseException;

public interface RadarFetcher {
    HttpClient getClient();
    
    String getUrl(String quote);

    StockDetail fetch(String quote) throws DownloadException, ParseException;
}
