CREATE TABLE checklist_templates (
    id UUID PRIMARY KEY,
    type TEXT NOT NULL UNIQUE,
    title TEXT NOT NULL,
    version INT NOT NULL DEFAULT 0
);

CREATE TABLE template_sections (
    id UUID PRIMARY KEY,
    template_id UUID NOT NULL REFERENCES checklist_templates(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    order_index INT NOT NULL,
    version INT NOT NULL DEFAULT 0
);

CREATE TABLE template_items (
    id UUID PRIMARY KEY,
    section_id UUID NOT NULL REFERENCES template_sections(id) ON DELETE CASCADE,
    item_number TEXT NOT NULL,
    text TEXT NOT NULL,
    order_index INT NOT NULL,
    required BOOLEAN NOT NULL,
    version INT NOT NULL DEFAULT 0
);

CREATE TABLE checklist_instances (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    type TEXT NOT NULL,
    template_id UUID NOT NULL REFERENCES checklist_templates(id) ON DELETE CASCADE,
    check_date DATE,
    status TEXT,
    version INT NOT NULL DEFAULT 0
);

CREATE TABLE checklist_item_answers (
    id UUID PRIMARY KEY,
    instance_id UUID NOT NULL REFERENCES checklist_instances(id) ON DELETE CASCADE,
    template_item_id UUID NOT NULL REFERENCES template_items(id) ON DELETE CASCADE,
    answer TEXT,
    version INT NOT NULL DEFAULT 0,
    CONSTRAINT uq_instance_template_item UNIQUE(instance_id, template_item_id)
);


ALTER TABLE projects
    ADD COLUMN opening_checklist_id UUID;

ALTER TABLE projects
    ADD CONSTRAINT fk_project_opening_checklist
        FOREIGN KEY (opening_checklist_id) REFERENCES checklist_instances(id) ON DELETE SET NULL;
