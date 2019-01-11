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

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

public class MapperFactory
{
    private MappingManager manager;

    public MapperFactory(MappingManager manager)
    {
        this.manager = manager;
    }

    public Object newAccessor(Class type) throws ClassNotFoundException
    {
        return manager.createAccessor(type);
    }

    public Mapper<?> newMapper(Class type) throws ClassNotFoundException
    {
        return manager.mapper(type);
    }
}
