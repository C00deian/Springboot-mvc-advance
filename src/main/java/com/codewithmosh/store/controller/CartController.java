package com.codewithmosh.store.controller;

import com.codewithmosh.store.Dtos.AddItemToCartRequest;
import com.codewithmosh.store.Dtos.CartDto;
import com.codewithmosh.store.Dtos.CartItemDto;
import com.codewithmosh.store.Dtos.UpdateCartItemRequest;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import jdk.jfr.Frequency;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;


    @PostMapping
    ResponseEntity<CartDto> createCart() {
    var cart = new Cart();
    cartRepository.save(cart);
    var cartDto = cartMapper.toCartDto(cart);

    return new ResponseEntity<>(cartDto ,HttpStatus.CREATED);


    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddItemToCartRequest request

    ) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

     var product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }

    var cartItem = cart.addItem(product);
        cartRepository.save(cart);
        var cartItemDto = cartMapper.toCartItemDto(cartItem);
       return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);

    }


    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {

       var cart =  cartRepository.getCartWithItems(cartId).orElse(null);
       if (cart == null) {
           return ResponseEntity.notFound().build();
       }

       return ResponseEntity.ok(cartMapper.toCartDto(cart));

    }


    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateItem(
          @PathVariable("cartId") UUID  cartId,
          @PathVariable("productId") Long productId,
        @Valid @RequestBody UpdateCartItemRequest request
    ){
var cart = cartRepository.getCartWithItems(cartId).orElse(null);
if (cart == null) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("error" , "Cart not found")
    );
}
        var cartItem = cart.getItem(productId);

     if(cartItem == null) {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                 Map.of("error" , "Product ws not found in the cart")
         );
     }

     cartItem.setQuantity(request.getQuantity());
     cartRepository.save(cart);
     return ResponseEntity.ok(cartMapper.toCartItemDto(cartItem));

    }


    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId
    ){

        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error" , "Cart not found")
            );
        }

        cart.removeItem(productId);
        cartRepository.save(cart);
        return ResponseEntity.noContent().build();
    }
}
