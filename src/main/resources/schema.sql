-- Set timezone to UTC for all timestamps
SET TIME ZONE 'UTC';

-- Users table for Google OAuth authentication
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    google_sub VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Profiles table with foreign key to users
CREATE TABLE IF NOT EXISTS profiles (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    nickname VARCHAR(50) NOT NULL,
    avatar_url TEXT,
    bio VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_profiles_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
