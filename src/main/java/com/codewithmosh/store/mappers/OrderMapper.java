package com.codewithmosh.store.mappers;

import com.codewithmosh.store.Dtos.OrderResponse;
import com.codewithmosh.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toDto(Order order);
}
