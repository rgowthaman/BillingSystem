package com.billsystem;

import java.util.List;

public class Item {
    private final int itemId;
    private final String itemName;
    private final String itemCategory;
    private final float pricePerUnit;
    private final int availableQuantity;
    private final String discount;
    private int sellQuantity;

    public Item(int itemId, String itemName, String itemCategory, float pricePerUnit, int availableQuantity,
                String discount, int sellQuantity) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.pricePerUnit = pricePerUnit;
        this.availableQuantity = availableQuantity;
        this.discount = discount;
        this.setSellQuantity(sellQuantity);
    }

    public static void viewAll(Data data) {
        System.out.println("Id\tName\tCategory\tPrice\t\tQuantity");
        List<Item> itemList = data.all_items();
        for (Item item : itemList) {
            System.out.println(item.getItemId() + "\t" + item.getItemName() + "\t" + item.getItemCategory() + "\t"
                    + item.getPricePerUnit() + "\t" + item.getAvailableQuantity());
        }
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public float getPricePerUnit() {
        return pricePerUnit;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public String getDiscount() {
        return discount;
    }

    public int getSellQuantity() {
        return sellQuantity;
    }

    public void setSellQuantity(int sellQuantity) {
        this.sellQuantity = sellQuantity;
    }
}
