package com.yv.pettracker.tracker.data;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface TrackerRepository extends CrudRepository<Tracker, Long> {

    Optional<Tracker> findBySerialNumber(UUID uuid);

    @Query("select * from trackers t where t.id=:id for update ")
    Optional<Tracker> findByIdAndBlock(Long id);

}
