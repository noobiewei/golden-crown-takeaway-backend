-- Temporary seed data for local development.
-- Will be replaced once there's a real admin interface for menu management.
DELETE FROM menu_items;

INSERT INTO menu_items (name, description, price, category, available) VALUES
('Spring Rolls', 'Crispy vegetable spring rolls, 4 pieces', 4.50, 'STARTER', 1),
('Prawn Toast', 'Sesame prawn toast, 4 pieces', 5.20, 'STARTER', 1),
('Hot & Sour Soup', 'Classic Sichuan-style hot and sour soup', 3.80, 'SOUP', 1),
('Kung Po Chicken', 'Diced chicken with peanuts, chilli and spring onion', 8.90, 'MAIN_COURSE', 1),
('Sweet & Sour Pork', 'Battered pork with pineapple in sweet and sour sauce', 8.50, 'MAIN_COURSE', 1),
('Beef in Black Bean Sauce', 'Sliced beef with peppers and onion in black bean sauce', 9.20, 'MAIN_COURSE', 1),
('Egg Fried Rice', 'Wok-fried rice with egg and spring onion', 3.50, 'RICE', 1),
('Special Fried Rice', 'Egg fried rice with prawn, chicken and char siu', 4.80, 'RICE', 1),
('Singapore Noodles', 'Curry-flavoured rice noodles with prawn, char siu and vegetables', 7.90, 'NOODLES', 1),
('Prawn Crackers', 'Portion of prawn crackers', 2.00, 'SIDE', 1),
('Coca-Cola Can', '330ml', 1.20, 'DRINK', 1);
