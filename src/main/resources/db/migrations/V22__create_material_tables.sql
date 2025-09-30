CREATE TABLE project_materials (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    version INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_project_materials_project FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE
);

CREATE TABLE waybills (
    id UUID PRIMARY KEY,
    material_id UUID NOT NULL UNIQUE,
    material_name TEXT,
    receiver TEXT,
    delivery_date_time TIMESTAMPTZ,
    project_work_id UUID,
    invoice_number TEXT,
    volume DOUBLE PRECISION,
    net_weight DOUBLE PRECISION,
    gross_weight DOUBLE PRECISION,
    package_count INT,
    version INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_waybills_material FOREIGN KEY (material_id) REFERENCES project_materials (id) ON DELETE CASCADE,
    CONSTRAINT fk_waybills_project_work FOREIGN KEY (project_work_id) REFERENCES project_works (id) ON DELETE SET NULL
);

CREATE TABLE passport_qualities (
    id UUID PRIMARY KEY,
    material_id UUID NOT NULL UNIQUE,
    manufacturer TEXT NOT NULL,
    consumer_name_and_address TEXT NOT NULL,
    contract_number TEXT,
    product_name_and_grade TEXT NOT NULL,
    batch_number TEXT,
    batch_count INT,
    manufacture_date DATE,
    shipped_quantity INT,
    lab_chief TEXT,
    version INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_passport_quality_material FOREIGN KEY (material_id) REFERENCES project_materials (id) ON DELETE CASCADE
);

CREATE TABLE waybill_files (
    waybill_id UUID NOT NULL REFERENCES waybills (id) ON DELETE CASCADE,
    file_id UUID NOT NULL REFERENCES files (id) ON DELETE CASCADE,
    PRIMARY KEY (waybill_id, file_id)
);

CREATE TABLE waybill_images (
    waybill_id UUID NOT NULL REFERENCES waybills (id) ON DELETE CASCADE,
    file_id UUID NOT NULL REFERENCES files (id) ON DELETE CASCADE,
    PRIMARY KEY (waybill_id, file_id)
);

CREATE TABLE passport_quality_files (
    passport_quality_id UUID NOT NULL  REFERENCES passport_qualities (id) ON DELETE CASCADE,
    file_id UUID NOT NULL REFERENCES files (id) ON DELETE CASCADE,
    PRIMARY KEY (passport_quality_id, file_id)
);

CREATE TABLE passport_quality_images (
    passport_quality_id UUID NOT NULL REFERENCES passport_qualities (id) ON DELETE CASCADE,
    file_id UUID NOT NULL REFERENCES files (id) ON DELETE CASCADE,
    PRIMARY KEY (passport_quality_id, file_id)
);
