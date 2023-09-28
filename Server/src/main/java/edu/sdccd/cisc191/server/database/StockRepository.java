package edu.sdccd.cisc191.server.database;

// Jpa extends PagingAndSortingRepository and PagingAndSortingRepository extends CRUD
// This means Jpa has more features than CrudRepository
import edu.sdccd.cisc191.server.database.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    // @Query("SELECT stock FROM Stock stock WHERE stock.ticker = ?1")
    // Stock findByTicker(String ticker);
}
