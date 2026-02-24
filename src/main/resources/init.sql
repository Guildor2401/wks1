
CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    firstName VARCHAR(255) NOT NULL,
    lastName VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phoneNumber VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS professionals (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    profession VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phoneNumber VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS appointments (
    id SERIAL PRIMARY KEY,
    customerId INT NOT NULL,
    professionalId INT NOT NULL,

    appointmentDate TIMESTAMP NOT NULL,
    duration INT NOT NULL,

    status appointment_status NOT NULL,
    FOREIGN KEY (customerId) REFERENCES customers(id),
    FOREIGN KEY (professionalId) REFERENCES professionals(id)
);

CREATE TYPE appointment_status AS ENUM (
    'planned',
    'cancel',
    'finish'
);