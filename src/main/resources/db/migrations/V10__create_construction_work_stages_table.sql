CREATE TABLE construction_work_stages (
    id UUID PRIMARY KEY,
    work_id UUID NOT NULL,
    stage_number INT NOT NULL,
    stage_name TEXT NOT NULL,
    version INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_work FOREIGN KEY (work_id) REFERENCES construction_works (id)
);

CREATE INDEX idx_construction_work_stages_work_id
    ON construction_work_stages (work_id);