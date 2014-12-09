package com.example.saycv.stockartifact.service.fetcher;

import com.example.saycv.stockartifact.model.Index;
import com.example.saycv.stockartifact.service.exception.DownloadException;
import com.example.saycv.stockartifact.service.exception.ParseException;

import java.util.List;

/**
 * Return list of stock indexes
 * @author siuying
 *
 */
public interface RadarFetcher {
    List<Index> fetch() throws DownloadException, ParseException;
}
