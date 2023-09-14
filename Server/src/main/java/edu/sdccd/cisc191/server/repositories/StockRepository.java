package edu.sdccd.cisc191.server.repositories;

import org.springframework.data.repository.CrudRepository;
import edu.sdccd.cisc191.common.entities.Stock;

public interface StockRepository extends CrudRepository<Stock, String> {

    public static void main(String[] args){
        Stock myStock = new Stock();
    }
}
