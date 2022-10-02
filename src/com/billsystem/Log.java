package com.billsystem;

import com.billsystem.constants.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Log {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private Data data;

    public Log(Data data) {
        this.data = data;
    }

    /**
     * To sigIn to user account
     *
     * @param userId
     * @param password
     */
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

    /**
     * To create a new user account
     */
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
        data.store_user(new Customer(userId, userName, password, Constants.CUSTOMER_ROLE, data));
        signIn(userId, password);
    }

    /**
     * To authenticate user login
     *
     * @param user
     * @param password
     * @return
     */
    boolean authenticate(User user, String password) {
        return user.getStoredPassword().equals(User.encrypt(password));
    }

}
