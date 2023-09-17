package com.shop.service.order;

import com.shop.dto.order.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceFacade {

    private final OrderService orderService;

    public synchronized Long order(OrderDto orderDto, String email){
        return orderService.order(orderDto, email);
    }

}
