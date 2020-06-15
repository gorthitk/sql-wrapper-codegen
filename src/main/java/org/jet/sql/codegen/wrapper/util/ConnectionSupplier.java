package org.jet.sql.codegen.wrapper.util;

import java.sql.Connection;

/**
 * @author tgorthi
 * @since Jun 2020
 */
@FunctionalInterface
public interface ConnectionSupplier
{
    Connection get();
}
