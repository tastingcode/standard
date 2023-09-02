package com.shop.entity.item_rev;

import com.shop.dto.item_rev.ItemRevDto;
import com.shop.entity.BaseTimeEntity;
import com.shop.entity.item.Item;
import com.shop.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"item", "member"})
@Table(name = "item_rev")
public class ItemRev extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_rev_id")
    private Long id;

    private String title;

    @Column(length = 10000)
    private String content;

    @Column
    private int rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private ItemRev(String title, String content, int rate, Item item, Member member) {
        this.title = title;
        this.content = content;
        this.rate = rate;
        this.item = item;
        this.member = member;
    }


}
