package com.billsystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

public class Customer extends User {
    Data data;
    private BufferedReader reader;

    public Customer(int id, String name, String password, String role) {
        super(id, password, role, name);
    }

    @Override
    public void start(Data data) {
        try {
            this.data = data;
            reader = new BufferedReader(new InputStreamReader(System.in));
            boolean value = true;
            System.out.println("Welcome, " + this.getName() + "-" + this.getId() + "\n");
            while (value) {
                System.out.println("Press 1 to Place an Order\nPress 2 to View the order history\nPress 0 to Logout");
                int choice = Integer.parseInt(reader.readLine());
                switch (choice) {
                    case 1:
                        Item.viewAll(data);
                        placeOrder();
                        break;
                    case 2:
                        getHistory();
                        break;
                    case 0:
                        value = false;
                }
            }
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    private void placeOrder() {
        int orderId = new Random(System.currentTimeMillis()).nextInt(2000) + 10000;
        float totalPrice = 0, eligiblePrice = 0;
        boolean shopping = true;
        float eligible_percent = 0;
        HashMap<Integer, Integer> item_details = new HashMap<>();
        try {
            while (shopping) {
                int itemId;

                System.out.print("Enter item id: ");
                itemId = Integer.parseInt(reader.readLine());
                System.out.print("Enter quantity: ");
                int quantity = Integer.parseInt(reader.readLine());

                // get item
                Item item = data.load_item(itemId);
                int actualQuantity = item.getAvailableQuantity();

                // check quantity
                if (actualQuantity < quantity) {
                    System.out.println("Stock not available.");
                } else {
                    // save in map(id, quantity)
                    item_details.put(itemId, quantity);
                    if (item.getDiscount().equals("true"))
                        eligiblePrice += item.getPricePerUnit() * quantity;
                    else
                        totalPrice += item.getPricePerUnit() * quantity;
                    data.update_quantity(itemId, item.getAvailableQuantity() - quantity);
                    data.updateSellQuantity(itemId, item.getSellQuantity() + quantity);
                }
                // checkout ?
                System.out.print("Do you want to checkout (y|n): ");
                String checkout = reader.readLine();
                if (checkout.equalsIgnoreCase("y")) {
                    shopping = false;
                }
            }

            // coupon ?
            System.out.print("Do you have coupon: ");
            String coupon = reader.readLine();
            if (coupon.startsWith("PROMO")) {
                eligible_percent = (float) (1 - Float.parseFloat(coupon.replace("PROMO", "")) * 0.01);
                if (data.isCouponAcceptable(this.getId(), eligible_percent))
                    eligiblePrice *= eligible_percent;
            }
            totalPrice += eligiblePrice;
            data.store_orders(new Order(orderId, this.getId(), totalPrice, eligible_percent, item_details));

            // print cart
            System.out.println();
            Order details = data.load_order(this.getId(), orderId);
            displayCart(details.item_details, details.discount);
            System.out.print("Total Price: " + totalPrice + "\n\nConfirm your order (y|n): ");
            String confirmation = reader.readLine();

            if (confirmation.equalsIgnoreCase("y") && totalPrice > 0) {
                System.out.println("Order Confirmed.\n");
            } else {
                for (Entry<Integer, Integer> itr : data.load_order(this.getId(), orderId).item_details.entrySet()) {
                    Item item = data.load_item(itr.getKey());
                    data.update_quantity(item.getItemId(), item.getAvailableQuantity() + itr.getValue());
                    data.updateSellQuantity(item.getItemId(), item.getSellQuantity() - itr.getValue());
                    System.out.println();
                }
                data.remove_order(orderId);
                if (totalPrice > 0)
                    System.out.println("Order Canceled.\n");
            }
        } catch (NumberFormatException | IOException error) {
            System.out.println("Error: " + error);
        } catch (NullPointerException error) {
            error.printStackTrace();
            System.out.println("Invalid Item.\n");
        }
    }

    private void displayCart(HashMap<Integer, Integer> cart, float coupon) {
        System.out.println("Name\tCategory\tQuantity\tPrice");
        for (Entry<Integer, Integer> itemDetail_itr : cart.entrySet()) {
            Item item = data.load_item(itemDetail_itr.getKey());
            int quantity = itemDetail_itr.getValue();

            System.out.print(item.getItemName() + "\t" + item.getItemCategory() + "\t" + quantity);
            float itemPrice = item.getPricePerUnit() * quantity;
            if (item.getDiscount().equals("true") && coupon > 0 && !(data.isCouponAcceptable(this.getId(), coupon)))
                System.out.println("\t" + (itemPrice * coupon));
            else
                System.out.println("\t" + itemPrice);
        }
    }

    public void getHistory() {
        List<Order> orderList = data.all_orders(this.getId());
        if (orderList.isEmpty()) {
            System.out.println("No orders placed.\n");
            return;
        }
        int i = 1;
        for (Order orderDetail : orderList) {
            System.out.println("Order-" + i);
            displayCart(orderDetail.item_details, orderDetail.discount);
            System.out.println("Total Price: " + orderDetail.totalPrice + "\n");
            i++;
        }
    }

    public static class Order {
        public int orderId;
        public int userId;
        public float totalPrice;
        public float discount;
        public HashMap<Integer, Integer> item_details;

        public Order(int orderId, int customerId, float totalPrice, float discount,
                     HashMap<Integer, Integer> item_details) {
            this.orderId = orderId;
            this.userId = customerId;
            this.totalPrice = totalPrice;
            this.discount = discount;
            this.item_details = item_details;
        }

    }

}
