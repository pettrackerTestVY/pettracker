CREATE TABLE IF NOT EXISTS pets
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR    NOT NULL,
    pet_type VARCHAR(3) NOT NULL,
    owner_id BIGINT     NOT NULL,
    CONSTRAINT unique_name_owner UNIQUE (name, owner_id)
);

CREATE TABLE IF NOT EXISTS trackers
(
    id            BIGSERIAL PRIMARY KEY,
    serial_number UUID UNIQUE    NOT NULL,
    tracker_type  VARCHAR(10) NOT NULL,
    in_zone       BOOLEAN,
    lost_tracker  BOOLEAN,
    pet_id        BIGINT      NOT NULL,
    FOREIGN KEY (pet_id) REFERENCES pets (id)
);
