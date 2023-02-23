package com.example.project;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Ferreira
 * @version 1.0
 * @since 2023-02-23
 *
 * The StockPortfolio class represents a portfolio of stocks with the capability to calculate its total value based on the
 * current market price of each stock.
 * It holds a list of Stock objects and uses an IStockMarketService implementation to look up the current
 * market price of each stock.
 */
public class StockPortfolio {
    private List<Stock> stocks;
    private final IStockMarketService stockMarket;

    /**
     * Creates a new instance of StockPortfolio with an IStockMarketService implementation to be used for looking up the
     * current market price of the stocks.
     *
     * @param stockMarket an IStockMarketService implementation used for looking up the current market price of the stocks
     */
    public StockPortfolio(IStockMarketService stockMarket) {
        this.stockMarket = stockMarket;
    }
    /**
     * Adds a new stock to the portfolio.
     *
     * @param stock the stock to be added to the portfolio
     */
    public void addStock(Stock stock) {
        if (stocks == null)
            stocks = new ArrayList<>();
        stocks.add(stock);
    }
    /**
     * Calculates the total value of the portfolio based on the current market price of each stock.
     *
     * @return the total value of the portfolio
     */
    public double getTotalValue() {
        return stocks.stream().mapToDouble(stock -> stockMarket.lookUpPrice(stock.getLabel()) * stock.getQuantity()).sum();
    }
}
