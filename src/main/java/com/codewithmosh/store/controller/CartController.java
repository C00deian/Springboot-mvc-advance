package com.codewithmosh.store.controller;

import com.codewithmosh.store.Dtos.*;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.services.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@AllArgsConstructor
@RestController
@RequestMapping("/carts")
@Tag(name = "Carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    ResponseEntity<CartDto> createCart() {

      var cartDto = cartService.createCart();
      return new ResponseEntity<>(cartDto ,HttpStatus.CREATED);

    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddItemToCartRequest request

    ) {
        var cartItemDto = cartService.addToCart(cartId , request.getProductId());
       return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);

    }


    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {

        var cartDto = cartService.getCart(cartId);
        return ResponseEntity.ok(cartDto);
    }


    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateItem(
          @PathVariable("cartId") UUID  cartId,
          @PathVariable("productId") Long productId,
        @Valid @RequestBody UpdateCartItemRequest request
    ){

        var cartItem = cartService.updateCartItem(cartId , productId , request.getQuantity());
        return ResponseEntity.ok(cartItem);
    }


    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId
    ){

        cartService.removeFromCart(cartId , productId);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{cartId}/items")
        public ResponseEntity<Void> clearCart(@PathVariable UUID cartId){

        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCartNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
              new ErrorDto("Cart not found")
        );
    }
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFoundException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
               new ErrorDto("Product not found in cart")
        );
    }
}
