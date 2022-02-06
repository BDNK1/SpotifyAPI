package objects;

public class Category extends MusicObj{


    private String id;

    public String getId() {
        return id;
    }
    public Category(String name, String id) {
        super(name);
        this.id = id;
    }

    @Override
    public String toString() {
        return super.getName();
    }
}
