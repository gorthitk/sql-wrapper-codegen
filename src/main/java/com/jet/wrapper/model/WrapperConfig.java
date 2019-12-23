package com.jet.wrapper.model;

import java.util.Arrays;

/**
 * @author tgorthi
 * @since December 2019
 */
public class WrapperConfig
{
    private String packageName;
    private String className;
    private SqlQuery[] queries;

    public String getPackageName()
    {
        return packageName;
    }

    public String getClassName()
    {
        return className;
    }

    public SqlQuery[] getQueries()
    {
        return queries;
    }

    @Override
    public String toString()
    {
        return "WrapperConfig{" +
                "packageName='" + packageName + '\'' +
                ", className='" + className + '\'' +
                ", queries=" + Arrays.toString(queries) +
                '}';
    }
}
