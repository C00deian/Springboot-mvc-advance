package com.codewithmosh.store.controller;

import com.codewithmosh.store.Dtos.ErrorDto;
import com.codewithmosh.store.Dtos.OrderResponse;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;


    @GetMapping
    public List<OrderResponse> getAllOrders(){

       return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder( @PathVariable("orderId") Long orderId){
       return orderService.getOrderById(orderId);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Void> handleOrderNotFoundException(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDeniedException(Exception ex){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorDto(ex.getMessage()));
    }

}
