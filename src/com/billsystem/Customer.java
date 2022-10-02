package com.billsystem;

import com.billsystem.constants.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Customer extends User {
    private Data data;
    private BufferedReader reader;

    public Customer(int id, String name, String password, String role) {
        this(id, name, password, role, null);
    }

    public Customer(int id, String name, String password, String role, Data data) {
        super(id, password, role, name);
        this.data = data;
    }

    public void setStorage(Data data) {
        this.data = data;
    }

    /**
     * To start using customer access
     */
    @Override
    public void start() {
        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            boolean value = true;
            System.out.println(Constants.GREET + ", " + this.getName() + "-" + this.getId() + "\n");
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

    /**
     * To place an order
     */
    private void placeOrder() {
        int orderId = Integer.parseInt(Constants.GENERATE_ORDER_ID);
        float totalPrice = 0;
        float eligiblePrice = 0;
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
                    if (item.getDiscount().equals(Constants.TRUE))
                        eligiblePrice += item.getPricePerUnit() * quantity;
                    else
                        totalPrice += item.getPricePerUnit() * quantity;
                    data.update_quantity(itemId, item.getAvailableQuantity() - quantity);
                    data.updateSellQuantity(itemId, item.getSellQuantity() + quantity);
                }
                // checkout ?
                System.out.print("Do you want to checkout (y|n): ");
                String checkout = reader.readLine();
                if (checkout.equalsIgnoreCase(Constants.YES_SHORT)) {
                    shopping = false;
                }
            }

            // coupon ?
            System.out.print("Do you have coupon: ");
            String coupon = reader.readLine();
            if (coupon.startsWith(Constants.DISCOUNT_PROMO)) {
                eligible_percent = (float) (1 - Float.parseFloat(coupon.replace(Constants.DISCOUNT_PROMO, Constants.EMPTY_STRING)) * 0.01);
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

            if (confirmation.equalsIgnoreCase(Constants.YES_SHORT) && totalPrice > 0) {
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
        } catch (NumberFormatException | IOException | NullPointerException error) {
            error.printStackTrace();
        }
    }

    /**
     * To view customer order(s)
     *
     * @param cart
     * @param coupon
     */
    private void displayCart(HashMap<Integer, Integer> cart, float coupon) {
        System.out.format("%10s%15s%10s%10s\n", "Name", "Category", "Quantity", "Price");
        for (Entry<Integer, Integer> itemDetail_itr : cart.entrySet()) {
            Item item = data.load_item(itemDetail_itr.getKey());
            int quantity = itemDetail_itr.getValue();

            System.out.format("%10s%15s%10d", item.getItemName(), item.getItemCategory(), quantity);
            float itemPrice = item.getPricePerUnit() * quantity;
            if (item.getDiscount().equals(Constants.TRUE) && coupon > 0 && !(data.isCouponAcceptable(this.getId(), coupon)))
                System.out.format("%10s\n", itemPrice * coupon);
            else
                System.out.format("%10s\n", itemPrice);
        }
    }

    /**
     * To get customer order history
     */
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

    /**
     * To create new Order object
     */
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
