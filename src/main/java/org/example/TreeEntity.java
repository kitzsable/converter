package org.example;

public class TreeEntity {
    private Integer id;
    private String name;
    private Integer parentId;

    public TreeEntity(Integer id, String name, Integer parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getParentId() {
        return parentId;
    }
}
