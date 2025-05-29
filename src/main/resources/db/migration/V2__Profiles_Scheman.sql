-- Merchant Profile Details

CREATE TABLE merchant_profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    business_name VARCHAR(255) NOT NULL ,
    business_type VARCHAR(100),
    business_email VARCHAR(255),
    business_phone VARCHAR(20),
    business_address JSONB,
    tax_id VARCHAR(50) ,
    settlement_bank_account JSONB,
    webhook_url VARCHAR(500),
    webhook_secret VARCHAR(255) ,
    verified BOOLEAN DEFAULT false,
    verification_documents JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Api Keys for Merchant

CREATE TABLE api_keys (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    merchant_id UUID NOT NULL REFERENCES merchant_profiles(id) ON DELETE CASCADE ,
    key_name VARCHAR(100) NOT NULL ,
    public_key VARCHAR(255) UNIQUE NOT NULL ,
    secret_key_hash VARCHAR(255) NOT NULL,
    environment VARCHAR(20) CHECK ( environment IN ('PRODUCTION','TEST')) ,
    permissions JSONB,
    active BOOLEAN DEFAULT true,
    last_used TIMESTAMP WITH TIME ZONE ,
    expires_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


-- Payment processor Paystack

-- CREATE TABLE


CREATE TABLE customer_profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE SET NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20) ,
    first_name VARCHAR(100) ,
    last_name VARCHAR(100) ,
    address JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);