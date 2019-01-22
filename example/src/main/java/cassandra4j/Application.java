package cassandra4j;

import cassandra4j.model.Video;
import com.datastax.driver.extras.codecs.enums.EnumNameCodec;
import com.pingidentity.cassandra4j.CassandraConfigurationCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application
{
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CassandraConfigurationCustomizer codecCustomizer()
    {
        return cluster ->
        {
            cluster.getConfiguration().getCodecRegistry().register(new EnumNameCodec<>(Video.Genre.class));

            return cluster;
        };
    }
}
