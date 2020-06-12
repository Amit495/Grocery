package aka.studios.shribalaji.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Product {
    private int id;
    private int category_id;
    private String product_name;
    private String image;
    private int status;
    private int product_id;
    private String sku;
    private String size;
    private int price;
    private int oldPrice;
    private int stock;
    private JSONObject object;

    public Product(JSONObject jsonObject) throws JSONException {
        this.object = jsonObject;
        this.id = jsonObject.getInt("id");
        this.category_id = jsonObject.getInt("category_id");
        this.product_name = jsonObject.getString("product_name");
        this.image = jsonObject.getString("image");
        this.status = jsonObject.getInt("status");
        this.product_id = jsonObject.getInt("product_id");
        this.sku = jsonObject.getString("sku");
        this.size = jsonObject.getString("size");
        this.price = jsonObject.getInt("price");
        this.oldPrice = jsonObject.getInt("oldPrice");
        this.stock = jsonObject.getInt("stock");
    }

    public int getId() {
        return id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getImage() {
        return image;
    }

    public int getStatus() {
        return status;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getSku() {
        return sku;
    }

    public String getSize() {
        return size;
    }

    public int getPrice() {
        return price;
    }

    public int getOldPrice() {
        return oldPrice;
    }

    public int getStock() {
        return stock;
    }
}
