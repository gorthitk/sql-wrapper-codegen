package org.jet.sql.codegen.wrapper;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.jet.sql.codegen.wrapper.util.WrapperFileUtils;
import org.slf4j.Logger;

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
                logger.error("------------------- Failed to generate processor code -------------------");
                throw new RuntimeException();
            }
        });

    }

    public void run(final String relativeDirectoryPath, final Logger logger)
    {
        logger.info("-------------------------------------------------------------------------------------");
        logger.info("-------------------- Generating Sql Wrapper processing classes. ---------------------");
        logger.info("-------------------------------------------------------------------------------------");


        _process(relativeDirectoryPath, logger);

        logger.info("-------------------------------------------------------------------------------------");
        logger.info("-------------------------- Finished Building Sql Wrapper. ---------------------------");
        logger.info("-------------------------------------------------------------------------------------");
    }
}
