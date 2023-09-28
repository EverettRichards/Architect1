package edu.sdccd.cisc191.server.database;

import org.springframework.stereotype.Service;

@Service
public interface StockService {
    void addStock(Stock stock);
}
