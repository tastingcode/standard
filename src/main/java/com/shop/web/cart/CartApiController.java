package com.shop.web.cart;

import com.shop.dto.cart.CartOrderDto;
import com.shop.dto.cart_item.CartItemDto;
import com.shop.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;

    @PostMapping("/cart")
    public ResponseEntity<Map<String, Object>> cart(@RequestBody @Valid CartItemDto cartItemDto,
                                                    BindingResult bindingResult, Principal principal) {

        Map<String, Object> result = new HashMap<>();

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors)
                sb.append(fieldError.getDefaultMessage());

            result.put("message", sb.toString());
            result.put("code", HttpStatus.BAD_REQUEST);

            return ResponseEntity.ok(result);
        }

        String email = principal.getName();
        Long cartItemId;

        try {
            cartItemId = cartService.addCart(cartItemDto, email);

            result.put("cartItemId", cartItemId);
            result.put("code", HttpStatus.OK);
        } catch (Exception e) {
            result.put("message", e.getMessage());
            result.put("code", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(result);
    }

    @PatchMapping("/cartItem/{cartItemId}")
    public ResponseEntity<Map<String, Object>> updateCount(@PathVariable Long cartItemId,
                                                           int count, Principal principal) {

        Map<String, Object> result = new HashMap<>();

        if (count <= 0) {
            result.put("code", HttpStatus.BAD_REQUEST);
            result.put("message", "최소 1개 이상 담아주세요.");

            return ResponseEntity.ok(result);
        } else if (!cartService.validateCartItem(cartItemId, principal.getName())) {
            result.put("code", HttpStatus.BAD_REQUEST);
            result.put("message", "수정 권한이 없습니다.");

            return ResponseEntity.ok(result);
        }

        try {
            cartService.updateCartItemCount(cartItemId, count);

            result.put("cartItemId", cartItemId);
            result.put("code", HttpStatus.OK);
        } catch (Exception e) {
            result.put("message", e.getMessage());
            result.put("code", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/cartItem/{cartItemId}")
    public ResponseEntity<Map<String, Object>> deleteCartItem(@PathVariable Long cartItemId,
                                                              Principal principal){

        Map<String, Object> result = new HashMap<>();
        if (!cartService.validateCartItem(cartItemId, principal.getName())){
            result.put("code", HttpStatus.FORBIDDEN);
            result.put("message", "삭제 권한이 없습니다.");

            return ResponseEntity.ok(result);
        }

        try {
            cartService.deleteCartItem(cartItemId);

            result.put("cartItemId", cartItemId);
            result.put("code", HttpStatus.OK);

        } catch (Exception e) {
            result.put("message", e.getMessage());
            result.put("code", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/cart/orders")
    public ResponseEntity<Map<String, Object>> orderCartItem
            (@RequestBody CartOrderDto cartOrderDto, Principal principal){

        Map<String, Object> result = new HashMap<>();
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        if (cartOrderDtoList == null || cartOrderDtoList.size() == 0){
            result.put("message", "주문할 상품을 선택해주세요.");
            result.put("code", HttpStatus.FORBIDDEN);

            return ResponseEntity.ok(result);
        }

        for (CartOrderDto cartOrder : cartOrderDtoList) {
            if (!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName())){
                result.put("message", "주문 권한이 없습니다.");
                result.put("code", HttpStatus.FORBIDDEN);

                return ResponseEntity.ok(result);
            }
        }

        try {
            Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());

            result.put("orderId", orderId);
            result.put("code", HttpStatus.OK);
        } catch (Exception e) {
            result.put("message", e.getMessage());
            result.put("code", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(result);
    }




}
