package com.jet.wrapper.model;

import java.sql.JDBCType;

/**
 * Utility class that returns the Java Object (data) return type for a given {@link JDBCType}.
 *
 * @author tgorthi
 * @since Dec 2019
 */
public class JavaTypeSupplier
{
    public static String get(final JDBCType jdbcType)
    {
        switch (jdbcType)
        {
            case INTEGER:
                return "java.lang.Integer";
            case VARCHAR:
                return "java.lang.String";
            case ARRAY:
                return "java.lang.Object[]";
            case BOOLEAN:
                return "java.lang.Boolean";
            default:
                return "java.lang.Object";
        }
    }
}
