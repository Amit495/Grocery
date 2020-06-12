package aka.studios.shribalaji.model;

public class Category {
    public int id;
    public int parent_id;
    public String name;
    public String description;
    public String url;
    public String image;
    public int status;

    public Category(int id, int parent_id, String name, String description, String url, String image, int status) {
        this.id = id;
        this.parent_id = parent_id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.image = image;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

    public int getStatus() {
        return status;
    }
}
