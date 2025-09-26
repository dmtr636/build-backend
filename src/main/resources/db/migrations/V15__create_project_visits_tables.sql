CREATE TABLE project_visits (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    visit_date TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    version INT NOT NULL DEFAULT 0
);

CREATE TABLE visit_violations (
    visit_id UUID NOT NULL REFERENCES project_visits(id) ON DELETE CASCADE,
    violation_id UUID NOT NULL REFERENCES project_violations(id) ON DELETE CASCADE,
    PRIMARY KEY (visit_id, violation_id)
);

CREATE TABLE visit_works (
    visit_id UUID NOT NULL REFERENCES project_visits(id) ON DELETE CASCADE,
    work_id UUID NOT NULL REFERENCES project_works(id) ON DELETE CASCADE,
    PRIMARY KEY (visit_id, work_id)
);