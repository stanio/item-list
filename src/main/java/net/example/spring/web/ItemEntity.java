package net.example.spring.web;

/**
 * Item response representation.
 */
public class ItemEntity {

    private String id;

    private String label;

    public ItemEntity() {
        // default, empty
    }

    public ItemEntity(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

}
