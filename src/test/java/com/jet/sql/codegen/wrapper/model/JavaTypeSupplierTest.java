package com.jet.sql.codegen.wrapper.model;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.JDBCType;

/**
 * @author tgorthi
 * @since December 2019
 * Test class for {@link JavaTypeSupplier}
 */
public class JavaTypeSupplierTest
{
    @Test
    public void testGet()
    {
        Assert.assertEquals(JavaTypeSupplier.get(JDBCType.INTEGER), "java.lang.Integer");
        Assert.assertEquals(JavaTypeSupplier.get(JDBCType.VARCHAR), "java.lang.String");
        Assert.assertEquals(JavaTypeSupplier.get(JDBCType.BOOLEAN), "java.lang.Boolean");
        Assert.assertEquals(JavaTypeSupplier.get(JDBCType.ARRAY), "java.lang.Object[]");

        Assert.assertEquals(JavaTypeSupplier.get(JDBCType.JAVA_OBJECT), "java.lang.Object");
    }
}
