create table PARKING_ORDER (
    ID varchar(100) primary key,
    PARKING_LOT_NAME varchar(100),
    CAR_LICENSE_NUM varchar(100),
    BEGIN_DATE date,
    END_DATE date,
    STATUS BOOLEAN,
    PARKING_LOT_ID varchar(100)

);