package com.thoughtworks.parking_lot.controller;


import com.thoughtworks.parking_lot.entity.ParkingLot;
import com.thoughtworks.parking_lot.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/parking-lots")
public class ParkingLotController {

    @Autowired
    private ParkingLotService parkingLotService;

    @PostMapping
    public ResponseEntity createParkingLot(@RequestBody ParkingLot parkingLot) {
        parkingLotService.addParkingLot(parkingLot);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        parkingLotService.deleteParkingLot(id);
    }

    @GetMapping("{id}")
    public ResponseEntity getOne(@PathVariable String id) {
        return parkingLotService.getOne(id);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@RequestBody ParkingLot parkingLot, @PathVariable String id) {
        return parkingLotService.updateParkingLot(parkingLot, id);
    }
}
