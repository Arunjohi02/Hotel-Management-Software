CREATE DATABASE Hotel_Booking; 

USE Hotel_Booking;

CREATE TABLE Hotel
(
	id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(25) NOT NULL,
    location VARCHAR(30) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    email VARCHAR(45) NOT NULL UNIQUE
    
);

CREATE TABLE Users
(

	id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(25) NOT NULL,
    DOB DATE NOT NULL,
    email VARCHAR(45) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL UNIQUE,
    roll VARCHAR(20) NOT NULL,
    create_At DATE NOT NULL
);

	-- alter table Users
	-- add column password varchar(10) not null;

	INSERT INTO Users (name,DOB,email,phone,roll,create_At,password) VALUE("Ramarajan K",'2004-01-20',"ramarajan@gmail.com","9360183876","Admin",now(),"ramarajan4");

CREATE TABLE Room_Type
(

	id INT PRIMARY KEY AUTO_INCREMENT ,
    title VARCHAR(20) NOT NULL ,
    price DECIMAL(10,4) NOT NULL ,
    capacity INT NOT NULL
    
);

CREATE TABLE Rooms
(

	id INT PRIMARY KEY AUTO_INCREMENT,
	room_number INT NOT NULL UNIQUE,
	floor_no INT NOT NULL,
	status VARCHAR(20) NOT NULL,
	hotel_id INT,
	type_id INT,

	CONSTRAINT fk_hotel_id
	FOREIGN KEY (hotel_id)
	REFERENCES Hotel(id),

	CONSTRAINT fk_type_id
	FOREIGN KEY (type_id)
	REFERENCES Room_Type(id)

);

CREATE TABLE Booking
(

	id INT PRIMARY KEY AUTO_INCREMENT,
	chech_in DATE NOT NULL,
	check_out DATE NOT NULL,
	status VARCHAR(20) NOT NULL,
	amout DECIMAL(10,3) NOT NULL,
	user_id INT,

	CONSTRAINT fk_user_id
	FOREIGN KEY (user_id)
	REFERENCES Users(id)

);

CREATE TABLE Booking_Items
(

	id INT PRIMARY KEY AUTO_INCREMENT,
	price_per_ngt DECIMAL(10,3) NOT NULL,

	booking_id INT,
	room_id INT,

	CONSTRAINT fk_booking_id
	FOREIGN KEY (booking_id)
	REFERENCES Booking(id),

	CONSTRAINT fk_room_id
	FOREIGN KEY (room_id)
	REFERENCES Rooms(id)

);

CREATE TABLE Payments
(
	id INT PRIMARY KEY ,
	amount DECIMAL NOT NULL ,
	payemet_type VARCHAR(15) NOT NULL ,
	status VARCHAR(15) NOT NULL,
	booking_id INT,

	CONSTRAINT fk_Payemt_booking_id
	FOREIGN KEY (booking_id)
	REFERENCES Booking(id)
);

CREATE TABLE Coupons
(

	id INT PRIMARY KEY,
	code VARCHAR(10) NOT NULL UNIQUE,
	discount_Percentage INT NOT NULL,
	valid_date DATE NOT NULL
    
);