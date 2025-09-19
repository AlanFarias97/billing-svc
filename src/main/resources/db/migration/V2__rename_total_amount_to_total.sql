DO $$
BEGIN
    -- Si ya existe total y no existe total_amount, no hacemos nada
    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_name='invoices' AND column_name='total') THEN
        -- nada
    ELSIF EXISTS (SELECT 1 FROM information_schema.columns
                  WHERE table_name='invoices' AND column_name='total_amount') THEN
        -- crea total y copia datos
ALTER TABLE invoices ADD COLUMN total NUMERIC(14,2);
UPDATE invoices SET total = total_amount;
ALTER TABLE invoices ALTER COLUMN total SET NOT NULL;

-- opcional: eliminar la antigua si no la vas a usar
ALTER TABLE invoices DROP COLUMN total_amount;
ELSE
        -- si no existe ninguna, crea total desde cero
ALTER TABLE invoices ADD COLUMN total NUMERIC(14,2) NOT NULL DEFAULT 0;
END IF;
END $$;
