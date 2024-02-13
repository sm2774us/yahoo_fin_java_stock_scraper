package io.github.mainstringargs.yahooFinance;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import io.github.mainstringargs.stockData.spi.StockDataService;
import io.github.mainstringargs.yahooFinance.domain.FinancialData;
import io.github.mainstringargs.yahooFinance.domain.RecommendationTrend;
import io.github.mainstringargs.yahooFinance.domain.Trend;

// TODO: Auto-generated Javadoc
/**
 * The Class YahooFinanceService.
 */
public class YahooFinanceService implements StockDataService {

  /*
   * (non-Javadoc)
   * 
   * @see io.github.mainstringargs.stockData.spi.StockDataService#init()
   */
  public void init() {


  }

  /*
   * (non-Javadoc)
   * 
   * @see io.github.mainstringargs.stockData.spi.StockDataService#getServiceName()
   */
  public String getServiceName() {
    return "Yahoo Finance";
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.github.mainstringargs.stockData.spi.StockDataService#getShortServiceName()
   */
  @Override
  public String getShortServiceName() {
    return "YF";
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.github.mainstringargs.stockData.spi.StockDataService#getStockData(java.lang.String)
   */
  @Override
  public Map<String, Object> getStockData(String ticker) {
    return getYahooStockData(ticker);
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.github.mainstringargs.stockData.spi.StockDataService#getStockData(java.lang.String[])
   */
  public Map<String, Map<String, Object>> getStockData(String... tickers) {

    Map<String, Map<String, Object>> allStockData = new HashMap<String, Map<String, Object>>();

    for (String symbol : tickers) {

      Map<String, Object> stockData = getYahooStockData(symbol);

      allStockData.put(symbol.toUpperCase(), stockData);

    }

    return allStockData;

  }

  /**
   * Gets the yahoo stock data.
   *
   * @param symbol the symbol
   * @return the yahoo stock data
   */
  private Map<String, Object> getYahooStockData(String symbol) {
    YahooFinanceUrlBuilder builder = new YahooFinanceUrlBuilder()
        .modules(YahooFinanceModules.values()).symbol(symbol.toUpperCase());

    YahooFinanceRequest request = new YahooFinanceRequest();

    YahooFinanceData financeData = request.getFinanceData(request.invoke(builder));
    //
    // System.out.println(builder + " " + financeData);


    Map<String, Object> stockData = new LinkedHashMap<String, Object>();


    if (financeData.getFinancialData() != null) {
      FinancialData financials = financeData.getFinancialData();


      stockData.put("Current Price", financials.getCurrentPrice().getRaw());
      // stockData.put("Current Ratio", financials.getCurrentRatio().getRaw());
      stockData.put("Target Low Price", financials.getTargetLowPrice().getRaw());
      stockData.put("Target High Price", financials.getTargetHighPrice().getRaw());
      stockData.put("Target Mean Price", financials.getTargetMeanPrice().getRaw());
      stockData.put("Target Median Price", financials.getTargetMedianPrice().getRaw());
      stockData.put("Recommendation Key", financials.getRecommendationKey());
      stockData.put("Recommendation Mean", financials.getRecommendationMean().getRaw());
      stockData.put("Number of Analyst Opinions", financials.getNumberOfAnalystOpinions().getRaw());

    }

    if (financeData.getRecommendationTrend() != null) {
      RecommendationTrend trend = financeData.getRecommendationTrend();

      for (Trend trendData : trend.getTrend()) {

        stockData.put(trendData.getPeriod().trim() + " Strong Buy", trendData.getStrongBuy());
        stockData.put(trendData.getPeriod().trim() + " Buy", trendData.getBuy());
        stockData.put(trendData.getPeriod().trim() + " Hold", trendData.getHold());
        stockData.put(trendData.getPeriod().trim() + " Sell", trendData.getSell());
        stockData.put(trendData.getPeriod().trim() + " Strong Sell", trendData.getStrongSell());

      }

    }
    return stockData;
  }

}
