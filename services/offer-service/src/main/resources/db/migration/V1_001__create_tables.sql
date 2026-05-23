CREATE TABLE offers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    campaign_id UUID,
    merchant_id UUID,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    terms TEXT,
    image_url VARCHAR(512),
    discount_type VARCHAR(20) NOT NULL,
    discount_value NUMERIC(10,2),
    zone_id VARCHAR(100),
    merchant_name VARCHAR(255),
    center_lat DOUBLE PRECISION,
    center_lng DOUBLE PRECISION,
    max_redemptions INTEGER,
    max_per_user INTEGER,
    redemption_count INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    valid_from TIMESTAMP WITH TIME ZONE,
    valid_to TIMESTAMP WITH TIME ZONE
);

CREATE TABLE offer_target_categories (
    offer_id UUID REFERENCES offers(id),
    category VARCHAR(100)
);

CREATE TABLE redemptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    offer_id UUID REFERENCES offers(id),
    user_id UUID NOT NULL,
    redemption_token UUID UNIQUE NOT NULL,
    manual_code VARCHAR(10),
    status VARCHAR(20) DEFAULT 'PENDING',
    token_expires_at TIMESTAMP WITH TIME ZONE,
    confirmed_at TIMESTAMP WITH TIME ZONE,
    savings_amount NUMERIC(10,2)
);

CREATE TABLE ratings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    offer_id UUID REFERENCES offers(id),
    user_id UUID NOT NULL,
    score INTEGER CHECK (score BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE reports (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    offer_id UUID REFERENCES offers(id),
    user_id UUID NOT NULL,
    reason VARCHAR(255),
    details TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE share_links (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    offer_id UUID REFERENCES offers(id),
    user_id UUID NOT NULL,
    short_code VARCHAR(20) UNIQUE NOT NULL,
    click_count INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_offers_location ON offers(center_lat, center_lng);
CREATE INDEX idx_offers_status ON offers(status);
CREATE INDEX idx_redemptions_offer_user ON redemptions(offer_id, user_id);
CREATE INDEX idx_redemptions_token ON redemptions(redemption_token);
