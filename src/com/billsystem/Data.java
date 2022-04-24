package com.billsystem;

import com.billsystem.Customer.Order;

import java.util.HashMap;
import java.util.List;

public interface Data {

    /**
     * To store new user
     *
     * @param user
     */
    void store_user(User user);

    /**
     * To retrieve a user details
     *
     * @param user_id
     * @return
     */
    User load_user(int user_id);

    /**
     * To retrieve all the user details
     *
     * @return
     */
    List<User> all_users();

    /**
     * To remove a user
     *
     * @param user_id
     * @return
     */
    boolean remove_user(int user_id);

    /**
     * To store new item
     *
     * @param item
     */
    void store_item(Item item);

    /**
     * To retrieve all the items
     *
     * @return
     */
    List<Item> all_items();

    /**
     * To update available quantity of an item
     *
     * @param itemId
     * @param quantity
     * @return
     */
    boolean update_quantity(int itemId, int quantity);

    /**
     * To update sell quantity of an item
     *
     * @param itemId
     * @param quantity
     */
    void updateSellQuantity(int itemId, int quantity);

    /**
     * To retrieve an item
     *
     * @param itemId
     * @return
     */
    Item load_item(int itemId);

    /**
     * To store a new order
     *
     * @param order
     */
    void store_orders(Order order);

    /**
     * To store order details
     *
     * @param orderId
     * @param item_id
     * @param quantity
     */
    void store_orderDetails(int orderId, Integer item_id, Integer quantity);

    /**
     * To retrieve all orders for a user
     *
     * @param userId
     * @return
     */
    List<Order> all_orders(int userId);

    /**
     * To retrieve an order
     *
     * @param user_id
     * @param orderId
     * @return
     */
    Order load_order(int user_id, int orderId);

    /**
     * To retrieve an order detail
     *
     * @param orderId
     * @return
     */
    HashMap<Integer, Integer> load_orderDetails(int orderId);

    /**
     * To remove an order
     *
     * @param orderId
     */
    void remove_order(int orderId);

    /**
     * To check whether coupon is valid for a user
     *
     * @param userId
     * @param coupon
     * @return
     */
    boolean isCouponAcceptable(int userId, float coupon);

    /**
     * To retrieve a top-selling items
     *
     * @param limit
     * @return
     */
    List<Item> topSellByQuantity(int limit);

}
