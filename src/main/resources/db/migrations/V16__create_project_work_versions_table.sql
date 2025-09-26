ALTER TABLE project_works
    DROP COLUMN planned_start,
    DROP COLUMN planned_end,
    DROP COLUMN actual_start,
    DROP COLUMN actual_end;

CREATE TABLE project_work_versions (
    id UUID PRIMARY KEY,
    work_id UUID NOT NULL REFERENCES project_works(id) ON DELETE CASCADE,
    version_number INT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    start_date TIMESTAMPTZ NOT NULL,
    end_date TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    version INT NOT NULL DEFAULT 0,
    CONSTRAINT uq_work_version_number UNIQUE(work_id, version_number)
);

CREATE INDEX idx_project_work_versions_work_active
    ON project_work_versions (work_id, active);