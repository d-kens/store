package com.omoke.store.mappers;

import com.omoke.store.dtos.CartDto;
import com.omoke.store.dtos.CartItemDto;
import com.omoke.store.entities.Cart;
import com.omoke.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
