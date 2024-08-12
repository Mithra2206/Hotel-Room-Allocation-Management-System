# Hotel-Room-Allocation-Management-System
This is a Project on hotel management system based on core Java and MySQL. This implemented project provides the basic functionality of hotel management system.
# Setup
      DataBase:
      CREATE DATABASE hotel_database;

      Table:
       CREATE TABLE reservations(
       reservation_id INT AUTO_INCREMENT PRIMARY KEY,
       guest_name varchar(255) NOT NULL,
       room_number int NOT NULL,
       contact_number varchar(10) NOT NULL,
       reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
       );
         
