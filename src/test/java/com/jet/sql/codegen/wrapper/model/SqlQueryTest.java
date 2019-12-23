package com.jet.sql.codegen.wrapper.model;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * Unit tests for {@link SqlQuery}
 *
 * @author tgorthi
 * @since December 2019
 */
public class SqlQueryTest
{
    private QueryArgument[] arguments;
    private ResultColumns[] results;

    @BeforeTest
    public void setUp()
    {
        arguments = new QueryArgument[]{
                new QueryArgument("arg_company_name", "varchar"),
                new QueryArgument("arg_company_id", "integer"),
        };

        results = new ResultColumns[]{
                new ResultColumns("id", "integer"),
                new ResultColumns("name", "varchar")
        };

    }

    @Test
    public void testEvaluateAndGetArgumentPositions()
    {
        final SqlQuery query = new SqlQuery(
                "company_queries",
                "INSERT INTO COMPANY (NAME, ID) VALUES arg_company_name,arg_company_id RETURNING ID, NAME",
                arguments,
                results
        );

        Map<String, Integer> paramPosition = query.evaluateAndGetArgumentPositions();

        Assert.assertNotNull(paramPosition);
        Assert.assertEquals(paramPosition.size(), 2);

        Assert.assertTrue(paramPosition.containsKey("arg_company_name"));
        Assert.assertTrue(paramPosition.containsKey("arg_company_id"));

        Assert.assertEquals(paramPosition.get("arg_company_name"), Integer.valueOf(1));
        Assert.assertEquals(paramPosition.get("arg_company_id"), Integer.valueOf(2));
    }

    @Test
    public void testConvert()
    {
        final SqlQuery query = new SqlQuery(
                "company_queries",
                "INSERT INTO COMPANY (NAME, ID) VALUES arg_company_name,arg_company_id RETURNING ID, NAME",
                arguments,
                results
        );

        final String parsedSql = query.convert();

        Assert.assertEquals(parsedSql, "INSERT INTO COMPANY (NAME ,  ID) VALUES ? , ? RETURNING ID ,  NAME ");
        Assert.assertEquals(query.parseAndCreateClassName(), "CompanyQueries");
    }
}
