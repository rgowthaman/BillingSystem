package com.billsystem.constants;

// import java.io.File;

public class FileConstants {
    public static final String FILE_SEPARATOR = "/";
    public static final String FILE_DIR = System.getProperty("user.dir") + FILE_SEPARATOR + "Json";
    //    public static final String FILE_DIR = new File(FileConstants.class.getResource("/").getPath(), "Json").toString();
    public static final String USER_JSON_FILE = "user.json";
    public static final String ITEM_JSON_FILE = "item.json";
    public static final String ORDER_JSON_FILE = "order.json";
    public static final String ORDER_DETAILS_JSON_FILE = "order_details.json";
    public static final String STORE_AS_JSON = "json";
}
