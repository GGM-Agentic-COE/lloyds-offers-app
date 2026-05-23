CREATE TABLE user_profiles (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL UNIQUE,
    display_name VARCHAR(255),
    budget_sensitivity VARCHAR(10) NOT NULL DEFAULT 'MEDIUM',
    preferred_radius_meters INT NOT NULL DEFAULT 5000,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE user_profile_categories (
    profile_id VARCHAR(36) NOT NULL REFERENCES user_profiles(id) ON DELETE CASCADE,
    category VARCHAR(100) NOT NULL
);

CREATE TABLE notification_preferences (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL UNIQUE,
    mode VARCHAR(20) NOT NULL DEFAULT 'ALWAYS',
    quiet_hours_start TIME,
    quiet_hours_end TIME,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE notification_pref_categories (
    pref_id VARCHAR(36) NOT NULL REFERENCES notification_preferences(id) ON DELETE CASCADE,
    category VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_user_profiles_user_id ON user_profiles(user_id);
CREATE INDEX idx_notification_preferences_user_id ON notification_preferences(user_id);
