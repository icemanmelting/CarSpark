CREATE KEYSPACE IF NOT EXISTS maindata WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};
%%
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
%%
CREATE TABLE IF NOT EXISTS maindata.speed_data (
    id TIMEUUID,
    trip_id TIMEUUID,
    speed DOUBLE,
    rpm DOUBLE,
    gear INT,
    ts TIMESTAMP,

  PRIMARY KEY (id, ts)
) WITH CLUSTERING ORDER BY (ts DESC);
%%
CREATE TABLE IF NOT EXISTS maindata.temperature_data (
    id TIMEUUID,
    trip_id TIMEUUID,
    value DOUBLE,
    ts TIMESTAMP,

  PRIMARY KEY (id, ts)
) WITH CLUSTERING ORDER BY (ts DESC);
%%
CREATE TABLE IF NOT EXISTS maindata.car_logs (
    id TIMEUUID,
    message TEXT,
    ts TIMESTAMP,
    log_level TEXT,

  PRIMARY KEY (id, ts)
) WITH CLUSTERING ORDER BY (ts DESC);;
%%
CREATE TABLE IF NOT EXISTS maindata.car_trips (
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

    PRIMARY KEY (id, start_time)
) WITH CLUSTERING ORDER BY (start_time DESC);
%%
CREATE TABLE IF NOT EXISTS maindata.processed_speed_data (
    trip_id TIMEUUID,
    data SET<FROZEN<TUPLE<TIMESTAMP, DOUBLE>>>,

    PRIMARY KEY (trip_id)
);
%%
CREATE TABLE IF NOT EXISTS maindata.processed_temperature_data (
    trip_id TIMEUUID,
    data SET<FROZEN<TUPLE<TIMESTAMP, DOUBLE>>>,

    PRIMARY KEY (trip_id)
);
%%
CREATE TABLE IF NOT EXISTS maindata.processed_rpm_data (
    trip_id TIMEUUID,
    data SET<FROZEN<TUPLE<TIMESTAMP, DOUBLE>>>,

    PRIMARY KEY (trip_id)
);
%%
CREATE TABLE IF NOT EXISTS maindata.processed_gear_data (
    trip_id TIMEUUID,
    data SET<FROZEN<TUPLE<TIMESTAMP, DOUBLE>>>,
    gear_stats MAP<INT, BIGINT>,

    PRIMARY KEY (trip_id)
);
