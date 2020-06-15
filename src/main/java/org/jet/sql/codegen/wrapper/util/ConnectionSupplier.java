package org.jet.sql.codegen.wrapper.util;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionSupplier
{
    public Connection get(String name) throws SQLException;
}
