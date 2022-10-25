package stockTrader.models.repositories;

import stockTrader.models.entities.StockInformation;

import java.util.ArrayList;

public class StockCommandRepository {
    private ArrayList<StockInformation> userHistory;

    public ArrayList<StockInformation> getStockCommands() {
        return userHistory;
    }

    public boolean StoreStockCommands() {
        if (userHistory.isEmpty()) return false;
        return true;
    }

/*    public boolean AppendLastStockCommand() {

    }*/
}
