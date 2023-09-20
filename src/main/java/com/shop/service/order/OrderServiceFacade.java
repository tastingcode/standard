package com.shop.service.order;

import com.shop.dto.order.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceFacade {

    private final OrderService orderService;

    public Long order(OrderDto orderDto, String email) {
        while (true) {
            try {
                Long orderId = orderService.order(orderDto, email);
                return orderId;
            } catch (ObjectOptimisticLockingFailureException e) {
                handleOptimisticLockingException(e);
            }
        }
    }

    public Long orders(List<OrderDto> orderDtoList, String email) {
        while (true) {
            try {
                Long orderId = orderService.orders(orderDtoList, email);
                return orderId;
            } catch (ObjectOptimisticLockingFailureException e) {
                handleOptimisticLockingException(e);
            }
        }
    }

    private void handleOptimisticLockingException(ObjectOptimisticLockingFailureException e) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            throw new RuntimeException("sleep for order");
        }
    }

}
