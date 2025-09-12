package com.acme.billing_svc.web.controller;


import com.acme.billing_svc.domain.model.Invoice;
import com.acme.billing_svc.service.InvoiceService;
import com.acme.billing_svc.service.dto.CreateInvoiceRequest;
import com.acme.billing_svc.service.dto.InvoiceResponse;
import com.acme.billing_svc.service.dto.UpdateInvoiceRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BILLING_USER')")
public class InvoiceController {

    private final InvoiceService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceResponse create(@Valid @RequestBody CreateInvoiceRequest req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public InvoiceResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public List<InvoiceResponse> list(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return service.list(from, to);
    }

    @PutMapping("/{id}")
    public InvoiceResponse update(@PathVariable Long id,
                                  @Valid @RequestBody UpdateInvoiceRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}