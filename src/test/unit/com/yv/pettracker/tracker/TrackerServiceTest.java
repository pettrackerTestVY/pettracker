package com.yv.pettracker.tracker;

import com.yv.pettracker.pet.PetNotFound;
import com.yv.pettracker.pet.PetService;
import com.yv.pettracker.pet.data.Pet;
import com.yv.pettracker.tracker.data.Tracker;
import com.yv.pettracker.tracker.data.TrackerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrackerServiceTest {

    private static final Boolean IN_ZONE = Boolean.TRUE;
    private static final Boolean LOST_TRACKER = Boolean.FALSE;

    @Mock
    private PetService petService;
    @Mock
    private TrackerRepository trackerRepository;

    @InjectMocks
    private TrackerService trackerService;

    @Test
    void shouldRegisterTrackerTest() {
        //given
        Exchange.RegisterTrackerRequest registerTrackerRequest = getRegisterTrackerRequest();

        //when
        when(trackerRepository.findBySerialNumber(registerTrackerRequest.serialNumber())).thenReturn(Optional.empty());
        when(petService.findPetById(registerTrackerRequest.petId())).thenReturn(Optional.of(new Pet()));

        //then
        trackerService.register(registerTrackerRequest);

        verify(trackerRepository, atMostOnce()).save(argThat(tracker -> {
                            assertEquals(registerTrackerRequest.serialNumber(), tracker.getSerialNumber());
                            assertEquals(registerTrackerRequest.trackerType().toString(), tracker.getTrackerType().toString());
                            assertEquals(registerTrackerRequest.petId(), tracker.getPetId());
                            return true;
                        }
                )
        );
    }

    @Test
    void shouldDoNothingIfTrackerAlreadyExistsTest() {
        //given
        Exchange.RegisterTrackerRequest registerTrackerRequest = getRegisterTrackerRequest();

        //when
        when(trackerRepository.findBySerialNumber(registerTrackerRequest.serialNumber())).thenReturn(Optional.of(new Tracker()));

        //then
        trackerService.register(registerTrackerRequest);

        verify(trackerRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionIfPetNotExistTest() {
        //given
        Exchange.RegisterTrackerRequest registerTrackerRequest = getRegisterTrackerRequest();

        //when
        when(trackerRepository.findBySerialNumber(registerTrackerRequest.serialNumber())).thenReturn(Optional.empty());
        when(petService.findPetById(any())).thenReturn(Optional.empty());

        //then
        assertThrows(PetNotFound.class, () -> trackerService.register(registerTrackerRequest));
    }

    @Test
    void shouldSaveTrackDataTest() {
        //given
        Exchange.TrackData trackData = getTrackData();
        Tracker tracker = new Tracker();
        assertNull(tracker.getInZone());
        assertNull(tracker.getLostTracker());

        //when
        when(trackerRepository.findByIdAndBlock(trackData.trackerId())).thenReturn(Optional.of(tracker));
        //then
        trackerService.track(trackData);
        verify(trackerRepository, atLeastOnce()).save(argThat(
                        updated -> {
                            assertEquals(IN_ZONE, updated.getInZone());
                            assertEquals(LOST_TRACKER, updated.getLostTracker());
                            return tracker == updated;
                        }
                )
        );
    }

    @Test
    void shouldThrowExceptionIfTrackerNotFoundTest() {
        //given
        Exchange.TrackData trackData = getTrackData();
        //when
        when(trackerRepository.findByIdAndBlock(any())).thenReturn(Optional.empty());
        //then
        assertThrows(TrackerNotFoundException.class, () -> trackerService.track(trackData));
    }

    private static Exchange.TrackData getTrackData() {
        return new Exchange.TrackData(1L, IN_ZONE, LOST_TRACKER);
    }

    private static Exchange.RegisterTrackerRequest getRegisterTrackerRequest() {
        return new Exchange.RegisterTrackerRequest(UUID.randomUUID(),
                Exchange.TrackerType.BIG,
                1L);
    }

}
