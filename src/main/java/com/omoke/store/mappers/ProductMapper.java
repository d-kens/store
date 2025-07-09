package com.omoke.store.mappers;

import com.omoke.store.dtos.ProductDto;
import com.omoke.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toProductDto(Product product);

    Product toProductEntity(ProductDto productDto);

    @Mapping(target = "id", ignore = true)
    void update(ProductDto request, @MappingTarget Product product);
}
