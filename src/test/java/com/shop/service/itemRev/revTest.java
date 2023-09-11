package com.shop.service.itemRev;

import com.shop.constant.category.Category;
import com.shop.entity.item.Item;
import com.shop.entity.item.ItemRepository;
import com.shop.entity.item_rev.ItemRevRepository;
import com.shop.entity.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class revTest {

    @Autowired
    ItemRevService itemRevService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemRevRepository itemRevRepository;


    /**
    @Commit
    @Test
    public void createReview(){
        //given
        Long memberId = 2L;
        Member member = memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);

        Long itemId = 2L;
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        for (int i = 0; i < 270; i++) {
            ItemRevDto itemRevDto = new ItemRevDto();
            itemRevDto.setTitle("title" + i);
            itemRevDto.setContent("content" + i);
            itemRevDto.setRate(i);
            itemRevDto.setItemId(item.getId());
            itemRevDto.setMemberId(member.getId());

            itemRevService.createReview(itemRevDto);
        }


    }
    */


}
