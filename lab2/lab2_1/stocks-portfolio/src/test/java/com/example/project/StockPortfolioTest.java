package com.example.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * @author Daniel Ferreira
 * @version 1.0
 * @since 2023-02-23
 *
 * Unit tests for the {@link StockPortfolio} class.
 */
@ExtendWith(MockitoExtension.class)
public class StockPortfolioTest {

    /**
     * Mock instance of the remote stock market service.
     */
    @Mock
    private IStockMarketService mockStockMarket;

    /**
     * Inject the mock instance of the remote stock market service.
     */
    @InjectMocks
    private StockPortfolio stockPortfolio;

    /**
     * Tests the {@link StockPortfolio#getTotalValue()} method.
     */
    @Test
    @DisplayName("Test StockPortfolio#getTotalValue()")
    public void testGetTotalValue() {
        // Step 1: Prepare a mock to substitute the remote service
        mockStockMarket = mock(IStockMarketService.class);

        // Step 2: Create an instance of the subject under test (SuT)
        // and use the mock to set the (remote) service instance
        StockPortfolio stockPortfolio = new StockPortfolio(mockStockMarket);

        // Step 3: Load the mock with the proper expectations
        // assuming a portfolio with two stocks: G2 and NAVI
        when(mockStockMarket.lookUpPrice("G2")).thenReturn(125.0);
        when(mockStockMarket.lookUpPrice("NAVI")).thenReturn(250.0);

        // Step 4: Execute the test (use the service in the SuT)
        stockPortfolio.addStock(new Stock("G2", 100));
        stockPortfolio.addStock(new Stock("NAVI", 200));
        double expectedTotalValue = 100 * 125.0 + 200 * 250.0;
        double actualTotalValue = stockPortfolio.getTotalValue();

        // Step 5: Verify the result (assert) and the use of the mock (verify)
        assertThat(actualTotalValue, is(equalTo(expectedTotalValue)));
        verify(mockStockMarket, times(2)).lookUpPrice(anyString());
    }

    /**
     * Tests the {@link StockPortfolio#getTotalValue()} method.
     * Steps 1 & 2 can be omitted, using annotations @Mock and @InjectMocks.
     */
    @Test
    @DisplayName("Test StockPortfolio#getTotalValue() using @Mock and @InjectMocks")
    public void testGetTotalValueWithoutSteps1and2() {
        // Step 3: Load the mock with the proper expectations
        // assuming a portfolio with two stocks: G2 and NAVI
        when(mockStockMarket.lookUpPrice("G2")).thenReturn(125.0);
        when(mockStockMarket.lookUpPrice("NAVI")).thenReturn(250.0);

        // Step 4: Execute the test (use the service in the SuT)
        stockPortfolio.addStock(new Stock("G2", 100));
        stockPortfolio.addStock(new Stock("NAVI", 200));
        double expectedTotalValue = 100 * 125.0 + 200 * 250.0;
        double actualTotalValue = stockPortfolio.getTotalValue();

        // Step 5: Verify the result (assert) and the use of the mock (verify)
        assertThat(actualTotalValue, is(equalTo(expectedTotalValue)));
        verify(mockStockMarket, times(2)).lookUpPrice(anyString());
    }
}
