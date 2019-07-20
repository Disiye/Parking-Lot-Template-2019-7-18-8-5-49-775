package com.thoughtworks.parking_lot;

import com.thoughtworks.parking_lot.entity.ParkingOrder;
import com.thoughtworks.parking_lot.repository.ParkingOrderRepository;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ParkingOrderApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ParkingOrderRepository parkingOrderRepository;

    @Before
    public void deleteAll() {
        parkingOrderRepository.deleteAll();
    }

    @Test
    public void should_return_status_is_created_when_post_new_parking_order() throws Exception {
        ParkingOrder parkingOrder = new ParkingOrder("parkingLot1", "粤C 888888", true);
        String objectJson = new JSONObject(parkingOrder).toString();
        this.mockMvc.perform(post("/parking-orders").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectJson)).andExpect(status().isCreated());
    }

    @Test
    public void should_return_update_order_when_ferch_car() throws Exception {
        ParkingOrder parkingOrder = new ParkingOrder("parkingLot1", "粤C 888888", true);
        String objectJson = new JSONObject(parkingOrder).toString();
        this.mockMvc.perform(post("/parking-orders").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectJson)).andExpect(status().isCreated());

        String carLicenseNum = "粤C 888888";
        String content = this.mockMvc.perform(put("/parking-orders/" + carLicenseNum).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONObject obj = new JSONObject(content);
        assertEquals(carLicenseNum, obj.get("carLicenseNum"));
        assertEquals(false, obj.get("status"));
        assertNotNull(obj.get("endDate"));
    }


    @Test
    public void should_return_errorMsg_when_parking_lot_is_full() throws Exception {
        for (int i = 0; i < 10; i++) {
            ParkingOrder parkingOrder = new ParkingOrder("parkingLot1", "粤C 88888" + String.valueOf(i), true);
            String objectJson = new JSONObject(parkingOrder).toString();
            this.mockMvc.perform(post("/parking-orders").contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectJson)).andExpect(status().isCreated());
        }
        ParkingOrder parkingOrder = new ParkingOrder("parkingLot1", "粤C 888888", true);
        String objectJson = new JSONObject(parkingOrder).toString();
        this.mockMvc.perform(post("/parking-orders").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectJson));

    }
}
