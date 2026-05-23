CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE cities (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    country_code VARCHAR(2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE geofence_zones (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    merchant_id UUID NOT NULL,
    city_id UUID NOT NULL REFERENCES cities(id),
    center_lat DOUBLE PRECISION NOT NULL,
    center_lng DOUBLE PRECISION NOT NULL,
    radius_meters INTEGER NOT NULL CHECK (radius_meters BETWEEN 50 AND 5000),
    active_hours_start TIME,
    active_hours_end TIME,
    days_of_week VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE geofence_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    zone_id UUID NOT NULL REFERENCES geofence_zones(id),
    event_type VARCHAR(10) NOT NULL,
    accuracy_meters DOUBLE PRECISION,
    timestamp TIMESTAMPTZ NOT NULL,
    processed BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_zones_location ON geofence_zones USING GIST (
    ST_MakePoint(center_lng, center_lat)::geography
);

CREATE INDEX idx_zones_merchant ON geofence_zones(merchant_id);
CREATE INDEX idx_zones_city ON geofence_zones(city_id);
CREATE INDEX idx_events_user ON geofence_events(user_id);
CREATE INDEX idx_events_zone ON geofence_events(zone_id);
