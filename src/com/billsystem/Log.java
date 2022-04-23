package com.billsystem;

import com.billsystem.constants.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Log {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static Data data;

    public void signIn(int userId, String password) {
        try {
            User user = data.load_user(userId);
            user.setStorage(data);
            if (authenticate(user, password)) {
                user.start();
            } else
                System.out.println("Invalid password");
        } catch (NumberFormatException | NullPointerException error) {
            error.printStackTrace();
        }
    }

    void signup() {
        String userName = null;
        String password = null;
        try {
            System.out.print("Enter user name: ");
            userName = reader.readLine();
            System.out.print("Enter password: ");
            password = reader.readLine();
        } catch (IOException error) {
            error.printStackTrace();
        }
        int userId = Integer.parseInt(Constants.GENERATE_CUSTOMER_ID);
        data.store_user(new Customer(userId, userName, password, Constants.CUSTOMER_ROLE));
        signIn(userId, password);
    }

    boolean authenticate(User user, String password) {
        return user.getPassword().equals(password);
    }

}
