package org.jet.sql.codegen.wrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.jet.sql.codegen.ObjectMapperFactory;
import org.jet.sql.codegen.wrapper.model.WrapperConfig;
import org.jet.sql.codegen.wrapper.util.WrapperFileUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author tgorthi
 * @since December 2019
 */
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
}
