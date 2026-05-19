-- Seed mock employees for local/testing (idempotent: skips rows that violate unique employee_code).
INSERT IGNORE INTO employees (employee_code, first_name, last_name, email, phone_number, role, salary, joining_date, active, created_at, updated_at) VALUES
    ('EMP-PB-001', 'Rajesh', 'Kumarraj', 'rajesh.kumar@petrolbunk.local', '+91 98765 43210', 'Station Manager', 52000.00, '2023-06-01', true, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
    ('EMP-PB-002', 'Priya', 'Shankar', 'priya.shankar@petrolbunk.local', '+91 91234 56789', 'Cashier', 28000.00, '2024-01-15', true, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
    ('EMP-PB-003', 'Vikram', 'Devi', 'vikram.devi@petrolbunk.local', '+91 99887 76655', 'Fuel Attendant', 22000.00, '2024-03-20', true, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
    ('EMP-PB-004', 'Anitha', 'Menon', 'anitha.menon@petrolbunk.local', '+91 90123 45678', 'Shift Supervisor', 35000.00, '2022-11-10', true, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
    ('EMP-PB-005', 'Mohammed', 'Hassan', 'mohammed.hassan@petrolbunk.local', '+91 94444 33221', 'Security', 20000.00, '2023-09-01', true, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6));
