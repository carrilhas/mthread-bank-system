package com.banksystem.model;

public record RequestMessage(Long accountId, String type, Long amount, String correlationId) {}
