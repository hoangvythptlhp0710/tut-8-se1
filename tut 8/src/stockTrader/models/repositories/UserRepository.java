package stockTrader.models.repositories;

import stockTrader.models.entities.StockInformation;

import java.util.ArrayList;

public class UserRepository {

    private ArrayList<StockInformation> userStocks;
    private Double userMoney;

    public ArrayList<StockInformation> getUserInfo() {
        return userStocks;
    }

    public boolean storeUserInfo() {
        return !userStocks.isEmpty() && userStocks != null;
    }

    public double getUserMoney() {
        return userMoney;
    }

}
