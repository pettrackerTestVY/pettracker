package com.yv.pettracker.pet.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("pets")
public class Pet {

    @Id
    private Long id;
    private String name;
    private PetType petType;
    private Long ownerId;

}
