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

package com.pingidentity.cassandra4j.springboot.health;

import com.datastax.driver.core.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CassandraHealthIndicator implements HealthIndicator
{
    private Session session;

    @Autowired
    public CassandraHealthIndicator(Session session)
    {
        this.session = session;
    }

    @Override
    public Health health()
    {
        Health.Builder builder = Health.up();

        try
        {
            session.execute("select now() from system.local;");
        }
        catch (Exception e)
        {
            builder.down(e);
        }

        return builder.build();
    }
}
