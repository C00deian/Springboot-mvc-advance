package com.codewithmosh.store.mappers;

import com.codewithmosh.store.Dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id" , target = "categoryId")
    ProductDto toProductDto(Product product);
    Product toEntity(ProductDto productDto);
    @Mapping(target = "id" , ignore = true)
    void update(ProductDto productDto, @MappingTarget Product product);
}
