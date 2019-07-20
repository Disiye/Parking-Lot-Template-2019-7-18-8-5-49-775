package com.thoughtworks.parking_lot.service;

import com.thoughtworks.parking_lot.entity.ParkingLot;
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
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private ParkingOrderRepository parkingOrderRepository;

    public void addParkingOrder(ParkingOrder parkingOrder) {
        ParkingLot parkingLot = parkingLotRepository.findByName(parkingOrder.getParkingLotName());
        parkingOrder.setBeginDate(new Date());
        parkingOrder.setParkingLot(parkingLot);
        parkingOrderRepository.save(parkingOrder);
    }

    public ResponseEntity updateParkingOrder(String carLicenseNum) {
        ParkingOrder parkingOrderDb = parkingOrderRepository.findByCarLicenseNumAndStatus(carLicenseNum, true);
        parkingOrderDb.setEndDate(new Date());
        parkingOrderDb.setStatus(false);
        return ResponseEntity.ok().body(parkingOrderDb);
    }
}
