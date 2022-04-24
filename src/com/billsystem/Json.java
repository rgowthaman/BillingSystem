package com.billsystem;

import com.billsystem.Customer.Order;
import com.billsystem.constants.Constants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Json implements Data {
    static int new_order_item_index = Integer.parseInt(Constants.NEW_INDEX_VALUE);
    JSONArray orderList;

    /**
     * To write a json file
     *
     * @param userObject
     * @param jsonFile
     */
    void write(JSONObject userObject, String jsonFile) {
        try {
            FileWriter fileWriter = new FileWriter(Constants.FILE_DIR + Constants.FILE_SEPARATOR + jsonFile);
            fileWriter.write(userObject.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * To read a json file
     *
     * @param jsonFile
     * @return JSONObject
     */
    public JSONObject read(String jsonFile) {
        Object object;
        JSONObject jsonObject = null;
        try {
            object = new JSONParser().parse(new FileReader(Constants.FILE_DIR + Constants.FILE_SEPARATOR + jsonFile));
            jsonObject = (JSONObject) object;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void store_user(User user) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", user.getId());
        jsonObject.put("name", user.getName());
        jsonObject.put("password", user.getEncryptedPassword());
        jsonObject.put("role", user.getRole());
        JSONObject userObject = read(Constants.USER_JSON_FILE);
        userObject.put(Integer.toString(user.getId()), jsonObject);
        write(userObject, Constants.USER_JSON_FILE);
    }

    @Override
    public User load_user(int userId) {
        JSONObject userObject = (JSONObject) read(Constants.USER_JSON_FILE).get(Integer.toString(userId));
        if (userObject.get("role").equals(Constants.CUSTOMER_ROLE)) {
            return new Customer(userId, userObject.get("name").toString(), userObject.get("password").toString(),
                    userObject.get("role").toString());
        } else {
            return new Admin(userId, userObject.get("name").toString(), userObject.get("password").toString(),
                    userObject.get("role").toString());
        }
    }

    @Override
    public List<User> all_users() {
        List<User> userList = new ArrayList<>();
        JSONObject jsonObject = read(Constants.USER_JSON_FILE);
        @SuppressWarnings("unchecked")
        Set<String> keys = jsonObject.keySet();
        for (Object key : keys) {
            JSONObject userObject = (JSONObject) jsonObject.get(key);
            userList.add(
                    new Customer(Integer.parseInt(userObject.get("id").toString()), userObject.get("name").toString(),
                            userObject.get("password").toString(), userObject.get("role").toString()));
        }
        return userList;
    }

    @Override
    public boolean remove_user(int userId) {
        JSONObject userObject = read(Constants.USER_JSON_FILE);
        try {
            userObject.remove(Integer.toString(userId));
            write(userObject, Constants.USER_JSON_FILE);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void store_item(Item item) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("itemId", item.getItemId());
        jsonObject.put("itemName", item.getItemName());
        jsonObject.put("itemCategory", item.getItemCategory());
        jsonObject.put("pricePerUnit", item.getPricePerUnit());
        jsonObject.put("availableQuantity", item.getAvailableQuantity());
        jsonObject.put("discount", item.getDiscount());
        jsonObject.put("sellQuantity", item.getSellQuantity());
        JSONObject itemObject = read(Constants.ITEM_JSON_FILE);
        itemObject.put(Integer.toString(item.getItemId()), jsonObject);
        write(itemObject, Constants.ITEM_JSON_FILE);
    }

    @Override
    public Item load_item(int itemID) {
        JSONObject itemObject = (JSONObject) read(Constants.ITEM_JSON_FILE).get(Integer.toString(itemID));
        return new Item(Integer.parseInt(itemObject.get("itemId").toString()), itemObject.get("itemName").toString(),
                itemObject.get("itemCategory").toString(), Float.parseFloat(itemObject.get("pricePerUnit").toString()),
                Integer.parseInt(itemObject.get("availableQuantity").toString()), itemObject.get("discount").toString(),
                Integer.parseInt(itemObject.get("sellQuantity").toString()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean update_quantity(int itemId, int quantity) {
        JSONObject jsonObject = read(Constants.ITEM_JSON_FILE);
        try {
            JSONObject itemObject = (JSONObject) jsonObject.get(Integer.toString(itemId));
            itemObject.put("availableQuantity", quantity);
            write(jsonObject, Constants.ITEM_JSON_FILE);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void updateSellQuantity(int itemId, int quantity) {
        JSONObject jsonObject = read(Constants.ITEM_JSON_FILE);
        try {
            JSONObject itemObject = (JSONObject) jsonObject.get(Integer.toString(itemId));
            itemObject.put("sellQuantity", quantity);
            write(jsonObject, Constants.ITEM_JSON_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Item> all_items() {
        List<Item> itemList = new ArrayList<>();
        JSONObject jsonObject = read(Constants.ITEM_JSON_FILE);
        @SuppressWarnings("unchecked")
        Set<String> keys = jsonObject.keySet();
        for (Object key : keys) {
            JSONObject itemObject = (JSONObject) jsonObject.get(key);
            itemList.add(new Item(Integer.parseInt(itemObject.get("itemId").toString()),
                    itemObject.get("itemName").toString(), itemObject.get("itemCategory").toString(),
                    Float.parseFloat(itemObject.get("pricePerUnit").toString()),
                    Integer.parseInt(itemObject.get("availableQuantity").toString()),
                    itemObject.get("discount").toString(),
                    Integer.parseInt(itemObject.get("sellQuantity").toString())));
        }
        return itemList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void store_orders(Order order) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("order_id", order.orderId);
        jsonObject.put("user_id", order.userId);
        jsonObject.put("discount", order.discount);
        jsonObject.put("total_price", order.totalPrice);
        for (Integer item_id : order.item_details.keySet()) {
            store_orderDetails(order.orderId, item_id, order.item_details.get(item_id));
        }
        new_order_item_index = 0;
        JSONObject ordersJsonObject = read(Constants.ORDER_JSON_FILE);
        ordersJsonObject.put(Integer.toString(order.orderId), jsonObject);
        write(ordersJsonObject, Constants.ORDER_JSON_FILE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void store_orderDetails(int orderId, Integer item_id, Integer quantity) {
        JSONObject jsonObject = read(Constants.ORDER_DETAILS_JSON_FILE);
        JSONObject itemJsonObject = new JSONObject();
        if (new_order_item_index == 0)
            orderList = new JSONArray();
        else
            orderList = (JSONArray) jsonObject.get(Integer.toString(orderId));
        itemJsonObject.put("item_id", item_id);
        itemJsonObject.put("quantity", quantity);
        orderList.add(itemJsonObject);
        jsonObject.put(Integer.toString(orderId), orderList);
        new_order_item_index++;
        write(jsonObject, Constants.ORDER_DETAILS_JSON_FILE);
    }

    @Override
    public List<Order> all_orders(int userId) {
        List<Order> orderList = new ArrayList<>();
        JSONObject jsonObject = read(Constants.ORDER_JSON_FILE);
        @SuppressWarnings("unchecked")
        Set<String> keys = jsonObject.keySet();
        for (Object key : keys) {
            JSONObject orderObject = (JSONObject) jsonObject.get(key);
            int user_id = Integer.parseInt(orderObject.get("user_id").toString());
            if (user_id == userId)
                orderList.add(new Order(Integer.parseInt(orderObject.get("order_id").toString()), user_id,
                        Float.parseFloat(orderObject.get("total_price").toString()),
                        Float.parseFloat(orderObject.get("discount").toString()),
                        load_orderDetails(Integer.parseInt(orderObject.get("order_id").toString()))));
        }
        return orderList;
    }

    @Override
    public Order load_order(int user_id, int orderId) {
        JSONObject orderObject = (JSONObject) read(Constants.ORDER_JSON_FILE).get(Integer.toString(orderId));
        return new Order(Integer.parseInt(orderObject.get("order_id").toString()),
                Integer.parseInt(orderObject.get("user_id").toString()),
                Float.parseFloat(orderObject.get("total_price").toString()),
                Float.parseFloat(orderObject.get("discount").toString()),
                load_orderDetails(Integer.parseInt(orderObject.get("order_id").toString())));
    }

    @SuppressWarnings("unchecked")
    @Override
    public HashMap<Integer, Integer> load_orderDetails(int orderId) {
        HashMap<Integer, Integer> itemListHashMap = new HashMap<>();
        JSONArray ordArray = (JSONArray) read(Constants.ORDER_DETAILS_JSON_FILE).get(Integer.toString(orderId));

        for (JSONObject orderObject : (Iterable<JSONObject>) ordArray)
            itemListHashMap.put(Integer.parseInt(orderObject.get("item_id").toString()),
                    Integer.parseInt(orderObject.get("quantity").toString()));
        return itemListHashMap;
    }

    @Override
    public void remove_order(int orderId) {
        JSONObject userObject = read(Constants.ORDER_JSON_FILE);
        remove_orderDetails(orderId);
        try {
            userObject.remove(Integer.toString(orderId));
            write(userObject, Constants.ORDER_JSON_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To remove order details
     *
     * @param orderId
     */
    public void remove_orderDetails(int orderId) {
        JSONObject userObject = read(Constants.ORDER_DETAILS_JSON_FILE);
        try {
            userObject.remove(Integer.toString(orderId));
            write(userObject, Constants.ORDER_DETAILS_JSON_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isCouponAcceptable(int userId, float coupon) {
        JSONObject jsonObject = read(Constants.ORDER_JSON_FILE);
        @SuppressWarnings("unchecked")
        Set<String> keys = jsonObject.keySet();
        for (Object key : keys) {
            JSONObject orderObject = (JSONObject) jsonObject.get(key);
            float used_discounts = Float.parseFloat(orderObject.get("discount").toString());
            int user_id = Integer.parseInt(orderObject.get("user_id").toString());
            if (user_id == userId && used_discounts == coupon)
                return false;
        }
        return true;
    }

    @Override
    public List<Item> topSellByQuantity(int limit) {
        List<Item> itemList = new ArrayList<>();
        JSONObject jsonObject = read(Constants.ITEM_JSON_FILE);
        @SuppressWarnings("unchecked")
        Set<String> keys = jsonObject.keySet();
        for (Object key : keys) {
            JSONObject itemObject = (JSONObject) jsonObject.get(key);
            itemList.add(new Item(Integer.parseInt(itemObject.get("itemId").toString()),
                    itemObject.get("itemName").toString(), itemObject.get("itemCategory").toString(),
                    Float.parseFloat(itemObject.get("pricePerUnit").toString()),
                    Integer.parseInt(itemObject.get("availableQuantity").toString()),
                    itemObject.get("discount").toString(),
                    Integer.parseInt(itemObject.get("sellQuantity").toString())));
        }
        itemList.sort((i1, i2) -> i2.getSellQuantity() - i1.getSellQuantity());
        return String.valueOf(itemList.get(0).getSellQuantity()).equals(Constants.NO_ORDERS_PLACED) ? new ArrayList<>() : itemList.stream().limit(limit).collect(Collectors.toList());
    }

}
