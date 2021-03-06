/***************************************************************************
 * Copyright (C) 2005-2019 Ping Identity Corporation
 * All rights reserved.
 *
 * The contents of this file are the property of Ping Identity Corporation.
 * You may not copy or use this file, in either source code or executable
 * form, except in compliance with terms set by Ping Identity Corporation.
 * For further information please contact:
 *
 *      Ping Identity Corporation
 *      1001 17th Street
 *      Suite 100
 *      Denver, CO 80202
 *      303.468.2900
 *      http://www.pingidentity.com
 *
 **************************************************************************/

package com.pingidentity.cassandra4j.springboot;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ProtocolOptions;
import com.datastax.driver.core.policies.TokenAwarePolicy.ReplicaOrdering;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cassandra")
public class CassandraProperties
{
    public enum LoadBalancingPolicies { DC_AWARE, ROUND_ROBIN }
    public enum RetryPolicies { DEFAULT, FALLTHROUGH }
    public enum ReconnectionPolicies { CONSTANT, EXPONENTIAL }
    public enum AddressTranslator { IDENTITY, EC2 }
    public enum TimestampGenerator { SERVER_SIDE, ATOMIC, THREAD_LOCAL }
    public enum SpeculativeExecution { NONE, CONSTANT, PERCENTILE}

    /**
     * Name of cluster
     */
    private String clusterName;

    /**
     * Keyspace name
     */
    private String keyspaceName;

    /**
     *  List of cluster contact points, comma separated
     */
    private String[] contactPoints;

    /**
     * Port to connect to, 9042 if omitted.
     */
    private Integer port;

    /**
     * Native protocol version to use (1 to 5)
     */
    private Integer protocolVersion;

    /**
     * Use latest development protocol version
     */
    private boolean allowBetaProtocolVersion;

    /**
     * Enable ssl for connection
     */
    private boolean ssl;

    /**
     * Enable 'NO_COMPACT' option
     */
    private boolean noCompact;

    /**
     * Max timeout for schema agreement, seconds
     */
    private Integer maxSchemaAgreementWaitSeconds;

    /**
     * Client-side timestamp generator strategy
     */
    private TimestampGenerator timestampGenerator;
    private Generator atomic;
    private Generator threadLocal;

    /**
     * Address translator
     */
    private AddressTranslator addressTranslator;

    /**
     * Speculative query execution policy
     */
    private SpeculativeExecution speculativeExecution;
    private ConstantExecution constantExecution;
    private PercentileExecution percentileExecution;

    /**
     * Transport compression.
     */
    private ProtocolOptions.Compression compression;
    private Auth auth;

    /**
     * Load-balancing strategy.
     */
    private LoadBalancingPolicies loadBalancingPolicy;
    private DcAwarePolicy dcaware;
    private LatencyAwarePolicy latencyaware;
    private TokenAwarePolicy tokenaware;
    private SocketOptions socket;
    private PoolOptions pool;
    private RetryPolicyOptions retry;
    private ReconnectionPolicyOptions reconnection;
    private QueryPolicyOptions query;

    /**
     * Metrics collection for cluster
     */
    private boolean metricsEnabled=true;

    /**
     * Metrics reporting over JMX
     */
    private boolean jmxReportingEnabled=true;

    /**
     * Packages to scan for mappers and accessors, comma separated.
     */
    private String scanPackages;

    public static class Generator
    {
        /**
         * Timestamps drift before a warning, seconds
         */
        private  long warningThresholdSec;

        /**
         * Drift warning interval, seconds
         */
        private long warningIntervalSec;

        public long getWarningThresholdSec()
        {
            return warningThresholdSec;
        }

        public void setWarningThresholdSec(long warningThresholdSec)
        {
            this.warningThresholdSec = warningThresholdSec;
        }

        public long getWarningIntervalSec()
        {
            return warningIntervalSec;
        }

        public void setWarningIntervalSec(long warningIntervalSec)
        {
            this.warningIntervalSec = warningIntervalSec;
        }
    }

    public static class Auth
    {
        /**
         * Username to connect to cassandra cluster.
         */
        private String username;

        /**
         * Password to connect to cassandra cluster.
         */
        private String password;

        public String getUsername()
        {
            return username;
        }

        public void setUsername(String username)
        {
            this.username = username;
        }

        public String getPassword()
        {
            return password;
        }

        public void setPassword(String password)
        {
            this.password = password;
        }
    }

    public static class QueryPolicyOptions
    {
        /**
         * Default consistency level
         */
        private ConsistencyLevel consistencyLevel;

        /**
         * Default serial consistency level
         */
        private ConsistencyLevel serialConsistencyLevel;

        /**
         * Default fetch size for SELECT queries
         */
        private Integer fetchSize;

        /**
         * Default idempotence.
         */
        private Boolean defaultIdempotence;

        /**
         * Prepare statements on all hosts in the cluster
         */
        private Boolean prepareOnAllHosts;

        /**
         * Re-prepare all cached prepared statements on node restart
         */
        private Boolean reprepareOnUp;

        /**
         * Client-side token and schema metadata
         */
        private Boolean metadataEnabled;

        /**
         * Default schema refresh interval, ms
         */
        private Integer refreshSchemaIntervalMs;

        /**
         * Max schema refresh requests for control connection
         */
        private Integer maxPendingRefreshSchemaRequests;

        /**
         * Default node list refresh interval, ms
         */
        private Integer refreshNodeListIntervalMs;

        /**
         * Max node list refresh requests for control connection
         */
        private Integer maxPendingRefreshNodeListRequests;

        /**
         * Default node refresh interval, ms
         */
        private Integer refreshNodeIntervalMs;

        /**
         * Max node refresh requests for control connection
         */
        private Integer maxPendingRefreshNodeRequests;

        public ConsistencyLevel getConsistencyLevel()
        {
            return consistencyLevel;
        }

        public void setConsistencyLevel(ConsistencyLevel consistencyLevel)
        {
            this.consistencyLevel = consistencyLevel;
        }

        public ConsistencyLevel getSerialConsistencyLevel()
        {
            return serialConsistencyLevel;
        }

        public void setSerialConsistencyLevel(ConsistencyLevel serialConsistencyLevel)
        {
            this.serialConsistencyLevel = serialConsistencyLevel;
        }

        public Integer getFetchSize()
        {
            return fetchSize;
        }

        public void setFetchSize(Integer fetchSize)
        {
            this.fetchSize = fetchSize;
        }

        public Boolean getDefaultIdempotence()
        {
            return defaultIdempotence;
        }

        public void setDefaultIdempotence(Boolean defaultIdempotence)
        {
            this.defaultIdempotence = defaultIdempotence;
        }

        public Boolean getPrepareOnAllHosts()
        {
            return prepareOnAllHosts;
        }

        public void setPrepareOnAllHosts(Boolean prepareOnAllHosts)
        {
            this.prepareOnAllHosts = prepareOnAllHosts;
        }

        public Boolean getReprepareOnUp()
        {
            return reprepareOnUp;
        }

        public void setReprepareOnUp(Boolean reprepareOnUp)
        {
            this.reprepareOnUp = reprepareOnUp;
        }

        public Boolean getMetadataEnabled()
        {
            return metadataEnabled;
        }

        public void setMetadataEnabled(Boolean metadataEnabled)
        {
            this.metadataEnabled = metadataEnabled;
        }

        public Integer getRefreshSchemaIntervalMs()
        {
            return refreshSchemaIntervalMs;
        }

        public void setRefreshSchemaIntervalMs(Integer refreshSchemaIntervalMs)
        {
            this.refreshSchemaIntervalMs = refreshSchemaIntervalMs;
        }

        public Integer getMaxPendingRefreshSchemaRequests()
        {
            return maxPendingRefreshSchemaRequests;
        }

        public void setMaxPendingRefreshSchemaRequests(Integer maxPendingRefreshSchemaRequests)
        {
            this.maxPendingRefreshSchemaRequests = maxPendingRefreshSchemaRequests;
        }

        public Integer getRefreshNodeListIntervalMs()
        {
            return refreshNodeListIntervalMs;
        }

        public void setRefreshNodeListIntervalMs(Integer refreshNodeListIntervalMs)
        {
            this.refreshNodeListIntervalMs = refreshNodeListIntervalMs;
        }

        public Integer getMaxPendingRefreshNodeListRequests()
        {
            return maxPendingRefreshNodeListRequests;
        }

        public void setMaxPendingRefreshNodeListRequests(Integer maxPendingRefreshNodeListRequests)
        {
            this.maxPendingRefreshNodeListRequests = maxPendingRefreshNodeListRequests;
        }

        public Integer getRefreshNodeIntervalMs()
        {
            return refreshNodeIntervalMs;
        }

        public void setRefreshNodeIntervalMs(Integer refreshNodeIntervalMs)
        {
            this.refreshNodeIntervalMs = refreshNodeIntervalMs;
        }

        public Integer getMaxPendingRefreshNodeRequests()
        {
            return maxPendingRefreshNodeRequests;
        }

        public void setMaxPendingRefreshNodeRequests(Integer maxPendingRefreshNodeRequests)
        {
            this.maxPendingRefreshNodeRequests = maxPendingRefreshNodeRequests;
        }
    }

    public static class ReconnectionPolicyOptions
    {
        /**
         * Reconnection strategy.
         */
        private ReconnectionPolicies policy;

        /**
         * Constant delay between reconnection attempts, ms
         */
        private long constantDelay;

        /**
         * Exponential delay base, ms
         */
        private long exponentialBaseDelay;

        /**
         * Exponential delay max, ms
         */
        private long exponentialMaxDelay;

        public ReconnectionPolicies getPolicy()
        {
            return policy;
        }

        public void setPolicy(ReconnectionPolicies policy)
        {
            this.policy = policy;
        }

        public long getConstantDelay()
        {
            return constantDelay;
        }

        public void setConstantDelay(long constantDelay)
        {
            this.constantDelay = constantDelay;
        }

        public long getExponentialBaseDelay()
        {
            return exponentialBaseDelay;
        }

        public void setExponentialBaseDelay(long exponentialBaseDelay)
        {
            this.exponentialBaseDelay = exponentialBaseDelay;
        }

        public long getExponentialMaxDelay()
        {
            return exponentialMaxDelay;
        }

        public void setExponentialMaxDelay(long exponentialMaxDelay)
        {
            this.exponentialMaxDelay = exponentialMaxDelay;
        }
    }

    public static class RetryPolicyOptions
    {
        /**
         * Retry decisions logging.
         */
        private boolean loggingEnabled;

        /**
         * Retry strategy.
         */
        private RetryPolicies policy;

        public boolean isLoggingEnabled()
        {
            return loggingEnabled;
        }

        public void setLoggingEnabled(boolean loggingEnabled)
        {
            this.loggingEnabled = loggingEnabled;
        }

        public RetryPolicies getPolicy()
        {
            return policy;
        }

        public void setPolicy(RetryPolicies policy)
        {
            this.policy = policy;
        }
    }

    public static class PoolOptions
    {
        /**
         * Heartbeat interval, seconds.
         */
        private int heartbeatIntervalSeconds;

        /**
         * Idle connection timeout, seconds.
         */
        private int idleTimeoutSeconds;

        /**
         * Pool connection acquiring timeout, ms.
         */
        private int poolTimeoutMillis;

        private PoolConfig local;
        private PoolConfig remote;

        public int getHeartbeatIntervalSeconds()
        {
            return heartbeatIntervalSeconds;
        }

        public void setHeartbeatIntervalSeconds(int heartbeatIntervalSeconds)
        {
            this.heartbeatIntervalSeconds = heartbeatIntervalSeconds;
        }

        public int getIdleTimeoutSeconds()
        {
            return idleTimeoutSeconds;
        }

        public void setIdleTimeoutSeconds(int idleTimeoutSeconds)
        {
            this.idleTimeoutSeconds = idleTimeoutSeconds;
        }

        public int getPoolTimeoutMillis()
        {
            return poolTimeoutMillis;
        }

        public void setPoolTimeoutMillis(int poolTimeoutMillis)
        {
            this.poolTimeoutMillis = poolTimeoutMillis;
        }

        public PoolConfig getLocal()
        {
            return local;
        }

        public void setLocal(PoolConfig local)
        {
            this.local = local;
        }

        public PoolConfig getRemote()
        {
            return remote;
        }

        public void setRemote(PoolConfig remote)
        {
            this.remote = remote;
        }

        public static class PoolConfig
        {
            /**
             * Initial number of connections to each host
             */
            private int coreConnectionsPerHost;

            /**
             * Max number of connections to each host
             */
            private int maxConnectionsPerHost;

            /**
             * Max number of requests for each connection
             */
            private int maxRequestsPerConnection;

            /**
             * Threshold to trigger new connection to host
             */
            private int newConnectionThreshold;

            public int getCoreConnectionsPerHost()
            {
                return coreConnectionsPerHost;
            }

            public void setCoreConnectionsPerHost(int coreConnectionsPerHost)
            {
                this.coreConnectionsPerHost = coreConnectionsPerHost;
            }

            public int getMaxConnectionsPerHost()
            {
                return maxConnectionsPerHost;
            }

            public void setMaxConnectionsPerHost(int maxConnectionsPerHost)
            {
                this.maxConnectionsPerHost = maxConnectionsPerHost;
            }

            public int getMaxRequestsPerConnection()
            {
                return maxRequestsPerConnection;
            }

            public void setMaxRequestsPerConnection(int maxRequestsPerConnection)
            {
                this.maxRequestsPerConnection = maxRequestsPerConnection;
            }

            public int getNewConnectionThreshold()
            {
                return newConnectionThreshold;
            }

            public void setNewConnectionThreshold(int newConnectionThreshold)
            {
                this.newConnectionThreshold = newConnectionThreshold;
            }
        }
    }

    public static class SocketOptions
    {
        /**
         * Connection timeout, ms
         */
        private int connectTimeout;

        /**
         * Per host read timeout, ms
         */
        private int readTimeout;

        /**
         * TCP connection keep alive
         */
        private boolean keepAlive;

        /**
         * Enable address reuse
         */
        private boolean reuseAddress;

        /**
         * linger-on-close timeout
         */
        private int soLinger;

        /**
         * TCP no delay
         */
        private boolean tcpNoDelay;

        /**
         * Incoming network IO buffer, bytes
         */
        private int receiveBufferSize;

        /**
         * Outgoing network IO buffer, bytes
         */
        private int sendBufferSize;

        public int getConnectTimeout()
        {
            return connectTimeout;
        }

        public void setConnectTimeout(int connectTimeout)
        {
            this.connectTimeout = connectTimeout;
        }

        public int getReadTimeout()
        {
            return readTimeout;
        }

        public void setReadTimeout(int readTimeout)
        {
            this.readTimeout = readTimeout;
        }

        public boolean isKeepAlive()
        {
            return keepAlive;
        }

        public void setKeepAlive(boolean keepAlive)
        {
            this.keepAlive = keepAlive;
        }

        public boolean isReuseAddress()
        {
            return reuseAddress;
        }

        public void setReuseAddress(boolean reuseAddress)
        {
            this.reuseAddress = reuseAddress;
        }

        public int getSoLinger()
        {
            return soLinger;
        }

        public void setSoLinger(int soLinger)
        {
            this.soLinger = soLinger;
        }

        public boolean isTcpNoDelay()
        {
            return tcpNoDelay;
        }

        public void setTcpNoDelay(boolean tcpNoDelay)
        {
            this.tcpNoDelay = tcpNoDelay;
        }

        public int getReceiveBufferSize()
        {
            return receiveBufferSize;
        }

        public void setReceiveBufferSize(int receiveBufferSize)
        {
            this.receiveBufferSize = receiveBufferSize;
        }

        public int getSendBufferSize()
        {
            return sendBufferSize;
        }

        public void setSendBufferSize(int sendBufferSize)
        {
            this.sendBufferSize = sendBufferSize;
        }
    }

    public static class ConstantExecution
    {
        /**
         * constant execution delay in millis
         */
        private long constantDelayMillis;

        /**
         * cap on total executions using constant strategy
         */
        private int maxSpeculativeExecutions;

        public long getConstantDelayMillis()
        {
            return constantDelayMillis;
        }

        public void setConstantDelayMillis(long constantDelayMillis)
        {
            this.constantDelayMillis = constantDelayMillis;
        }

        public int getMaxSpeculativeExecutions()
        {
            return maxSpeculativeExecutions;
        }

        public void setMaxSpeculativeExecutions(int maxSpeculativeExecutions)
        {
            this.maxSpeculativeExecutions = maxSpeculativeExecutions;
        }
    }

    public static class PercentileExecution
    {
        /**
         * the percentile that a request's latency must fall into to be considered slow.
         */
        private double percentile;

        /**
         * Cap on total executions for percentile strategy
         */
        private int maxSpeculativeExecutions;
        private PrecentileTracker clusterWide;
        private PrecentileTracker perHost;

        public double getPercentile()
        {
            return percentile;
        }

        public void setPercentile(double percentile)
        {
            this.percentile = percentile;
        }

        public int getMaxSpeculativeExecutions()
        {
            return maxSpeculativeExecutions;
        }

        public void setMaxSpeculativeExecutions(int maxSpeculativeExecutions)
        {
            this.maxSpeculativeExecutions = maxSpeculativeExecutions;
        }

        public PrecentileTracker getClusterWide()
        {
            return clusterWide;
        }

        public void setClusterWide(PrecentileTracker clusterWide)
        {
            this.clusterWide = clusterWide;
        }

        public PrecentileTracker getPerHost()
        {
            return perHost;
        }

        public void setPerHost(PrecentileTracker perHost)
        {
            this.perHost = perHost;
        }
    }

    public static class PrecentileTracker
    {
        /**
         *  Highest latency to be tracked in ms
         */
        private long highestTrackableLatencyMs;

        /**
         * number of significant decimal digits for histogram resolution
         */
        private int numberOfSignificantValueDigits;

        /**
         * Minimal number of records per sample
         */
        private int minRecordedValues;

        /**
         * Sample interval, ms
         */
        private long intervalMs;

        public long getHighestTrackableLatencyMs()
        {
            return highestTrackableLatencyMs;
        }

        public void setHighestTrackableLatencyMs(long highestTrackableLatencyMs)
        {
            this.highestTrackableLatencyMs = highestTrackableLatencyMs;
        }

        public int getNumberOfSignificantValueDigits()
        {
            return numberOfSignificantValueDigits;
        }

        public void setNumberOfSignificantValueDigits(int numberOfSignificantValueDigits)
        {
            this.numberOfSignificantValueDigits = numberOfSignificantValueDigits;
        }

        public int getMinRecordedValues()
        {
            return minRecordedValues;
        }

        public void setMinRecordedValues(int minRecordedValues)
        {
            this.minRecordedValues = minRecordedValues;
        }

        public long getIntervalMs()
        {
            return intervalMs;
        }

        public void setIntervalMs(long intervalMs)
        {
            this.intervalMs = intervalMs;
        }
    }

    public static class DcAwarePolicy
    {
        /**
         * Name of the "local" data center
         */
        private String localDc;

        /**
         * Number of hosts per remote data center to consider
         */
        private int usedHostsPerRemoteDc;

        /**
         * Return remote hosts for query plans with LOCAL levels
         */
        private boolean allowRemoteDcsForLocalConsistencyLevel;

        public String getLocalDc()
        {
            return localDc;
        }

        public void setLocalDc(String localDc)
        {
            this.localDc = localDc;
        }

        public int getUsedHostsPerRemoteDc()
        {
            return usedHostsPerRemoteDc;
        }

        public void setUsedHostsPerRemoteDc(int usedHostsPerRemoteDc)
        {
            this.usedHostsPerRemoteDc = usedHostsPerRemoteDc;
        }

        public boolean isAllowRemoteDcsForLocalConsistencyLevel()
        {
            return allowRemoteDcsForLocalConsistencyLevel;
        }

        public void setAllowRemoteDcsForLocalConsistencyLevel(boolean allowRemoteDcsForLocalConsistencyLevel)
        {
            this.allowRemoteDcsForLocalConsistencyLevel = allowRemoteDcsForLocalConsistencyLevel;
        }
    }

    public static class TokenAwarePolicy
    {
        /**
         * Token awareness for load balancing policy
         */
        private boolean enabled;

        /**
         * Replica ordering
         */
        private ReplicaOrdering replicaOrdering = ReplicaOrdering.RANDOM;

        public boolean isEnabled()
        {
            return enabled;
        }

        public void setEnabled(boolean enabled)
        {
            this.enabled = enabled;
        }

        public ReplicaOrdering getReplicaOrdering()
        {
            return replicaOrdering;
        }

        public void setReplicaOrdering(ReplicaOrdering replicaOrdering)
        {
            this.replicaOrdering = replicaOrdering;
        }
    }

    public static class LatencyAwarePolicy
    {
        /**
         * Exclusion threshold
         */
        private double exclusionThreshold;

        /**
         * Minimum number of measurements to consider, per host
         */
        private int minimumMeasurements;

        /**
         * Retry period, ms
         */
        private long retryPeriod;

        /**
         * Scale, ms
         */
        private long scale;

        /**
         * Update rate, ms
         */
        private long updateRate;

        public double getExclusionThreshold()
        {
            return exclusionThreshold;
        }

        public void setExclusionThreshold(double exclusionThreshold)
        {
            this.exclusionThreshold = exclusionThreshold;
        }

        public int getMinimumMeasurements()
        {
            return minimumMeasurements;
        }

        public void setMinimumMeasurements(int minimumMeasurements)
        {
            this.minimumMeasurements = minimumMeasurements;
        }

        public long getRetryPeriod()
        {
            return retryPeriod;
        }

        public void setRetryPeriod(long retryPeriod)
        {
            this.retryPeriod = retryPeriod;
        }

        public long getScale()
        {
            return scale;
        }

        public void setScale(long scale)
        {
            this.scale = scale;
        }

        public long getUpdateRate()
        {
            return updateRate;
        }

        public void setUpdateRate(long updateRate)
        {
            this.updateRate = updateRate;
        }
    }

    public String getClusterName()
    {
        return clusterName;
    }

    public void setClusterName(String clusterName)
    {
        this.clusterName = clusterName;
    }

    public String getKeyspaceName()
    {
        return keyspaceName;
    }

    public void setKeyspaceName(String keyspaceName)
    {
        this.keyspaceName = keyspaceName;
    }

    public String[] getContactPoints()
    {
        return contactPoints;
    }

    public void setContactPoints(String[] contactPoints)
    {
        this.contactPoints = contactPoints;
    }

    public Integer getPort()
    {
        return port;
    }

    public void setPort(Integer port)
    {
        this.port = port;
    }

    public Integer getProtocolVersion()
    {
        return protocolVersion;
    }

    public void setProtocolVersion(Integer protocolVersion)
    {
        this.protocolVersion = protocolVersion;
    }

    public ProtocolOptions.Compression getCompression()
    {
        return compression;
    }

    public void setCompression(ProtocolOptions.Compression compression)
    {
        this.compression = compression;
    }

    public Auth getAuth()
    {
        return auth;
    }

    public void setAuth(Auth auth)
    {
        this.auth = auth;
    }

    public LoadBalancingPolicies getLoadBalancingPolicy()
    {
        return loadBalancingPolicy;
    }

    public void setLoadBalancingPolicy(LoadBalancingPolicies loadBalancingPolicy)
    {
        this.loadBalancingPolicy = loadBalancingPolicy;
    }

    public DcAwarePolicy getDcaware()
    {
        return dcaware;
    }

    public void setDcaware(DcAwarePolicy dcaware)
    {
        this.dcaware = dcaware;
    }

    public LatencyAwarePolicy getLatencyaware()
    {
        return latencyaware;
    }

    public void setLatencyaware(LatencyAwarePolicy latencyaware)
    {
        this.latencyaware = latencyaware;
    }

    public TokenAwarePolicy getTokenaware()
    {
        return tokenaware;
    }

    public void setTokenaware(TokenAwarePolicy tokenaware)
    {
        this.tokenaware = tokenaware;
    }

    public SocketOptions getSocket()
    {
        return socket;
    }

    public void setSocket(SocketOptions socket)
    {
        this.socket = socket;
    }

    public PoolOptions getPool()
    {
        return pool;
    }

    public void setPool(PoolOptions pool)
    {
        this.pool = pool;
    }

    public RetryPolicyOptions getRetry()
    {
        return retry;
    }

    public void setRetry(RetryPolicyOptions retry)
    {
        this.retry = retry;
    }

    public ReconnectionPolicyOptions getReconnection()
    {
        return reconnection;
    }

    public void setReconnection(ReconnectionPolicyOptions reconnection)
    {
        this.reconnection = reconnection;
    }

    public QueryPolicyOptions getQuery()
    {
        return query;
    }

    public void setQuery(QueryPolicyOptions query)
    {
        this.query = query;
    }

    public boolean isMetricsEnabled()
    {
        return metricsEnabled;
    }

    public void setMetricsEnabled(boolean metricsEnabled)
    {
        this.metricsEnabled = metricsEnabled;
    }

    public boolean isJmxReportingEnabled()
    {
        return jmxReportingEnabled;
    }

    public void setJmxReportingEnabled(boolean jmxReportingEnabled)
    {
        this.jmxReportingEnabled = jmxReportingEnabled;
    }

    public String getScanPackages()
    {
        return scanPackages;
    }

    public void setScanPackages(String scanPackages)
    {
        this.scanPackages = scanPackages;
    }

    public boolean isAllowBetaProtocolVersion()
    {
        return allowBetaProtocolVersion;
    }

    public void setAllowBetaProtocolVersion(boolean allowBetaProtocolVersion)
    {
        this.allowBetaProtocolVersion = allowBetaProtocolVersion;
    }

    public boolean isSsl()
    {
        return ssl;
    }

    public void setSsl(boolean ssl)
    {
        this.ssl = ssl;
    }

    public boolean isNoCompact()
    {
        return noCompact;
    }

    public void setNoCompact(boolean noCompact)
    {
        this.noCompact = noCompact;
    }

    public Integer getMaxSchemaAgreementWaitSeconds()
    {
        return maxSchemaAgreementWaitSeconds;
    }

    public void setMaxSchemaAgreementWaitSeconds(Integer maxSchemaAgreementWaitSeconds)
    {
        this.maxSchemaAgreementWaitSeconds = maxSchemaAgreementWaitSeconds;
    }

    public TimestampGenerator getTimestampGenerator()
    {
        return timestampGenerator;
    }

    public void setTimestampGenerator(TimestampGenerator timestampGenerator)
    {
        this.timestampGenerator = timestampGenerator;
    }

    public AddressTranslator getAddressTranslator()
    {
        return addressTranslator;
    }

    public void setAddressTranslator(AddressTranslator addressTranslator)
    {
        this.addressTranslator = addressTranslator;
    }

    public SpeculativeExecution getSpeculativeExecution()
    {
        return speculativeExecution;
    }

    public void setSpeculativeExecution(SpeculativeExecution speculativeExecution)
    {
        this.speculativeExecution = speculativeExecution;
    }

    public ConstantExecution getConstantExecution()
    {
        return constantExecution;
    }

    public void setConstantExecution(ConstantExecution constantExecution)
    {
        this.constantExecution = constantExecution;
    }

    public PercentileExecution getPercentileExecution()
    {
        return percentileExecution;
    }

    public void setPercentileExecution(PercentileExecution percentileExecution)
    {
        this.percentileExecution = percentileExecution;
    }

    public Generator getAtomic()
    {
        return atomic;
    }

    public void setAtomic(Generator atomic)
    {
        this.atomic = atomic;
    }

    public Generator getThreadLocal()
    {
        return threadLocal;
    }

    public void setThreadLocal(Generator threadLocal)
    {
        this.threadLocal = threadLocal;
    }
}
