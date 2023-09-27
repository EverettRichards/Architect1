package edu.sdccd.cisc191.server;

import edu.sdccd.cisc191.common.entities.User;

public class StockTrader extends User
{
    public String stockTraderName;

    public String stockTraderID;

    public StockTrader(String name, int authLevel) {
        super(name, authLevel);
    }

    public String getStockTraderName() {
        return stockTraderName;
    }
    public void setStockTraderName(String traderName) {
        this.stockTraderName = traderName;
    }
    public String getStockTraderID(){
        return stockTraderID;
    }
    public void setStockTraderID(String traderID){
        this.stockTraderID = traderID;
    }

    //TODO other methods
}

