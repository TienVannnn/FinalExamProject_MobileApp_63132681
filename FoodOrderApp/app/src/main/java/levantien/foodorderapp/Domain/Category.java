package levantien.foodorderapp.Domain;

public class Category {
    private int id;
    private String imagePath;
    private String name;

    public Category(int id, String imagePath, String name) {
        this.id = id;
        this.imagePath = imagePath;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
