package com.billsystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

public class Admin extends User {
    Data data;

    public Admin(int id, String name, String password, String role) {
        super(id, password, role, name);
    }

    @Override
    public void start(Data data) {
        try {
            this.data = data;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            boolean value = true;
            System.out.println("Welcome Admin-" + this.getId());
            while (value) {
                System.out.println(
                        "\nPress 1 to view all users\nPress 2 to remove user\nPress 3 to add item\nPress 4 to view all items\nPress 5 to update item quantity\nPress 6 to add admin\nPress 7 to top sell by quantity\nPress 0 to Logout");
                int choice = Integer.parseInt(reader.readLine());
                switch (choice) {
                    case 1:
                        viewAllUsers();
                        break;
                    case 2:
                        System.out.print("Enter UserId: ");
                        int userId = Integer.parseInt(reader.readLine());
                        removeUser(userId);
                        break;
                    case 3:
                        System.out.print("Enter item name: ");
                        String itemName = reader.readLine();
                        System.out.print("Enter item category: ");
                        String category = reader.readLine();
                        System.out.print("Enter price per unit: ");
                        float price = Float.parseFloat(reader.readLine());
                        System.out.print("Enter quantity: ");
                        int quantity = Integer.parseInt(reader.readLine());
                        System.out.print("Did item have discount (y|n): ");
                        String discount = reader.readLine();
                        addItem(itemName, category, price, quantity, discount);
                        break;
                    case 4:
                        Item.viewAll(data);
                        break;
                    case 5:
                        System.out.print("Enter item id: ");
                        int itemId = Integer.parseInt(reader.readLine());
                        System.out.print("Enter quantity: ");
                        int newQuantity = Integer.parseInt(reader.readLine());
                        updateItem(itemId, newQuantity);
                        break;
                    case 6:
                        System.out.print("Enter user name: ");
                        String userName = reader.readLine();
                        System.out.print("Enter password: ");
                        String password = reader.readLine();
                        addAdmin(userName, password);
                        break;
                    case 7:
                        System.out.print("Enter limit [Default: 3]: ");
                        String limit = reader.readLine();
                        topSellByQuantity(limit);
                        break;
                    case 0:
                        value = false;
                }
            }
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    void viewAllUsers() {
        System.out.println("Id\tName\tPassword\tRole");
        List<User> userList = data.all_users();
        for (User user : userList) {
            System.out
                    .println(user.getId() + "\t" + user.getName() + "\t" + user.getEncryptedPassword() + "\t" + user.getRole());
        }
    }

    private void removeUser(int userId) {
        if (data.remove_user(userId)) {
            System.out.println("User Removed");
        } else
            System.out.println("Invalid User");
    }

    private void addItem(String itemName, String category, float price, int quantity, String discount) {
//		int itemId = data.all_items().size() + 1;

        int itemId = new Random(System.currentTimeMillis()).nextInt(2000) + 10000;
        int sellQuantity = 0;
        if (discount.equals("y"))
            discount = "true";
        else
            discount = "false";
        Item item = new Item(itemId, itemName, category, price, quantity, discount, sellQuantity);
        data.store_item(item);
        System.out.println("Item added.");
    }

    private void updateItem(int itemId, int quantity) {
        if (data.update_quantity(itemId, quantity)) {
            System.out.println("Item quantity updated.");
        } else {
            System.out.println("Invalid Item");
        }
    }

    private void addAdmin(String userName, String password) {
//		int userId = data.all_users().size();

        int userId = new Random(System.currentTimeMillis()).nextInt(2000) + 10000;
        data.store_user(new Admin(userId, userName, password, "Admin"));
        System.out.println("Admin added.");
    }

    private void topSellByQuantity(String limit) {
        System.out.println("Id" + "\t" + "Name" + "\t" + "Category" + "\t" + "Quantity");
        List<Item> List;
        if (limit.isEmpty())
            List = data.topSellByQuantity(3);
        else
            List = data.topSellByQuantity(Integer.parseInt(limit));
        for (Item item : List) {
            System.out.println(item.getItemId() + "\t" + item.getItemName() + "\t" + item.getItemCategory() + "\t"
                    + item.getSellQuantity());
        }
    }

}
