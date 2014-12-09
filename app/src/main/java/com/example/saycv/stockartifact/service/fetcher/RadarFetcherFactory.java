package com.example.saycv.stockartifact.service.fetcher;

public class QuoteFetcherFactory {
    public static QuoteFetcher getQuoteFetcher() {
        return new Money18QuoteFetcher();
    }
}
