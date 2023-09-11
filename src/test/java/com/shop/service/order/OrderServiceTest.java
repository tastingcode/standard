package com.shop.service.order;

import com.shop.constant.item.ItemSellStatus;
import com.shop.constant.order.OrderStatus;
import com.shop.dto.order.OrderDto;
import com.shop.entity.item.Item;
import com.shop.entity.item.ItemRepository;
import com.shop.entity.member.Member;
import com.shop.entity.member.MemberRepository;
import com.shop.entity.order.Order;
import com.shop.entity.order.OrderRepository;
import com.shop.entity.order_item.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

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
    @DisplayName("주문 테스트")
    public void order() {
        //given
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setItemId(item.getId());
        orderDto.setCount(10);

        //when
        Long orderId = orderService.order(orderDto, member.getEmail());
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems)
            System.out.println("orderItem.getItem().getItemNm() = " + orderItem.getItem().getItemNm());

        int totalPrice = orderDto.getCount() * item.getPrice();

        //then
        assertThat(totalPrice).isEqualTo(order.getTotalPrice());

    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void cancelOrder(){
        //given
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        Long orderId = orderService.order(orderDto, member.getEmail());

        //when
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        orderService.cancelOrder(orderId);

        //then
        assertThat(OrderStatus.CANCEL).isEqualTo(order.getOrderStatus());
        assertThat(100).isEqualTo(item.getStockNumber());
    }

}
