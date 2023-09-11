package com.shop.service.itemRev;

import com.shop.constant.item.ItemSellStatus;
import com.shop.dto.item_rev.ItemRevDto;
import com.shop.dto.page.PageRequestDto;
import com.shop.dto.page.PageResultDto;
import com.shop.entity.item.Item;
import com.shop.entity.item.ItemRepository;
import com.shop.entity.item_rev.ItemRev;
import com.shop.entity.item_rev.ItemRevRepository;
import com.shop.entity.member.Member;
import com.shop.entity.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRevServiceTest {

    @Autowired
    ItemRevService itemRevService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemRevRepository itemRevRepository;


    public Item saveItem() {
        Item item = Item.builder()
                .itemNm("테스트 상품")
                .price(10000)
                .itemDetail("상품 상세 설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNumber(100)
                .build();

        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member = Member.builder()
                .email("test@test.com")
                .build();

        return memberRepository.save(member);
    }

    @Test
    @DisplayName("상품평 생성 테스트")
    public void createReview() throws Exception {
        //given
        Item item = saveItem();
        Member member = saveMember();

        ItemRevDto itemRevDto = new ItemRevDto();
        itemRevDto.setTitle("titleA");
        itemRevDto.setContent("contentA");
        itemRevDto.setRate(3);
        itemRevDto.setItemId(item.getId());
        itemRevDto.setMemberId(member.getId());

        //when
        Long itemRevId = itemRevService.createReview(itemRevDto);
        ItemRev findItemRev = itemRevRepository.findById(itemRevId)
                .orElseThrow(EntityNotFoundException::new);


        //then
        assertThat(itemRevId).isEqualTo(findItemRev.getId());

    }

    @Test
    @DisplayName("상품평 조회 테스트")
    public void getReviewList() throws Exception {
        //given
        Item item = saveItem();
        Member member = saveMember();

        IntStream.rangeClosed(1, 55).forEach(i -> {
            ItemRevDto itemRevDto = ItemRevDto.builder()
                    .title("title..." + i)
                    .content("content..." + i)
                    .rate(3)
                    .itemId(item.getId())
                    .memberId(member.getId())
                    .build();

            itemRevService.createReview(itemRevDto);
        });

        //when
        HashMap<String, Object> resultMap = itemRevService.getReviewList(item.getId(), new PageRequestDto());
        PageResultDto pageResultDto = (PageResultDto) resultMap.get("result");


        //then
        assertThat(pageResultDto.getPage()).isEqualTo(1);
        assertThat(pageResultDto.getSize()).isEqualTo(10);

        assertThat(pageResultDto.getDtoList().size()).isEqualTo(10);
        assertThat(pageResultDto.getTotalPage()).isEqualTo(6);
        assertThat(pageResultDto.getPageList().size()).isEqualTo(6);

        assertThat(((ItemRevDto) pageResultDto.getDtoList().get(0)).getId()).isEqualTo(55);
    }



}