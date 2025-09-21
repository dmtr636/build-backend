CREATE TABLE IF NOT EXISTS organizations (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    image_id TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    version INT NOT NULL
);

CREATE INDEX IF NOT EXISTS organizations_idx_name ON organizations(name);
