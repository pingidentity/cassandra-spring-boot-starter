# Cassandra spring-boot starter
Provides pure cassandra datastax driver spring-boot autoconfiguration with optional health check.
Easy cassandra getting started expirience without spring-data complexity or if you prefer to be closer to driver level.
Declarative configuration for all supported datastax driver options and automatic spring bindings for mappers and query accessors.


## Note about versioning
cassandra spring-boot starter versions are aligned with spring-boot versions to guarantee compatibility.
If you using spring-boot v2.1.x stick with v2.1.x cassandra starter version. (v1.5.x for spring-boot v1.5.x, e.t.c.)

We trying to support latest cassandra driver versions as they released.

## Getting started
### Maven
```xml
<dependency>
    <groupId>com.pingidentity.oss.cassandra4j</groupId>
    <artifactId>spring-boot-cassandra-auto-configuration</artifactId>
    <version>2.1.0</version>
</dependency>
```

### Minimal configuration
For cassandra running on localhost add following to your spring-boot `application.properties`:
```Java Properties
cassandra.contact-points=127.0.0.1
cassandra.keyspaceName=my\_app\_keyspace
```

### Get session and query data
```Java
@Autowired
private Session session;

.............
ResultSet rs = session.execute("select release_version from system.local");
// work your magic

```

## Registered beans
Auto configration makes following beans avaliable for autowiring anywhere:

- `cluster` (com.datastax.driver.core.Cluster), fully initialized instance, singleton
- `session` (com.datastax.driver.core.Session), connected session, ready to execute queries, singleton
- `mappingManager` (com.datastax.driver.mapping.MappingManager), object mapper associated with session

## Auto-binding of mappers & accessors

## Configuration

## Customizing 

## Docs & Examples

