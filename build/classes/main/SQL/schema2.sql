

drop table HotelDiscounts;
drop table Room;
drop table BookingDetails;
drop table Booking;
drop table Consumer;
drop table RoomType;
drop table Hotel;


create table Hotel (
	id integer,
	city varchar(50),
	primary key (id)
);

create table RoomType (
	id integer,
	typename varchar(10),
	initialPrice double,
	primary key(id)

);

create table Consumer (
	id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	creditcard varchar (16),
	emailID varchar (50),
	primary key(id)
);

create table Booking (
	bkid integer,
	pin integer,
	consid integer not null references Consumer(id),
	primary key(bkid)
);


create table BookingDetails (
	id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	checkin date,
	checkout date,
	bookid integer not null references Booking(bkid),
	price double,
	xbed boolean,
	roomType integer not null references RoomType(id),
	hotel integer not null references Hotel(id),
	primary key(id)
);


create table Room (
	id integer,
	hotelid integer not null references Hotel(id),
	roomTypeid integer not null references RoomType(id),
	status varchar(20) default 'Available',
	bookid integer references BookingDetails(id),
	bkid integer references Booking(bkid) default 0,

	--alter table room add column status varchar(20) DEFAULT 'Available'
	--alter table room drop column bookid,
	--alter table room add column bookid integer references BookingDetails(id),
	--added extra column for room
	--alter table room add column bkid integer references Booking(bkid)
	
	primary key (id)
);

create table HotelDiscounts (
    disc double,
    checkinDate date,
    checkoutDate date,
    hotel integer references Hotel(id),
    roomType integer references RoomType(id)
);
