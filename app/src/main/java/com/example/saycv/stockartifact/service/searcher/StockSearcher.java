package com.example.saycv.stockartifact.service.searcher;

import com.example.saycv.stockartifact.model.Stock;
import com.example.saycv.stockartifact.service.Lang;

public interface StockSearcher {
    void setLanguage(Lang language);

    Stock searchStock(String quote);

}
