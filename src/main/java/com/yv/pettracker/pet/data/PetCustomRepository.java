package com.yv.pettracker.pet.data;

import com.yv.pettracker.pet.Exchange;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class PetCustomRepository implements PetRepository {

    private static final String SELECT_COUNT_BASE_SQL = """ 
            SELECT COUNT(*) FROM pets p JOIN trackers t ON p.id = t.pet_id WHERE 1=1
            """;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final PetCrudRepository delegate;

    public PetCustomRepository(JdbcTemplate jdbcTemplate, PetCrudRepository delegate) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.delegate = delegate;
    }

    @Override
    public Long countBy(Exchange.PetType petType, Exchange.TrackerType trackerType, Boolean inZone) {
        StringBuilder sql = new StringBuilder(SELECT_COUNT_BASE_SQL);
        Map<String, Object> params = new HashMap<>();

        if (petType != null) {
            sql.append(" AND p.pet_type = :petType");
            params.put("petType", petType.name());
        }

        if (trackerType != null) {
            sql.append(" AND t.tracker_type = :trackerType");
            params.put("trackerType", trackerType.name());
        }

        if (inZone != null) {
            sql.append(" AND t.in_zone = :inZone");
            params.put("inZone", inZone);
        }

        return namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Long.class);
    }

    @Override
    public Pet save(Pet pet) {
        return delegate.save(pet);
    }

    @Override
    public Optional<Pet> findById(Long id) {
        return delegate.findById(id);
    }

}
