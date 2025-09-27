CREATE TABLE project_violation_resolution_photos (
    violation_id UUID NOT NULL REFERENCES project_violations(id) ON DELETE CASCADE,
    file_id UUID NOT NULL REFERENCES files(id) ON DELETE CASCADE,
    PRIMARY KEY (violation_id, file_id)
);

CREATE INDEX idx_project_violation_resolution_photos_violation
    ON project_violation_resolution_photos (violation_id);

CREATE INDEX idx_project_violation_resolution_photos_file
    ON project_violation_resolution_photos (file_id);
