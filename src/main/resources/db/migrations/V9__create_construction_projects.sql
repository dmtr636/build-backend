CREATE TABLE projects (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    object_number TEXT UNIQUE,
    address_city TEXT,
    address_street TEXT,
    address_house TEXT,
    centroid_latitude DOUBLE PRECISION,
    centroid_longitude DOUBLE PRECISION,
    customer_organization_id UUID REFERENCES organizations(id),
    contractor_organization_id UUID REFERENCES organizations(id),
    last_inspection TIMESTAMPTZ,
    planned_start DATE,
    planned_end DATE,
    actual_start DATE,
    actual_end DATE,
    type TEXT,
    image_id TEXT,
    status TEXT,
    has_violations BOOLEAN,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now() NOT NULL,
    version INT NOT NULL DEFAULT 0
);

CREATE TABLE project_coordinates (
    project_id UUID REFERENCES projects(id) ON DELETE CASCADE NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (project_id, latitude, longitude)
);

CREATE TABLE project_users (
    project_id UUID REFERENCES projects(id) ON DELETE CASCADE NOT NULL,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    side TEXT NOT NULL,
    is_responsible BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY (project_id, user_id)
);

CREATE TABLE project_images (
    id UUID PRIMARY KEY,
    project_id UUID REFERENCES projects(id) ON DELETE CASCADE NOT NULL,
    file_id UUID REFERENCES files(id) ON DELETE CASCADE NOT NULL,
    caption TEXT,
    taken_at DATE,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    version INT NOT NULL DEFAULT 0
);

CREATE TABLE project_documents (
    id UUID PRIMARY KEY,
    project_id UUID REFERENCES projects(id) ON DELETE CASCADE NOT NULL,
    file_id UUID REFERENCES files(id) ON DELETE CASCADE NOT NULL,
    document_group TEXT,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    version INT NOT NULL DEFAULT 0
);
