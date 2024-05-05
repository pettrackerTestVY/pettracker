package com.yv.pettracker.tracker;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class Exchange {

    public enum TrackerType {
        SMALL, MEDIUM, BIG
    }

    public record RegisterTrackerRequest(@NotNull UUID serialNumber,
                                         @NotNull TrackerType trackerType,
                                         @Positive Long petId) {

    }

    public record TrackData(@Positive Long trackerId,
                            @NotNull Boolean inZone,
                            Boolean lostTracker) {

    }

}
