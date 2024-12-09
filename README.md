# mthread-bank-system
Part of a code challenge

DB Schema: 

CREATE TABLE public.account (
id varchar(255) DEFAULT nextval('account_id_seq'::regclass) NOT NULL,
account_id varchar(255) NOT NULL,
balance int8 DEFAULT 0.00 NOT NULL,
created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
"type" varchar(255) NOT NULL,
"version" int8 NULL,
CONSTRAINT account_pkey PRIMARY KEY (id)
);

CREATE TABLE public.transfer (
id bigserial NOT NULL,
from_account_id varchar(255) NOT NULL,
to_account_id varchar(255) NOT NULL,
transfer_amount numeric(10, 2) NOT NULL,
transfer_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
correlation_id varchar(255) NULL,
created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
amount float4 NULL,
CONSTRAINT transfer_pkey PRIMARY KEY (id)
);