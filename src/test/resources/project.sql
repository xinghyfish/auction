create database auction;
use auction;

create table admin (
    adminID varchar(30) not null primary key,
    pwd varchar(30) not null
);

create table customer (
    customerID int auto_increment not null unique,
    customerName varchar(30) not null,
    pwd varchar(30) not null,
    email varchar(30) not null unique,
    phone varchar(11) not null unique,
    isLogout bool default false
);

create table auctioneer (
    auctioneerID int auto_increment not null unique,
    auctioneerName varchar(30) not null,
    pwd varchar(30) not null,
    email varchar(30) not null unique,
    phone varchar(11) not null unique,
    isLogout bool default false
);

create table auction (
    auctionID int auto_increment not null unique,
    auctionName varchar(30) not null,
    startPrice int not null,
    status tinyint not null default 0
);

create table auctionVenue (
    venueID int auto_increment not null unique,
    location varchar(50) not null
);

create table auctionRecord (
    recordID int auto_increment not null unique,
    auctionID int,
    customerID int,
    venueID int,
    startTime TIMESTAMP not null,
    enfTime TIMESTAMP not null,
    finalPrice int,
    status tinyint not null,
    constraint foreign key (auctionID) references auction(auctionID),
    constraint foreign key (customerID) references customer(customerID),
    constraint foreign key (venueID) references auctionVenue(venueID)
);

