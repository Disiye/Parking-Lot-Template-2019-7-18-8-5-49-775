package com.thoughtworks.parking_lot.repository;

import com.thoughtworks.parking_lot.entity.ParkingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParkingOrderRepository extends JpaRepository<ParkingOrder, String> {
    ParkingOrder findByCarLicenseNumAndStatus(String carLicenseNum, Boolean status);

    @Query(value = "select * from PARKING_ORDER where PARKING_LOT_NAME = ?1 and STATUS = ?2", nativeQuery = true)
    List<ParkingOrder> findByNameAndStatus(String parkingLotName, Boolean status);
}
