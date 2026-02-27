CREATE TYPE appointment_status AS ENUM (
    'planned',
    'cancel',
    'finish'
);

CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS professionals (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    profession VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS appointments (
    id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    professional_id INT NOT NULL,

    appointment_date TIMESTAMP NOT NULL,
    duration INT NOT NULL,

    status appointment_status NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (professional_id) REFERENCES professionals(id)
);