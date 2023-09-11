package com.shop.config;

import com.shop.constant.category.Category;

import javax.persistence.AttributeConverter;

public class CategoryToCodeConverter implements AttributeConverter<Category, String> {

    @Override
    public String convertToDatabaseColumn(Category category) {
        return category.getCode();
    }

    @Override
    public Category convertToEntityAttribute(String code) {
        return Category.ofCode(code);
    }
}
