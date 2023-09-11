package com.shop.service.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shop.constant.category.Category;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    private ObjectNode categoryNode;

    public ObjectNode getCategoryNode(Category category){
        if (categoryNode == null)
            categoryNode = categoryToJson(category);

        return categoryNode;
    }

    private ObjectNode categoryToJson(Category category) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode categoryNode = objectMapper.createObjectNode();
        {}

        categoryNode.put("code", category.getCode());
        categoryNode.put("name", category.getName());

        List<Category> children = category.getChildren();
        if (!children.isEmpty()) {
            ArrayNode childrenArray = objectMapper.createArrayNode();
            for (Category child : children) {
                childrenArray.add(categoryToJson(child)); // 재귀 호출
            }
            categoryNode.set("children", childrenArray);
        }

        return categoryNode;
    }


}
