package cassandra4j.model;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Table(name="videos")
public class Video
{
    public enum Genre { thriller, drama, comedy, horror, crime, romance, animation, documentary, biography }

    @PartitionKey(0)
    @Column(name="video_id")
    private UUID videoId;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="release_date")
    @JsonFormat(pattern="yyyy/MM/dd")
    private Date date;

    @Column(name="release_year")
    private int year;

    @Column(name="genres")
    private Set<Genre> genres;

    public UUID getVideoId()
    {
        return videoId;
    }

    public void setVideoId(UUID videoId)
    {
        this.videoId = videoId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public Set<Genre> getGenres()
    {
        return genres;
    }

    public void setGenres(Set<Genre> genres)
    {
        this.genres = genres;
    }
}
