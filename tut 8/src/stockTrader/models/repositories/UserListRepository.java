package stockTrader.models.repositories;

import stockTrader.models.entities.User;

import java.util.ArrayList;

public class UserListRepository {
    private ArrayList<User> users;

    public ArrayList<User> getUserList() {
        return users;
    }

    public boolean StoreUserList() {
        if (users.isEmpty() || users == null) return false;
        return true;
    }



}
