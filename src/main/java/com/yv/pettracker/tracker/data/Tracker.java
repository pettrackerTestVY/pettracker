package com.yv.pettracker.tracker.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@Table("trackers")
public class Tracker {

    @Id
    private Long id;
    private UUID serialNumber;
    private TrackerType trackerType;
    private Boolean inZone;
    private Boolean lostTracker;
    private Long petId;

}
