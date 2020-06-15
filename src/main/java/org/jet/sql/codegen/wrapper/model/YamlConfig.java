package org.jet.sql.codegen.wrapper.model;

/**
 * @author tgorthi
 * @since Jun 2020
 */
public class YamlConfig
{
    private String packageName;
    private RawSql[] queries;
    private String className;

    public YamlConfig() { }

    public String getPackageName()
    {
        return packageName;
    }

    public String getClassName()
    {
        return className;
    }

    public RawSql[] getQueries()
    {
        return queries;
    }

    public static class RawSql
    {
        String name;
        String sql;

        public RawSql(){}

        public String getName()
        {
            return name;
        }

        public String getSql()
        {
            return sql;
        }
    }
}
