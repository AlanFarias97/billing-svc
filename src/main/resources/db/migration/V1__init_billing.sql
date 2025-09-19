-- V1__init.sql  (Esquema consolidado que MATCHEA a tus entidades)

-- Invoices
CREATE TABLE IF NOT EXISTS invoices (
                                        id            BIGSERIAL PRIMARY KEY,
                                        number        VARCHAR(60)  NOT NULL,
    customer_id   VARCHAR(60)  NOT NULL,
    date          DATE         NOT NULL,          -- <- OJO: columna 'date' (no issue_date)
    due_date      DATE         NOT NULL,
    status        VARCHAR(20)  NOT NULL,
    currency      VARCHAR(3)   NOT NULL DEFAULT 'USD',
    total_amount  NUMERIC(14,2) NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT NOW()
    );

-- Único para number (suele ser el número de factura)
ALTER TABLE invoices
    ADD CONSTRAINT uq_invoices_number UNIQUE (number);

-- Invoice items
CREATE TABLE IF NOT EXISTS invoice_items (
                                             id           BIGSERIAL PRIMARY KEY,
                                             invoice_id   BIGINT       NOT NULL REFERENCES invoices(id) ON DELETE CASCADE,
    description  VARCHAR(255) NOT NULL,
    quantity     INTEGER      NOT NULL,          -- <- OJO: integer (no numeric)
    unit_price   NUMERIC(14,2) NOT NULL,
    line_total   NUMERIC(14,2) NOT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP    NOT NULL DEFAULT NOW()
    );

-- Índices útiles
CREATE INDEX IF NOT EXISTS idx_invoices_date ON invoices(date);
CREATE INDEX IF NOT EXISTS idx_invoice_items_invoice_id ON invoice_items(invoice_id);
