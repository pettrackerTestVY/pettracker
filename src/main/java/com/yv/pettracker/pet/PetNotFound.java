package com.yv.pettracker.pet;

public class PetNotFound extends RuntimeException {

    public PetNotFound(Long petId) {
        super(String.format("Pet not found, id: %s", petId));
    }

}
