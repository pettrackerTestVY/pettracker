package com.yv.pettracker.tracker;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tracker")
public class TrackerController {

    private final TrackerService trackerService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public Long register(@Valid @RequestBody Exchange.RegisterTrackerRequest request) {
        return trackerService.register(request).getId();
    }

    @PostMapping("/track")
    @ResponseStatus(HttpStatus.OK)
    public void track(@Valid @RequestBody Exchange.TrackData trackData) {
        trackerService.track(trackData);
    }

}
