package org.jet.sql.codegen.wrapper.model;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author tgorthi
 * @since December 2019
 */
public class WrapperConfig
{
    private final String packageName;
    private final String className;
    private final SqlQuery[] queries;

    public WrapperConfig(String packageName, String className, SqlQuery[] query)
    {
        this.packageName = packageName;
        this.className = className;
        this.queries = query;
    }

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

    public String getGeneratedDate()
    {
        return LocalDateTime.now().toString();
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
