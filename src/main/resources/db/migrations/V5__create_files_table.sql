CREATE TABLE IF NOT EXISTS files (
    id UUID PRIMARY KEY,
    original_file_name TEXT NOT NULL,
    size BIGINT NOT NULL,
    user_id UUID,
    type TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    version INT NOT NULL
);

CREATE INDEX IF NOT EXISTS files_idx_user_id ON files(user_id);
