package org.jet.sql.codegen.wrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.jet.sql.codegen.ObjectMapperFactory;
import org.jet.sql.codegen.wrapper.model.QueryArgument;
import org.jet.sql.codegen.wrapper.model.ResultColumns;
import org.jet.sql.codegen.wrapper.model.SqlQuery;
import org.jet.sql.codegen.wrapper.model.WrapperConfig;
import org.jet.sql.codegen.wrapper.util.WrapperFileUtils;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

public class SqlWrapperCodeGen
{
    private static final ObjectMapper OBJECT_MAPPER = ObjectMapperFactory.create();

    private WrapperConfig _parse(final String pathToYamlSqlFile, final Logger logger)
    {
        try
        {
            logger.info("Parsing file at " + pathToYamlSqlFile);
            return OBJECT_MAPPER.readValue(new File(pathToYamlSqlFile), WrapperConfig.class);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Failed to parse yaml file : [ " + pathToYamlSqlFile + " ]", e);
        }
    }

    private void _process(final WrapperConfig sqlWrapperConfig, final String relativeDirectoryPath, final Logger logger)
    {
        final String packageName = sqlWrapperConfig.getPackageName();
        final String className = sqlWrapperConfig.getClassName();


        final String dir = WrapperFileUtils.createAndGetGeneratedClassesPath(packageName, relativeDirectoryPath, logger);


        final File outputClass = new File(dir, className + ".java");
        try
        {
            final FileWriter fileWriter = new FileWriter(outputClass);
            final MustacheFactory factory = new DefaultMustacheFactory();
            final Mustache m = factory.compile("codegen/SqlWrapper.mustache");
            m.execute(fileWriter, sqlWrapperConfig).flush();
        }
        catch (IOException e)
        {
            logger.error("Failed to generate processor code");
            throw new RuntimeException();
        }

    }

    public void run(final String path, final String relativeDirectoryPath, final Logger logger)
    {
        logger.info("========================");
        logger.info("Building Sql Wrapper");

        final WrapperConfig config = _parse(path, logger);

        _process(config, relativeDirectoryPath, logger);

        logger.info("Finished Building Sql Wrapper");
        logger.info("========================");
    }

    public void run(final WrapperConfig sqlWrapperConfig, final String relativeDirectoryPath, final Logger logger)
    {
        logger.info("========================");
        logger.info("Generating Sql Wrapper processing classes.");

        _process(sqlWrapperConfig, relativeDirectoryPath, logger);

        logger.info("Finished Building Sql Wrapper");
        logger.info("========================");
    }

    public static void main(String[] args)
    {
        SqlWrapperCodeGen codeGen = new SqlWrapperCodeGen();
        WrapperConfig config = new WrapperConfig("com.test", "CompanyQueries", new SqlQuery(
                "add_company",
                "INSERT INTO COMPANY (NAME, ID) VALUES arg_company_name,arg_company_id RETURNING ID, NAME",
                new QueryArgument[]{
                        new QueryArgument("arg_company_name", "varchar"),
                        new QueryArgument("arg_company_id", "integer"),
                },
                new ResultColumns[]{
                        new ResultColumns("id", "integer"),
                        new ResultColumns("name", "varchar")
                }
        ));

        Stream.of(config.getQueries()).forEach(SqlQuery::preProcess);
        codeGen._process(config, "src/gen", new Logger()
        {
            @Override public String getName()
            {
                return null;
            }

            @Override public boolean isTraceEnabled()
            {
                return false;
            }

            @Override public void trace(String s)
            {

            }

            @Override public void trace(String s, Object o)
            {

            }

            @Override public void trace(String s, Object o, Object o1)
            {

            }

            @Override public void trace(String s, Object... objects)
            {

            }

            @Override public void trace(String s, Throwable throwable)
            {

            }

            @Override public boolean isTraceEnabled(Marker marker)
            {
                return false;
            }

            @Override public void trace(Marker marker, String s)
            {

            }

            @Override public void trace(Marker marker, String s, Object o)
            {

            }

            @Override public void trace(Marker marker, String s, Object o, Object o1)
            {

            }

            @Override public void trace(Marker marker, String s, Object... objects)
            {

            }

            @Override public void trace(Marker marker, String s, Throwable throwable)
            {

            }

            @Override public boolean isDebugEnabled()
            {
                return false;
            }

            @Override public void debug(String s)
            {

            }

            @Override public void debug(String s, Object o)
            {

            }

            @Override public void debug(String s, Object o, Object o1)
            {

            }

            @Override public void debug(String s, Object... objects)
            {

            }

            @Override public void debug(String s, Throwable throwable)
            {

            }

            @Override public boolean isDebugEnabled(Marker marker)
            {
                return false;
            }

            @Override public void debug(Marker marker, String s)
            {

            }

            @Override public void debug(Marker marker, String s, Object o)
            {

            }

            @Override public void debug(Marker marker, String s, Object o, Object o1)
            {

            }

            @Override public void debug(Marker marker, String s, Object... objects)
            {

            }

            @Override public void debug(Marker marker, String s, Throwable throwable)
            {

            }

            @Override public boolean isInfoEnabled()
            {
                return false;
            }

            @Override public void info(String s)
            {

            }

            @Override public void info(String s, Object o)
            {

            }

            @Override public void info(String s, Object o, Object o1)
            {

            }

            @Override public void info(String s, Object... objects)
            {

            }

            @Override public void info(String s, Throwable throwable)
            {

            }

            @Override public boolean isInfoEnabled(Marker marker)
            {
                return false;
            }

            @Override public void info(Marker marker, String s)
            {

            }

            @Override public void info(Marker marker, String s, Object o)
            {

            }

            @Override public void info(Marker marker, String s, Object o, Object o1)
            {

            }

            @Override public void info(Marker marker, String s, Object... objects)
            {

            }

            @Override public void info(Marker marker, String s, Throwable throwable)
            {

            }

            @Override public boolean isWarnEnabled()
            {
                return false;
            }

            @Override public void warn(String s)
            {

            }

            @Override public void warn(String s, Object o)
            {

            }

            @Override public void warn(String s, Object... objects)
            {

            }

            @Override public void warn(String s, Object o, Object o1)
            {

            }

            @Override public void warn(String s, Throwable throwable)
            {

            }

            @Override public boolean isWarnEnabled(Marker marker)
            {
                return false;
            }

            @Override public void warn(Marker marker, String s)
            {

            }

            @Override public void warn(Marker marker, String s, Object o)
            {

            }

            @Override public void warn(Marker marker, String s, Object o, Object o1)
            {

            }

            @Override public void warn(Marker marker, String s, Object... objects)
            {

            }

            @Override public void warn(Marker marker, String s, Throwable throwable)
            {

            }

            @Override public boolean isErrorEnabled()
            {
                return false;
            }

            @Override public void error(String s)
            {

            }

            @Override public void error(String s, Object o)
            {

            }

            @Override public void error(String s, Object o, Object o1)
            {

            }

            @Override public void error(String s, Object... objects)
            {

            }

            @Override public void error(String s, Throwable throwable)
            {

            }

            @Override public boolean isErrorEnabled(Marker marker)
            {
                return false;
            }

            @Override public void error(Marker marker, String s)
            {

            }

            @Override public void error(Marker marker, String s, Object o)
            {

            }

            @Override public void error(Marker marker, String s, Object o, Object o1)
            {

            }

            @Override public void error(Marker marker, String s, Object... objects)
            {

            }

            @Override public void error(Marker marker, String s, Throwable throwable)
            {

            }
        });
    }
}
