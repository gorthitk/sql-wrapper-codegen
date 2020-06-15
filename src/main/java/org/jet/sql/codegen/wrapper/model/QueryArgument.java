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
    private final int paramType;
    private final String paramClassName;
    private final String paramName;
    private final int paramIndex;

    public QueryArgument(
            final String paramName, final int paramType, final String paramClassName,
            final int paramIndex
    )
    {
        this.paramType = paramType;
        this.paramClassName = paramClassName;
        this.paramName = paramName;
        this.paramIndex = paramIndex;
    }

    public String getParamTypeName()
    {
        return JDBCType.valueOf(paramType).getName();
    }

    public String getParamClassName()
    {
        return paramClassName;
    }

    public String getParamName()
    {
        return paramName;
    }

    public int getParamIndex()
    {
        return paramIndex;
    }

    /**
     * @return Returns a string in special format after
     * - removing any and all underscores
     * - the return string is in camelcase format , except the first character is lower case
     * and upper case
     * (since the method names need to start with lower case and the value returned by this
     * method is used as the method name in the generated sql wrapper classes)
     * @throws RuntimeException if the argument name has special characters excluding ("-")
     */
    public String evaluateAndGetArgumentSetterMethodName()
    {
        final StringBuilder sb = new StringBuilder();
        boolean previousCharWasUnderscore = false;

        for (int i = 0; i < paramName.length(); i++)
        {
            final char c = paramName.charAt(i);
            if (c == '_')
            {
                if (previousCharWasUnderscore)
                {
                    throw new RuntimeException("Invalid argument name, underscore must be followed by a valid " +
                            "alphanumeric character");
                }
                previousCharWasUnderscore = true;
                sb.append(c);
                continue;
            }

            if (!Character.isAlphabetic(c) && !Character.isDigit(c))
            {
                throw new RuntimeException("argument name cannot contain special characters, only letters or numbers " +
                        "are" +
                        " allowed");
            }

            sb.append(previousCharWasUnderscore && Character.isAlphabetic(c) ? Character.toUpperCase(c) : Character.isAlphabetic(c) ? Character.toLowerCase(c) : c);
            previousCharWasUnderscore = false;
        }

        if (sb.length() == 0)
        {
            throw new RuntimeException("Invalid argument name : " + paramName);
        }

        return sb.toString().toUpperCase();
    }

    @Override
    public String toString()
    {
        return "QueryArgument{" +
                "paramType=" + paramType +
                ", paramClassName='" + paramClassName + '\'' +
                ", paramName='" + paramName + '\'' +
                ", paramIndex=" + paramIndex +
                '}';
    }
}
