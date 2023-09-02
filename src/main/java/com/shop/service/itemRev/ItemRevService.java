package com.shop.service.itemRev;

import com.shop.dto.item_rev.ItemRevDto;
import com.shop.dto.page.PageRequestDto;
import com.shop.dto.page.PageResultDto;
import com.shop.entity.item.Item;
import com.shop.entity.item.ItemRepository;
import com.shop.entity.item_rev.ItemRev;
import com.shop.entity.item_rev.ItemRevRepository;
import com.shop.entity.member.Member;
import com.shop.entity.member.MemberRepository;
import com.shop.entity.order_item.OrderItemRepository;
import com.shop.exception.item.NotExistItemException;
import com.shop.exception.itemRev.CheckReviewAuthorityException;
import com.shop.exception.member.NotExistMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemRevService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemRevRepository itemRevRepository;
    private final OrderItemRepository orderItemRepository;


    @Transactional
    public Long createReview(ItemRevDto itemRevDto) {
        Member member = memberRepository.findById(itemRevDto.getMemberId())
                .orElseThrow(() -> new NotExistMemberException("존재하지 않는 회원입니다."));

        Item item = itemRepository.findById(itemRevDto.getItemId())
                .orElseThrow(() -> new NotExistItemException("존재하지 않는 상품입니다."));

        //댓글 생성 가능한 지 예외 처리
        checkExceptionForCreateReview(itemRevDto, item);

        ItemRev itemRev = itemRevDto.toEntity(itemRevDto, member, item);

        itemRevRepository.save(itemRev);

        return itemRev.getId();

        //상품 평점 세팅하고
        //상품 평점 업데이트 추가하기
    }

    public HashMap<String, Object> getReviewList(Long itemId, PageRequestDto pageRequestDto) {
        Pageable pageable = pageRequestDto.getPageable(Sort.by("id").descending());

        Page<ItemRev> itemRevPage = itemRevRepository.findByItemId(itemId, pageable);

        Function<ItemRev, ItemRevDto> fn = (entity -> entityToDto(entity));

        HashMap<String, Object> resultMap = new HashMap<>();

        resultMap.put("result", new PageResultDto<>(itemRevPage, fn));

        // 평점 평균 조회 추가 예정

        return resultMap;
    }

    private void checkExceptionForCreateReview(ItemRevDto itemRevDto, Item item) {

        if (itemRevDto.getId() != null)
            throw new IllegalArgumentException("댓글 생성 실패! 댓글 생성 시 현재 댓글의 id가 없어야 합니다.");
        if (itemRevDto.getItemId() != item.getId())
            throw new IllegalArgumentException("댓글 생성 실패! 상품의 id가 잘못됐습니다.");
    }

    public ItemRevDto entityToDto(ItemRev itemRev){
        return ItemRevDto.builder()
                .id(itemRev.getId())
                .title(itemRev.getTitle())
                .content(itemRev.getContent())
                .rate(itemRev.getRate())
                .itemId(itemRev.getItem().getId())
                .memberId(itemRev.getMember().getId())
                .build();
    }


    public String checkAuthority(HashMap<String, String> paramMap) {
        int memberId = Integer.parseInt(paramMap.get("memberId"));
        Long itemId = Long.parseLong(paramMap.get("itemId"));

        List<Integer> memberIdList = orderItemRepository.findMemberIdListByItemId(itemId);

        if (memberIdList.contains(memberId))
            return "상품평을 작성할 수 있습니다.";

        throw new CheckReviewAuthorityException("해당 상품 결제를 완료한 회원만 리뷰를 작성할 수 있습니다.");

    }

}
