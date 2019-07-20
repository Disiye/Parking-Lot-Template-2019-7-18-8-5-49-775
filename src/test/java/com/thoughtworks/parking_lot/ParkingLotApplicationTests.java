package com.thoughtworks.parking_lot;

import antlr.build.Tool;
import com.thoughtworks.parking_lot.entity.ParkingLot;
import com.thoughtworks.parking_lot.entity.ParkingOrder;
import com.thoughtworks.parking_lot.repository.ParkingLotRepository;
import com.thoughtworks.parking_lot.repository.ParkingOrderRepository;
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
import java.util.Date;
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

    @Autowired
    private ParkingOrderRepository parkingOrderRepository;

    @Before
    public void deleteAll() {
        parkingLotRepository.deleteAll();
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void should_return_status_is_created_when_post_new_parking_lot() throws Exception {
        //Given
        ParkingLot parkingLot = new ParkingLot("parkingLot1", 10, "parkingLot1");

        //When + Than
        String objectJson = new JSONObject(parkingLot).toString();
        this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectJson)).andExpect(status().isCreated());
    }

    @Test
    public void should_return_exception_when_post_same_name() throws Exception {
        //Given
        ParkingLot parkingLot = new ParkingLot("parkingLot1", 10, "parkingLot1");
        String objectJson = new JSONObject(parkingLot).toString();
        this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8).content(objectJson)).andReturn();

        //When + Than
        Assertions.assertThrows(Exception.class, () -> {
            this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectJson));
        });
    }

    @Test
    public void should_return_exception_when_post_negative_number() throws Exception {
        //Given
        ParkingLot parkingLot = new ParkingLot("parkingLot1", -10, "parkingLot1");
        String objectJson = new JSONObject(parkingLot).toString();

        //When + Than
        Assertions.assertThrows(Exception.class, () -> {
            this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectJson));
        });
    }

    @Test
    public void should_return_status_code_is_ok_when_delete_success() throws Exception {
        //Given
        ParkingLot parkingLot = new ParkingLot("parkingLot1", 10, "parkingLot1");
        String objectJson = new JSONObject(parkingLot).toString();
        this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8).content(objectJson)).andReturn();

        //When
        List<ParkingLot> parkingLots = parkingLotRepository.findAll();
        String id = parkingLots.get(0).getId();

        //Than
        this.mockMvc.perform(delete("/parking-lots/" + id)).andExpect(status().isOk());
        Assertions.assertThrows(Exception.class, () -> {
            this.mockMvc.perform(get("/parking-lots/" + id)).andReturn().getResponse().getContentAsString();
        });
    }

    @Test
    public void should_return_parking_lot_when_find_parking_lot() throws Exception {
        //Given
        ParkingLot parkingLot = new ParkingLot("parkingLot1", 10, "location1");
        String objectJson = new JSONObject(parkingLot).toString();
        this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8).content(objectJson)).andReturn();

        List<ParkingLot> parkingLots = parkingLotRepository.findAll();
        String id = parkingLots.get(0).getId();

        //When
        String content = this.mockMvc.perform(put("/parking-lots/" + id).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectJson)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONObject obj = new JSONObject(content);

        //Than
        assertEquals("parkingLot1", obj.get("name"));
        assertEquals(10, obj.get("capacity"));
        assertEquals("location1", obj.get("location"));
    }

    @Test
    public void should_return_update_parking_lot_capacity_when_put_new_parking_lot_capacity() throws Exception {
        //Given
        ParkingLot parkingLot = new ParkingLot("parkingLot1", 10, "location1");
        String objectJson = new JSONObject(parkingLot).toString();
        this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8).content(objectJson)).andReturn();
        List<ParkingLot> parkingLots = parkingLotRepository.findAll();
        String id = parkingLots.get(0).getId();
        ParkingLot newParkingLot = new ParkingLot("parkingLot1", 20, "location1");

        //When
        String newObjectJson = new JSONObject(newParkingLot).toString();
        String content = this.mockMvc.perform(put("/parking-lots/" + id).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(newObjectJson)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONObject obj = new JSONObject(content);

        //Then
        assertEquals(20, obj.get("capacity"));
    }

    @Test
    public void should_return_parking_lots_page_when_find_parking_lot_page() throws Exception {
        //Given
        ParkingLot parkingLot1 = new ParkingLot("parkingLot1", 10, "location1");
        String objectJson1 = new JSONObject(parkingLot1).toString();
        this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8).content(objectJson1)).andReturn();

        ParkingLot parkingLot2 = new ParkingLot("parkingLot2", 10, "location2");
        String objectJson2 = new JSONObject(parkingLot2).toString();
        this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8).content(objectJson2)).andReturn();

        ParkingLot parkingLot3 = new ParkingLot("parkingLot3", 10, "location3");
        String objectJson3 = new JSONObject(parkingLot3).toString();
        this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8).content(objectJson3)).andReturn();

        ParkingLot parkingLot4 = new ParkingLot("parkingLot4", 10, "location4");
        String objectJson4 = new JSONObject(parkingLot4).toString();
        this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8).content(objectJson4)).andReturn();

        ParkingLot parkingLot5 = new ParkingLot("parkingLot5", 10, "location5");
        String objectJson5 = new JSONObject(parkingLot5).toString();
        this.mockMvc.perform(post("/parking-lots").contentType(MediaType.APPLICATION_JSON_UTF8).content(objectJson5)).andReturn();

        List<ParkingLot> parkingLots = parkingLotRepository.findAll();
        ParkingLot parkingLot = parkingLots.get(2);

        //When
        String content = this.mockMvc.perform(get("/parking-lots?pageNumber=2&pageSize=2")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONObject json = new JSONObject(content);

        //Then
        Assertions.assertEquals(2, json.get("size"));
    }

}
