package com.acme.billing_svc.service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreateInvoiceRequest(
        @NotBlank String number,
        @NotNull LocalDate date,
        @NotBlank String customerId,
        @Valid List<InvoiceItemDto> items
) {}