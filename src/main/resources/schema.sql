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

-- Room posts table for user room photo submissions
CREATE TABLE IF NOT EXISTS room_posts (
    id UUID PRIMARY KEY,  -- UUIDv7をアプリケーション側で生成して挿入
    user_id UUID NOT NULL,
    title VARCHAR(100) NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_room_posts_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_room_posts_user_id ON room_posts(user_id);
CREATE INDEX IF NOT EXISTS idx_room_posts_id_desc ON room_posts(id DESC);  -- UUIDv7での新着順ソート用
