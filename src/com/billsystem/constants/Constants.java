package com.billsystem.constants;

import java.util.Random;

public class Constants extends DBConstants {
    public static final String GREET = "Welcome";
    public static final String ADMIN_ROLE = "Admin";
    public static final String CUSTOMER_ROLE = "Customer";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String YES_SHORT = "y";
    public static final String DISCOUNT_PROMO = "PROMO";
    public static final String EMPTY_STRING = "";
    public static final String NEW_INDEX_VALUE = "0";
    public static final String NO_ORDERS_PLACED = "0";
    public static final String DEFAULT_LIMIT = "3";
    public static final String GENERATE_ADMIN_ID = String.valueOf(new Random(System.currentTimeMillis()).nextInt(2000) + 10000);
    public static final String GENERATE_CUSTOMER_ID = String.valueOf(new Random(System.currentTimeMillis()).nextInt(2000) + 10000);
    public static final String GENERATE_ITEM_ID = String.valueOf(new Random(System.currentTimeMillis()).nextInt(2000) + 10000);
    public static final String GENERATE_ORDER_ID = String.valueOf(new Random(System.currentTimeMillis()).nextInt(2000) + 10000);
}
