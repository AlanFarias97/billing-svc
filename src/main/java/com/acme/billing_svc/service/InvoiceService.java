package com.acme.billing_svc.service;

import com.acme.billing_svc.domain.model.Invoice;
import com.acme.billing_svc.domain.model.InvoiceItem;
import com.acme.billing_svc.domain.model.InvoiceStatus;
import com.acme.billing_svc.domain.repository.InvoiceRepository;
import com.acme.billing_svc.service.dto.CreateInvoiceRequest;
import com.acme.billing_svc.service.dto.InvoiceItemDto;
import com.acme.billing_svc.service.dto.InvoiceResponse;
import com.acme.billing_svc.service.dto.UpdateInvoiceRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository repo;

    @Transactional
    public InvoiceResponse create(CreateInvoiceRequest req) {
        var invoice = new Invoice();
        invoice.setNumber(req.number());
        invoice.setDate(req.date());
        invoice.setCustomerId(req.customerId());
        invoice.setStatus(InvoiceStatus.ISSUED);

        var items = req.items() == null ? List.<InvoiceItem>of()
                : req.items().stream().map(this::toEntityItem).toList();
        invoice.setItems(items);

        recalcTotal(invoice);

        return toDto(repo.save(invoice));
    }

    @Transactional(readOnly = true)
    public InvoiceResponse get(Long id) {
        return repo.findById(id).map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponse> list(LocalDate from, LocalDate to) {
        var result = (from != null && to != null)
                ? repo.findByDateBetween(from, to)
                : repo.findAll();
        return result.stream().map(this::toDto).toList();
    }

    @Transactional
    public InvoiceResponse update(Long id, UpdateInvoiceRequest req) {
        var invoice = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + id));

        invoice.setNumber(req.number());
        invoice.setDate(req.date());
        invoice.setCustomerId(req.customerId());

        var newItems = req.items().stream().map(this::toEntityItem).toList();
        invoice.setItems(newItems);

        recalcTotal(invoice);

        return toDto(invoice);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    /* ---------- helpers ---------- */

    private InvoiceItem toEntityItem(InvoiceItemDto dto) {
        var lineTotal = dto.unitPrice().multiply(BigDecimal.valueOf(dto.quantity()));
        return InvoiceItem.builder()
                .description(dto.description())
                .quantity(dto.quantity())
                .unitPrice(dto.unitPrice())
                .lineTotal(lineTotal)
                .build();
    }

    private void recalcTotal(Invoice invoice) {
        var total = invoice.getItems().stream()
                .map(InvoiceItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        invoice.setTotal(total);
    }

    private InvoiceResponse toDto(Invoice inv) {
        var items = inv.getItems().stream()
                .map(i -> new InvoiceResponse.InvoiceResponseItem(
                        i.getDescription(), i.getQuantity(), i.getUnitPrice(), i.getLineTotal()))
                .toList();
        return new InvoiceResponse(inv.getId(), inv.getNumber(), inv.getDate(),
                inv.getCustomerId(), inv.getStatus(), inv.getTotal(), items);
    }
}