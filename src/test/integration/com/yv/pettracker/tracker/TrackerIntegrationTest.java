package com.yv.pettracker.tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yv.pettracker.IntegrationTestBase;
import com.yv.pettracker.pet.data.Pet;
import com.yv.pettracker.pet.data.PetCrudRepository;
import com.yv.pettracker.tracker.data.Tracker;
import com.yv.pettracker.tracker.data.TrackerRepository;
import com.yv.pettracker.TestDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc()
class TrackerIntegrationTest extends IntegrationTestBase {


    public static final Boolean IN_ZONE = Boolean.TRUE;
    public static final Boolean LOST_TRACKER = Boolean.FALSE;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;
    @Autowired
    private PetCrudRepository petCrudRepository;
    @Autowired
    private TrackerRepository trackerRepository;

    @Test
    void trackDataTest() throws Exception {
        Pet saved = petCrudRepository.save(TestDataProvider.getRandomPet(1));

        Exchange.RegisterTrackerRequest registerTrackerRequest = new Exchange.RegisterTrackerRequest(UUID.randomUUID(), Exchange.TrackerType.SMALL, saved.getId());

        String contentAsString = mvc.perform(post("/api/v1/tracker/register")
                                                     .contentType(MediaType.APPLICATION_JSON)
                                                     .content(objectMapper.writeValueAsString(registerTrackerRequest)))
                                         .andExpect(status().isOk())
                                         .andReturn().getResponse().getContentAsString();

        Long id = Long.valueOf(contentAsString);

        Optional<Tracker> savedTracker = trackerRepository.findById(id);
        assertNotNull(savedTracker);
        assertTrue(savedTracker.isPresent());

        Tracker tracker = savedTracker.get();
        assertEquals(registerTrackerRequest.trackerType().toString(), tracker.getTrackerType().toString());
        assertEquals(registerTrackerRequest.serialNumber(), tracker.getSerialNumber());
        assertNull(tracker.getInZone());
        assertNull(tracker.getLostTracker());

        Exchange.TrackData trackData = new Exchange.TrackData(id, IN_ZONE, LOST_TRACKER);

        mvc.perform(post("/api/v1/tracker/track")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(trackData)))
                .andExpect(status().isOk());

        Optional<Tracker> udpdatedOptional = trackerRepository.findById(id);
        assertNotNull(udpdatedOptional);
        assertTrue(udpdatedOptional.isPresent());

        Tracker updated = udpdatedOptional.get();
        assertEquals(IN_ZONE, updated.getInZone());
        assertEquals(LOST_TRACKER, updated.getLostTracker());
    }

}
