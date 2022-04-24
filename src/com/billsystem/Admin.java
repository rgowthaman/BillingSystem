package com.billsystem;

import com.billsystem.constants.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Admin extends User {
    static Data data;

    public Admin(int id, String name, String password, String role) {
        super(id, password, role, name);
    }

    /**
     * To start using Admin access
     */
    @Override
    public void start() {
        try {
            Admin.data = this.getStorage();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            boolean value = true;
            System.out.println(Constants.GREET + " " + Constants.ADMIN_ROLE + "-" + this.getId());
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
                        System.out.print("Enter limit [Default: " + Constants.DEFAULT_LIMIT + "]: ");
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

    /**
     * To print all user details
     */
    void viewAllUsers() {
        System.out.format("%5s%10s%15s%10s\n", "Id", "Name", "Password", "Role");
        List<User> userList = data.all_users();
        for (User user : userList) {
            System.out.format("%5d%10s%15s%10s\n", user.getId(), user.getName(), user.getStoredPassword(), user.getRole());
        }
    }

    /**
     * To remove user data from database
     *
     * @param userId
     */
    private void removeUser(int userId) {
        if (data.remove_user(userId)) {
            System.out.println("User Removed");
        } else
            System.out.println("Invalid User");
    }

    /**
     * To add new item to the database
     *
     * @param itemName
     * @param category
     * @param price
     * @param quantity
     * @param discount
     */
    private void addItem(String itemName, String category, float price, int quantity, String discount) {
        int itemId = Integer.parseInt(Constants.GENERATE_ITEM_ID);
        int sellQuantity = 0;
        discount = discount.equalsIgnoreCase(Constants.YES_SHORT) ? Constants.TRUE : Constants.FALSE;
        Item item = new Item(itemId, itemName, category, price, quantity, discount, sellQuantity);
        data.store_item(item);
        System.out.println("Item added.");
    }

    /**
     * To update available quantity of the item
     *
     * @param itemId
     * @param quantity
     */
    private void updateItem(int itemId, int quantity) {
        if (data.update_quantity(itemId, quantity)) {
            System.out.println("Item quantity updated.");
        } else {
            System.out.println("Invalid Item");
        }
    }

    /**
     * To add new admin to database
     *
     * @param userName
     * @param password
     */
    private void addAdmin(String userName, String password) {
        int userId = Integer.parseInt(Constants.GENERATE_ADMIN_ID);
        data.store_user(new Admin(userId, userName, password, Constants.ADMIN_ROLE));
        System.out.println("Admin added.");
    }

    /**
     * To print top-selling items
     *
     * @param limit
     */
    private void topSellByQuantity(String limit) {
        limit = limit.isEmpty() ? Constants.DEFAULT_LIMIT : limit;
        List<Item> List = data.topSellByQuantity(Integer.parseInt(limit));
        if (List.size() > 0) {
            System.out.format("%5s%10s%15s%10s\n", "ID", "Name", "Category", "Quantity");
            for (Item item : List) {
                System.out.format("%5d%10s%15s%10d\n", item.getItemId(), item.getItemName(), item.getItemCategory(), item.getSellQuantity());
            }
        } else {
            System.out.println("No Orders Placed.\n");
        }
    }

}
