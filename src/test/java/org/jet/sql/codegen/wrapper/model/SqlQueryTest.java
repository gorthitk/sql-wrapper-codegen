package org.jet.sql.codegen.wrapper.model;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

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

        query.preProcess();

        for (QueryArgument argument : query.getArguments())
        {
            if (argument.getName().equals("arg_company_name"))
            {
                Assert.assertEquals(argument.getIndex(), 1);
            }
            else if (argument.getName().equals("arg_company_id"))
            {
                Assert.assertEquals(argument.getIndex(), 2);
            }
            else
            {
                throw new RuntimeException("Invalid argument" + argument.getName());
            }
        }
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

        query.preProcess();
        final String parsedSql = query.getSql();

        Assert.assertEquals(parsedSql, "INSERT INTO COMPANY (NAME ,  ID) VALUES ? , ? RETURNING ID ,  NAME ");
        Assert.assertEquals(query.getClassName(), "CompanyQueries");
    }
}
