package aka.studios.shribalaji.adapter;

public class ProductImages {
    public int id;
    public String product_id;
    public String image;

    public ProductImages(int id, String product_id, String image) {
        this.id = id;
        this.product_id = product_id;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getImage() {
        return image;
    }
}
