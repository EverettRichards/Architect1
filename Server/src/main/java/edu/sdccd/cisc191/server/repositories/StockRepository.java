package edu.sdccd.cisc191.server.repositories;

import edu.sdccd.cisc191.common.entities.Stock;
import org.springframework.data.repository.CrudRepository;

public interface StockRepository extends CrudRepository<Stock, String> {
}
