package com.billsystem.constants;

import java.util.Random;

public class Constants extends DBConstants {
    public static String GREET = "Welcome";
    public static String ADMIN_ROLE = "Admin";
    public static String CUSTOMER_ROLE = "Customer";
    public static String TRUE = "true";
    public static String FALSE = "false";
    public static String YES_SHORT = "y";
    public static String DISCOUNT_PROMO = "PROMO";
    public static String EMPTY_STRING = "";
    public static String NEW_INDEX_VALUE = "0";
    public static String DEFAULT_LIMIT = "3";
    public static String GENERATE_ADMIN_ID = String.valueOf(new Random(System.currentTimeMillis()).nextInt(2000) + 10000);
    public static String GENERATE_CUSTOMER_ID = String.valueOf(new Random(System.currentTimeMillis()).nextInt(2000) + 10000);
    public static String GENERATE_ITEM_ID = String.valueOf(new Random(System.currentTimeMillis()).nextInt(2000) + 10000);
    public static String GENERATE_ORDER_ID = String.valueOf(new Random(System.currentTimeMillis()).nextInt(2000) + 10000);
}
