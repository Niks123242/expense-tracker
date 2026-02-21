CREATE TABLE expenses (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    expense_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);