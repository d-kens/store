package com.omoke.store;

import org.springframework.stereotype.Service;

@Service
public class MpesaService implements PaymentService {
    @Override
    public void processPayment(double amount) {
        System.out.println("MPESA");
        System.out.println("Amount: " + amount);
    }
}
