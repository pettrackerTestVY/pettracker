package com.yv.pettracker.tracker;

import com.yv.pettracker.pet.PetNotFound;
import com.yv.pettracker.pet.PetService;
import com.yv.pettracker.tracker.data.Tracker;
import com.yv.pettracker.tracker.data.TrackerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrackerService {

    private final PetService petService;
    private final TrackerRepository trackerRepository;

    public Tracker register(Exchange.RegisterTrackerRequest request) {

        Optional<Tracker> bySerialNumber = trackerRepository.findBySerialNumber(request.serialNumber());

        if (bySerialNumber.isPresent()) {
            Tracker tracker = bySerialNumber.get();
            log.warn("Tracker already registered - id: {}, serialNumber: {}, petId: {}",
                    tracker.getId(), tracker.getSerialNumber(), tracker.getPetId());
            return bySerialNumber.get();
        }
        verifyPetExists(request);
        return trackerRepository.save(TrackerMapper.toTracker(request));
    }

    private void verifyPetExists(Exchange.RegisterTrackerRequest request) {
        petService.findPetById(request.petId()).orElseThrow(() -> new PetNotFound(request.petId()));
    }

    @Transactional
    public void track(Exchange.TrackData trackData) {
        Tracker tracker = requireTracker(trackData.trackerId());
        tracker.setInZone(trackData.inZone());
        tracker.setLostTracker(trackData.lostTracker());
        trackerRepository.save(tracker);
    }

    private Tracker requireTracker(Long id) {
        return trackerRepository.findByIdAndBlock(id)
                       .orElseThrow(() -> new TrackerNotFoundException(id));
    }

}
