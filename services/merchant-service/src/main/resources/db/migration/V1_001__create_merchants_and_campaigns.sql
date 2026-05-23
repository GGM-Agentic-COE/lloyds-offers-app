CREATE TABLE merchants (
    id UUID PRIMARY KEY,
    business_name VARCHAR(255) NOT NULL,
    registration_number VARCHAR(100),
    business_type VARCHAR(100),
    address TEXT,
    contact_email VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    rejection_reason TEXT,
    password_hash VARCHAR(255),
    approved_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE merchant_document_urls (
    merchant_id UUID NOT NULL REFERENCES merchants(id),
    url TEXT NOT NULL
);

CREATE TABLE campaigns (
    id UUID PRIMARY KEY,
    merchant_id UUID NOT NULL REFERENCES merchants(id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    terms TEXT,
    image_url TEXT,
    discount_type VARCHAR(50),
    discount_value NUMERIC(10,2),
    active_hours_start TIME,
    active_hours_end TIME,
    max_redemptions INTEGER,
    max_per_user INTEGER,
    valid_from TIMESTAMP WITH TIME ZONE,
    valid_to TIMESTAMP WITH TIME ZONE,
    status VARCHAR(20) NOT NULL,
    published_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE campaign_zone_ids (
    campaign_id UUID NOT NULL REFERENCES campaigns(id),
    zone_id VARCHAR(100) NOT NULL
);

CREATE TABLE campaign_target_categories (
    campaign_id UUID NOT NULL REFERENCES campaigns(id),
    category VARCHAR(100) NOT NULL
);
