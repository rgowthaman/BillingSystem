package com.billsystem;

import com.billsystem.Customer.Order;
import com.billsystem.constants.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DB implements Data {

    public Connection connect() throws SQLException {
        try {
            Class.forName(Constants.DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(Constants.PSQL_URL + Constants.DATABASE_NAME, Constants.DB_USER, Constants.DB_PASSWORD);
    }

    @Override
    public void store_user(User user) {
        String SQL = "INSERT INTO users(user_id, user_name, password, role) " + "VALUES(?,?,?,?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEncryptedPassword());
            pstmt.setString(4, user.getRole());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User load_user(int user_id) {
        User user = null;
        String SQL = "SELECT * FROM users WHERE user_id=?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("role").equals(Constants.ADMIN_ROLE))
                    user = new Admin(rs.getInt("user_id"), rs.getString("user_name"), rs.getString("password"),
                            rs.getString("role"));
                else
                    user = new Customer(rs.getInt("user_id"), rs.getString("user_name"), rs.getString("password"),
                            rs.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> all_users() {
        List<User> userList = new ArrayList<>();
        String SQL = "SELECT * FROM users";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                if (rs.getString("role").equals(Constants.CUSTOMER_ROLE))
                    userList.add(new Customer(rs.getInt("user_id"), rs.getString("user_name"), rs.getString("password"),
                            rs.getString("role")));
                else
                    userList.add(new Admin(rs.getInt("user_id"), rs.getString("user_name"), rs.getString("password"),
                            rs.getString("role")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }

    @Override
    public boolean remove_user(int user_id) {
        String SQL = "DELETE FROM users WHERE user_id=?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, user_id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public void store_item(Item item) {
        String SQL = "INSERT INTO item(item_id, item_name, category, price, available_quantity, discount, sell_quantity) VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, item.getItemId());
            pstmt.setString(2, item.getItemName());
            pstmt.setString(3, item.getItemCategory());
            pstmt.setFloat(4, item.getPricePerUnit());
            pstmt.setInt(5, item.getAvailableQuantity());
            pstmt.setString(6, item.getDiscount());
            pstmt.setInt(7, item.getSellQuantity());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Item load_item(int item_id) {
        Item item = null;
        String SQL = "SELECT * FROM item WHERE item_id=?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, item_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                item = new Item(rs.getInt("item_id"), rs.getString("item_name"), rs.getString("category"),
                        rs.getFloat("price"), rs.getInt("available_quantity"), rs.getString("discount"),
                        rs.getInt("sell_quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public boolean update_quantity(int item_id, int quantity) {
        String SQL = "UPDATE item SET available_quantity=?" + " WHERE item_id=?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, item_id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public void updateSellQuantity(int itemId, int quantity) {
        String SQL = "UPDATE item SET sell_quantity=?" + " WHERE item_id=?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, itemId);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<Item> all_items() {
        List<Item> itemList = new ArrayList<>();
        String SQL = "SELECT * FROM item";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                itemList.add(new Item(rs.getInt("item_id"), rs.getString("item_name"), rs.getString("category"),
                        rs.getFloat("price"), rs.getInt("available_quantity"), rs.getString("discount"),
                        rs.getInt("sell_quantity")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemList;
    }

    @Override
    public void store_orders(Order order) {
        String SQL = "INSERT INTO orders(order_id, user_id, discount, total_price) VALUES (?,?,?,?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, order.orderId);
            pstmt.setInt(2, order.userId);
            pstmt.setFloat(3, order.discount);
            pstmt.setFloat(4, order.totalPrice);
            pstmt.executeUpdate();
            for (Integer item_id : order.item_details.keySet()) {
                store_orderDetails(order.orderId, item_id, order.item_details.get(item_id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void store_orderDetails(int orderId, Integer item_id, Integer quantity) {
        String SQL = "INSERT INTO order_details(order_id, item_id, quantity) VALUES (?,?,?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, item_id);
            pstmt.setFloat(3, quantity);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> all_orders(int userId) {
        List<Order> orderList = new ArrayList<>();
        String SQL = "SELECT * FROM orders WHERE user_id=?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = new Order(rs.getInt("order_id"), rs.getInt("user_id"), rs.getFloat("total_price"),
                        rs.getFloat("discount"), load_orderDetails(rs.getInt("order_id")));
                orderList.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderList;
    }

    @Override
    public Order load_order(int userId, int order_id) {
        Order order = null;
        String SQL = "SELECT * FROM orders WHERE order_id=? AND user_id=?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, order_id);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                order = new Order(rs.getInt("order_id"), rs.getInt("user_id"), rs.getFloat("total_price"),
                        rs.getFloat("discount"), load_orderDetails(rs.getInt("order_id")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return order;
    }

    @Override
    public HashMap<Integer, Integer> load_orderDetails(int orderId) {
        HashMap<Integer, Integer> itemMap = new HashMap<>();
        String SQL = "SELECT item_id, quantity FROM order_details WHERE order_id=?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                itemMap.put(rs.getInt("item_id"), rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemMap;
    }

    @Override
    public void remove_order(int order_id) {
        String SQL = "DELETE FROM orders WHERE order_id=?";

        remove_orderDetails(order_id);
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, order_id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void remove_orderDetails(int order_id) {
        String SQL = "DELETE FROM order_details WHERE order_id=?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, order_id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean isCouponAcceptable(int userId, float coupon) {
        String SQL = "SELECT * FROM orders WHERE user_id=?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                if (rs.getFloat("discount") == coupon)
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public List<Item> topSellByQuantity(int limit) {
        List<Item> itemList = new ArrayList<>();
        String SQL = "SELECT * FROM item ORDER BY sell_quantity DESC LIMIT ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                itemList.add(new Item(rs.getInt("item_id"), rs.getString("item_name"), rs.getString("category"),
                        rs.getFloat("price"), rs.getInt("available_quantity"), rs.getString("discount"),
                        rs.getInt("sell_quantity")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }

}
