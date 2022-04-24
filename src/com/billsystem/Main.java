package com.billsystem;

import com.billsystem.constants.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    static BufferedReader reader;

    /**
     * To choose storage type
     * 1.database
     * 2.json
     *
     * @return Data
     * @throws IOException
     */
    private static Data chooseStorage() throws IOException {
        Data data;

        reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Data Storage (db|json): ");
        String choice = reader.readLine();
        if (choice.equals(Constants.STORE_AS_DB))
            data = new DB();
        else if (choice.equals(Constants.STORE_AS_JSON))
            data = new Json();
        else {
            System.out.print("Invalid Input. ");
            data = chooseStorage();
        }
        return data;
    }

    public static void main(String[] args) {
        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            Data data = chooseStorage();

            boolean run = true;
            while (run) {
                System.out.println("\nPress 1 to signIn\nPress 2 to signUp\nPress 0 to Close");
                int choice = Integer.parseInt(reader.readLine());
                Log log = new Log();
                Log.data = data;

                switch (choice) {
                    case 1:
                        System.out.print("Enter UserId: ");
                        int userId = Integer.parseInt(reader.readLine());
                        System.out.print("Enter Password: ");
                        String password = reader.readLine();
                        log.signIn(userId, password);
                        break;
                    case 2:
                        log.signup();
                        break;
                    case 0:
                        run = false;
                }
            }
        } catch (IOException | NullPointerException | NumberFormatException error) {
            error.printStackTrace();
        }
    }

}
