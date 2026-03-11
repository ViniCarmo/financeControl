CREATE SEQUENCE summaries_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE transactions_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE users
(
    id       INTEGER      NOT NULL DEFAULT nextval('users_seq'),
    username VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE transactions
(
    id               INTEGER NOT NULL DEFAULT nextval('transactions_seq'),
    transactionvalue FLOAT8,
    type             VARCHAR(10) CHECK (type IN ('EXPENSE', 'INCOME')),
    transactiondate  DATE,
    description      VARCHAR(255),
    user_id          INTEGER,
    PRIMARY KEY (id)
);

CREATE TABLE summaries
(
    id                 INTEGER NOT NULL DEFAULT nextval('summaries_seq'),
    balance            FLOAT8,
    final_date         DATE,
    initial_date       DATE,
    total_expensive    FLOAT8,
    total_income       FLOAT8,
    total_transactions INTEGER,
    user_id            INTEGER,
    PRIMARY KEY (id)
);

ALTER TABLE summaries
    ADD CONSTRAINT fk_summary_user FOREIGN KEY (user_id) REFERENCES users;
ALTER TABLE transactions
    ADD CONSTRAINT fk_transaction_user FOREIGN KEY (user_id) REFERENCES users;