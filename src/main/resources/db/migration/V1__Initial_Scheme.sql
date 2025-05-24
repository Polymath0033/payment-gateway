-- V1__Initial_schema.sql
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Users table
CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       role VARCHAR(20) NOT NULL CHECK (role IN ('MERCHANT', 'ADMIN', 'DEVELOPER', 'CUSTOMER')),
                       active BOOLEAN DEFAULT true,
                       email_verified BOOLEAN DEFAULT false,
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Email verification token table
CREATE TABLE email_verification_token (
                                          id SERIAL PRIMARY KEY,
                                          token VARCHAR(255) UNIQUE NOT NULL,
                                          user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                          expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                          created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                          verified_at TIMESTAMP WITH TIME ZONE
);

-- Refresh token table
CREATE TABLE refresh_token (
                               id SERIAL PRIMARY KEY,
                               token VARCHAR(500) NOT NULL UNIQUE,
                               issued_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                               expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
                               revoked BOOLEAN DEFAULT false,
                               user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

-- Indexes for users table
CREATE INDEX idx_users_email_active_verified ON users(email, active, email_verified);
CREATE INDEX idx_users_role_active ON users(role, active);
CREATE INDEX idx_users_email_verified_role ON users(email_verified, role);
CREATE INDEX idx_users_active_created_at ON users(active, created_at);
CREATE INDEX idx_users_created_role ON users(created_at, role);

-- Indexes for email_verification_token table
CREATE INDEX idx_email_verification_token ON email_verification_token(token);
CREATE INDEX idx_email_verification_user_id ON email_verification_token(user_id);
CREATE INDEX idx_email_verification_expires_at ON email_verification_token(expires_at);

-- Indexes for refresh_token table
CREATE INDEX idx_refresh_token_token ON refresh_token(token);
CREATE INDEX idx_refresh_token_user_id ON refresh_token(user_id);
CREATE INDEX idx_refresh_token_expires_at ON refresh_token(expires_at);