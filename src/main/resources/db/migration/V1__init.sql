-- Note: enable pgcrypto extension for gen_random_uuid or manage UUIDs in app code
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS user_preferences (
    user_id UUID PRIMARY KEY,
    email_enabled BOOLEAN DEFAULT TRUE,
    sms_enabled BOOLEAN DEFAULT FALSE,
    whatsapp_enabled BOOLEAN DEFAULT FALSE,
    in_app_enabled BOOLEAN DEFAULT TRUE,
    email VARCHAR(255),
    phone VARCHAR(64)
);

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'status_enum') THEN
        CREATE TYPE status_enum AS ENUM ('PENDING','SENT','FAILED');
    END IF;
END$$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'channel_enum') THEN
        CREATE TYPE channel_enum AS ENUM ('EMAIL','SMS','WHATSAPP','IN_APP');
    END IF;
END$$;

CREATE TABLE IF NOT EXISTS notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(64) NOT NULL,
    channel channel_enum NOT NULL,
    status status_enum NOT NULL DEFAULT 'PENDING',
    content TEXT,
    retries INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW(),
    sent_at TIMESTAMP NULL
);
