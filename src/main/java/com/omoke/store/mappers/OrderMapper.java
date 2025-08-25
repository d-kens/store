package com.omoke.store.mappers;

import com.omoke.store.dtos.OrderDto;
import com.omoke.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
