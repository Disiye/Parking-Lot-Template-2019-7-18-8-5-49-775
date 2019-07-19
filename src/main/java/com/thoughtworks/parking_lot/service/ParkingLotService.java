package com.thoughtworks.parking_lot.service;

import com.thoughtworks.parking_lot.entity.ParkingLot;
import com.thoughtworks.parking_lot.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ParkingLotService {

    @Autowired
    private
    ParkingLotRepository parkingLotRepository;

    public void addParkingLot(ParkingLot parkingLot) {
        parkingLotRepository.save(parkingLot);
    }

    public void deleteParkingLot(String id) {
        parkingLotRepository.deleteById(id);
    }

    public ResponseEntity<ParkingLot> getOne(String id) {
        ParkingLot parkingLot = parkingLotRepository.findById(id).get();
        return ResponseEntity.ok().body(parkingLot);
    }

    public ResponseEntity updateParkingLot(ParkingLot parkingLot, String id) {
        ParkingLot parkingLotDb = parkingLotRepository.findById(id).get();
        parkingLotDb.setCapacity(parkingLot.getCapacity());
        return ResponseEntity.ok().body(parkingLotDb);
    }

    public Page<ParkingLot> findAllByPage(int pageNumber, int pageSize) {
        return parkingLotRepository.findAll(PageRequest.of(pageNumber - 1, pageSize));
    }
}
