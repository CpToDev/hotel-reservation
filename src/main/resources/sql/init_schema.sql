-- Create Rooms Table
CREATE TABLE rooms (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT NOT NULL,
    price REAL NOT NULL,
    facility TEXT NOT NULL,
    available INTEGER DEFAULT 1 -- 1 = available, 0 = booked
);

-- Create Bookings Table
CREATE TABLE bookings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_email TEXT NOT NULL,
    room_id INTEGER NOT NULL,
    checkin_date TEXT NOT NULL,
    name TEXT NOT NULL,
    dob TEXT NOT NULL,
    aadhar TEXT NOT NULL,
    address TEXT NOT NULL,
    mobile TEXT NOT NULL,
    FOREIGN KEY (room_id) REFERENCES rooms(id)
);

-- Users Table
CREATE TABLE users (
    email TEXT PRIMARY KEY,
    password_hash TEXT NOT NULL
);



-- Insert sample rooms
INSERT INTO rooms (type, price, facility, available) VALUES
('Deluxe', 3500.0, 'AC, WiFi, TV', 1),
('Standard', 2000.0, 'Fan, TV', 1),
('Suite', 5000.0, 'AC, WiFi, TV, Mini Bar', 1);


