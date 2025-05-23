
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE TABLE users (
    id UUID PRIMARY KEY ,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL ,
    email VARCHAR(255) NOT NULL UNIQUE ,
    role VARCHAR(20) NOT NULL CHECK ( role in ('MERCHANT', 'ADMIN','DEVELOPER','CUSTOMER') ),
    is_active BOOLEAN DEFAULT true,
    email_verified BOOLEAN DEFAULT false,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP);



CREATE TABLE email_verification_token(
    id SERIAL PRIMARY KEY,
    token VARCHAR(255) ,
    user_id SERIAL REFERENCES users(id),
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    verified_at TIMESTAMP WITH TIME ZONE

);

CREATE TABLE refresh_token (
    id SERIAL PRIMARY KEY ,
    token VARCHAR(500) NOT NULL UNIQUE,
    issued_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked BOOLEAN DEFAULT false,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE
);


-- Indexes
CREATE INDEX idx_users_email_active_verified ON users(email,is_active,email_verified);

CREATE INDEX idx_users_role_active ON users(role,is_active);

CREATE INDEX idx_users_email_verified_role ON users(email_verified,role);

CREATE INDEX idx_users_active_created_at ON users(is_active,created_at);

CREATE INDEX idx_users_created_role ON users(created_at,role);



CREATE INDEX idx_token ON email_verification_token(token);

CREATE INDEX idx_user_id ON email_verification_token(user_id);

CREATE INDEX idx_expires_at ON email_verification_token(expires_at);