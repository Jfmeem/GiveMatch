-- Default categories
INSERT OR IGNORE INTO categories (name, description) VALUES
    ('Clothing', 'Clothes, shoes, and accessories'),
    ('Books', 'Textbooks, novels, and other reading material'),
    ('Educational Supplies', 'Notebooks, stationery, school supplies'),
    ('Food', 'Non-perishable food items'),
    ('Furniture', 'Household furniture'),
    ('Electronics', 'Working electronic items'),
    ('Household Items', 'General household goods'),
    ('Other', 'Anything that does not fit another category');

-- Sample users (password_hash values are placeholders — replace with real hashes when AuthService is built)
INSERT OR IGNORE INTO users (id, name, username, hashed_password, role, contact_info, priority_level) VALUES
    (1, 'Admin User', 'admin', 'REPLACE_WITH_HASH', 'ADMIN', 'admin@givematch.local', 0),
    (2, 'Sample Donor', 'donor', 'REPLACE_WITH_HASH', 'DONOR', '01700000000', 0),
    (3, 'Sample Receiver', 'receiver', 'REPLACE_WITH_HASH', 'RECEIVER', '01800000000', 0);

-- Sample donation
INSERT OR IGNORE INTO donations (id, donor_id, category_id, item_name, description, quantity, quantity_remaining, status) VALUES
    (1, 2, 2, 'Introduction to Programming', 'Good condition, 2 copies', 2, 2, 'AVAILABLE');
