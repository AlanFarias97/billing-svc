package com.acme.billing_svc.service.dto;

import com.acme.billing_svc.domain.model.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record InvoiceResponse(
        Long id,
        String number,
        LocalDate date,
        String customerId,
        InvoiceStatus status,
        BigDecimal total,
        List<InvoiceResponseItem> items
) {
    public record InvoiceResponseItem(String description, Integer quantity, BigDecimal unitPrice, BigDecimal lineTotal) {}
}