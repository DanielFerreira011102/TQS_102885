package com.example.project;

/**
 * @author Daniel Ferreira
 * @version 1.0
 * @since 2023-02-23
 *
 * The IStockMarketService interface represents a service that can be used to look up the price of a stock by its label.
 * It defines a single method to retrieve the stock price.
 */
public interface IStockMarketService {
    /**
     * Retrieves the price of a stock by its label.
     *
     * @param stockLabel the label of the stock to retrieve the price for
     * @return the price of the stock as a double
     */
    double lookUpPrice(String stockLabel);
}
