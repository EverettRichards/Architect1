package edu.sdccd.cisc191.server.repositories;

import org.springframework.data.repository.CrudRepository;

public interface StockRepository extends CrudRepository<Stock, String> {

    public static void main(String[] args){
        Stock myStock = new Stock();
    }
}
