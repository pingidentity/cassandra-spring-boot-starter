package cassandra4j.controllers;

import cassandra4j.model.Video;
import cassandra4j.model.query.VideoQueries;
import com.datastax.driver.mapping.Mapper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/videos")
public class VideosController
{
    private Mapper<Video> videos;
    private VideoQueries queries;

    public VideosController(Mapper<Video> videos, VideoQueries queries)
    {
        this.videos = videos;
        this.queries = queries;
    }

    @GetMapping("/{id}")
    public Video findById(@PathVariable("id") UUID videoId)
    {
        return videos.get(videoId);
    }

    @PostMapping
    public Video createVideo(@RequestBody Video video)
    {
        video.setVideoId(UUID.randomUUID());

        videos.save(video);

        return video;
    }

    @PutMapping("/{id}")
    public Video updateVideo(@PathVariable("id") UUID videoId, @RequestBody Video video)
    {
        video.setVideoId(videoId);

        videos.save(video);

        return video;
    }

    @DeleteMapping("/{id}")
    public void deleteVideo(@PathVariable("id") UUID videoId)
    {
        videos.delete(videoId);
    }

    @GetMapping
    public Collection<Video> findByYear(@RequestParam("start") int startYear, @RequestParam("end") int endYear)
    {
        return queries.findVideosByYear(startYear, endYear).all();
    }
}
