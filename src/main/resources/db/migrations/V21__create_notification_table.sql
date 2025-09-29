CREATE TABLE notifications (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    type VARCHAR(50),
    object_id UUID,
    content TEXT,
    author_id UUID REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    version INT NOT NULL DEFAULT 0
);

CREATE TABLE notification_recipients (
    notification_id UUID NOT NULL REFERENCES notifications(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    read BOOLEAN DEFAULT FALSE NOT NULL,
    PRIMARY KEY (notification_id, user_id)
);

CREATE INDEX idx_notifications_project_id ON notifications(project_id);
CREATE INDEX idx_recipients_user_id ON notification_recipients(user_id);
