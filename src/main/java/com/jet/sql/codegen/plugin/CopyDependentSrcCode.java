package com.jet.sql.codegen.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Util class to copy Container and RowWrapper classes to the project.
 * @author tgorthi
 * @since Dec 2019
 */
public class CopyDependentSrcCode
{
    private static final String PACKAGE_NAME = "com.jet";
    private static final List<String> CLASSES_TO_COPY = List.of("QueryProcessor", "ResultSetRowContainer");

    public void execute(final String destination)
    {
        final String absoluteDirectoryPath = String.join(File.separator,
                Paths.get(destination).toAbsolutePath().toString(),
                PACKAGE_NAME);

        final File directory = new File(absoluteDirectoryPath);

        if (!directory.exists())
        {
            boolean created = directory.mkdirs();
            if (!created)
            {
                throw new RuntimeException("Unable to create directory at : [" + directory + "]");
            }
        }

        CLASSES_TO_COPY.forEach(className -> {
            final File file = new File("src/main/java/com/jet/" + className + ".java");
            try
            {
                Files.copy(file.toPath(), Paths.get(directory.toPath().toString(), className + ".java"));
            }
            catch (IOException e)
            {
                throw new RuntimeException("Unable to copy java class " + className);
            }
        });
    }
}
