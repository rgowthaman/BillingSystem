package com.billsystem;

import com.billsystem.Customer.Order;

import java.util.HashMap;
import java.util.List;

public interface Data {

    void store_user(User user);

    User load_user(int user_id);

    List<User> all_users();

    boolean remove_user(int user_id);

    void store_item(Item item);

    List<Item> all_items();

    boolean update_quantity(int itemId, int quantity);

    void updateSellQuantity(int itemId, int quantity);

    Item load_item(int itemId);

    void store_orders(Order order);

    void store_orderDetails(int orderId, Integer item_id, Integer quantity);

    List<Order> all_orders(int userId);

    Order load_order(int user_id, int orderId);

    HashMap<Integer, Integer> load_orderDetails(int orderId);

    void remove_order(int orderId);

    boolean isCouponAcceptable(int userId, float coupon);

    List<Item> topSellByQuantity(int limit);

}
