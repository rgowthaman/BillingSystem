package com.billsystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Log {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static Data data;

    public void signIn(int userId, String password) {
        try {
            User user = data.load_user(userId);
            if (authenticate(user, password)) {
                user.start(data);
            } else
                System.out.println("Invalid password");
        } catch (NumberFormatException error) {
            System.out.println("Error: " + error);
        } catch (NullPointerException err) {
            System.out.println("Invalid User");
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
            System.out.println("Error: " + error);
        }
//		int userId = data.all_users().size();

        int userId = new Random(System.currentTimeMillis()).nextInt(2000) + 10000;
        String userRole = "Customer";

        data.store_user(new Customer(userId, userName, password, userRole));
        signIn(userId, password);
    }

    boolean authenticate(User user, String password) {
        return user.getPassword().equals(password);
    }

}
