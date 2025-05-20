package com.codewithmosh.store.mappers;

import com.codewithmosh.store.Dtos.CartDto;
import com.codewithmosh.store.Dtos.CartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

//   @Mapping(target = "items"  ,source = "items")
    @Mapping(target = "totalPrice" , expression = "java(cart.getTotalPrice())")
    CartDto toCartDto(Cart cart);
    @Mapping(target = "totalPrice" ,expression = "java(cartItem.getTotalPrice())")
    CartItemDto toCartItemDto(CartItem cartItem);
}
