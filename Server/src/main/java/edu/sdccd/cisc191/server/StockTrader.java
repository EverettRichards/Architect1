package edu.sdccd.cisc191.server;

import edu.sdccd.cisc191.common.entities.User;

public class StockTrader extends User
{
    public String stockTraderName;

    public String stockTraderID;

    /**
     *  Class constructor that creates new StockTrader by entering name and authorization level
     * @param name  the name of the Stock Trader
     * @param authLevel   the authorization level for the Stock Trader
     * @see User
     */
    public StockTrader(String name, int authLevel) {
        super(name, authLevel);
    }

    @Override
    public int getAuthorizationLevel() {
        return super.getAuthorizationLevel();
    }

    @Override
    public void setAuthorizationLevel(int level) {
        super.setAuthorizationLevel(level);
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

