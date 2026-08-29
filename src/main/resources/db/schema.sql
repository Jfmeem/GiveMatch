-- GiveMatch schema

CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    full_name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    phone TEXT,
    role TEXT NOT NULL CHECK (role IN ('DONOR', 'RECEIVER', 'ADMIN')),
    created_at TEXT NOT NULL DEFAULT (datetime('now'))
);

CREATE TABLE IF NOT EXISTS categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE IF NOT EXISTS donations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    donor_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    item_name TEXT NOT NULL,
    description TEXT,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    quantity_remaining INTEGER NOT NULL CHECK (quantity_remaining >= 0),
    status TEXT NOT NULL DEFAULT 'AVAILABLE'
        CHECK (status IN ('AVAILABLE', 'PARTIALLY_ALLOCATED', 'FULLY_ALLOCATED', 'COMPLETED')),
    posted_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (donor_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS need_requests (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    receiver_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    item_name TEXT NOT NULL,
    quantity_needed INTEGER NOT NULL CHECK (quantity_needed > 0),
    status TEXT NOT NULL DEFAULT 'OPEN'
        CHECK (status IN ('OPEN', 'MATCHED', 'CLOSED')),
    posted_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (receiver_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS requests (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    donation_id INTEGER NOT NULL,
    receiver_id INTEGER NOT NULL,
    quantity_requested INTEGER NOT NULL CHECK (quantity_requested > 0),
    status TEXT NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'COMPLETED')),
    requested_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (donation_id) REFERENCES donations(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS distributions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    request_id INTEGER NOT NULL UNIQUE,
    quantity_distributed INTEGER NOT NULL CHECK (quantity_distributed > 0),
    distributed_at TEXT NOT NULL DEFAULT (datetime('now')),
    notes TEXT,
    FOREIGN KEY (request_id) REFERENCES requests(id)
);

CREATE INDEX IF NOT EXISTS idx_donations_category ON donations(category_id);
CREATE INDEX IF NOT EXISTS idx_donations_status ON donations(status);
CREATE INDEX IF NOT EXISTS idx_requests_donation ON requests(donation_id);
CREATE INDEX IF NOT EXISTS idx_requests_status ON requests(status);
CREATE INDEX IF NOT EXISTS idx_need_requests_category ON need_requests(category_id);
