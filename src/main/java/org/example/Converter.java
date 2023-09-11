package org.example;

import java.util.*;

public class Converter {

    /**
     * Метод работает за O(2n) для любого порядка элементов в коллекции.
     */
    public static Collection<TreeDTO> convert(Collection<TreeEntity> entities) {
        List<TreeDTO> result = new ArrayList<>();
        Map<Integer, TreeDTO> hash = new HashMap<>();

        for (TreeEntity entity : entities) {
            TreeDTO dto = convertEntityToDTO(entity);
            hash.put(entity.getId(), dto);
            if (entity.getParentId() == null) result.add(dto);
        }

        for (TreeEntity entity : entities) {
            if (entity.getParentId() == null) continue;
            hash.get(entity.getParentId()).addChild(hash.get(entity.getId()));
        }

        return result;
    }

    /**
     * Метод работает за O(n). Но работает только при условии того,
     * что в переданной коллекции каждый элемент располагается после своего родителя.
     */
    public static Collection<TreeDTO> convertOrderedNodes(Collection<TreeEntity> entities) {
        List<TreeDTO> result = new ArrayList<>();
        Map<Integer, TreeDTO> hash = new HashMap<>();

        for (TreeEntity entity : entities) {
            TreeDTO dto = convertEntityToDTO(entity);
            hash.put(entity.getId(), dto);
            if (entity.getParentId() == null) {
                result.add(dto);
            } else {
                hash.get(entity.getParentId()).addChild(hash.get(entity.getId()));
            }
        }

        return result;
    }

    public static TreeDTO convertEntityToDTO(TreeEntity entity) {
        return new TreeDTO(entity.getId(), entity.getName());
    }

}
