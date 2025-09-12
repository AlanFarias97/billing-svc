package com.acme.billing_svc.domain.repository;


import com.acme.billing_svc.domain.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByNumber(String number);
    List<Invoice> findByDateBetween(LocalDate from, LocalDate to);
}