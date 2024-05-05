package com.yv.pettracker.tracker;

public class TrackerNotFoundException extends RuntimeException {

    public TrackerNotFoundException(Long id) {
        super(String.format("Tracker id: %s", id));
    }

}
