CREATE TABLE device_tokens (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    device_id VARCHAR(255) NOT NULL,
    platform VARCHAR(10) NOT NULL,
    push_token VARCHAR(512) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    registered_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_device_tokens_user_id ON device_tokens(user_id);

CREATE TABLE notification_logs (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    offer_id VARCHAR(255),
    type VARCHAR(50),
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    deep_link VARCHAR(512),
    status VARCHAR(20) NOT NULL DEFAULT 'QUEUED',
    sent_at TIMESTAMP WITH TIME ZONE,
    failure_reason TEXT
);

CREATE INDEX idx_notification_logs_user_id ON notification_logs(user_id);
