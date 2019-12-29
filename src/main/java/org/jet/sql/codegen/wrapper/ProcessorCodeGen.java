package org.jet.sql.codegen.wrapper;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.jet.sql.codegen.wrapper.util.WrapperFileUtils;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author tgorthi
 * @since December 2019
 */
public class ProcessorCodeGen
{
    private static final String PACKAGE_NAME = "org.jet.sql.codegen";
    private static final Map<String, String> TEMPLATE_BY_CLASS_NAME = Map.of(
            "QueryProcessor", "codegen/QueryProcessor.mustache",
            "ResultSetRowContainer", "codegen/ResultSetRowContainer.mustache"
    );

    private void _process(final String relativeDirectoryPath, final Logger logger)
    {
        final String dir = WrapperFileUtils.createAndGetGeneratedClassesPath(PACKAGE_NAME, relativeDirectoryPath,
                logger);
        final Map<String, String> classMetaData = Map.of("generatedDate", LocalDateTime.now().toString());

        TEMPLATE_BY_CLASS_NAME.forEach((className, template) ->
        {
            final File outputClass = new File(dir, className + ".java");
            try
            {
                final FileWriter fileWriter = new FileWriter(outputClass);
                final MustacheFactory factory = new DefaultMustacheFactory();
                final Mustache m = factory.compile(template);
                m.execute(fileWriter, classMetaData).flush();
            }
            catch (IOException e)
            {
                logger.error("Failed to generate processor code");
                throw new RuntimeException();
            }
        });

    }

    public void run(final String relativeDirectoryPath, final Logger logger)
    {
        logger.info("========================");
        logger.info("Generating Sql Wrapper processing classes.");

        _process(relativeDirectoryPath, logger);

        logger.info("Finished Building Sql Wrapper");
        logger.info("========================");
    }

    public static void main(String[] args)
    {
        ProcessorCodeGen codeGen = new ProcessorCodeGen();
        codeGen.run("src/gen", new Logger()
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
