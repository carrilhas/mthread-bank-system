package com.banksystem.BankSystem;

public record ResponseMessage(String correlationId, String status, String errorMessage) {}
