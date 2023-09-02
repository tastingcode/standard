package com.shop.web.order;

import com.shop.dto.order.OrderHistDto;
import com.shop.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping({"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {

        try {
            PageRequest pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);

            Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(), pageable);

            model.addAttribute("orders", orderHistDtoList);
            model.addAttribute("page", pageable.getPageNumber());
            model.addAttribute("maxPage", 5);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "주문 정보 조회 중 에러가 발생하였습니다.");
            return "redirect:/";
        }

        return "order/orderHist";
    }



}
