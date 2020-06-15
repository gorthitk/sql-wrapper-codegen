package org.jet.sql.codegen.wrapper.util;

import org.jet.sql.codegen.plugin.SqlWrapperExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author tgorthi
 * @since Jun 2020
 */
public class JDBCConnectionSupplier implements ConnectionSupplier
{
    private final Connection connection;

    private JDBCConnectionSupplier(final Connection connection)
    {
        this.connection = connection;
    }

    public static JDBCConnectionSupplier create(final SqlWrapperExtension sqlWrapperExtension)
    {
        try
        {
            final String type = sqlWrapperExtension.getType();
            final Properties properties = new Properties();

            properties.put("user", sqlWrapperExtension.getUserName());
            properties.put("password", sqlWrapperExtension.getPassword());

            final SupportedDatabase supportedDatabase = SupportedDatabase.get(type);
            final String connectionUrl = String.format("jdbc:%s://%s:%d/", type, sqlWrapperExtension.getHost(), supportedDatabase.getPort());

            // load the db driver.
            Class.forName(supportedDatabase.getDriverClass());

            // establish the connection.
            final Connection connection = DriverManager.getConnection(connectionUrl, properties);

            return new JDBCConnectionSupplier(connection);
        }
        catch (ClassNotFoundException | SQLException e)
        {
            throw new RuntimeException("Unable to establish DB connection", e);
        }
    }

    @Override
    public Connection get()
    {
        return connection;
    }
}
