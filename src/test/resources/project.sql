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

update auction.customer
set pwd = md5('xosmos')
where customerID = 1;

insert into
auction.auction(auctionName, startPrice, status)
values ('X-BOX原型机', 350000, '待拍卖'),
       ('《Abbey Road》原版CD', 10000, '待拍卖'),
       ('蔡徐坤原版背带裤', 5000, '流拍'),
       ('邢寰宇早年手稿一份', 480, '待拍卖'),
       ('三星1T固态硬盘TLC', 500, '流拍');

insert into
auction.auctioneer(auctioneerName, pwd, email, phone)
values ('John Lennon', md5('john_lennon'), 'lennon@beatles.com', '13098472845'),
       ('Prince', md5('prince'), 'purple_rain@prince.com', '1332789102'),
       ('Kanye West', md5('yeezy'), 'yeezy@jeesus.com', '1338764532');


insert into
auction.auctionVenue(location, auctioneerID, startTime, endTime, isOnline)
values ('江苏省南京市拍卖礼堂', 1, TIMESTAMP('2022-09-12 09:30:00'), null, 1),
       ('北京市人民大会堂', 1, TIMESTAMP('2022-09-01 10:00:00'), TIMESTAMP('2022-09-02 15:30:00'), 0),
       ('浙江省杭州市拍卖礼堂', 2, TIMESTAMP('2022-09-05 13:30:00'), TIMESTAMP('2022-09-05 18:30:00'), 0);

