package com.acme.billing_svc.service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record InvoiceItemDto(
        @NotBlank String description,
        @NotNull @Min(1) Integer quantity,
        @NotNull BigDecimal unitPrice
) {}