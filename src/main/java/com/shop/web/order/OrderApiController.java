package com.shop.web.order;

import com.shop.dto.order.OrderDto;
import com.shop.service.order.OrderService;
import com.shop.service.order.OrderServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private final OrderServiceFacade orderServiceFacade;

    @PostMapping("/order")
    public ResponseEntity<Map<String, Object>> order(@RequestBody @Valid OrderDto orderDto,
                                                     BindingResult bindingResult, Principal principal){
        Map<String, Object> result = new HashMap<>();

        if (bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors)
                sb.append(fieldError.getDefaultMessage());

            result.put("message", sb.toString());
            result.put("code", HttpStatus.BAD_REQUEST);

            return ResponseEntity.ok(result);
        }

        String email = principal.getName();
        Long orderId;

        try {
            orderId = orderServiceFacade.order(orderDto, email);
            result.put("orderId", orderId);
            result.put("code", HttpStatus.OK);
        } catch (Exception e) {
            result.put("message", e.getMessage());
            result.put("code", HttpStatus.BAD_REQUEST);
        }



        return ResponseEntity.ok(result);
    }

    @PostMapping("/order/{orderId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable Long orderId, Principal principal){

        Map<String, Object> result = new HashMap<>();

        if(!orderService.validateOrder(orderId, principal.getName())){
            result.put("message", "주문 취소 권한이 없습니다.");
            result.put("code", HttpStatus.FORBIDDEN);

            return ResponseEntity.ok(result);
        }

        try{
            orderService.cancelOrder(orderId);

            result.put("orderId", orderId);
            result.put("code", HttpStatus.OK);
        } catch (Exception e){
            result.put("message", e.getMessage());
            result.put("code", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(result);
    }
}
