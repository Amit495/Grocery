package aka.studios.shribalaji.model;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderedItems {
    private int id;
    private int order_id;
    private int user_id;
    private int product_id;
    private String product_name;
    private String product_code;
    private String product_size;
    private int product_price;
    private int product_qty;
    private JSONObject object;

    public OrderedItems(JSONObject jsonObject) throws JSONException {
        this.object = jsonObject;
        this.id = jsonObject.getInt("id");
        this.order_id = jsonObject.getInt("order_id");
        this.user_id = jsonObject.getInt("user_id");
        this.product_id = jsonObject.getInt("product_id");
        this.product_name = jsonObject.getString("product_name");
        this.product_code = jsonObject.getString("product_code");
        this.product_size = jsonObject.getString("product_size");
        this.product_price = jsonObject.getInt("product_price");
        this.product_qty = jsonObject.getInt("product_qty");
    }

    public int getId() {
        return id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getUser_id() {
        return user_id;
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

    public String getProduct_size() {
        return product_size;
    }

    public int getProduct_price() {
        return product_price;
    }

    public int getProduct_qty() {
        return product_qty;
    }
}
