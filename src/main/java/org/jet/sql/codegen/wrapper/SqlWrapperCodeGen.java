package org.jet.sql.codegen.wrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.jet.sql.codegen.ObjectMapperFactory;
import org.jet.sql.codegen.wrapper.model.SqlQuery;
import org.jet.sql.codegen.wrapper.model.WrapperConfig;
import org.jet.sql.codegen.wrapper.model.YamlConfig;
import org.jet.sql.codegen.wrapper.util.ConnectionSupplier;
import org.jet.sql.codegen.wrapper.util.WrapperFileUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;

/**
 * @author tgorthi
 * @since December 2019
 */
public class SqlWrapperCodeGen
{
    private static final ObjectMapper OBJECT_MAPPER = ObjectMapperFactory.create();

    private YamlConfig[] _parse(final File sqlFile, final Logger logger)
    {
        try
        {
            logger.info("-------------------------------------------------------------------------------------");
            logger.info("-------------------- Parsing file at : " + sqlFile.getPath() + "---------------------");
            logger.info("-------------------------------------------------------------------------------------");

            return OBJECT_MAPPER.readValue(sqlFile, YamlConfig[].class);
        }
        catch (Throwable e)
        {
            throw new RuntimeException("-------------------- Failed to parse yaml file : [ " + sqlFile.getPath() + " " +
                    "] --------------------", e);
        }
    }

    private void _process(final YamlConfig yamlConfig,
                          final String relativeDirectoryPath,
                          final ConnectionSupplier connectionSupplier,
                          final Logger logger)
    {
        final String packageName = yamlConfig.getPackageName();
        final String className = yamlConfig.getClassName();


        final String dir = WrapperFileUtils.createAndGetGeneratedClassesPath(packageName,
                relativeDirectoryPath,
                logger);


        final File outputClass = new File(dir, className + ".java");
        try
        {
            final FileWriter fileWriter = new FileWriter(outputClass);
            final MustacheFactory factory = new DefaultMustacheFactory();
            final Mustache m = factory.compile("codegen/SqlWrapper.mustache");

            final YamlConfig.RawSql[] rawQueries = yamlConfig.getQueries();
            final SqlQuery[] sqlQueries = new SqlQuery[rawQueries.length];
            for (int i = 0; i < rawQueries.length; i++)
            {
                sqlQueries[i] = QueryParser.convert(rawQueries[i].getName(),
                        rawQueries[i].getSql(),
                        connectionSupplier.get());
            }

            m.execute(fileWriter, new WrapperConfig(packageName, className, sqlQueries)).flush();
        }
        catch (Exception e)
        {
            logger.error("-------------------- Failed to generate processor code --------------------");
            throw new RuntimeException(e);
        }

    }

    public void run(
            final File sqlFile, final String relativeDirectoryPath,
            final ConnectionSupplier connectionSupplier, final Logger logger
    )
    {
        logger.info("-------------------------------------------------------------------------------------");
        logger.info("------------------------------- Building Sql Wrapper --------------------------------");
        logger.info("-------------------------------------------------------------------------------------");

        logger.info("----Generating sql wrapper code gen for file :" + sqlFile.getPath() + "--------");

        final YamlConfig[] configurations = _parse(sqlFile, logger);

        for (YamlConfig config : configurations)
        {
            _process(config, relativeDirectoryPath, connectionSupplier, logger);
        }

        logger.info("-------------------------------------------------------------------------------------");
        logger.info("-------------------------- Finished Building Sql Wrapper ----------------------------");
        logger.info("-------------------------------------------------------------------------------------");
    }
}
