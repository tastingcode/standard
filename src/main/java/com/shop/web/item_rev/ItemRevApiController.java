package com.shop.web.item_rev;

import com.shop.dto.item_rev.ItemRevDto;
import com.shop.dto.page.PageRequestDto;
import com.shop.service.itemRev.ItemRevService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;


@Slf4j
@RequiredArgsConstructor
@RestController
public class ItemRevApiController {

    private final ItemRevService itemRevService;


    //리뷰 작성 권한 파악
    //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/reviews/authority")
    public ResponseEntity<?> checkReviewAuthority(@RequestParam HashMap<String, String> paramMap){
        return ResponseEntity.ok().body(itemRevService.checkAuthority(paramMap));
    }


    /**
     * @PreAuthorize hasAnyRole
     */
    //  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/reviews")
    public ResponseEntity<?> makeReview(@RequestBody @Valid ItemRevDto itemRevDto,
                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }


        itemRevService.createReview(itemRevDto);


        return ResponseEntity.ok().body("상품평이 등록 되었습니다.");
    }

    // 상품평 조회
    @GetMapping("/item/{itemId}/reviews")
    public ResponseEntity<?> getReviewList(@PathVariable Long itemId,
                                           PageRequestDto pageRequestDto) {

        return ResponseEntity.ok().body(itemRevService.getReviewList(itemId, pageRequestDto));
    }


}
