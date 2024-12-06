package com.banksystem.BankSystem;

public record RequestMessage(String accountId, String type, Long amount, String correlationId) {}
