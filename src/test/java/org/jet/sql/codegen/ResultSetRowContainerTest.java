package org.jet.sql.codegen;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ResultSetRowContainer}
 *
 * @author tgorthi
 * @since December 2019
 */
public class ResultSetRowContainerTest
{
    @Test
    public void testIterator() throws SQLException
    {
        final ResultSetRowContainer container = new TestContainer();
        Assert.assertTrue(container.hasNext());
        Assert.assertEquals(container.getValueByName("id"), 1);
        Assert.assertEquals(container.getValueByName("name"), "Test Company Name");

        Assert.assertFalse(container.hasNext());
    }

    private class TestContainer extends ResultSetRowContainer
    {

        public TestContainer() throws SQLException
        {
            super(_mockResultSet());
        }
    }

    private ResultSet _mockResultSet() throws SQLException
    {
        final ResultSet resultSet = mock(ResultSet.class);
        final ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(2);
        when(metaData.getColumnName(1)).thenReturn("id");
        when(metaData.getColumnName(2)).thenReturn("name");

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(resultSet.next()).thenReturn(true, false);

        when(resultSet.getObject(1)).thenReturn(1);
        when(resultSet.getObject(2)).thenReturn("Test Company Name");

        return resultSet;
    }
}
