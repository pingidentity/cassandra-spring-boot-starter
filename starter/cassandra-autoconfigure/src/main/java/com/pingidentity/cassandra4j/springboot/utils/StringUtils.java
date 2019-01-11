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

package com.pingidentity.cassandra4j.springboot.utils;

public class StringUtils
{
    public static String decapitalize(String string)
    {
        if (string == null || string.length() == 0)
        {
            return string;
        }

        char chars[] = string.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);

        return new String(chars);
    }

    public static String simpleClassName(String fullClassName)
    {
        return fullClassName.substring(
                fullClassName.lastIndexOf('.') + 1);
    }

    public static String[] split(String commaSeparated)
    {
        if(commaSeparated==null)
        {
            return new String[0];
        }

        return commaSeparated.trim().split(",[\\s]*");
    }
}

