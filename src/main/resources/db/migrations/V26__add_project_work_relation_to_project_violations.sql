ALTER TABLE project_violations
    ADD COLUMN project_work_id UUID;

ALTER TABLE project_violations
    ADD CONSTRAINT fk_project_violations_work
        FOREIGN KEY (project_work_id) REFERENCES project_works(id) ON DELETE SET NULL;
