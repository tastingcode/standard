package com.shop.entity.order_item;

import com.shop.entity.BaseEntity;
import com.shop.entity.item.Item;
import com.shop.entity.order.Order;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    @Builder
    public OrderItem(Item item, Order order, int orderPrice, int count) {
        this.item = item;
        this.order = order;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public static OrderItem createOrderItem(Item item, int count) {
        item.removeStock(count);
        return OrderItem.builder()
                .item(item)
                .orderPrice(item.getPrice())
                .count(count)
                .build();
    }

    public int getTotalPrice(){
        return orderPrice * count;
    }

    public void setOrder(Order order){
        this.order = order;
    }

    public void cancel(){
        this.getItem().addStock(count);
    }



}
