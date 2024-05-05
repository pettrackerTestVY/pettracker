package com.yv.pettracker;

import com.yv.pettracker.pet.data.Pet;
import com.yv.pettracker.pet.data.PetType;
import com.yv.pettracker.tracker.data.Tracker;
import com.yv.pettracker.tracker.data.TrackerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TestDataProvider {

    private static final String[] PET_NAMES = {"Max", "Bella", "Charlie", "Luna", "Cooper", "Daisy", "Rocky", "Lola", "Buddy", "Lucy"};

    private TestDataProvider() {
    }

    public static List<Pet> generateRandomPets() {

        List<Pet> randomPets = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Pet randomPet = getRandomPet(i);

            randomPets.add(randomPet);
        }

        return randomPets;
    }

    public static Pet getRandomPet(int ownerId) {
        Pet randomPet = new Pet();
        int index = ownerId % 10;
        String randomName = PET_NAMES[index];
        randomPet.setName(randomName);

        PetType randomPetType = ownerId % 2 == 0 ? PetType.CAT : PetType.DOG;
        randomPet.setPetType(randomPetType);

        randomPet.setOwnerId((long) ownerId);
        return randomPet;
    }

    public static List<Tracker> createAndAssignToPets(List<Pet> pets) {
        return pets.stream()
                       .map(TestDataProvider::createTrackerAndAssign)
                       .toList();
    }

    public static Tracker createTrackerAndAssign(Pet pet) {
        Tracker tracker = new Tracker();
        tracker.setPetId(pet.getId());
        tracker.setSerialNumber(UUID.randomUUID());
        tracker.setInZone(pet.getId() % 3 == 0);
        tracker.setTrackerType(getRandomTrackerType());
        return tracker;
    }

    private static TrackerType getRandomTrackerType() {
        TrackerType[] trackerTypes = TrackerType.values();
        Random random = new Random();
        int index = random.nextInt(trackerTypes.length);
        return trackerTypes[index];
    }


}
