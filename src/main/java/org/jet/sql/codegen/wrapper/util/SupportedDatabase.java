package org.jet.sql.codegen.wrapper.util;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tgorthi
 * @since Jun 2020
 */
public enum SupportedDatabase
{
    POSTGRES("postgresql", 5432, "org.postgresql.Driver"),
    MYSQL("mysql", 3306, "com.mysql.jdbc.Driver"),
    ;

    private final String typeName;
    private final int port;
    private final String driverClass;

    SupportedDatabase(final String typeName, final int port, final String driverClass)
    {
        this.typeName = typeName;
        this.port = port;
        this.driverClass  = driverClass;
    }

    private static final Map<String, SupportedDatabase> LOOKUP =
            Stream.of(SupportedDatabase.values()).collect(Collectors.toMap(db -> db.typeName, Function.identity()));

    public String getTypeName()
    {
        return typeName;
    }

    public int getPort()
    {
        return port;
    }

    public String getDriverClass()
    {
        return driverClass;
    }

    public static SupportedDatabase get(final String typeName)
    {
        if (LOOKUP.containsKey(typeName))
        {
            return LOOKUP.get(typeName);
        }

        throw new RuntimeException("Database type [ " + typeName + " ] is not currently not supported");
    }
}
