package org.jet.sql.codegen.wrapper.model;

import java.util.Arrays;

/**
 * @author tgorthi
 * @since December 2019
 */
public class SqlQuery
{
    private final String name;
    private final String sql;
    private final QueryArgument[] arguments;
    private final ResultColumns[] results;

    /**
     * Constructor only used for testing purposes.
     */
    public SqlQuery(String name, String sql, QueryArgument[] arguments, ResultColumns[] results)
    {
        this.name = name;
        this.sql = sql;
        this.arguments = arguments;
        this.results = results;
    }

    public String getName()
    {
        return name.toUpperCase();
    }

    public String getSql()
    {
        return sql;
    }

    public QueryArgument[] getArguments()
    {
        return arguments;
    }

    public ResultColumns[] getResults()
    {
        return results;
    }

    public String getClassName()
    {
        final StringBuilder sb = new StringBuilder();

        boolean previousCharWasUnderscore = true;
        for (char c : name.toCharArray())
        {
            if (c == '_')
            {
                previousCharWasUnderscore = true;
                continue;
            }

            if (previousCharWasUnderscore)
            {
                sb.append(Character.toUpperCase(c));
            }
            else
            {
                sb.append(Character.toLowerCase(c));
            }

            previousCharWasUnderscore = false;
        }

        return sb.toString();
    }

    @Override
    public String toString()
    {
        return "SqlQuery{" +
                "name='" + name + '\'' +
                ", sql='" + sql + '\'' +
                ", arguments=" + Arrays.toString(arguments) +
                ", results=" + Arrays.toString(results) +
                '}';
    }
}
