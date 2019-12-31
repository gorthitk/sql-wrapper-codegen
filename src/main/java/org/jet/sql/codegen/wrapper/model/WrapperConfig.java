package org.jet.sql.codegen.wrapper.model;

import java.time.LocalDateTime;
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

    /**
     * Default constructor used by JAXB Object parser.
     */
    public WrapperConfig()
    {
    }

    public WrapperConfig(String packageName, String className, SqlQuery query)
    {
        this.packageName = packageName;
        this.className = className;
        this.queries = new SqlQuery[] { query };
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

    public void preProcess()
    {
        for (SqlQuery query : queries)
        {
            query.preProcess();
        }
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
