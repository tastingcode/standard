package com.shop.entity.order_item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderItemRepositoryTest {

    @Autowired
    OrderItemRepository orderItemRepository;


    // 의도대로 조회 했는지 대충 확인완료
    @Test
    @DisplayName("특정 상품을 주문한 회원 조회")
    public void findMemberIdByItemId(){
        Long itemId = 1L;
        int num = 4;

        List<Integer> result = orderItemRepository.findMemberIdListByItemId(itemId);

        System.out.println(result.contains(num));
        System.out.println(result.contains(9999999));
        System.out.println("result.size() = " + result.size());

    }
}