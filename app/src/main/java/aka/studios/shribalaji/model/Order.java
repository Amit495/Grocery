package aka.studios.shribalaji.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Order {
    public int id;
    public String orderId;
    public int user_id;
    public String mobile;
    public String name;
    public String address;
    public String city;
    public String state;
    public String pincode;
    public String landmark;
    public int type;
    public int items;
    public int shippingCharges;
    public String orderStatus;
    public String paymentMethod;
    public int grandTotal;
    public String created_at;
    private JSONObject object;

    public Order(JSONObject jsonObject) throws JSONException {
        this.object = jsonObject;
        this.id = jsonObject.getInt("id");
        this.orderId = jsonObject.getString("orderId");
        this.user_id = jsonObject.getInt("user_id");
        this.mobile = jsonObject.getString("mobile");
        this.name = jsonObject.getString("name");
        this.address = jsonObject.getString("address");
        this.city = jsonObject.getString("city");
        this.state = jsonObject.getString("state");
        this.pincode = jsonObject.getString("pincode");
        this.landmark = jsonObject.getString("landmark");
        this.type = jsonObject.getInt("type");
        this.items = jsonObject.getInt("items");
        this.shippingCharges = jsonObject.getInt("shippingCharges");
        this.orderStatus = jsonObject.getString("orderStatus");
        this.paymentMethod = jsonObject.getString("paymentMethod");
        this.grandTotal = jsonObject.getInt("grandTotal");
        this.created_at = jsonObject.getString("created_at");
    }

    public int getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }

    public String getLandmark() {
        return landmark;
    }

    public int getType() {
        return type;
    }

    public int getItems() {
        return items;
    }

    public int getShippingCharges() {
        return shippingCharges;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public int getGrandTotal() {
        return grandTotal;
    }

    public String getCreated_at() {
        return created_at;
    }
}
