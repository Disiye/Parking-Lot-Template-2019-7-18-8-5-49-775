package com.thoughtworks.parking_lot.service;

import com.thoughtworks.parking_lot.entity.ParkingLot;
import com.thoughtworks.parking_lot.entity.ParkingOrder;
import com.thoughtworks.parking_lot.repository.ParkingLotRepository;
import com.thoughtworks.parking_lot.repository.ParkingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ParkingOrderService {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private ParkingOrderRepository parkingOrderRepository;

    public ResponseEntity addParkingOrder(ParkingOrder parkingOrder) {
        String errorMsg = "The parking lot is full!";
        List<ParkingOrder> parkingOrders = parkingOrderRepository.findByNameAndStatus(parkingOrder.getParkingLotName(), true);
        ParkingLot parkingLot = parkingLotRepository.findByName(parkingOrder.getParkingLotName());
        if (parkingLot != null && parkingOrders.size() < parkingLot.getCapacity()) {
            parkingOrder.setBeginDate(new Date());
            parkingOrder.setParkingLot(parkingLot);
            parkingOrderRepository.save(parkingOrder);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
    }

    public ResponseEntity updateParkingOrder(String carLicenseNum) {
        ParkingOrder parkingOrderDb = parkingOrderRepository.findByCarLicenseNumAndStatus(carLicenseNum, true);
        parkingOrderDb.setEndDate(new Date());
        parkingOrderDb.setStatus(false);
        return ResponseEntity.ok().body(parkingOrderDb);
    }
}
