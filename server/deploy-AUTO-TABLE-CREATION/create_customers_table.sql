-- SQL commands to create the customers table in your RDS database
-- Connect to your PostgreSQL RDS instance and run these commands

-- Create the customers table
CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL
);

-- Insert some test data
INSERT INTO customers (first_name, last_name) VALUES 
('John', 'Woo'),
('Jeff', 'Dean'),
('Josh', 'Bloch'),
('Josh', 'Long')
ON CONFLICT DO NOTHING;

-- Verify the table and data
SELECT COUNT(*) as customer_count FROM customers;
SELECT * FROM customers LIMIT 5;