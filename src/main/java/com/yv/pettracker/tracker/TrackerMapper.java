package com.yv.pettracker.tracker;

import com.yv.pettracker.tracker.data.Tracker;
import com.yv.pettracker.tracker.data.TrackerType;
import lombok.experimental.UtilityClass;

@UtilityClass
class TrackerMapper {

    public Tracker toTracker(Exchange.RegisterTrackerRequest request) {
        Tracker tracker = new Tracker();
        tracker.setSerialNumber(request.serialNumber());
        tracker.setTrackerType(toType(request.trackerType()));
        tracker.setPetId(request.petId());
        return tracker;
    }

    private TrackerType toType(Exchange.TrackerType trackerType) {
        return switch (trackerType) {
            case SMALL -> TrackerType.SMALL;
            case MEDIUM -> TrackerType.MEDIUM;
            case BIG -> TrackerType.BIG;
        };
    }

}
