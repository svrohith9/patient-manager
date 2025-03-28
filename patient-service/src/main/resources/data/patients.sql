INSERT INTO patient
(id, address, birth_date, email, first_name, gender, last_name, phone_number, registration_date)
VALUES
    ('00000000-0000-0000-0000-000000000002',
     '456 Elm St, Springfield, USA',
     '1990-02-15',
     'jane.smith@example.com',
     'Jane',
     'FEMALE',
     'Smith',
     '9876543210',  -- A different phone number value
     '2023-03-02');

INSERT INTO patient
(id, address, birth_date, email, first_name, gender, last_name, phone_number, registration_date)
VALUES
    ('00000000-0000-0000-0000-000000000001',
     '123 Main St, Springfield, USA',
     '1980-01-01',
     'john.doe@example.com',
     'John',
     'MALE',
     'Doe',
     '1234567890',  -- Provide a valid phone number here
     '2023-03-01');
