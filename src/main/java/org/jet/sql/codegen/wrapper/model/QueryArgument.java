package org.jet.sql.codegen.wrapper.model;

import java.sql.JDBCType;

/**
 * Simple POJO to hold the query arguments parsed from the yaml file.
 *
 * @author tgorthi
 * @since December 2019
 */
public class QueryArgument
{
    private String type;
    private String name;
    private int index;

    /**
     * Default constructor used by JAXB Object parser.
     */
    public QueryArgument()
    {
    }

    public QueryArgument(final String name, final String type)
    {
        this.name = name;
        this.type = type;
    }

    /**
     * @return Name of the query argument.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return JDBCType corresponding to the string value in the yaml file.
     * @throws RuntimeException when the type defined in the yaml file is not a valid JDBC Type.
     */
    public JDBCType getType()
    {
        try
        {
            return JDBCType.valueOf(type.toUpperCase());
        }
        catch (IllegalArgumentException ie)
        {
            throw new RuntimeException("Invalid JDBC Type ", ie);
        }
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public int getIndex()
    {
        return index;
    }

    /**
     * @return Returns a string in special format after
     * - removing the {@link SqlQuery#QUERY_PARAM_PREFIX} prefix
     * - removing any and all underscores
     * - the return string is in camelcase format , except the first character is lower case
     * and upper case
     * (since the method names need to start with lower case and the value returned by this
     * method is used as the method name in the generated sql wrapper classes)
     *
     * @throws RuntimeException if the argument name is not prefixed by {@link SqlQuery#QUERY_PARAM_PREFIX} or if the argument name has special characters excluding ("-")
     */
    public String evaluateAndGetArgumentSetterMethodName()
    {
        if (!name.startsWith(SqlQuery.QUERY_PARAM_PREFIX))
        {
            throw new RuntimeException("Query argument names must be prefixed with " + SqlQuery.QUERY_PARAM_PREFIX + ": [ " + name + " ]");
        }

        final StringBuilder sb = new StringBuilder();
        boolean previousCharWasUnderscore = false;

        for (int i = SqlQuery.QUERY_PARAM_PREFIX.length(); i < name.length(); i++)
        {
            final char c = name.charAt(i);
            if (c == '_')
            {
                if (previousCharWasUnderscore)
                {
                    throw new RuntimeException("Invalid argument name, underscore must be followed by a valid alphanumeric character");
                }
                previousCharWasUnderscore = true;
                sb.append(c);
                continue;
            }

            if (!Character.isAlphabetic(c) && !Character.isDigit(c))
            {
                throw new RuntimeException("argument name cannot contain special characters, only letters or numbers are" +
                        " allowed");
            }

            sb.append(previousCharWasUnderscore && Character.isAlphabetic(c) ? Character.toUpperCase(c) : c);
            previousCharWasUnderscore = false;
        }

        if (sb.length() == 0)
        {
            throw new RuntimeException("Invalid argument name : " + name);
        }

        return sb.toString().toUpperCase();
    }

    public String getJavaReturnType()
    {
        return JavaTypeLookUp.get(getType());
    }

    @Override
    public String toString()
    {
        return "QueryArgument{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
