package com.example.saycv.stockartifact;

import android.app.Application;
import android.util.Log;

import com.example.saycv.stockartifact.model.Portfolio;
import com.example.saycv.stockartifact.model.Stock;
import com.example.saycv.stockartifact.service.FilePortfolioService;
import com.example.saycv.stockartifact.service.PortfolioService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StockApplication extends Application {
    private static final String TAG = "StockApplication";
    private static PortfolioService portfolioService;
    private static Portfolio currentPortfolio;
    private static ExecutorService executor;

    public static PortfolioService getPortfolioService() {
        return portfolioService;
    }

    public static Portfolio getCurrentPortfolio() {
        return currentPortfolio;
    }

    public static void setCurrentPortfolio(Portfolio currentPortfolio) {
        StockApplication.currentPortfolio = currentPortfolio;
    }

    /**
     * @return the executor
     */
    public static ExecutorService getExecutor() {
        return executor;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        portfolioService = new FilePortfolioService(this.getFilesDir());
        executor = Executors.newFixedThreadPool(SettingsActivity.getConcurrent(this));

        List<Portfolio> allPortfolios = portfolioService.list();
        if (allPortfolios.size() > 0) {
            Log.i(TAG, "selected first portfolio");
            currentPortfolio = allPortfolios.get(0);

        } else {
            Log.i(TAG, "created new portfolio");
            Portfolio p = new Portfolio();
            p.setName(getResources().getString(R.string.new_portfolio_label));
            List<Stock> stocks = new ArrayList<Stock>();
            p.setStocks(stocks);
            portfolioService.create(p);
            currentPortfolio = p;

        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
