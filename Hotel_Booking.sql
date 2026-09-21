CREATE DATABASE Hotel_Booking;

USE Hotel_Booking;

CREATE TABLE Hotel
(

	id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(25) NOT NULL,
    location VARCHAR(30) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    email VARCHAR(45) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    checkin TIME NOT  NULL,	
    checkout TIME NOT NULL,
    totalcount INT NOT NULL
);

Select * from Hotel ;

Select * from Hotel ;
Select id,name,phone,email,description,checkin,checkout,totalcount from Hotel where location='Sivakasi';

-- insert into Hotel(name,location,phone,email,description,checkin,checkout,totalcount) values("Bell Hotel","Sivakasi","9878678767","Bell@gmail.com","Best Hotel in the Sivakasi Circle","08:00:00","22:00:00",15);

delete from hotel where id=15;

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
-- 	modify column 
    
	alter table Users
	add column password varchar(10) not null; 

	INSERT INTO Users (name,DOB,email,phone,roll,create_At,password) VALUE("Ramarajan K",'2004-01-20',"ramarajan@gmail.com","9360183876","Admin",now(),"ramarajan4");

	SELECT * FROM Users;

describe room_type;

alter table Room_Type add column ACType varchar(10) not null;

CREATE TABLE MealsPlan
(

	id INT PRIMARY KEY ,
	planName VARCHAR(25) NOT NULL,
	mealPerDay INT NOT NULL,
	price DECIMAL NOT NULL,
	description TEXT NOT NULL,
    hotelId INT,
    
    constraint fk_hotel_meal_id
    FOREIGN KEY (hotelId)
    references hotel(id)
    
);

ALTER TABLE MealsPlan
DROP FOREIGN KEY fk_hotel_meal_id ;

ALTER TABLE MealsPlan
ADD CONSTRAINT fk_hotel_meal_id
FOREIGN KEY (hotelId)
REFERENCES hotel(id)
ON DELETE CASCADE;

CREATE TABLE mealItem
(

	id INT PRIMARY KEY,
	mealType VARCHAR(10) NOT NULL, -- (DINNER,LUNCH,DIFFEN)
	serviceTime TIME NOT NULL,
	description TEXT NOT NULL,
	mealId INT,

	CONSTRAINT fk_meal_id
	FOREIGN KEY (mealId)
	REFERENCES MealsPlan(id)
    
);


-- CREATE TABLE Room_Type
-- (

-- 	id INT PRIMARY KEY AUTO_INCREMENT ,
--     title VARCHAR(20) NOT NULL ,
--     ACType VARCHAR(10) NOT NULL,
--     price DECIMAL(10,4) NOT NULL ,
--     capacity INT NOT NULL
--     
-- );


drop table Room_Type;

alter table rooms add title varchar(20) not null;
alter table rooms add ACType varchar(10) not null;
alter table rooms add price DECIMAL(10,4) not null;
alter table rooms add capacity int not null;

CREATE TABLE Rooms
(

	id INT PRIMARY KEY AUTO_INCREMENT ,
	room_number INT NOT NULL UNIQUE ,
	floor_no INT NOT NULL ,
	status VARCHAR(20) NOT NULL ,
    
    title VARCHAR(20) NOT NULL ,
    ACType VARCHAR(10) NOT NULL ,
    price DECIMAL(10,4) NOT NULL ,
    capacity INT NOT NULL ,
    
	hotel_id INT,

	CONSTRAINT fk_hotel_id
	FOREIGN KEY (hotel_id)
	REFERENCES Hotel(id)

);

select * from rooms ;
select * from Hotel ;
select * from Users ;

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE rooms;

SET FOREIGN_KEY_CHECKS = 1;


ALTER TABLE Rooms
DROP INDEX fk_hotel_id;

ALTER TABLE Rooms
ADD CONSTRAINT fk_hotel_id
FOREIGN KEY (hotel_id)
REFERENCES Hotel(id)
ON DELETE CASCADE;

SHOW CREATE TABLE Rooms;

ALTER TABLE Rooms
DROP FOREIGN KEY fk_hotel_id;

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

alter table booking add column roomNumber int not null ;

select * from Booking ;

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

CREATE TABLE payments
(
	payment_id INT PRIMARY KEY AUTO_INCREMENT,
	booking_id INT NOT NULL,
	customer_id INT NOT NULL,
	amount DECIMAL(10,2) NOT NULL,
	payment_type VARCHAR(20) NOT NULL,
	payment_status VARCHAR(20) NOT NULL,
	payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

	CONSTRAINT fk_payment_booking
	FOREIGN KEY (booking_id)
	REFERENCES Booking(id),

	CONSTRAINT fk_payment_customer
	FOREIGN KEY (customer_id)
	REFERENCES Users(id)
);

CREATE TABLE Coupons
(

	id INT PRIMARY KEY,
	code VARCHAR(10) NOT NULL UNIQUE,
	discount_Percentage INT NOT NULL,
	valid_date DATE NOT NULL
    
);