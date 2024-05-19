package levantien.foodorderapp.Domain;

import java.util.ArrayList;

public class Order {
    private String name;
    private String phoneNumber;
    private String address;
    private ArrayList<Foods> items;
    private long orderTime;

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public Order() {
    }

    public Order(String name, String phoneNumber, String address, ArrayList<Foods> items) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Foods> getItems() {
        return items;
    }

    public void setItems(ArrayList<Foods> items) {
        this.items = items;
    }
}
