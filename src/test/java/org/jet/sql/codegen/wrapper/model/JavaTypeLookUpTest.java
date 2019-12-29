package org.jet.sql.codegen.wrapper.model;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.JDBCType;

/**
 * @author tgorthi
 * @since December 2019
 * Test class for {@link JavaTypeLookUp}
 */
public class JavaTypeLookUpTest
{
    @Test
    public void testGet()
    {
        Assert.assertEquals(JavaTypeLookUp.get(JDBCType.INTEGER), "java.lang.Integer");
        Assert.assertEquals(JavaTypeLookUp.get(JDBCType.VARCHAR), "java.lang.String");
        Assert.assertEquals(JavaTypeLookUp.get(JDBCType.BOOLEAN), "java.lang.Boolean");
        Assert.assertEquals(JavaTypeLookUp.get(JDBCType.ARRAY), "java.lang.Object[]");

        Assert.assertEquals(JavaTypeLookUp.get(JDBCType.JAVA_OBJECT), "java.lang.Object");
    }
}
