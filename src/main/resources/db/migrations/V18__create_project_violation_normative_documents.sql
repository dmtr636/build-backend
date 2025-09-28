CREATE TABLE IF NOT EXISTS project_violation_normative_documents (
    violation_id UUID NOT NULL REFERENCES project_violations (id) ON DELETE CASCADE,
    document_id UUID NOT NULL REFERENCES normative_documents (id) ON DELETE CASCADE,
    PRIMARY KEY (violation_id, document_id)
);
