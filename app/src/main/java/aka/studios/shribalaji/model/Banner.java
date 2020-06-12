package aka.studios.shribalaji.model;

public class Banner {
    public int id;
    public String title;
    public String link;
    public String image;
    public String status;

    public Banner(int id, String title, String link, String image, String status) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.image = image;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }
}
