package com.shop.dto.item;

import com.shop.constant.category.Category;
import com.shop.constant.item.ItemSellStatus;
import com.shop.dto.item_img.ItemImgDto;
import com.shop.entity.item.Item;
import lombok.*;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    @NotNull(message = "카테고리 선택은 필수입니다.")
    private String code;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item toEntity(ItemFormDto itemFormDto) {
        return Item.builder()
                .itemNm(itemFormDto.getItemNm())
                .price(itemFormDto.getPrice())
                .stockNumber(itemFormDto.getStockNumber())
                .itemDetail(itemFormDto.getItemDetail())
                .itemSellStatus(itemFormDto.getItemSellStatus())
                .category(Category.ofCode(itemFormDto.getCode()))
                .build();
    }

    public static ItemFormDto of(Item item) {
        return ItemFormDto.builder()
                .id(item.getId())
                .itemNm(item.getItemNm())
                .price(item.getPrice())
                .itemDetail(item.getItemDetail())
                .stockNumber(item.getStockNumber())
                .itemSellStatus(item.getItemSellStatus())
                .code(item.getCategory().getCode())
                .build();

//        return modelMapper.map(item, ItemFormDto.class);
    }

}
