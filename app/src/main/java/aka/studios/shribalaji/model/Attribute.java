package aka.studios.shribalaji.model;

public class Attribute {
    public int id;
    public int product_id;
    public String sku;
    public String size;
    public int price;
    public int oldPrice;
    public int stock;

    public Attribute(int id, int product_id, String sku, String size, int price, int oldPrice, int stock) {
        this.id = id;
        this.product_id = product_id;
        this.sku = sku;
        this.size = size;
        this.price = price;
        this.oldPrice = oldPrice;
        this.stock = stock;
    }

    public int getId() {
        return id;
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
