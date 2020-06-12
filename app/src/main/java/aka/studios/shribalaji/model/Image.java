package aka.studios.shribalaji.model;

public class Image {
    public int id;
    public int product_id;
    public String image;

    public Image(int id, int product_id, String image) {
        this.id = id;
        this.product_id = product_id;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getImage() {
        return image;
    }
}
