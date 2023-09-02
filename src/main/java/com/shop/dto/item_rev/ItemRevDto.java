package com.shop.dto.item_rev;


import com.shop.entity.item.Item;
import com.shop.entity.item_rev.ItemRev;
import com.shop.entity.member.Member;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
@ToString
public class ItemRevDto {

    private Long id;
    @NotNull(message = "제목을 작성하세요.")
    private String title;
    @NotNull(message = "내용을 작성하세요.")
    private String content;
    @Min(value = 1, message = "평점은 1보다 작을 수 없습니다.")
    @Max(value = 5, message = "평점은 5보다 클 수 없습니다.")
    private int rate;
    private Long itemId;
    private Long memberId;



    public ItemRev toEntity(ItemRevDto itemRevDto, Member member, Item item) {
        return ItemRev.builder()
                .title(itemRevDto.getTitle())
                .content(itemRevDto.getContent())
                .rate(itemRevDto.getRate())
                .member(member)
                .item(item)
                .build();
    }




}
