package aka.studios.shribalaji.model;

public class FeaturedProduct {
    public int id;
    public int category_id;
    public String product_name;
    public String product_semi_desc;
    public String short_qty;
    public double product_rating;
    public String description;
    public String image;
    public int status;
    public int product_id;
    public String sku;
    public String size;
    public int price;
    public int stock;

    public FeaturedProduct(int id, int category_id, String product_name, String product_semi_desc, String short_qty, double product_rating, String description, String image, int status, int product_id, String sku, String size, int price, int stock) {
        this.id = id;
        this.category_id = category_id;
        this.product_name = product_name;
        this.product_semi_desc = product_semi_desc;
        this.short_qty = short_qty;
        this.product_rating = product_rating;
        this.description = description;
        this.image = image;
        this.status = status;
        this.product_id = product_id;
        this.sku = sku;
        this.size = size;
        this.price = price;
        this.stock = stock;
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

    public String getProduct_semi_desc() {
        return product_semi_desc;
    }

    public String getShort_qty() {
        return short_qty;
    }

    public double getProduct_rating() {
        return product_rating;
    }

    public String getDescription() {
        return description;
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

    public int getStock() {
        return stock;
    }
}
