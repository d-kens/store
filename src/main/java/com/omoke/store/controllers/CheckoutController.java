package com.omoke.store.controllers;

import com.omoke.store.dtos.CheckoutRequest;
import com.omoke.store.dtos.CheckoutResponse;
import com.omoke.store.dtos.ErrorDto;
import com.omoke.store.exceptions.CartEmptyException;
import com.omoke.store.exceptions.CartNotFoundException;
import com.omoke.store.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private CheckoutService checkoutService;

    @PostMapping
    public CheckoutResponse checkout(
            @Valid @RequestBody CheckoutRequest request
    ) {
        return checkoutService.checkout(request);
    }


    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }
}
