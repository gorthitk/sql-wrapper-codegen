package org.jet.sql.codegen.wrapper.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnectionSupplier implements ConnectionSupplier
{
    @Override
    public Connection get(final String name) throws SQLException
    {
        return null;
    }
}
