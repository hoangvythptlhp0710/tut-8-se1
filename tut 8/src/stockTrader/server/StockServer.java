package stockTrader.server;

import stockTrader.entities.Stock;
import stockTrader.entities.StockInformation;
import stockTrader.entities.User;

import java.io.File;
import java.nio.file.AccessDeniedException;
import java.time.LocalTime;
import java.util.*;

public class StockServer {

    private Map<Integer, Stock> map;
    private ArrayList<User> users;
    private ArrayList<Stock> stocks;
    private ArrayList<StockInformation> userStocks;
    private ArrayList<StockInformation> userHistory;
    private Double userMoney;
    private boolean isLoggedin;
    private LocalTime currentDate;

    public StockServer(ArrayList<User> users, ArrayList<Stock> stocks, ArrayList<StockInformation> userStocks, ArrayList<StockInformation> userHistory, Double userMoney, boolean isLoggedin, LocalTime currentDate, Map<Integer, Stock> map) {
        this.users = users;
        this.stocks = stocks;
        this.userStocks = userStocks;
        this.userHistory = userHistory;
        this.userMoney = userMoney;
        this.isLoggedin = isLoggedin;
        this.currentDate = currentDate;
        this.map = map;
    }

    public boolean login(String username, String password) throws AccessDeniedException {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                isLoggedin = true;
            } else {
                isLoggedin = false;
                throw new AccessDeniedException("User or password is incorrect!");
            }
        }
        return isLoggedin;

    }

    public String listAllStocks() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stocks.size(); i++) {
            map.put(i + 1, stocks.get(i));
        }
        for (Map.Entry<Integer, Stock> entrySet : map.entrySet()) {
            sb.append(String.format("%d. %s", entrySet.getKey(), entrySet.getValue().getQuantity()));
        }
        return sb.toString();
    }

    public boolean purchase(int stockNo, int quantity) {
        boolean purchased = true;
        for (Map.Entry<Integer, Stock> stocksMap : map.entrySet()) {
            if (stockNo > stocksMap.getKey() || stockNo < 0) {
                purchased = false;
            } else {
                if (quantity > stocksMap.getValue().getQuantity() || quantity < 0) {
                    purchased = false;
                }
            }
        }
        return purchased;
    }

    public String listOwnStocks() {
        StringBuilder ownStock = new StringBuilder();
//        map.entrySet().forEach(entry -> {
//            userStocks.forEach(stockInfo -> {
//                Stock purchasedStock = stockInfo.getStock();
//                if (Objects.equals(purchasedStock, entry.getValue())) {
//                    ownStock.append(String.format("%d: %s", entry.getKey(), entry.getValue().getName()));
//                }
//            });
//        });
        for (Map.Entry<Integer, Stock> stocksMap : map.entrySet()) {
            for (StockInformation stockInfo : userStocks) {
                if (stocksMap.getValue().equals(stockInfo.getStock())) {
                    ownStock.append(String.format("%d. %s", stocksMap.getKey(), stocksMap.getValue().getName()));
                }
            }
        }
        return ownStock.toString();
    }

    public boolean sellStock(int stockNo, int quantity) {
        for (Map.Entry<Integer, Stock> stocksMap : map.entrySet()) {
            for (StockInformation stockInfo : userStocks) {
                if (stocksMap.getKey().equals(stockNo)) {
                    if (stockInfo.getStock().getQuantity() >= quantity) {
                        int res = stockInfo.getStock().getQuantity() - quantity;
                        stockInfo.setStock(new Stock(stockInfo.getStock().getName(), stockInfo.getStock().getPrice(), res));
                        userHistory.add(stockInfo);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void nextDay() {
        for (Map.Entry<Integer, Stock> stocksMap : map.entrySet()) {
            stocksMap.getValue().setQuantity(stocksMap.getValue().getQuantity() + 1);
        }
    }

    public Double checkBalance() {
        return userMoney;
    }

    public String getPrice() {
        StringBuilder sb = new StringBuilder();
        for (Stock stock: stocks) {
            sb.append(String.format("%s. %f", stock.getName(), stock.getPrice()));
            System.out.println();
        }
        return sb.toString();
    }

    private ArrayList<Stock> getCurrentPrice() {
        ArrayList<Stock> currentPrice = new ArrayList<>();
        for (Stock stock : stocks) {
            if (stock.getPrice() > 0) {
                currentPrice.add(stock);
            }
        }
        return currentPrice;
    }

    private boolean storeInformation(StockInformation info) {
        try {
            File file = new File("stocks.txt");
            Scanner sc = new Scanner(file);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] split = line.split(" ");
                String name = split[0];
                Double price = Double.parseDouble(split[1]);
                Integer quantity = Integer.parseInt(split[2]);
                Stock stock = new Stock(name, price, quantity);
                stocks.add(stock);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
