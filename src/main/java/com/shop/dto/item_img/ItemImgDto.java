package com.shop.dto.item_img;

import com.shop.entity.item_img.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class ItemImgDto {

    private Long id;

    private String oriImgName;

    private String imgName;

    private String imgUrl;

    private String repImgYn;

    public static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg){
        return modelMapper.map(itemImg, ItemImgDto.class);
    }

}
