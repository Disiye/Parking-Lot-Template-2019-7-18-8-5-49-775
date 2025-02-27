package com.thoughtworks.parking_lot.controller;


import com.thoughtworks.parking_lot.entity.ParkingOrder;
import com.thoughtworks.parking_lot.service.ParkingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/parking-orders")
public class ParkingOrderController {

    @Autowired
    private ParkingOrderService parkingOrderService;

    @PostMapping
    public ResponseEntity createParkingOrder(@RequestBody ParkingOrder parkingOrder) {
        return parkingOrderService.addParkingOrder(parkingOrder);
    }

    @PutMapping("{carLicenseNum}")
    public ResponseEntity update(@PathVariable String carLicenseNum) {
        return parkingOrderService.updateParkingOrder(carLicenseNum);
    }
}
