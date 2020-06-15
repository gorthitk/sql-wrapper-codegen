package org.jet.sql.codegen.wrapper.model;

/**
 * Simple POJO to hold the configuration for result columns of sql specified in parsed from the yaml file.
 *
 * @author tgorthi
 * @since December 2019
 */
public class ResultColumns
{
    private String type;
    private String name;

    public ResultColumns(final String name, final String type)
    {
        this.name = name;
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public String getJavaReturnType()
    {
        return type;
    }

    /**
     * @return Returns a string in special format after
     * - removing any and all underscores
     * - the return string is in camelcase format , except the first character is lower case
     * and upper case
     * (since the method names need to start with lower case and the value returned by this
     * method is used as the method name in the generated sql wrapper classes)
     * @throws RuntimeException if the return column name has special characters excluding ("-")
     */
    public String evaluateAndGetResultSetAccessorMethodName()
    {
        final StringBuilder sb = new StringBuilder();
        boolean previousCharWasUnderscore = false;

        for (char c : name.toCharArray())
        {
            if (c == '_')
            {
                previousCharWasUnderscore = true;
                continue;
            }

            if (!Character.isAlphabetic(c) && !Character.isDigit(c))
            {
                throw new RuntimeException("column name cannot contain special characters, only letters or numbers " +
                        "are" +
                        " allowed");
            }

            sb.append(previousCharWasUnderscore && Character.isAlphabetic(c) ? Character.toUpperCase(c) : Character.isAlphabetic(c) ? Character.toLowerCase(c) : c);

            previousCharWasUnderscore = false;
        }

        if (sb.length() == 0)
        {
            throw new RuntimeException("Invalid column name : " + name);
        }

        return sb.toString();
    }

    @Override
    public String toString()
    {
        return "ResultColumns{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}