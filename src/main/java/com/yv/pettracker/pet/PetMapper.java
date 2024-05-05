package com.yv.pettracker.pet;

import com.yv.pettracker.pet.data.Pet;
import com.yv.pettracker.pet.data.PetType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PetMapper {

    public Pet toPet(Exchange.PetCreateRequest request) {
        if (request == null) {
            return null;
        }

        Pet pet = new Pet();
        pet.setName(request.name());
        pet.setPetType(mapToPetType(request.petType()));
        pet.setOwnerId(request.ownerId());
        return pet;
    }

    private PetType mapToPetType(Exchange.PetType exchangePetType) {
        if (exchangePetType == null) {
            return null;
        }

        return switch (exchangePetType) {
            case CAT -> PetType.CAT;
            case DOG -> PetType.DOG;
        };
    }

}
