package levantien.foodorderapp.Domain;

import java.util.ArrayList;

public class Order {
    private String name;
    private String phoneNumber;
    private String address;
    private ArrayList<Foods> items;
    private String subTotal;
    private String ToTal;
    private String delivery;

    private String tax;

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getToTal() {
        return ToTal;
    }

    public void setToTal(String toTal) {
        ToTal = toTal;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public Order() {
    }

    public Order(String name, String phoneNumber, String address, ArrayList<Foods> items, String subTotal, String toTal, String delivery, String tax) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.items = items;
        this.subTotal = subTotal;
        ToTal = toTal;
        this.delivery = delivery;
        this.tax = tax;
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
