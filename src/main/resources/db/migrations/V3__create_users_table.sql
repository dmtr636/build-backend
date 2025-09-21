CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    login TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL,
    enabled BOOLEAN NOT NULL,
    position TEXT,
    name TEXT,
    last_name TEXT,
    first_name TEXT,
    patronymic TEXT,
    messenger TEXT,
    email TEXT,
    work_phone TEXT,
    personal_phone TEXT,
    image_id TEXT,
    organization_id UUID REFERENCES organizations(id),
    info JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    version INT NOT NULL
);

CREATE INDEX IF NOT EXISTS users_idx_role ON users(role);
CREATE INDEX IF NOT EXISTS users_idx_position ON users(position);
CREATE INDEX IF NOT EXISTS users_idx_name ON users(name);
CREATE INDEX IF NOT EXISTS users_idx_enabled ON users(enabled);
CREATE INDEX IF NOT EXISTS users_idx_created_at ON users(created_at);
CREATE INDEX IF NOT EXISTS users_idx_updated_at ON users(updated_at);
