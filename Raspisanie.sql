CREATE DATABASE Raspisanie;
USE Raspisanie;

CREATE TABLE Route 
(
  	ID_route INT NOT NULL AUTO_INCREMENT,
  	Numbers CHAR(9) NULL,
    Departure CHAR(30) NULL,
    Arrival CHAR(30) NULL,
	PRIMARY KEY (ID_route)
)	ENGINE=InnoDB;

CREATE TABLE Stations 
(
  	ID_note INT NOT NULL AUTO_INCREMENT,
  	ID_route INT NOT NULL,
    Station CHAR(30) NULL,
	Departure TIME NULL,
	Arrival TIME NULL,
	Travel TIME NULL,
    PRIMARY KEY (ID_note),
    FOREIGN KEY (ID_route) REFERENCES Route (ID_route) ON DELETE CASCADE ON UPDATE CASCADE
)	ENGINE=InnoDB;

