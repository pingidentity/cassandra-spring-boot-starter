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
Cassandra auto configuration provides spring-boot HealthIndicator with id `cassandra` for probing cluster connection.
Automatically enabled when spring-boot-starter-actuator dependency is present. Can be controlled normal way with `management.health.cassandra.enabled` property.

## Configuration

## Customizing 

## Docs & Examples

