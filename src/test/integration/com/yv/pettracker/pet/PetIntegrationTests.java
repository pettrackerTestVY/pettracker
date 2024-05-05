package com.yv.pettracker.pet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yv.pettracker.IntegrationTestBase;
import com.yv.pettracker.TestDataProvider;
import com.yv.pettracker.pet.data.Pet;
import com.yv.pettracker.pet.data.PetCrudRepository;
import com.yv.pettracker.pet.data.PetType;
import com.yv.pettracker.tracker.data.Tracker;
import com.yv.pettracker.tracker.data.TrackerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.yv.pettracker.TestDataProvider.createAndAssignToPets;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc()
class PetIntegrationTests extends IntegrationTestBase {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;
    @Autowired
    private PetCrudRepository petCrudRepository;
    @Autowired
    private TrackerRepository trackerRepository;

    @Test
    void shouldAddPetTest() throws Exception {
        assertEquals(0L, petCrudRepository.count());
        Exchange.PetCreateRequest petCreateRequest = new Exchange.PetCreateRequest("Boss", Exchange.PetType.DOG, 1L);

        MvcResult mvcResult = mvc.perform(post("/api/v1/pets")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(petCreateRequest)))
                                      .andExpect(status().isCreated())
                                      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                      .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

        Exchange.PetCreatedResponse petCreatedResponse = objectMapper.readValue(contentAsString, Exchange.PetCreatedResponse.class);
        assertNotNull(petCreatedResponse);
        assertNotNull(petCreatedResponse.id());

        Optional<Pet> petOptional = petCrudRepository.findById(petCreatedResponse.id());

        assertNotNull(petOptional);
        assertTrue(petOptional.isPresent());
        Pet pet = petOptional.get();

        assertEquals(petCreateRequest.name(), pet.getName());
        assertEquals(petCreateRequest.petType().name(), pet.getPetType().name());
        assertEquals(petCreateRequest.ownerId(), pet.getOwnerId());

        flushDB();
    }

    @Test
    void petCountTest() throws Exception {
        List<Pet> pets = TestDataProvider.generateRandomPets();
        petCrudRepository.saveAll(pets);

        List<Tracker> trackers = createAndAssignToPets(pets);
        trackerRepository.saveAll(trackers);

        verifyCountByPetType(pets);

        //verify by inzone

        verifyByInZone(trackers);
        flushDB();
    }

    private void flushDB() {
        trackerRepository.deleteAll();
        petCrudRepository.deleteAll();
    }

    private void verifyCountByPetType(List<Pet> pets) throws Exception {
        //verify total
        MvcResult mvcResult = mvc.perform(get("/api/v1/pets/count"))
                                      .andExpect(status().isOk())
                                      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                      .andReturn();

        assertEquals(pets.size(), extractCountFromResponse(mvcResult));

        //verify by pet type
        Map<PetType, Long> petTypeCount = petTypeCount(pets);

        for (Map.Entry<PetType, Long> entry : petTypeCount.entrySet()) {
            verifyPetCount(entry.getKey(), entry.getValue());
        }

    }

    private void verifyPetCount(PetType petType, Long amount) throws Exception {
        MvcResult forPet = mvc.perform(get("/api/v1/pets/count")
                                               .queryParam("petType", petType.name()))
                                   .andExpect(status().isOk())
                                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                   .andReturn();

        assertEquals(amount, extractCountFromResponse(forPet));
    }

    private void verifyByInZone(List<Tracker> trackers) throws Exception {
        long countOutOfZone = trackers.stream()
                                      .filter(tracker -> Boolean.TRUE.equals(tracker.getInZone()))
                                      .count();

        MvcResult mvcResult = mvc.perform(get("/api/v1/pets/count")
                                                  .queryParam("inZone", Boolean.TRUE.toString()))
                                      .andExpect(status().isOk())
                                      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                      .andReturn();

        assertEquals(countOutOfZone, extractCountFromResponse(mvcResult));
    }

    private Map<PetType, Long> petTypeCount(List<Pet> pets) {
        return pets.stream()
                       .collect(Collectors.groupingBy(Pet::getPetType, Collectors.summingLong(e -> 1)));

    }

    private static Long extractCountFromResponse(MvcResult mvcResult) throws UnsupportedEncodingException {
        String contentAsString = mvcResult.getResponse().getContentAsString();
        return Long.valueOf(contentAsString);
    }

}
