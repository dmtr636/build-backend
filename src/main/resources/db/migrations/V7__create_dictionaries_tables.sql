CREATE TABLE construction_violations (
    id UUID PRIMARY KEY,
    category VARCHAR NOT NULL,
    kind VARCHAR NOT NULL,
    severity_type VARCHAR NOT NULL,
    name VARCHAR NOT NULL,
    remediation_due_days INT,
    version INT NOT NULL DEFAULT 0
);

CREATE TABLE construction_works (
    id UUID PRIMARY KEY,
    name VARCHAR NOT NULL,
    unit VARCHAR,
    classification_code VARCHAR,
    version INT NOT NULL DEFAULT 0
);

CREATE TABLE normative_documents (
    id UUID PRIMARY KEY,
    regulation VARCHAR NOT NULL,
    name VARCHAR NOT NULL,
    version INT NOT NULL DEFAULT 0
);
