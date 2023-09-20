package com.shop.entity.item;

import com.shop.constant.category.Category;
import com.shop.repository.custom.ItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

    List<Item> findByItemNm(String itemNm);

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc",
            nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);


    @Query("select i from Item i where i.category = :category")
    List<Item> findByCategory(@Param("category") Category category);

//    @Query(value = "select * from Item i where i.category like :category%", nativeQuery = true)
//    List<Item> findByCategoryLike(@Param("category") String categoryCode);


    @Override
    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select i from Item i where i.id = :itemId")
    Optional<Item> findById(@Param("itemId") Long itemId);

}
