package com.thoughtworks.parking_lot.service;

import com.thoughtworks.parking_lot.entity.ParkingOrder;
import com.thoughtworks.parking_lot.repository.ParkingLotRepository;
import com.thoughtworks.parking_lot.repository.ParkingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ParkingOrderService {

    @Autowired
    private ParkingOrderRepository parkingOrderRepository;

    public void addParkingOrder(ParkingOrder parkingOrder) {
        parkingOrderRepository.save(parkingOrder);
    }

    public ResponseEntity updateParkingOrder(String carLicenseNum) {
        Boolean status = true;
        ParkingOrder parkingOrderDb = parkingOrderRepository.findByCarLicenseNumAndStatus(carLicenseNum, status);
        parkingOrderDb.setEndDate(new Date());
        parkingOrderDb.setStatus(false);
        return ResponseEntity.ok().body(parkingOrderDb);
    }
}
