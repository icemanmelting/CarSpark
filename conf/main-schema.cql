DROP KEYSPACE IF EXISTS maindata;
CREATE KEYSPACE IF NOT EXISTS maindata WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};
USE maindata;

CREATE TABLE IF NOT EXISTS maindata.car_settings (
    id BIGINT,
    constant_kilometers DOUBLE,
    trip_kilometers DOUBLE,
    trip_initial_fuel_level DOUBLE,
    average_fuel_consumption DOUBLE,
    dashboard_type TEXT,
    tyre_offset DOUBLE,
    next_oil_change DOUBLE,
    serial_port TEXT,

  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS maindata.speed_data (
    id TIMEUUID,
    speed DOUBLE,
    rpm DOUBLE,
    gear INT,
    timeframe TIMESTAMP,

  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS maindata.temperature_data (
    id TIMEUUID,
    value DOUBLE,
    timeframe TIMESTAMP,

  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS maindata.car_logs (
    id TIMEUUID,
    message TEXT,
    timeframe TIMESTAMP,
    log_level TEXT,

  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS maindata.car_trip (
    id TIMEUUID,
    starting_km DOUBLE,
    ending_km DOUBLE,
    trip_length_km DOUBLE,
    max_temperature DOUBLE,
    max_speed DOUBLE,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    trip_duration DOUBLE,
    average_speed DOUBLE,

    PRIMARY KEY (id)
);
