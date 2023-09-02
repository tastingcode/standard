package com.shop.entity.cart;

import com.shop.entity.BaseEntity;
import com.shop.entity.member.Member;
import lombok.*;

import javax.persistence.*;
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "cart")
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Cart(Member member) {
        this.member = member;
    }

    public void setMember(Member member){
        this.member = member;
    }

    public static Cart createCart(Member member){
        return Cart.builder()
                .member(member)
                .build();
    }

}
