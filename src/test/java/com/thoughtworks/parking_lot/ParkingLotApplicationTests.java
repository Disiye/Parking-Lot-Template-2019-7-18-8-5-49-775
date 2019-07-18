package com.thoughtworks.parking_lot;

import antlr.build.Tool;
import com.thoughtworks.parking_lot.entity.ParkingLot;
import com.thoughtworks.parking_lot.repository.ParkingLotRepository;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ParkingLotApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Before
    public void deleteAll() {
        parkingLotRepository.deleteAll();
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void should_return_status_is_created_when_post_new_parking_lot() throws Exception {
        ParkingLot parkingLot = new ParkingLot("parkingLot1", 10, "parkingLot1");
        String objectJson = new JSONObject(parkingLot).toString();
        this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectJson)).andExpect(status().isCreated());
    }

    @Test
    public void should_return_exception_when_post_same_name() throws Exception {
        ParkingLot parkingLot = new ParkingLot("parkingLot1", 10, "parkingLot1");
        String objectJson = new JSONObject(parkingLot).toString();
        this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8).content(objectJson)).andReturn();

        Assertions.assertThrows(Exception.class, () -> {
            this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectJson));
        });
    }

    @Test
    public void should_return_exception_when_post_negative_number() throws Exception {
        ParkingLot parkingLot = new ParkingLot("parkingLot1", -10, "parkingLot1");
        String objectJson = new JSONObject(parkingLot).toString();

        Assertions.assertThrows(Exception.class, () -> {
            this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectJson));
        });
    }

    @Test
    public void should_return_status_code_is_ok_when_delete_success() throws Exception {
        ParkingLot parkingLot = new ParkingLot("parkingLot1", 10, "parkingLot1");
        String objectJson = new JSONObject(parkingLot).toString();
        this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8).content(objectJson)).andReturn();

        List<ParkingLot> parkingLots = parkingLotRepository.findAll();
        String id = parkingLots.get(0).getId();

        this.mockMvc.perform(delete("/parking-lots/" + id)).andExpect(status().isOk());
    }

    @Test
    public void should_return_parking_lot_when_find_parking_lot() throws Exception {
        ParkingLot parkingLot = new ParkingLot("parkingLot1", 10, "location1");
        String objectJson = new JSONObject(parkingLot).toString();
        this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8).content(objectJson)).andReturn();

        List<ParkingLot> parkingLots = parkingLotRepository.findAll();
        String id = parkingLots.get(0).getId();

        String content = this.mockMvc.perform(put("/parking-lots/" + id).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectJson)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONObject obj = new JSONObject(content);

        assertEquals("parkingLot1", obj.get("name"));
        assertEquals(10, obj.get("capacity"));
        assertEquals("location1", obj.get("location"));
    }

    @Test
    public void should_return_update_parking_lot_capacity_when_put_new_parking_lot_capacity() throws Exception {
        ParkingLot parkingLot = new ParkingLot("parkingLot1", 10, "location1");
        String objectJson = new JSONObject(parkingLot).toString();
        this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8).content(objectJson)).andReturn();

        List<ParkingLot> parkingLots = parkingLotRepository.findAll();
        String id = parkingLots.get(0).getId();

        ParkingLot newParkingLot = new ParkingLot("parkingLot1", 20, "location1");

        String newObjectJson = new JSONObject(newParkingLot).toString();
        String content = this.mockMvc.perform(put("/parking-lots/" + id).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(newObjectJson)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONObject obj = new JSONObject(content);
        assertEquals(20, obj.get("capacity"));
    }

}
