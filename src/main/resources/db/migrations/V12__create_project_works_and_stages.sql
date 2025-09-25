CREATE TABLE project_works (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL,
    name TEXT NOT NULL,
    planned_start DATE,
    planned_end DATE,
    actual_start DATE,
    actual_end DATE,
    status TEXT NOT NULL,
    planned_volume NUMERIC,
    actual_volume NUMERIC,
    volume_unit TEXT,
    completion_percent INT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now() NOT NULL,
    version INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_project FOREIGN KEY(project_id) REFERENCES projects(id) ON DELETE CASCADE
);

CREATE TABLE project_work_stages (
    id UUID PRIMARY KEY,
    work_id UUID NOT NULL,
    name TEXT NOT NULL,
    order_number INT NOT NULL,
    status TEXT NOT NULL,
    date DATE,
    version INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_work FOREIGN KEY(work_id) REFERENCES project_works(id) ON DELETE CASCADE
);

CREATE TABLE project_work_comments (
    id UUID PRIMARY KEY,
    work_id UUID NOT NULL REFERENCES project_works(id) ON DELETE CASCADE,
    text TEXT NOT NULL,
    author_id UUID,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    version INT NOT NULL DEFAULT 0
);

CREATE TABLE project_work_comment_files (
    comment_id UUID NOT NULL REFERENCES project_work_comments(id) ON DELETE CASCADE,
    file_id UUID NOT NULL,
    PRIMARY KEY(comment_id, file_id)
);

CREATE INDEX idx_project_works_project_id ON project_works(project_id);
CREATE INDEX idx_project_work_stages_work_id ON project_work_stages(work_id);
CREATE INDEX idx_project_work_stages_order_number ON project_work_stages(work_id, order_number);
