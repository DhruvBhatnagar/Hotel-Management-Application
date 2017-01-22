
insert into Hotel values (1,'Sydney');
insert into Hotel values (2,'Melbourne');
insert into Hotel values (3,'Brisbane');
insert into Hotel values (4,'Adelaide');
insert into Hotel values (5, 'Hobart');

insert into RoomType values (1, 'single', 70);
insert into RoomType values (2, 'twin', 120);
insert into RoomType values (3, 'queen',120);
insert into RoomType values (4, 'executive', 180);
insert into RoomType values (5, 'suite', 300);

insert into Room values (1, 1, 1,'available',null,null);
insert into Room values (2, 1, 1,'available',null,null);
insert into Room values (3, 1, 1,'available',null,null);
insert into Room values (4, 1, 1,'available',null,null);
insert into Room values (5, 1, 2,'available',null,null);
insert into Room values (6, 1, 2,'available',null,null);
insert into Room values (7, 1, 2,'available',null,null);
insert into Room values (8, 1, 3,'available',null,null);
insert into Room values (9, 1, 3,'available',null,null);
insert into Room values (10, 1, 3,'available',null,null);
insert into Room values (11, 1, 4,'available',null,null);
insert into Room values (12, 1, 4,'available',null,null);
insert into Room values (13, 1, 4,'available',null,null);
insert into Room values (14, 1, 5,'available',null,null);
insert into Room values (15, 1, 5,'available',null,null);

insert into Room values (16, 2, 1,'available',null,null);
insert into Room values (17, 2, 1,'available',null,null);
insert into Room values (18, 2, 1,'available',null,null);
insert into Room values (19, 2, 1,'available',null,null);
insert into Room values (20, 2, 2,'available',null,null);
insert into Room values (21, 2, 2,'available',null,null);
insert into Room values (22, 2, 2,'available',null,null);
insert into Room values (23, 2, 3,'available',null,null);
insert into Room values (24, 2, 3,'available',null,null);
insert into Room values (25, 2, 3,'available',null,null);
insert into Room values (26, 2, 4,'available',null,null);
insert into Room values (27, 2, 4,'available',null,null);
insert into Room values (28, 2, 4,'available',null,null);
insert into Room values (29, 2, 5,'available',null,null);
insert into Room values (30, 2, 5,'available',null,null);

insert into Room values (31, 3, 1,'available',null,null);
insert into Room values (32, 3, 1,'available',null,null);
insert into Room values (33, 3, 1,'available',null,null);
insert into Room values (34, 3, 1,'available',null,null);
insert into Room values (35, 3, 2,'available',null,null);
insert into Room values (36, 3, 2,'available',null,null);
insert into Room values (37, 3, 2,'available',null,null);
insert into Room values (38, 3, 3,'available',null,null);
insert into Room values (39, 3, 3,'available',null,null);
insert into Room values (40, 3, 3,'available',null,null);
insert into Room values (41, 3, 4,'available',null,null);
insert into Room values (42, 3, 4,'available',null,null);
insert into Room values (43, 3, 4,'available',null,null);
insert into Room values (44, 3, 5,'available',null,null);
insert into Room values (45, 3, 5,'available',null,null);

insert into Room values (46, 4, 5,'available',null,null);

insert into HotelDiscounts values (0.1,'2014-12-15', '2015-2-15', 1,1);

select * from hoteldiscounts

--insert into Consumer(creditcard, emailID) values ('John','Doe','123','a@b.com');
--insert into Booking(cid,pin) values(3, 234);

--check sql inserts above this ine
--insert into BookingDetails(checkin, checkout, bookid, price,xbed,roomType,hotel) values ('2014-04-20','2014-04-30',1234567890,598.40,false,1,1);

--insert into Booking(id, pin, cid) values(9876543, 1234, 1);
--insert into BookingDetails(checkin, checkout, bookid, price,xbed,roomType,hotel) values ('2014-05-05','2014-05-14',9876543,598.40,false,1,1);



--Use this table to add discounts:
--insert into HotelDiscounts values  (0.8, '2014-4-20','2014-4-30',1,null);


