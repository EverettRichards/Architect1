package edu.sdccd.cisc191.server.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sdccd.cisc191.server.database.StockService;

import edu.sdccd.cisc191.server.Finnhub;

@Service
public class StockServiceJpa implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Transactional
    public void addStock(Stock stock) {
        stockRepository.saveAndFlush(stock);
    }

    // @Transactional
    // public String getCandle(String ticker) {
    //     // Stock stockData = stockRepository.findByTicker(ticker);

    //     // if(stockData.isNull()) {
    //     //     return Finnhub.getCandle();
    //     // }

    //     // return stockData.getCandle();
    //     return null;
    // }

    // @Transactional
    // public String getProfile(String ticker) {
    //     // TODO Auto-generated method stub
    //     return null;
    // }

    // @Transactional
    // public String getStockQuote(String ticker) {
    //     // TODO Auto-generated method stub
    //     return null;
    // }

    // @Transactional
    // public String getStockSymbol(String ticker) {
    //     // TODO Auto-generated method stub
    //     return null;
    // }
}
