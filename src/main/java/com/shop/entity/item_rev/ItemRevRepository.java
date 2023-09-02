package com.shop.entity.item_rev;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRevRepository extends JpaRepository<ItemRev, Long> {

//    Page<ItemRev> findAllByItemIdOrderByRegTimeDesc(Long itemId, Pageable pageable);

    Page<ItemRev> findByItemId(Long itemId, Pageable pageable);

}
