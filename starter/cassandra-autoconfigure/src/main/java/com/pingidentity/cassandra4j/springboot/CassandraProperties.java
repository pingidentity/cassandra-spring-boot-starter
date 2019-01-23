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

import com.datastax.driver.core.ProtocolOptions;
import com.datastax.driver.core.policies.TokenAwarePolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy.ReplicaOrdering;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cassandra")
public class CassandraProperties
{
    public enum LoadBalancingPolicies { DC_AWARE, ROUND_ROBIN }
    public enum RetryPolicies { DEFAULT, FALLTHROUGH }
    public enum ReconnectionPolicies { CONSTANT, EXPONENTIAL }

    private String clusterName;
    private String keyspaceName;
    private String[] contactPoints;
    private Integer port;
    private Integer protocolVersion;
    private boolean allowBetaProtocolVersion;
    private Integer maxSchemaAgreementWaitSeconds;
    private ProtocolOptions.Compression compression;
    private Auth auth;
    private LoadBalancingPolicies loadBalancingPolicy;
    private DcAwarePolicy dcaware;
    private LatencyAwarePolicy latencyaware;
    private TokenAwarePolicy tokenaware;
    private SocketOptions socket;
    private PoolOptions pool;
    private RetryPolicyOptions retry;
    private ReconnectionPolicyOptions reconnection;
    private QueryPolicyOptions query;
    private boolean metricsEnabled=true;
    private boolean jmxReportingEnabled=true;
    private String scanPackages;

    public static class Auth
    {
        private String username;
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
        private String consistencyLevel;
        private String serialConsistencyLevel;
        private int fetchSize;

        public String getConsistencyLevel()
        {
            return consistencyLevel;
        }

        public void setConsistencyLevel(String consistencyLevel)
        {
            this.consistencyLevel = consistencyLevel;
        }

        public String getSerialConsistencyLevel()
        {
            return serialConsistencyLevel;
        }

        public void setSerialConsistencyLevel(String serialConsistencyLevel)
        {
            this.serialConsistencyLevel = serialConsistencyLevel;
        }

        public int getFetchSize()
        {
            return fetchSize;
        }

        public void setFetchSize(int fetchSize)
        {
            this.fetchSize = fetchSize;
        }
    }

    public static class ReconnectionPolicyOptions
    {
        private ReconnectionPolicies policy;
        private long constantDelay;
        private long exponentialBaseDelay;
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
        private boolean loggingEnabled;
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
        private int heartbeatIntervalSeconds;
        private int idleTimeoutSeconds;
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
            private int coreConnectionsPerHost;
            private int maxConnectionsPerHost;
            private int maxRequestsPerConnection;
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
        private int connectTimeout;
        private int readTimeout;
        private boolean keepAlive;
        private boolean reuseAddress;
        private int soLinger;
        private boolean tcpNoDelay;
        private int receiveBufferSize;
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

    public static class DcAwarePolicy
    {
        private String localDc;
        private int usedHostsPerRemoteDc;
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
        private boolean enabled;
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
        private double exclusionThreshold;
        private int minimumMeasurements;
        private long retryPeriod;
        private long scale;
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
}
