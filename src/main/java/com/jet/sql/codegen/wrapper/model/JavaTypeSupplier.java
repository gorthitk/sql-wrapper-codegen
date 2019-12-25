package com.jet.sql.codegen.wrapper.model;

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
            case BIT:
            case BOOLEAN:
                return "java.lang.Boolean";
            case TINYINT:
            case SMALLINT:
                return "java.lang.Short";
            case BIGINT:
                return "java.math.BigInteger";
            case FLOAT:
                return "java.lang.Float";
            case DECIMAL:
                return "java.lang.Double";
            case CHAR:
                return "java.lang.Character";
            case INTEGER:
                return "java.lang.Integer";
            case VARCHAR:
            case LONGVARCHAR:
                return "java.lang.String";
            case ARRAY:
                return "java.lang.Object[]";
            case TIME:
            case TIME_WITH_TIMEZONE:
                return "java.sql.Time";
            case TIMESTAMP:
            case TIMESTAMP_WITH_TIMEZONE:
                return "java.sql.Timestamp";
            default:
                return "java.lang.Object";
        }
    }
}
