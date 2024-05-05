package com.yv.pettracker.pet;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Exchange {

    public enum PetType {
        CAT, DOG
    }

    public enum TrackerType {
        SMALL, MEDIUM, BIG
    }

    public record PetCreateRequest(@NotNull String name, @NotNull PetType petType, @Positive Long ownerId) {

    }

    public record PetCreatedResponse(Long id) {

    }

}
