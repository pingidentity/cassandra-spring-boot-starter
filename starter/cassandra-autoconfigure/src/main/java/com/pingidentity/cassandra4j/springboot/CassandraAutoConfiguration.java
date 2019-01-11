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

import com.codahale.metrics.jmx.JmxReporter;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;
import com.datastax.driver.core.policies.ExponentialReconnectionPolicy;
import com.datastax.driver.core.policies.FallthroughRetryPolicy;
import com.datastax.driver.core.policies.LatencyAwarePolicy;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.LoggingRetryPolicy;
import com.datastax.driver.core.policies.ReconnectionPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Table;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.pingidentity.cassandra4j.ConfigurationCustomizer;
import com.pingidentity.cassandra4j.springboot.CassandraProperties.ReconnectionPolicies;
import com.pingidentity.cassandra4j.springboot.CassandraProperties.RetryPolicies;
import com.pingidentity.cassandra4j.springboot.health.CassandraHealthIndicator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.pingidentity.cassandra4j.springboot.utils.StringUtils.decapitalize;
import static com.pingidentity.cassandra4j.springboot.utils.StringUtils.simpleClassName;
import static com.pingidentity.cassandra4j.springboot.utils.StringUtils.split;

@Configuration
@ConditionalOnClass({Cluster.class})
@EnableConfigurationProperties(CassandraProperties.class)
public class CassandraAutoConfiguration implements BeanDefinitionRegistryPostProcessor, EnvironmentAware
{
    public static final String DATASTAX_MAPPER_FACTORY_BEAN = "mapperFactory";

    private Environment env;
    private List<ConfigurationCustomizer> customizers;

    @ConditionalOnMissingBean
    @ConditionalOnProperty("cassandra.contact-points")
    @Bean
    public MappingManager cassandraMappings(Session session)
    {
        return new MappingManager(session);
    }

    @ConditionalOnMissingBean
    @ConditionalOnProperty("cassandra.contact-points")
    @Bean("mapperFactory")
    public MapperFactory mapperFactory(MappingManager manager)
    {
        return new MapperFactory(manager);
    }

    @ConditionalOnMissingBean
    @ConditionalOnProperty("cassandra.contact-points")
    @Bean(destroyMethod = "close")
    public Session session(Cluster cluster, CassandraProperties props)
    {
        return cluster.connect(props.getKeyspaceName());
    }

    @ConditionalOnMissingBean
    @ConditionalOnProperty("cassandra.contact-points")
    @Bean(destroyMethod = "close")
    public Cluster cluster(CassandraProperties props, ObjectProvider<List<ConfigurationCustomizer>> customizersProvider)
    {
        customizers = customizersProvider.getIfAvailable();

        Cluster.Builder builder = Cluster.builder()
                                         .withClusterName(props.getClusterName());

        if (props.getProtocolVersion() != null)
        {
            builder.withProtocolVersion(ProtocolVersion.fromInt(props.getProtocolVersion()));
        }

        for (String contactPoint : props.getContactPoints())
        {
            builder.addContactPoint(contactPoint);
        }

        if (props.getPort() != null)
        {
            builder.withPort(props.getPort());
        }

        if (props.getCompression() != null)
        {
            builder.withCompression(props.getCompression());
        }

        if(props.getAuth()!=null)
        {
            builder.withCredentials(props.getAuth().getUsername(),props.getAuth().getPassword());
        }

        LoadBalancingPolicy loadBalancingPolicy = null;

        // Configure the DC aware load balancing policy.
        if (CassandraProperties.LoadBalancingPolicies.DC_AWARE.equals(props.getLoadBalancingPolicy()) && props.getDcaware()!=null)
        {
            DCAwareRoundRobinPolicy.Builder dcBuilder = new DCAwareRoundRobinPolicy.Builder()
                    .withLocalDc(props.getDcaware().getLocalDc())
                    .withUsedHostsPerRemoteDc(props.getDcaware().getUsedHostsPerRemoteDc());

            if (props.getDcaware().isAllowRemoteDCsForLocalConsistencyLevel())
            {
                dcBuilder.allowRemoteDCsForLocalConsistencyLevel();
            }

            loadBalancingPolicy = dcBuilder.build();
        }
        // Configure a generic Round Robin load balancing policy as the child of the latency aware.
        else if (CassandraProperties.LoadBalancingPolicies.ROUND_ROBIN.equals(props.getLoadBalancingPolicy()))
        {
            loadBalancingPolicy = new RoundRobinPolicy();
        }

        // If a wrapper load balancing policy is defined, configure it here.
        if (loadBalancingPolicy != null)
        {
            if (props.getLatencyaware() != null)
            {
                loadBalancingPolicy = new LatencyAwarePolicy.Builder(loadBalancingPolicy)
                        .withExclusionThreshold(props.getLatencyaware().getExclusionThreshold())
                        .withMininumMeasurements(props.getLatencyaware().getMinimumMeasurements())
                        .withRetryPeriod(props.getLatencyaware().getRetryPeriod(), TimeUnit.MILLISECONDS)
                        .withScale(props.getLatencyaware().getScale(), TimeUnit.MILLISECONDS)
                        .withUpdateRate(props.getLatencyaware().getUpdateRate(), TimeUnit.MILLISECONDS)
                        .build();
            }
            else if (props.isTokenAwarePolicyEnabled())
            {
                loadBalancingPolicy = new TokenAwarePolicy(loadBalancingPolicy);
            }
        }

        if (loadBalancingPolicy != null)
        {
            builder.withLoadBalancingPolicy(loadBalancingPolicy);
        }

        if (props.getSocket() != null)
        {
            SocketOptions socketOptions = new SocketOptions()
                    .setConnectTimeoutMillis(props.getSocket().getConnectTimeout())
                    .setReadTimeoutMillis(props.getSocket().getReadTimeout())
                    .setKeepAlive(props.getSocket().isKeepAlive())
                    .setReuseAddress(props.getSocket().isReuseAddress())
                    .setSoLinger(props.getSocket().getSoLinger())
                    .setTcpNoDelay(props.getSocket().isTcpNoDelay());

            if (props.getSocket().getReceiveBufferSize() > 0)
            {
                socketOptions = socketOptions.setReceiveBufferSize(props.getSocket().getReceiveBufferSize());
            }
            if (props.getSocket().getSendBufferSize() > 0)
            {
                socketOptions = socketOptions.setSendBufferSize(props.getSocket().getSendBufferSize());
            }

            builder.withSocketOptions(socketOptions);
        }

        if (props.getPool() != null)
        {
            PoolingOptions poolingOptions = new PoolingOptions()
                    .setHeartbeatIntervalSeconds(props.getPool().getHeartbeatIntervalSeconds())
                    .setIdleTimeoutSeconds(props.getPool().getIdleTimeoutSeconds())
                    .setPoolTimeoutMillis(props.getPool().getPoolTimeoutMillis()) ;

                    if(props.getPool().getLocal()!=null)
                    {
                        poolingOptions.setCoreConnectionsPerHost(HostDistance.LOCAL, props.getPool().getLocal().getCoreConnectionsPerHost())
                                      .setMaxConnectionsPerHost(HostDistance.LOCAL, props.getPool().getLocal()
                                                                                         .getMaxConnectionsPerHost())
                                      .setMaxRequestsPerConnection(HostDistance.LOCAL, props.getPool().getLocal().getMaxRequestsPerConnection())
                                      .setNewConnectionThreshold(HostDistance.LOCAL, props.getPool().getLocal().getNewConnectionThreshold());
                    }

                    if(props.getPool().getRemote()!=null)
                    {
                        poolingOptions.setCoreConnectionsPerHost(HostDistance.REMOTE, props.getPool().getRemote().getCoreConnectionsPerHost())
                                      .setMaxConnectionsPerHost(HostDistance.REMOTE, props.getPool().getRemote().getMaxConnectionsPerHost())
                                      .setMaxRequestsPerConnection(HostDistance.REMOTE, props.getPool().getRemote().getMaxRequestsPerConnection())
                                      .setNewConnectionThreshold(HostDistance.REMOTE, props.getPool().getRemote().getNewConnectionThreshold());
                    }

            builder.withPoolingOptions(poolingOptions);
        }

        if (props.getRetry() != null)
        {
            RetryPolicy retryPolicy = null;

            if (RetryPolicies.DEFAULT.equals(props.getRetry().getPolicy()))
            {
                retryPolicy = DefaultRetryPolicy.INSTANCE;
            }
            else if (RetryPolicies.DOWNGRADING_CONSISTENCY.equals(props.getRetry().getPolicy()))
            {
                retryPolicy = DowngradingConsistencyRetryPolicy.INSTANCE;
            }
            else if (RetryPolicies.FALLTHROUGH.equals(props.getRetry().getPolicy()))
            {
                retryPolicy = FallthroughRetryPolicy.INSTANCE;
            }
            if (props.getRetry().isLoggingEnabled())
            {
                retryPolicy = new LoggingRetryPolicy(retryPolicy);
            }
            if (retryPolicy != null)
            {
                builder.withRetryPolicy(retryPolicy);
            }
        }

        if(props.getReconnection()!=null)
        {
            ReconnectionPolicy reconnectionPolicy = null;
            if (ReconnectionPolicies.CONSTANT.equals(props.getReconnection().getPolicy()))
            {
                reconnectionPolicy = new ConstantReconnectionPolicy(props.getReconnection().getConstantDelay());
            }
            else if (ReconnectionPolicies.EXPONENTIAL.equals(props.getReconnection().getPolicy()))
            {
                reconnectionPolicy = new ExponentialReconnectionPolicy(
                        props.getReconnection().getExponentialBaseDelay(),
                        props.getReconnection().getExponentialMaxDelay());
            }
            if (reconnectionPolicy != null)
            {
                builder.withReconnectionPolicy(reconnectionPolicy);
            }
        }
        if (props.getQuery() != null)
        {
            QueryOptions queryOptions = new QueryOptions()
                    .setConsistencyLevel(ConsistencyLevel.valueOf(props.getQuery().getConsistencyLevel()))
                    .setSerialConsistencyLevel(ConsistencyLevel.valueOf(props.getQuery().getSerialConsistencyLevel()))
                    .setFetchSize(props.getQuery().getFetchSize());

            builder.withQueryOptions(queryOptions);
        }

        // Enabled by default.
        if (!props.isMetricsEnabled())
        {
            builder.withoutMetrics();
        }

        // See: https://docs.datastax.com/en/developer/java-driver/3.5/manual/metrics/
        // spring-boot v2.1+ includes dropwizard metrics v4
        // we providing customizer bean for post configuration if enabled later
        builder.withoutJMXReporting();

        Cluster cluster = builder.build().init();

        //apply custom client side customizations
        return customize(cluster);
    }

    private Cluster customize(Cluster cluster)
    {
        Cluster result = cluster;

        if (customizers != null)
        {
            for (ConfigurationCustomizer customizer : customizers)
            {
                result = customizer.customize(cluster);
            }
        }

        return result;
    }

    @ConditionalOnClass(JmxReporter.class)
    @ConditionalOnMissingBean(name="jmxReporter")
    @Bean
    public ConfigurationCustomizer jmxReporterCustomizer(CassandraProperties props)
    {
        return cluster ->
        {
            if(!props.isJmxReportingEnabled() || !props.isMetricsEnabled())
            {
                return cluster;
            }

            if(cluster.getMetrics()!=null)
            {
                JmxReporter reporter =
                        JmxReporter.forRegistry(cluster.getMetrics().getRegistry())
                                .inDomain(cluster.getClusterName() + "-metrics")
                                .build();

                reporter.start();
            }

            return cluster;
        };
    }

    @Override
    public void setEnvironment(Environment environment)
    {
        this.env=environment;
    }

    @Bean
    @ConditionalOnMissingBean(name="iterableSerializer")
    @ConditionalOnProperty("cassandra.contact-points")
    public Module iterableSerializer()
    {
        return new SimpleModule("java.lang.Iterable").addSerializer(Iterable.class, new JsonSerializer<Iterable>()
        {
            @Override
            public void serialize(Iterable value, JsonGenerator gen, SerializerProvider serializers) throws IOException
            {
                serializers.findValueSerializer(Iterator.class, null)
                           .serialize(value.iterator(), gen, serializers);
            }
        });
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException
    {
        String[] packagesToScan = split(env.getProperty("cassandra.scanPackages"));
        String[] contactPoints = split(env.getProperty("cassandra.contact-points"));

        if(contactPoints.length == 0)
        {
            //no cassandra configuration, skipping
            return;
        }

        if (packagesToScan.length == 0)
        {
            //no scan package defined, skipping
            return;
        }

        final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) registry;

        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false)
        {
            //accept interfaces as bean definition
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition)
            {
                return beanDefinition.getMetadata().isIndependent();
            }
        };

        provider.addIncludeFilter(new AnnotationTypeFilter(Table.class, true, false)); //@Table on classes
        provider.addIncludeFilter(new AnnotationTypeFilter(Accessor.class, true, true)); //@Accessor on interfaces

        Set<BeanDefinition> allBeans = new HashSet<>();

        //get all matching resources
        for (String targetPackage : packagesToScan)
        {
            if(!targetPackage.isEmpty())
            {
                allBeans.addAll(provider.findCandidateComponents(targetPackage));
            }
        }

        try
        {
            for (BeanDefinition bd : allBeans)
            {
                Class targetClass = Class.forName(bd.getBeanClassName());

                if (targetClass.isInterface() && targetClass.getAnnotation(Accessor.class) != null)
                {
                    //register accessor bean implementation
                    RootBeanDefinition accessorBeanDefinition = new RootBeanDefinition(targetClass);

                    accessorBeanDefinition.setTargetType(ResolvableType.forClass(targetClass));
                    accessorBeanDefinition.setFactoryBeanName(DATASTAX_MAPPER_FACTORY_BEAN);
                    accessorBeanDefinition.setFactoryMethodName("newAccessor");
                    accessorBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
                    accessorBeanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, targetClass);

                    String beanName = decapitalize(
                            simpleClassName(bd.getBeanClassName())) + "Accessor";

                    beanFactory.registerBeanDefinition(beanName, accessorBeanDefinition);
                }
                else
                {
                    //register mapper bean implementation
                    RootBeanDefinition mapperBeanDefinition = new RootBeanDefinition();

                    mapperBeanDefinition.setTargetType(ResolvableType.forClassWithGenerics(Mapper.class, targetClass));
                    mapperBeanDefinition.setFactoryBeanName(DATASTAX_MAPPER_FACTORY_BEAN);
                    mapperBeanDefinition.setFactoryMethodName("newMapper");
                    mapperBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
                    mapperBeanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, targetClass);

                    String beanName = decapitalize(
                            simpleClassName(bd.getBeanClassName())) + "Mapper";

                    beanFactory.registerBeanDefinition(beanName, mapperBeanDefinition);
                }
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new BeanCreationException("Unable to auto register datastax mapper/accessor beans", e);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {}

    @Configuration
    @ConditionalOnClass(Endpoint.class)
    @ConditionalOnProperty("cassandra.contact-points")
    protected static class CassandraHealthConfig
    {
        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnEnabledHealthIndicator("cassandra")
        public CassandraHealthIndicator cassandraHealthIndicator(Session session)
        {
            return new CassandraHealthIndicator(session);
        }
    }
}
