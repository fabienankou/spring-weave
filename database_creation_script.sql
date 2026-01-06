-- =====================================================
-- SCRIPT DE CRÉATION DE BASE DE DONNÉES
-- Plateforme E-Commerce Cross-Border & Fintech
-- Version: 1.0
-- Database: PostgreSQL 14+
-- =====================================================

-- Activation des extensions nécessaires
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =====================================================
-- TABLES PRINCIPALES
-- =====================================================

-- Table: Customers (Clients)
CREATE TABLE customers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    kyc_status VARCHAR(50) DEFAULT 'pending' CHECK (kyc_status IN ('pending', 'in_progress', 'verified', 'rejected')),
    credit_score INTEGER DEFAULT 0 CHECK (credit_score >= 0 AND credit_score <= 1000),
    credit_limit DECIMAL(15, 2) DEFAULT 0.00,
    available_credit DECIMAL(15, 2) DEFAULT 0.00,
    is_premium BOOLEAN DEFAULT FALSE,
    premium_expiry TIMESTAMP,
    country VARCHAR(3) DEFAULT 'TG',
    city VARCHAR(100),
    address TEXT,
    date_of_birth DATE,
    gender VARCHAR(10),
    occupation VARCHAR(100),
    monthly_income DECIMAL(15, 2),
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index pour les recherches fréquentes
CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_customers_phone ON customers(phone);
CREATE INDEX idx_customers_kyc_status ON customers(kyc_status);
CREATE INDEX idx_customers_is_premium ON customers(is_premium);

-- Table: Vendors (Fournisseurs)
CREATE TABLE vendors (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    api_key VARCHAR(255) UNIQUE,
    vendor_score INTEGER DEFAULT 0 CHECK (vendor_score >= 0 AND vendor_score <= 100),
    country VARCHAR(3) NOT NULL,
    type VARCHAR(50) CHECK (type IN ('international', 'local', 'artisan')),
    is_certified BOOLEAN DEFAULT FALSE,
    business_registration VARCHAR(100),
    tax_id VARCHAR(100),
    bank_account VARCHAR(100),
    contact_person VARCHAR(255),
    website VARCHAR(255),
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_vendors_type ON vendors(type);
CREATE INDEX idx_vendors_country ON vendors(country);
CREATE INDEX idx_vendors_is_active ON vendors(is_active);

-- Table: Products (Produits)
CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vendor_id UUID NOT NULL REFERENCES vendors(id) ON DELETE CASCADE,
    sku VARCHAR(100) UNIQUE NOT NULL,
    external_product_id VARCHAR(255),
    name VARCHAR(500) NOT NULL,
    description TEXT,
    category VARCHAR(100) NOT NULL,
    subcategory VARCHAR(100),
    price DECIMAL(15, 2) NOT NULL CHECK (price >= 0),
    currency VARCHAR(3) DEFAULT 'XOF',
    cost_price DECIMAL(15, 2),
    origin VARCHAR(100),
    condition VARCHAR(50) DEFAULT 'new' CHECK (condition IN ('new', 'used', 'refurbished')),
    is_available BOOLEAN DEFAULT TRUE,
    stock_status VARCHAR(50) DEFAULT 'in_stock' CHECK (stock_status IN ('in_stock', 'out_of_stock', 'on_demand')),
    weight_kg DECIMAL(10, 2),
    dimensions_cm VARCHAR(50),
    brand VARCHAR(100),
    model VARCHAR(100),
    specifications JSONB,
    images JSONB,
    is_credit_eligible BOOLEAN DEFAULT TRUE,
    min_credit_duration INTEGER DEFAULT 3,
    max_credit_duration INTEGER DEFAULT 12,
    views_count INTEGER DEFAULT 0,
    orders_count INTEGER DEFAULT 0,
    average_rating DECIMAL(3, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_vendor_id ON products(vendor_id);
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_sku ON products(sku);
CREATE INDEX idx_products_is_available ON products(is_available);
CREATE INDEX idx_products_price ON products(price);

-- Table: Orders (Commandes)
CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    status VARCHAR(50) DEFAULT 'pending' CHECK (status IN ('pending', 'confirmed', 'processing', 'shipped', 'in_transit', 'customs', 'out_for_delivery', 'delivered', 'cancelled', 'refunded')),
    payment_status VARCHAR(50) DEFAULT 'pending' CHECK (payment_status IN ('pending', 'partial', 'paid', 'failed', 'refunded')),
    payment_method VARCHAR(50) CHECK (payment_method IN ('credit', 'mobile_money', 'card', 'bank_transfer', 'cash')),
    subtotal DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    shipping_cost DECIMAL(15, 2) DEFAULT 0.00,
    customs_cost DECIMAL(15, 2) DEFAULT 0.00,
    insurance_cost DECIMAL(15, 2) DEFAULT 0.00,
    tax_amount DECIMAL(15, 2) DEFAULT 0.00,
    discount_amount DECIMAL(15, 2) DEFAULT 0.00,
    total_amount DECIMAL(15, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'XOF',
    shipping_address TEXT NOT NULL,
    shipping_city VARCHAR(100),
    shipping_country VARCHAR(3),
    billing_address TEXT,
    tracking_number VARCHAR(100),
    delivery_instructions TEXT,
    customer_notes TEXT,
    admin_notes TEXT,
    credit_application_id UUID,
    estimated_delivery_date DATE,
    delivered_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    cancellation_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_order_number ON orders(order_number);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_payment_status ON orders(payment_status);
CREATE INDEX idx_orders_created_at ON orders(created_at);

-- Table: Order Items (Articles de commande)
CREATE TABLE order_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE RESTRICT,
    product_name VARCHAR(500) NOT NULL,
    product_sku VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(15, 2) NOT NULL,
    subtotal DECIMAL(15, 2) NOT NULL,
    discount DECIMAL(15, 2) DEFAULT 0.00,
    tax_amount DECIMAL(15, 2) DEFAULT 0.00,
    total DECIMAL(15, 2) NOT NULL,
    vendor_id UUID NOT NULL REFERENCES vendors(id),
    product_snapshot JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);

-- Table: Credit Applications (Demandes de crédit)
CREATE TABLE credit_applications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    order_id UUID REFERENCES orders(id) ON DELETE SET NULL,
    application_number VARCHAR(50) UNIQUE NOT NULL,
    amount DECIMAL(15, 2) NOT NULL CHECK (amount > 0),
    interest_rate DECIMAL(5, 2) NOT NULL CHECK (interest_rate >= 0),
    duration_months INTEGER NOT NULL CHECK (duration_months BETWEEN 1 AND 24),
    monthly_payment DECIMAL(15, 2) NOT NULL,
    total_interest DECIMAL(15, 2) NOT NULL,
    total_to_repay DECIMAL(15, 2) NOT NULL,
    processing_fee DECIMAL(15, 2) DEFAULT 0.00,
    status VARCHAR(50) DEFAULT 'pending' CHECK (status IN ('pending', 'under_review', 'approved', 'rejected', 'active', 'completed', 'defaulted', 'cancelled')),
    credit_score_at_application INTEGER,
    risk_category VARCHAR(50) CHECK (risk_category IN ('low', 'medium', 'high')),
    scoring_details JSONB,
    approval_conditions TEXT,
    first_payment_date DATE,
    last_payment_date DATE,
    approved_at TIMESTAMP,
    approved_by VARCHAR(255),
    rejected_at TIMESTAMP,
    rejection_reason TEXT,
    disbursed_at TIMESTAMP,
    completed_at TIMESTAMP,
    defaulted_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_credit_applications_customer_id ON credit_applications(customer_id);
CREATE INDEX idx_credit_applications_order_id ON credit_applications(order_id);
CREATE INDEX idx_credit_applications_status ON credit_applications(status);
CREATE INDEX idx_credit_applications_application_number ON credit_applications(application_number);

-- Ajouter la contrainte de clé étrangère pour orders
ALTER TABLE orders 
ADD CONSTRAINT fk_orders_credit_application 
FOREIGN KEY (credit_application_id) 
REFERENCES credit_applications(id) ON DELETE SET NULL;

-- Table: Repayment Schedule (Échéancier de remboursement)
CREATE TABLE repayment_schedules (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    credit_application_id UUID NOT NULL REFERENCES credit_applications(id) ON DELETE CASCADE,
    installment_number INTEGER NOT NULL CHECK (installment_number > 0),
    due_date DATE NOT NULL,
    principal_amount DECIMAL(15, 2) NOT NULL,
    interest_amount DECIMAL(15, 2) NOT NULL,
    amount_due DECIMAL(15, 2) NOT NULL,
    amount_paid DECIMAL(15, 2) DEFAULT 0.00,
    outstanding_balance DECIMAL(15, 2) NOT NULL,
    status VARCHAR(50) DEFAULT 'pending' CHECK (status IN ('pending', 'paid', 'partially_paid', 'late', 'defaulted')),
    paid_at TIMESTAMP,
    late_fee DECIMAL(15, 2) DEFAULT 0.00,
    days_late INTEGER DEFAULT 0,
    payment_method VARCHAR(50),
    transaction_id UUID,
    reminder_sent_at TIMESTAMP,
    reminder_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(credit_application_id, installment_number)
);

CREATE INDEX idx_repayment_schedules_credit_application_id ON repayment_schedules(credit_application_id);
CREATE INDEX idx_repayment_schedules_due_date ON repayment_schedules(due_date);
CREATE INDEX idx_repayment_schedules_status ON repayment_schedules(status);

-- Table: Transactions (Paiements)
CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    order_id UUID REFERENCES orders(id) ON DELETE SET NULL,
    repayment_schedule_id UUID REFERENCES repayment_schedules(id) ON DELETE SET NULL,
    transaction_number VARCHAR(50) UNIQUE NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN ('payment', 'refund', 'credit_disbursement', 'credit_repayment', 'late_fee', 'premium_subscription')),
    amount DECIMAL(15, 2) NOT NULL CHECK (amount > 0),
    currency VARCHAR(3) DEFAULT 'XOF',
    gateway VARCHAR(50) CHECK (gateway IN ('tmoney', 'flooz', 'visa', 'mastercard', 'bank_transfer', 'cash', 'internal')),
    gateway_reference VARCHAR(255),
    status VARCHAR(50) DEFAULT 'pending' CHECK (status IN ('pending', 'processing', 'completed', 'failed', 'cancelled', 'refunded')),
    failure_reason TEXT,
    metadata JSONB,
    ip_address VARCHAR(45),
    user_agent TEXT,
    processed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_transactions_customer_id ON transactions(customer_id);
CREATE INDEX idx_transactions_order_id ON transactions(order_id);
CREATE INDEX idx_transactions_transaction_number ON transactions(transaction_number);
CREATE INDEX idx_transactions_status ON transactions(status);
CREATE INDEX idx_transactions_type ON transactions(type);
CREATE INDEX idx_transactions_created_at ON transactions(created_at);

-- Table: KYC Documents (Documents de vérification d'identité)
CREATE TABLE kyc_documents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    document_type VARCHAR(50) NOT NULL CHECK (document_type IN ('national_id', 'passport', 'driver_license', 'residence_permit', 'utility_bill', 'bank_statement', 'selfie', 'video_selfie')),
    document_number VARCHAR(100),
    file_path VARCHAR(500) NOT NULL,
    file_name VARCHAR(255),
    file_size INTEGER,
    mime_type VARCHAR(100),
    verification_status VARCHAR(50) DEFAULT 'pending' CHECK (verification_status IN ('pending', 'verified', 'rejected', 'expired')),
    verified_at TIMESTAMP,
    verified_by VARCHAR(255),
    rejection_reason TEXT,
    expiry_date DATE,
    issuing_country VARCHAR(3),
    metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_kyc_documents_customer_id ON kyc_documents(customer_id);
CREATE INDEX idx_kyc_documents_verification_status ON kyc_documents(verification_status);

-- Table: Scoring Data (Données de scoring)
CREATE TABLE scoring_data (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    data_source VARCHAR(100) NOT NULL CHECK (data_source IN ('platform_history', 'mobile_money', 'employment', 'kyc', 'behavioral', 'social', 'bank_api', 'utility_bills')),
    data_type VARCHAR(100) NOT NULL,
    data_content JSONB NOT NULL,
    score_contribution INTEGER DEFAULT 0,
    weight DECIMAL(5, 2) DEFAULT 0.00,
    is_verified BOOLEAN DEFAULT FALSE,
    collected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_scoring_data_customer_id ON scoring_data(customer_id);
CREATE INDEX idx_scoring_data_data_source ON scoring_data(data_source);
CREATE INDEX idx_scoring_data_collected_at ON scoring_data(collected_at);

-- Table: Insurance Policies (Polices d'assurance)
CREATE TABLE insurance_policies (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    policy_number VARCHAR(50) UNIQUE NOT NULL,
    policy_type VARCHAR(50) DEFAULT 'credit' CHECK (policy_type IN ('credit', 'product', 'shipping')),
    coverage_amount DECIMAL(15, 2) NOT NULL,
    premium_amount DECIMAL(15, 2) NOT NULL,
    premium_frequency VARCHAR(50) DEFAULT 'monthly' CHECK (premium_frequency IN ('monthly', 'quarterly', 'annually')),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(50) DEFAULT 'active' CHECK (status IN ('pending', 'active', 'expired', 'cancelled', 'claimed')),
    terms_and_conditions TEXT,
    insurance_provider VARCHAR(255),
    provider_policy_number VARCHAR(100),
    beneficiary_name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_insurance_policies_customer_id ON insurance_policies(customer_id);
CREATE INDEX idx_insurance_policies_status ON insurance_policies(status);
CREATE INDEX idx_insurance_policies_policy_number ON insurance_policies(policy_number);

-- Table: Fraud Scores (Scores de fraude)
CREATE TABLE fraud_scores (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID REFERENCES customers(id) ON DELETE SET NULL,
    order_id UUID REFERENCES orders(id) ON DELETE CASCADE,
    transaction_id UUID REFERENCES transactions(id) ON DELETE SET NULL,
    risk_score INTEGER NOT NULL CHECK (risk_score >= 0 AND risk_score <= 100),
    risk_level VARCHAR(50) CHECK (risk_level IN ('low', 'medium', 'high', 'critical')),
    risk_factors JSONB,
    ip_address VARCHAR(45),
    device_fingerprint VARCHAR(255),
    geolocation JSONB,
    velocity_check JSONB,
    blacklist_check BOOLEAN DEFAULT FALSE,
    action_taken VARCHAR(50) CHECK (action_taken IN ('approved', 'flagged', 'blocked', 'manual_review')),
    manual_review_required BOOLEAN DEFAULT FALSE,
    reviewed_by VARCHAR(255),
    reviewed_at TIMESTAMP,
    review_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_fraud_scores_customer_id ON fraud_scores(customer_id);
CREATE INDEX idx_fraud_scores_order_id ON fraud_scores(order_id);
CREATE INDEX idx_fraud_scores_risk_level ON fraud_scores(risk_level);
CREATE INDEX idx_fraud_scores_manual_review_required ON fraud_scores(manual_review_required);

-- Table: Logistics Partners (Partenaires logistiques)
CREATE TABLE logistics_partners (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN ('freight_forwarder', 'customs_broker', 'last_mile_delivery', 'warehouse', 'full_service')),
    country VARCHAR(3) NOT NULL,
    city VARCHAR(100),
    contact_email VARCHAR(255) NOT NULL,
    contact_phone VARCHAR(20),
    contact_person VARCHAR(255),
    api_endpoint VARCHAR(500),
    api_key VARCHAR(255),
    performance_score INTEGER DEFAULT 0 CHECK (performance_score >= 0 AND performance_score <= 100),
    average_delivery_time_days INTEGER,
    success_rate DECIMAL(5, 2),
    contract_start_date DATE,
    contract_end_date DATE,
    pricing_model VARCHAR(50),
    base_rate DECIMAL(15, 2),
    is_active BOOLEAN DEFAULT TRUE,
    capabilities JSONB,
    service_areas JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_logistics_partners_type ON logistics_partners(type);
CREATE INDEX idx_logistics_partners_country ON logistics_partners(country);
CREATE INDEX idx_logistics_partners_is_active ON logistics_partners(is_active);

-- Table: Shipping Tracking (Suivi logistique)
CREATE TABLE shipping_trackings (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    tracking_number VARCHAR(100) UNIQUE NOT NULL,
    logistics_partner_id UUID REFERENCES logistics_partners(id) ON DELETE SET NULL,
    current_status VARCHAR(100) NOT NULL,
    current_location VARCHAR(255),
    origin_country VARCHAR(3),
    origin_city VARCHAR(100),
    destination_country VARCHAR(3),
    destination_city VARCHAR(100),
    estimated_delivery_date DATE,
    actual_delivery_date DATE,
    shipping_method VARCHAR(50),
    weight_kg DECIMAL(10, 2),
    dimensions_cm VARCHAR(50),
    customs_info JSONB,
    customs_status VARCHAR(50),
    customs_cleared_at TIMESTAMP,
    customs_duty_paid DECIMAL(15, 2),
    carrier_name VARCHAR(255),
    carrier_tracking_url VARCHAR(500),
    last_updated_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_shipping_trackings_order_id ON shipping_trackings(order_id);
CREATE INDEX idx_shipping_trackings_tracking_number ON shipping_trackings(tracking_number);
CREATE INDEX idx_shipping_trackings_current_status ON shipping_trackings(current_status);

-- Table: Tracking Events (Événements de suivi)
CREATE TABLE tracking_events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    shipping_tracking_id UUID NOT NULL REFERENCES shipping_trackings(id) ON DELETE CASCADE,
    event_type VARCHAR(100) NOT NULL,
    event_description TEXT NOT NULL,
    location VARCHAR(255),
    event_timestamp TIMESTAMP NOT NULL,
    metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_tracking_events_shipping_tracking_id ON tracking_events(shipping_tracking_id);
CREATE INDEX idx_tracking_events_event_timestamp ON tracking_events(event_timestamp);

-- Table: Notifications
CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID REFERENCES customers(id) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL CHECK (type IN ('order_confirmation', 'payment_reminder', 'delivery_update', 'credit_approved', 'credit_rejected', 'kyc_status', 'promotional', 'system')),
    channel VARCHAR(50) NOT NULL CHECK (channel IN ('email', 'sms', 'push', 'in_app')),
    subject VARCHAR(500),
    content TEXT NOT NULL,
    template_id VARCHAR(100),
    variables JSONB,
    priority VARCHAR(20) DEFAULT 'normal' CHECK (priority IN ('low', 'normal', 'high', 'urgent')),
    is_sent BOOLEAN DEFAULT FALSE,
    sent_at TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP,
    error_message TEXT,
    retry_count INTEGER DEFAULT 0,
    metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_notifications_customer_id ON notifications(customer_id);
CREATE INDEX idx_notifications_type ON notifications(type);
CREATE INDEX idx_notifications_is_sent ON notifications(is_sent);
CREATE INDEX idx_notifications_created_at ON notifications(created_at);

-- Table: Product Reviews (Avis clients)
CREATE TABLE product_reviews (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    order_id UUID REFERENCES orders(id) ON DELETE SET NULL,
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    title VARCHAR(255),
    comment TEXT,
    is_verified_purchase BOOLEAN DEFAULT FALSE,
    is_approved BOOLEAN DEFAULT FALSE,
    approved_at TIMESTAMP,
    helpful_count INTEGER DEFAULT 0,
    not_helpful_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_product_reviews_product_id ON product_reviews(product_id);
CREATE INDEX idx_product_reviews_customer_id ON product_reviews(customer_id);
CREATE INDEX idx_product_reviews_rating ON product_reviews(rating);

-- Table: Admin Users (Utilisateurs administrateurs)
CREATE TABLE admin_users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('super_admin', 'admin', 'finance', 'customer_support', 'logistics', 'marketing')),
    permissions JSONB,
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP,
    two_factor_enabled BOOLEAN DEFAULT FALSE,
    two_factor_secret VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_admin_users_email ON admin_users(email);
CREATE INDEX idx_admin_users_role ON admin_users(role);

-- Table: Audit Logs (Journaux d'audit)
CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID,
    user_type VARCHAR(50) CHECK (user_type IN ('customer', 'admin', 'vendor', 'system')),
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100),
    entity_id UUID,
    old_values JSONB,
    new_values JSONB,
    ip_address VARCHAR(45),
    user_agent TEXT,
    metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_entity_type ON audit_logs(entity_type);
CREATE INDEX idx_audit_logs_entity_id ON audit_logs(entity_id);
CREATE INDEX idx_audit_logs_action ON audit_logs(action);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);

-- Table: System Settings (Paramètres système)
CREATE TABLE system_settings (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value TEXT NOT NULL,
    setting_type VARCHAR(50) CHECK (setting_type IN ('string', 'number', 'boolean', 'json')),
    description TEXT,
    is_public BOOLEAN DEFAULT FALSE,
    updated_by UUID REFERENCES admin_users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_system_settings_setting_key ON system_settings(setting_key);

-- =====================================================
-- TRIGGERS POUR UPDATED_AT
-- =====================================================

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Application des triggers sur toutes les tables avec updated_at
CREATE TRIGGER update_customers_updated_at BEFORE UPDATE ON customers FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_vendors_updated_at BEFORE UPDATE ON vendors FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_products_updated_at BEFORE UPDATE ON products FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_orders_updated_at BEFORE UPDATE ON orders FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_credit_applications_updated_at BEFORE UPDATE ON credit_applications FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_repayment_schedules_updated_at BEFORE UPDATE ON repayment_schedules FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_transactions_updated_at BEFORE UPDATE ON transactions FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_kyc_documents_updated_at BEFORE UPDATE ON kyc_documents FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_insurance_policies_updated_at BEFORE UPDATE ON insurance_policies FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_logistics_partners_updated_at BEFORE UPDATE ON logistics_partners FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_shipping_trackings_updated_at BEFORE UPDATE ON shipping_trackings FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_product_reviews_updated_at BEFORE UPDATE ON product_reviews FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_admin_users_updated_at BEFORE UPDATE ON admin_users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_system_settings_updated_at BEFORE UPDATE ON system_settings FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
-- =====================================================
-- VUES UTILES
-- =====================================================

-- Vue: Statistiques clients
CREATE VIEW customer_statistics AS
SELECT 
    c.id,
    c.name,
    c.email,
    c.kyc_status,
    c.credit_score,
    c.credit_limit,
    c.is_premium,
    COUNT(DISTINCT o.id) AS total_orders,
    COALESCE(SUM(o.total_amount), 0) AS total_spent,
    COUNT(DISTINCT ca.id) AS total_credit_applications,
    COUNT(DISTINCT CASE WHEN ca.status = 'active' THEN ca.id END) AS active_credits,
    COALESCE(SUM(CASE WHEN ca.status = 'active' THEN ca.amount END), 0) AS active_credit_amount
FROM customers c
LEFT JOIN orders o ON c.id = o.customer_id AND o.status != 'cancelled'
LEFT JOIN credit_applications ca ON c.id = ca.customer_id
GROUP BY c.id;

-- Vue: Performance des vendeurs
CREATE VIEW vendor_performance AS
SELECT 
    v.id,
    v.name,
    v.type,
    v.vendor_score,
    COUNT(DISTINCT p.id) AS total_products,
    COUNT(DISTINCT oi.order_id) AS total_orders,
    COALESCE(SUM(oi.total), 0) AS total_revenue,
    AVG(pr.rating) AS average_rating,
    COUNT(pr.id) AS review_count
FROM vendors v
LEFT JOIN products p ON v.id = p.vendor_id
LEFT JOIN order_items oi ON p.id = oi.product_id
LEFT JOIN product_reviews pr ON p.id = pr.product_id
GROUP BY v.id;

-- Vue: Crédit en cours
CREATE VIEW active_credits_overview AS
SELECT 
    ca.id,