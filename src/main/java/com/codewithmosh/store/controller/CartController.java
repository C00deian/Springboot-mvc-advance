package com.codewithmosh.store.controller;

import com.codewithmosh.store.Dtos.CartDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;



    @PostMapping
    ResponseEntity<CartDto> createCart() {
    var cart = new Cart();
    cartRepository.save(cart);
    var cartDto = cartMapper.toCartDto(cart);

    return new ResponseEntity<>(cartDto ,HttpStatus.CREATED);


    }


}
