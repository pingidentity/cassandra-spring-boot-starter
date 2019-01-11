package cassandra4j.model.query;

import cassandra4j.model.Video;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface VideoQueries
{
    @Query("select video_id, release_year, title, description, release_date from videos where release_year >= :start and release_year < :end")
    Result<Video> findVideosByYear(@Param("start") int startYear, @Param("end") int endYear);
}
