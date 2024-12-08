package com.banksystem.model;

public record ResponseMessage(String correlationId, String status, String errorMessage) {}
