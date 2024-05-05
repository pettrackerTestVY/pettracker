package com.yv.pettracker.pet.data;

import org.springframework.data.repository.CrudRepository;

public interface PetCrudRepository extends CrudRepository<Pet, Long> {

}
