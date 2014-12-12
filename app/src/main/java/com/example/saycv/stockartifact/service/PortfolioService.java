package com.example.saycv.stockartifact.service;

import com.example.saycv.stockartifact.model.Portfolio;

import java.util.List;

public interface PortfolioService {
    void create(Portfolio portfolio);

    List<Portfolio> list();

    void update(Portfolio portfolio);

    void delete(Portfolio portfolio);


}
