package com.omoke.store.payments;

import com.omoke.store.entities.Order;
import com.omoke.store.exceptions.CartEmptyException;
import com.omoke.store.exceptions.CartNotFoundException;
import com.omoke.store.repositories.CartRepository;
import com.omoke.store.repositories.OrderRepository;

import com.omoke.store.services.AuthService;
import com.omoke.store.services.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class CheckoutService {
    private final AuthService authService;
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final PaymentGateway paymentGateway;
    private final OrderRepository orderRepository;

    @Transactional
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

        try {
            var session = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cart.getId());
            return  new CheckoutResponse(order.getId(), session.getCheckoutUrl());
        } catch (PaymentException ex) {
            orderRepository.delete(order);
            throw ex;
        }
    }


    public void handleWebhookEvent(WebhookRequest request) {
        paymentGateway
                .parseWebhookRequest(request)
                .ifPresent(
                paymentResult -> {
                    var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                    order.setStatus(paymentResult.getPaymentStatus());
                    orderRepository.save(order);
                }
        );
    }
}