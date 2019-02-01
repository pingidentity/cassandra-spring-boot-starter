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
Cassandra auto configuration provides special support for datastax object mapper.
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

### Connection info
`cassandra.contact-points` - required. Comma separated list of initial contact points (ip addresses or host names)  
`cassandra.keyspace-name` - Required. Keyspace name to use for connecting session.  
`cassandra.cluster-name` -  Optional name of the cluster.  
`cassandra.port` - Port to connect to (9042 if omitted).   
`cassandra.protocol-version` - 	 (auto discovery will be used when ommited).  
`cassandra.allow-beta-protocol-version` - use latest development protocol version.     
`cassandra.ssl` - when set to `true` will enable ssl for connection, using JDK-based  implementation with the default options. 
If you need more control over ssl engine please refer to [Customization section](#customizing).  
`cassandra.no-compact` - enables `NO_COMPACT` option.   
`cassandra.max-schema-agreement-wait-seconds` - maximum time to wait for schema agreement before returning from a DDL query in seconds.  
`cassandra.address-translator` - address translator, supported options: *IDENTITY* (default) | *EC2* (EC2MultiRegionAddressTranslator)

### Socket options
`cassandra.socket.connect-timeout` - connection timeout in ms.  
`cassandra.socket.keep-alive` - TCP connection keep alive.  
`cassandra.socket.read-timeout` - per host read timeout in ms.  
`cassandra.socket.receive-buffer-size` - size of incoming network IO buffer in bytes.  
`cassandra.socket.send-buffer-size` - size of outgoing network IO buffer in bytes.  
`cassandra.socket.so-linger` - linger-on-close timeout.  
`cassandra.socket.reuse-address` - whether to enable address reuse.  
`cassandra.socket.tcp-no-delay` - TCP no delay (Nagle).  

### Connection pooling options
`cassandra.pool.heartbeat-interval-seconds` - heartbeat interval in seconds.  
`cassandra.pool.idle-timeout-seconds` - idle connection timeout in seconds.  
`cassandra.pool.pool-timeout-millis` - timeout for acquiring connection from pool in ms.  

`cassandra.pool.local.core-connections-per-host` - initial number of connections to each "local" host.  
`cassandra.pool.local.max-connections-per-host` - max number of connections to each "local" host.  
`cassandra.pool.local.max-requests-per-connection` - max number of requests per connections to "local" host.  
`cassandra.pool.local.new-connection-threshold` - threshold to trigger new connection to "local" host.  

`cassandra.pool.remote.core-connections-per-host` - initial number of connections to each "remote" host.  
`cassandra.pool.remote.max-connections-per-host` - max number of connections to each "remote" host.  
`cassandra.pool.remote.max-requests-per-connection` - max number of requests per connections to "remote" host.  
`cassandra.pool.remote.new-connection-threshold` - threshold to trigger new connection to "remote" host.  

### Compression
`cassandra.compression` Transport compression. Supported options *NONE* | *SNAPPY* | *LZ4*

### Authentication
`cassandra.auth.username` - Username to connect to cassandra host.  
`cassandra.auth.password` - Password to connect to cassandra host.  

Please see Customizing section if you want to use custom `AuthProvider`.

### Load balancing 
`cassandra.load-balancing-policy` - Load balancing policy to use with a cluster. Supported options: 
 - *ROUND_ROBIN* - for round robin load balancing.
 - *DC_AWARE* for datacenter aware round robin load balancing. 

#### Datacenter aware load balancing
`cassandra.dcaware.local-dc` - name of the "local" datacenter.  
`cassandra.dcaware.used-hosts-per-remote-dc` - Number of hosts per remote datacenter to consider.  
`cassandra.dcaware.allow-remote-dcs-for-local-consistency-level` - Allows the policy to return remote hosts for query plans with LOCAL_* consistency levels.  

#### Token aware load balancing
`cassandra.tokenaware.enabled` - set to true to use token awareness for primary load balancing policy
`cassandra.tokenaware.replica-ordering` - replica ordering strategy (*TOPOLOGICAL* | *RANDOM* | *NEUTRAL*) (RANDOM when ommited).  

#### Latency aware load balancing
`cassandra.latencyaware.scale` - scale in ms.  
`cassandra.latencyaware.retry-period` - retry period in ms.  
`cassandra.latencyaware.minimum-measurements` - minimum number of measurements per-host to consider.  
`cassandra.latencyaware.exclusion-threshold` - exclusion threshold.  
`cassandra.latencyaware.update-rate` - update rate in ms.  


### Query retries options
`cassandra.retry.policy` - retry policy, supported options: *DEFAULT* | *FALLTHROUGH*. 
`cassandra.retry.logging-enabled` - enable retry decision logging. 

### Reconnection options
`cassandra.reconnection.policy` - reconnection policy, supported options *CONSTANT* | *EXPONENTIAL*.  

`cassandra.reconnection.constant-delay` - delay between reconnection attempts in ms for *CONSTANT* policy.  

`cassandra.reconnection.exponential-base-delay` - base delay in ms for *EXPONENTIAL* policy.  
`cassandra.reconnection.exponential-max-delay` - max delay in ms for *EXPONENTIAL* policy.  

### Timestamp generation options
`cassandra.timestamp-generator` - client-side generator that will produce the timestamp sent with each query: 
 - *ATOMIC* - (default) monotonically increasing timestamps among all client threads
 - *THREAD_LOCAL* - monotonically increasing timestamps on per thread basis
 - *SERVER_SIDE* - let cassandra node assign server-side timestamp. 

`cassandra.atomic.warning-interval-sec` - how often drift warning logged in seconds for *ATOMIC* generator.  
`cassandra.atomic.warning-threshold-sec` - timestamps drift allowed before a warning in seconds for *ATOMIC* generator.  

`cassandra.thread-local.warning-interval-sec` - how often drift warning logged in seconds for *THREAD_LOCAL* generator.  
`cassandra.thread-local.warning-threshold-sec` - timestamps drift allowed before a warning in seconds for *THREAD_LOCAL* generator.  

### Speculative execution options
`cassandra.speculative-execution` - speculative query execution policy, possible options:
 - *NONE* - (default) no speculative execution
 - *CONSTANT* - speculative execution with fixed delay
 - *PERCENTILE* - speculative execution with latency percentile threshold, based on recent statistics, cluster wide or per host   
 
`cassandra.constant-execution.constant-delay-millis` - constant execution delay in millis.  
`cassandra.constant-execution.max-speculative-executions` - cap on total executions for constant strategy.   

`cassandra.percentile-execution.max-speculative-executions` - cap on total executions for percentile strategy.  
`cassandra.percentile-execution.percentile` - the percentile that a request's latency must fall into to be considered slow.  

`cassandra.percentile-execution.cluster-wide.highest-trackable-latency-ms` - highest latency to be tracked in ms for cluster wide stats.  
`cassandra.percentile-execution.cluster-wide.interval-ms` - sample interval for cluster wide stats.  
`cassandra.percentile-execution.cluster-wide.min-recorded-values` - minimum number of recorded values to consider sample for cluster wide stats.  
`cassandra.percentile-execution.cluster-wide.number-of-significant-value-digits` - number of significant decimal digits to maintain histogram resolution for cluster wide stats.  

`cassandra.percentile-execution.per-host.highest-trackable-latency-ms` - highest latency to be tracked in ms for host stats.  
`cassandra.percentile-execution.per-host.interval-ms` - sample interval for host stats.  
`cassandra.percentile-execution.per-host.min-recorded-values` - minimum number of recorded values to consider sample for host stats.  
`cassandra.percentile-execution.per-host.number-of-significant-value-digits` - number of significant decimal digits to maintain histogram resolution for host wide stats.  

### Query options
`cassandra.query.consistency-level` - default consistency level for queries.  
`cassandra.query.fetch-size` - default fetch size for SELECT queries.  
`cassandra.query.serial-consistency-level` - default serial consistency level to use for queries.  
`cassandra.query.default-idempotence` - default idempotence for queries.  
`cassandra.query.prepare-on-all-hosts` - whether the driver should prepare statements on all hosts in the cluster.  
`cassandra.query.reprepare-on-up` - whether the driver should re-prepare all cached prepared statements on a host when it's back up.  
`cassandra.query.metadata-enabled` - whether client-side token and schema metadata enabled.  
`cassandra.query.refresh-schema-interval-ms` - default schema refresh interval in ms.  
`cassandra.query.max-pending-refresh-schema-requests` - cap schema refresh requests for control connection.  
`cassandra.query.refresh-node-list-interval-ms` - default node list refresh interval in ms.  
`cassandra.query.max-pending-refresh-node-list-requests` - cap node list refresh requests for control connection.  
`cassandra.query.refresh-node-interval-ms` - default node refresh interval in ms.  
`cassandra.query.max-pending-refresh-node-requests` - cap node refresh requests for control connection.  

### Metrics options
`cassandra.metrics-enabled` - enables/disables metrics collection for cluster, enabled by default.  
`cassandra.jmx-reporting-enabled` - enables/disables metrics reporting over JMX, enabled by default.

### Misc options
`cassandra.scan-packages` - comma separated list of package to scan for mappers and accessors. See [auto binding section](#auto-binding-of-mappers--accessors) for detailed information.

## Customizing 
When properties is not enough cassandra auto configuration provides a way for programmatic customization of cluster configuration.
There are two customization interfaces avaliable:
 - `com.pingidentity.cassandra4j.CassandraConfigurationCustomizer` called before cluster instance is constructed and initialized, `Cluster.Builder` instance passed as argumet after all declarative configuration was applied.  
 - `com.pingidentity.cassandra4j.CassandraPostConfigurationCustomizer` called right after cluster instance is constructed. Fully initialized & connected `Cluster` instance is passed as argument.  
 
Define a corresponding beans that implements interfaces above in your spring context and they will be automatically applied.

For instance if you want custom address translator or auth provider you'd go with `CassandraConfigurationCustomizer`:

```Java
@Bean
public CassandraConfigurationCustomizer authCustomizer()
{
    return builder -> builder.withAuthProvider(new GSSAPIAuthProvider());
}
```

If you want to register extra codecs for your application your choice is `CassandraPostConfigurationCustomizer`:

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

You can define several customizers beans of both types in your application as well, they will be applied in order.

## Docs & Examples
Full blown microservice example can be found under [example](example) folder. It implements small
REST API backed by cassandra auto configuration. 

Refer to corresponding README for information how to run it.

