package com.yv.pettracker.pet.data;

import com.yv.pettracker.pet.Exchange;

import java.util.Optional;

public interface PetRepository {

    Long countBy(Exchange.PetType petType, Exchange.TrackerType trackerType, Boolean inZone);

    Pet save(Pet pet);

    Optional<Pet> findById(Long id);

}
