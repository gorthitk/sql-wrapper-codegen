package org.jet.sql.codegen.wrapper.model;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.JDBCType;

/**
 * Unit tests for {@link QueryArgument}
 * @author tgorthi
 * @since December 2019
 */
public class QueryArgumentTest
{
    @Test(expectedExceptions = RuntimeException.class)
    public void testInValidQueryParam()
    {
        final QueryArgument argument = new QueryArgument("test_param", "varchar");
        argument.evaluateAndGetArgumentSetterMethodName();
    }

    @Test
    public void testGetBindVariableSetterMethodName()
    {
        final QueryArgument argument = new QueryArgument("arg_test_param", "varchar");
        Assert.assertEquals(argument.evaluateAndGetArgumentSetterMethodName(), "testParam");
    }


    @Test(expectedExceptions = RuntimeException.class)
    public void testGetBindVariableSetterMethodNameWithSpecialCharacters()
    {
        final QueryArgument argument = new QueryArgument("arg_test_param$%", "varchar");
        argument.evaluateAndGetArgumentSetterMethodName();
    }

    @Test
    public void testGetBindVariableSetterMethodNameWithNumericCharacters()
    {
        final QueryArgument argument = new QueryArgument("arg_test_param_1", "varchar");
        Assert.assertEquals(argument.evaluateAndGetArgumentSetterMethodName(), "testParam1");
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testEmptyParamType()
    {
        final QueryArgument argument = new QueryArgument("arg_", "varchar");
        argument.evaluateAndGetArgumentSetterMethodName();
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testEmptyParamType2()
    {
        final QueryArgument argument = new QueryArgument("arg_____", "varchar");
        argument.evaluateAndGetArgumentSetterMethodName();
    }


    @Test(expectedExceptions = RuntimeException.class)
    public void testInvalidQueryParamType()
    {
        final QueryArgument argument = new QueryArgument("arg_test_param", "string");
        argument.getType();
    }

    @Test
    public void testValidQueryParamType()
    {
        final QueryArgument argument = new QueryArgument("arg_test_param", "varchar");
        Assert.assertEquals(argument.getType(), JDBCType.VARCHAR);
    }
}
