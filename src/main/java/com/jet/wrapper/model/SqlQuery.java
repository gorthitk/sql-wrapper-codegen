package com.jet.wrapper.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tgorthi
 * @since December 2019
 */
public class SqlQuery
{
    public static final String QUERY_PARAM_PREFIX = "arg_";
    private String name;
    private String sql;
    private QueryArgument[] arguments;
    private ResultColumns[] results;

    /**
     * Default constructor used by JAXB Object parser.
     */
    public SqlQuery()
    {
    }

    public SqlQuery(String name, String sql, QueryArgument[] arguments, ResultColumns[] results)
    {
        this.name = name;
        this.sql = sql;
        this.arguments = arguments;
        this.results = results;
    }

    public String getName()
    {
        return name;
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

    public String convert()
    {
        final StringBuilder sb = new StringBuilder();
        for (String s : sql.replace(",", " , ").split(" "))
        {
            String[] withCommasRemoved = s.split(",");
            if (s.startsWith(QUERY_PARAM_PREFIX))
            {
                sb.append("?");
            }
            else
            {
                sb.append(s);
            }
            sb.append(" ");
        }

        return sb.toString();
    }

    public Map<String, Integer> evaluateAndGetArgumentPositions()
    {
        final Map<String, Integer> variables = new HashMap<>();
        int index = 1;
        for (String s : sql.split("\\s*(,|\\s)\\s*"))
        {
            if (s.startsWith(QUERY_PARAM_PREFIX))
            {
                variables.put(s, index);
                index++;
            }
        }

        return variables;
    }

    public String parseAndCreateClassName()
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
                sb.append(c);
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
