package org.jet.sql.codegen.wrapper.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public SqlQuery() {}

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

    public void preProcess()
    {
        final Map<String, QueryArgument> argumentMap = Stream.of(arguments)
                .collect(Collectors.toMap(QueryArgument::getName, Function.identity()));

        final StringBuilder sb = new StringBuilder();
        int index = 1;
        for (String s : sql.replace(",", " , ").split(" "))
        {
            if (s.startsWith(QUERY_PARAM_PREFIX))
            {
                argumentMap.get(s).setIndex(index);
                index++;
                sb.append("?");
            }
            else
            {
                sb.append(s);
            }
            sb.append(" ");
        }

        sql = sb.toString();
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
