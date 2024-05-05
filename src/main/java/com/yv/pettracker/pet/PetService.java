package com.yv.pettracker.pet;

import com.yv.pettracker.pet.data.Pet;
import com.yv.pettracker.pet.data.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PetService {

    private final PetRepository petRepository;

    public Pet createPet(Exchange.PetCreateRequest request) {
        log.info("PetCreateRequest: {}", request);
        return petRepository.save(PetMapper.toPet(request));
    }

    public Optional<Pet> findPetById(Long id) {
        Objects.requireNonNull(id);
        return petRepository.findById(id);
    }

    public Long countPets(Exchange.PetType petType, Exchange.TrackerType trackerType, Boolean inZone) {
        return petRepository.countBy(petType, trackerType, inZone);
    }

}
