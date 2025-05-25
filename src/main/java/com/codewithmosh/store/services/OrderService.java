package com.codewithmosh.store.services;

import com.codewithmosh.store.Dtos.OrderResponse;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.mappers.OrderMapper;
import com.codewithmosh.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
public class OrderService {

    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;



    public List<OrderResponse> getAllOrders() {
        var user =  authService.getCurrentUser();
        var orders = orderRepository.getOrdersByCustomer(user);
        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderResponse getOrderById(Long orderId) {
     var order = orderRepository.getOrderWithItems(orderId)
             .orElseThrow(OrderNotFoundException::new);


     var user = authService.getCurrentUser();

     if(!order.isPlacedBy(user)) {
         throw new AccessDeniedException("You do not have permission to access this resource");
     }

     return orderMapper.toDto(order);

    }
}

