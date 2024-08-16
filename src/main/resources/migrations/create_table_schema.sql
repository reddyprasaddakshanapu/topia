CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


create table if not exists exchange_rates (
    id uuid primary key DEFAULT uuid_generate_v4(),
    currency text,
    value text,
    date text
);



    ALTER TABLE exchange_rates
  ADD CONSTRAINT uq_date_currency  UNIQUE(date, currency);

