package org.example;

import java.util.ArrayList;
import java.util.List;

public class TreeDTO {
    private Integer id;
    private String name;
    private List<TreeDTO> children;

    public TreeDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
        children = new ArrayList<>();
    }

    public void addChild(TreeDTO child) {
        children.add(child);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<TreeDTO> getChildren() {
        return children;
    }
}
