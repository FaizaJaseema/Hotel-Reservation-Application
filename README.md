
A console-based Hotel Reservation System built in Java, implementing core OOP principles including interfaces, inheritance, polymorphism, and the Singleton design pattern.The application simulates a real-world hotel booking system with separate flows for customers and administrators.

Table of Contents

Features,
Project Structure,
Architecture Overview,
Technologies Used.

Features:

Customer Menu

Search available rooms by check-in and check-out dates,
Smart room recommendation — suggests rooms 7 days later if none are available,
Create a customer account with email validation,
Book a room with double-booking prevention,
View all personal reservations.

Admin Menu

View all registered customers,
View all rooms,
View all reservations across the system,
Add new rooms (paid or free, single or double).

Architecture Overview

UI Layer       →   MainMenu / AdminMenu,
API Layer      →   HotelResource / AdminResource,
Service Layer  →   ReservationService / CustomerService,
Model Layer    →   Customer, Room, FreeRoom, Reservation, IRoom, RoomType.

Technologies Used

Java (Core Java, OOP),
Java Collections Framework (HashMap, ArrayList),
Java Date & Calendar API,
Java Regex (Pattern matching for email validation),
IntelliJ IDEA.

