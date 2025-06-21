package com.omoke.store;

public class PayStack implements PaymentService {

    @Override
    public void processPayment(double amount) {
        System.out.println("PayStack");
        System.out.println("Amount: " + amount);
    }
}
