package com.shop.entity.order_item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select o.member.id from OrderItem oi left join oi.order o where oi.item.id = :itemId")
    List<Integer> findMemberIdListByItemId(@Param("itemId") Long itemId);


}
