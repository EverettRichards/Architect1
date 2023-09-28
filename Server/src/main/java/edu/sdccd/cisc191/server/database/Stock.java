package edu.sdccd.cisc191.server.database;

import java.util.HashMap;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="StockTable")
public class Stock {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) 	
    private int id;

    private String ticker;
    private String name;
    private String description;

    // @OneToOne
    // @JoinColumn(name="fk_ticker")
    // private CandleData candleData;

    // public Stock(String ticker, String name, String description, CandleData candleData) {
    //     this.ticker = ticker;
    //     this.name = name;
    //     this.description  = description;
    //     this.candleData = candleData;
    // }
}
