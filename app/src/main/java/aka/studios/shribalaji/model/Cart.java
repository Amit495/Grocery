package aka.studios.shribalaji.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Cart {
    public int id;
    public int product_id;
    public String product_name;
    public String product_code;
    public String size;
    public int price;
    public int quantity;
    public String mobile;
    private JSONObject object;

    public Cart(JSONObject jsonObject) throws JSONException {
        this.object = jsonObject;
        this.id = jsonObject.getInt("id");
        this.product_id = jsonObject.getInt("product_id");
        this.product_name = jsonObject.getString("product_name");
        this.product_code = jsonObject.getString("product_code");
        this.size = jsonObject.getString("size");
        this.price = jsonObject.getInt("price");
        this.quantity = jsonObject.getInt("quantity");
        this.mobile = jsonObject.getString("mobile");
    }

    public int getId() {
        return id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public String getSize() {
        return size;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMobile() {
        return mobile;
    }
}
