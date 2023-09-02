package com.shop.dto.item;

import com.shop.constant.item.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class  ItemSearchDto {

    private String searchDateType;

    private ItemSellStatus searchSellStatus;

    private String searchBy;

    private String searchQuery = "";
}
