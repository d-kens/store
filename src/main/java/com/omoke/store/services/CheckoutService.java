package com.omoke.store.services;


import com.omoke.store.dtos.CheckoutRequest;
import com.omoke.store.dtos.CheckoutResponse;
import com.omoke.store.entities.Order;
import com.omoke.store.exceptions.CartEmptyException;
import com.omoke.store.exceptions.CartNotFoundException;
import com.omoke.store.repositories.CartRepository;
import com.omoke.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutService {

    private AuthService authService;
    private CartService cartService;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException();
        }

        if (cart.getItems().isEmpty()) {
            throw new CartEmptyException();
        }

        var order = Order.fromCart(cart, authService.getCurrentUser());

        orderRepository.save(order);

        cartService.clearCart(cart.getId());
        return  new CheckoutResponse(order.getId());
    }

}
