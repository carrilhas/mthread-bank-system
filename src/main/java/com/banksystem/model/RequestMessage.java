package com.banksystem.model;

public record RequestMessage(String accountId, String type, Long amount, String correlationId) {}
