package org.jet.sql.codegen;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Class that abstracts away the process to run a prepared statement and return the results.
 *
 * @author tgorthi
 * @since December 2019
 */
public abstract class QueryProcessor<T extends ResultSetRowContainer>
{
    private final String sql;
    private final List<BindVariable> params = new ArrayList<>();
    private final ResultSetContainerSupplier<T> resultSetContainerSupplier;

    private Connection connection;

    public QueryProcessor(final String sql, final ResultSetContainerSupplier<T> resultSetContainerSupplier)
    {
        this.sql = sql;
        this.resultSetContainerSupplier = resultSetContainerSupplier;
    }

    /**
     * @param index -> index of the parameter in the sql query.
     * @param type -> {@link JDBCType} of the parameter.
     * @param value -> the actual "real" value of the parameter that needs to be set on the {@link PreparedStatement}.
     */
    protected void addParam(final int index, final JDBCType type, final Object value)
    {
        params.add(new BindVariable(index, type, value));
    }

    /**
     * @param connection -> Connection Object required to establish a connection with the database where the query needs to be executed.
     */
    protected void setConnection(final Connection connection)
    {
        this.connection = Objects.requireNonNull(connection);
    }

    /**
     * @return a new instance of {@link PreparedStatement} class with all the params values set.
     * @throws SQLException bubbles up the exceptions for any database errors to the calling methods.
     */
    private PreparedStatement _createPreparedStatement() throws SQLException
    {
        final PreparedStatement ps = connection.prepareStatement(sql);
        ps.setFetchSize(1000);

        this.params.forEach((p) -> p.accept(ps, connection));
        return ps;
    }

    public T executeAndReturnIterator() throws SQLException
    {
        final PreparedStatement ps = _createPreparedStatement();
        return resultSetContainerSupplier.supply(ps.executeQuery());
    }

    public Stream<T> execute() throws SQLException
    {
        final PreparedStatement ps = _createPreparedStatement();
        final T iterator = resultSetContainerSupplier.supply(ps.executeQuery());

        return (Stream<T>) StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
    }

    public interface ResultSetContainerSupplier<T extends ResultSetRowContainer>
    {
        T supply(final ResultSet rs);
    }

    /**
     * Object to hold the index, type and value of query bind variable.
     * Also, abstracts away the logic that sets the bind variables on a prepared statement.
     */
    private static class BindVariable implements BiConsumer<PreparedStatement, Connection>
    {
        private final int m_index;
        private final JDBCType m_type;
        private final Object m_value;

        private BindVariable(final int index, final JDBCType type, final Object value)
        {
            m_index = index;
            m_type = type;
            m_value = value;
        }

        @Override
        public void accept(final PreparedStatement preparedStatement, final Connection connection)
        {
            try
            {
                if (m_value == null)
                {
                    preparedStatement.setNull(m_index, m_type.getVendorTypeNumber());
                    return;
                }

                switch (m_type)
                {
                    case INTEGER:
                    {
                        preparedStatement.setInt(m_index, (int) m_value);
                        return;
                    }
                    case VARCHAR:
                    {
                        preparedStatement.setString(m_index, (String) m_value);
                        return;
                    }
                    case ARRAY:
                    {
                        preparedStatement.setArray(m_index, connection.createArrayOf("int", (Object[]) m_value));
                        return;
                    }
                    case BOOLEAN:
                    {
                        preparedStatement.setBoolean(m_index, (boolean) m_value);
                        return;
                    }
                    default:
                    {
                        throw new RuntimeException("JDBC Type " + m_type + " is currently not supported");
                    }
                }
            }
            catch (SQLException ex)
            {
                throw new RuntimeException("Unable to apply query param" + this.toString(), ex);
            }
        }
    }
}