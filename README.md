# Cassandra spring-boot starter
Provides pure cassandra datastax driver spring-boot autoconfiguration with optional health check.
Easy cassandra getting started experience without spring-data complexity or if you prefer to be closer to driver level.
Declarative configuration for all supported datastax driver options and automatic spring bindings for mappers and query accessors.


## Note about versioning
cassandra spring-boot starter versions are aligned with spring-boot versions to guarantee compatibility.
If you using spring-boot v2.1.x stick with v2.1.x cassandra starter version. (v1.5.x for spring-boot v1.5.x, e.t.c.)

We trying to support latest cassandra driver versions as they released.

## Getting started
Reference library from maven central:
```xml
<dependency>
    <groupId>com.pingidentity.oss.cassandra4j</groupId>
    <artifactId>spring-boot-cassandra-auto-configuration</artifactId>
    <version>2.1.0</version>
</dependency>
```

For cassandra running on localhost add following to your spring-boot `application.properties`:
```Java Properties
cassandra.contact-points=127.0.0.1
cassandra.keyspace-name=my_app_keyspace
```

Inject driver session and query data:
```Java
@Autowired
private Session session;

// somewhere in the code

ResultSet rs = session.execute("select release_version from system.local");

// work your magic

```

## Registered beans
Auto configration makes following beans avaliable for autowiring anywhere:

- `cluster` (com.datastax.driver.core.Cluster), fully initialized instance, singleton.
- `session` (com.datastax.driver.core.Session), connected session, ready to execute queries, singleton.
- `mappingManager` (com.datastax.driver.mapping.MappingManager), object mapper associated with session.

## Auto-binding of mappers & accessors
Cassandra auto configuration provides special support for datastax ObjectMapper.
When enabled it can discover and automatically register bean mappers and accessors with spring context.

First, let configuration know where your pojo and accessors resides: 

```Java Properties
cassandra.scan-packages=io.gitub.yourapp.model
```

Auto configuration will look for `@Table` and `@Accessor` annotated classes:

```Java
@Table
public class Video {...}

@Accessor
public interface VideoQueries {...}
```

and register corresponding `Mapper<Video>` and `VideoQueries` implementations with spring. 
Those can be simply injected and used in your code with no extra coding:

```Java
@Autowired
private Mapper<Video> videoMapper;

@Autowired
private VideoQueries videoQueries;
```		

## Health check
Cassandra auto configuration provides spring-boot health indicator with id `cassandra` for probing cluster connection.
Automatically enabled when spring-boot-starter-actuator dependency is present. Can be controlled normal way with `management.health.cassandra.enabled` property.

## Configuration


| Property      | Description   | Comments |
| ------------- | ------------- | ------------- |
| contact-points  | Required. Comma separated list of initial contact points (ip addresses or host names)   |
| keyspace-name    | Required. Keyspace name to use for connecting session.  |
| cluster-name     | Optional name of the cluster.  |
| port            | Port to connect to.  | 9042 |
| protocol-version  | Native protocol version to use (1 to 5)  |
| compression   | Transport compression (NONE \| SNAPPY \| LZ4)  |
| auth.username | Username to connect to cassandra host  |
| auth.password | Password to connect to cassandra host  |
| load-balancing-policy | Load balancing policy to use with a cluster (DC_AWARE \| ROUND_ROBIN) |
| dcaware.local-dc | (when load-balancing-policy == DC_AWARE) name of the "local" datacenter |
| dcaware.used-hosts-per-remote-dc | (when load-balancing-policy == DC_AWARE) Number of hosts per remote datacenter to consider. |
| dcaware.allow-remote-d-cs-for-local-consistency-level | (when load-balancing-policy == DC_AWARE) Allows the policy to return remote hosts for query plans with LOCAL_* consistency levels |

## Customizing 
When properties is not enough cassandra auto configuration provides a way for programmatic customization of cluster configuration.

Define a bean that implements `com.pingidentity.cassandra4j.CassandraConfigurationCustomizer` in your spring context
and it will be called with fully initialized instance of `Cluster`. 

For instance if you want to register extra codecs for your application:

```Java
@Bean
public CassandraConfigurationCustomizer codecCustomizer()
{
    return cluster ->
    {
        //custom enum codec
        cluster.getConfiguration().getCodecRegistry().register(new EnumNameCodec<>(Video.Genre.class));
        
        //codec to handle java.time.Instant type
        cluster.getConfiguration().getCodecRegistry().register(InstantCodec.instance);

        return cluster;
    };
}
```

You can define several customizers in your application as well. 

## Docs & Examples
There is full blown microservice example under [example](example) folder. It implements small
REST API backed by cassandra auto configuration. 

Clone folder and launch it locally with `mvn clean spring-boot:run` or explore sources.

