create keyspace cassandra4j_example with replication = {'class':'SimpleStrategy', 'replication_factor':1};

use cassandra4j_example;

CREATE TABLE videos (
    video_id uuid,
    title text,
    description text,
    release_date timestamp,
    release_year int,

    PRIMARY KEY(video_id)
);

CREATE CUSTOM INDEX videos_by_release_year_idx ON videos(release_year) USING 'org.apache.cassandra.index.sasi.SASIIndex';