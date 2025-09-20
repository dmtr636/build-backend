CREATE TABLE IF NOT EXISTS events (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    action TEXT NOT NULL,
    action_type TEXT NOT NULL,
    object_name TEXT,
    object_id TEXT,
    date TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    info JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    version INT NOT NULL
);

CREATE INDEX IF NOT EXISTS events_idx_user_id ON events(user_id);
CREATE INDEX IF NOT EXISTS events_idx_action ON events(action);
CREATE INDEX IF NOT EXISTS events_idx_action_type ON events(action_type);
CREATE INDEX IF NOT EXISTS events_idx_object_name ON events(object_name);
CREATE INDEX IF NOT EXISTS events_idx_object_id ON events(object_id);
CREATE INDEX IF NOT EXISTS events_idx_created_at ON events(date);
