package com.yv.pettracker.pet;

import com.yv.pettracker.pet.data.Pet;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/pets")
public class PetController {

    private final PetService petService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Exchange.PetCreatedResponse createPet(@Valid @RequestBody Exchange.PetCreateRequest request) {
        Pet created = petService.createPet(request);
        return new Exchange.PetCreatedResponse(created.getId());
    }

    @GetMapping("/count")
    private Long countBy(@RequestParam(required = false) Exchange.PetType petType,
                         @RequestParam(required = false) Exchange.TrackerType trackerType,
                         @RequestParam(required = false) Boolean inZone) {
        return petService.countPets(petType, trackerType, inZone);
    }

}
