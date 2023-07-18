package net.example.spring.web;

/**
 * Item response representation.
 */
public class ItemEntity {

    private String id;

    private String label;

    private String owner;

    public ItemEntity() {
        // default, empty
    }

    public ItemEntity(String id, String label, String owner) {
        this.id = id;
        this.label = label;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getOwner() {
        return owner;
    }

}
