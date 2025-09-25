CREATE TABLE project_violations (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL,
    name TEXT NOT NULL,
    due_date DATE,
    violation_time TIMESTAMPTZ NOT NULL,
    status TEXT NOT NULL,
    category TEXT NOT NULL,
    kind TEXT NOT NULL,
    severity_type TEXT NOT NULL,
    is_note BOOLEAN NOT NULL DEFAULT FALSE,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    author_id UUID NOT NULL,
    assignee_id UUID,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    version INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_violation_project FOREIGN KEY(project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_violation_author FOREIGN KEY(author_id) REFERENCES users(id),
    CONSTRAINT fk_violation_assignee FOREIGN KEY(assignee_id) REFERENCES users(id)
);

CREATE INDEX idx_project_violations_project_id ON project_violations(project_id);
CREATE INDEX idx_project_violations_author_id ON project_violations(author_id);
CREATE INDEX idx_project_violations_assignee_id ON project_violations(assignee_id);

CREATE TABLE project_violation_files (
    violation_id UUID NOT NULL REFERENCES project_violations(id) ON DELETE CASCADE,
    file_id UUID NOT NULL REFERENCES files(id),
    PRIMARY KEY(violation_id, file_id)
);

CREATE TABLE project_violation_photos (
    violation_id UUID NOT NULL REFERENCES project_violations(id) ON DELETE CASCADE,
    file_id UUID NOT NULL REFERENCES files(id),
    PRIMARY KEY(violation_id, file_id)
);

CREATE TABLE project_violation_comments (
    id UUID PRIMARY KEY,
    violation_id UUID NOT NULL REFERENCES project_violations(id) ON DELETE CASCADE,
    text TEXT NOT NULL,
    author_id UUID,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    version INT NOT NULL DEFAULT 0
);

CREATE TABLE project_violation_comment_files (
    comment_id UUID NOT NULL REFERENCES project_violation_comments(id) ON DELETE CASCADE,
    file_id UUID NOT NULL REFERENCES files(id),
    PRIMARY KEY(comment_id, file_id)
);

ALTER TABLE project_work_comment_files
    ADD CONSTRAINT fk_file FOREIGN KEY (file_id) REFERENCES files(id);
