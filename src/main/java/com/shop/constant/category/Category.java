package com.shop.constant.category;

import lombok.Data;
import lombok.Getter;

import java.util.*;


@Getter
public enum Category {
    Root("C","root",null),
        MEN("C1","남성", Root),
            MEN_TOP("C11","남성_상의", MEN),
                MEN_COAT("C111","남성_코트", MEN_TOP),
                MEN_JACKET("C112","남성_자켓", MEN_TOP),
            MEN_BOTTOM("C12","남성_하의", MEN),
                MEN_SLACKS("C121","남성_슬랙스", MEN_BOTTOM),
        WOMEN("C2","여성", Root),
            WOMEN_TOP("C21","여성_상의", WOMEN),
                WOMEN_COAT("C211","여성_코트", WOMEN_TOP),
                WOMEN_JACKET("C212","여성_자켓", WOMEN_TOP),
            WOMEN_BOTTOM("C22","여성_하의", WOMEN),
                WOMEN_SLACKS("C221","여성_슬랙스", WOMEN_BOTTOM);


    private final String code;
    private final String name;
    private final Optional<Category> parent;
    private final List<Category> children = new ArrayList<>();

    Category(String code, String name, Category parent) {
        this.code = code;
        this.name = name;
        this.parent = Optional.ofNullable(parent);
        this.parent.ifPresent(
                category -> category.children.add(this)
        );
    }

    public List<Category> getChildren(){
        return Collections.unmodifiableList(children);
    }

    public Boolean isLastCategory(){
        return children.isEmpty();
    }

    public static Category ofCode(String code){
        return Arrays.stream(Category.values())
                .filter(category -> category.getCode().equals(code))
                .findAny()
                .get();
    }

    @Data
    public static class CategoryResponse{
        private String key;
        private String code;

        private Optional<Category> parent;

        private List<Category> children;

        public CategoryResponse(String key, Category category){
            this.key = key;
            this.code = category.getCode();
            this.parent = category.getParent();
            this.children = category.getChildren();
        }
    }

}
