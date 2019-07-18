create table PARKING_LOT (
    ID varchar(100) primary key,
    NAME varchar(100) unique,
    CAPACITY int ,
    LOCATION varchar(100)
);

ALTER TABLE PARKING_LOT ADD CONSTRAINT CK_CAPACITY CHECK(CAPACITY > 0);