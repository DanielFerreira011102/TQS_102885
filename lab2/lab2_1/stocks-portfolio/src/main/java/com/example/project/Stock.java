package com.example.project;

/**
 * @author Daniel Ferreira
 * @version 1.0
 * @since 2023-02-23
 *
 * The Stock class represents a stock item in a store inventory.
 * It contains information about the label of the stock item and the quantity of the item that is available in the store.
 */
public class Stock {
    private String label; // the label of the stock item
    private Integer quantity; // the quantity of the stock item

    /**
     * Constructs a new Stock object with the specified label and quantity.
     *
     * @param label the label of the stock item
     * @param quantity the quantity of the stock item
     */
    public Stock(String label, Integer quantity) {
        this.label = label;
        this.quantity = quantity;
    }

    /**
     * Returns the label of the stock item.
     *
     * @return the label of the stock item
     */
    public String getLabel() {
        return label;
    }
    /**
     * Sets the label of the stock item.
     *
     * @param label the label of the stock item to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns the quantity of the stock item.
     *
     * @return quantity of the stock item
     */
    public Integer getQuantity() {
        return quantity;
    }
    /**
     * Sets the quantity of the stock item.
     *
     * @param quantity the quantity of the stock item to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
