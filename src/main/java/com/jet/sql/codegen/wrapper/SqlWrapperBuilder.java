package com.jet.sql.codegen.wrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jet.sql.codegen.wrapper.model.JavaTypeSupplier;
import com.jet.sql.codegen.wrapper.model.QueryArgument;
import com.jet.sql.codegen.wrapper.model.ResultColumns;
import com.jet.sql.codegen.wrapper.model.SqlQuery;
import com.jet.sql.codegen.wrapper.model.WrapperConfig;
import org.slf4j.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author tgorthi
 * @since December 2019
 */
public class SqlWrapperBuilder
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

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
        final SqlQuery[] queries = sqlWrapperConfig.getQueries();

        final List<String> listOfQueryRunners = new ArrayList<>();
        final String dir = _createAndGetGeneratedClassesPath(packageName, relativeDirectoryPath, logger);

        if (queries == null || queries.length == 0)
        {
            throw new RuntimeException("No queries found in the configuration file");
        }

        final File outputClass = new File(dir, className + ".java");
        try (PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(outputClass)), true,
                StandardCharsets.UTF_8))
        {
            ps.println("/* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */");
            ps.println("/* !!! Auto generated class by SqlWrapperBuilder , do not edit manually. !!! */");
            ps.println("/* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */");
            ps.println();
            ps.println("package " + packageName + ";");
            ps.println();
            ps.println();
            ps.println("import javax.annotation.processing.Generated;");
            ps.println("import com.jet.sql.codegen.QueryProcessor;");
            ps.println("import com.jet.sql.codegen.ResultSetRowContainer;");
            ps.println();
            ps.println("import static java.sql.JDBCType.*;");
            ps.println();
            ps.println("import java.sql.Connection;");
            ps.println("import java.sql.ResultSet;");
            ps.println();
            ps.println();

            // *** Main query class [START] ***//
            ps.println("@Generated(value=\"com.jet.sql.codegen.wrapper.SqlWrapperBuilder\",  date = \"" + LocalDateTime.now().toString() + "\")");
            ps.println("public class " + className);
            ps.println("{");

            for (SqlQuery query : queries)
            {
                final String name = query.getName();
                final String resultRowClassName = query.parseAndCreateClassName() + "ResultRow";

                // *********** Result Row container [START] ***********//

                ps.println("     public static final class " + resultRowClassName + " extends ResultSetRowContainer");
                ps.println("     {");
                ps.println();
                ps.println("     private " + resultRowClassName + " (final ResultSet resultSet)");
                ps.println("     {");
                ps.println("          super(resultSet);");
                ps.println("     }");

                if (query.getResults() != null)
                {
                    for (ResultColumns resultColumn : query.getResults())
                    {
                        ps.println("     public " + JavaTypeSupplier.get(resultColumn.getType()) + " " + resultColumn.evaluateAndGetResultSetAccessorMethodName() + "()");
                        ps.println("     {");
                        ps.println("          return (" + JavaTypeSupplier.get(resultColumn.getType()) + ") " +
                                "getValueByName(\"" + resultColumn.getName() + "\");");
                        ps.println("     }");
                    }
                }
                ps.println("     }");

                // *********** Result Row container [END] ***********//

                final String queryRunnerClassName = query.parseAndCreateClassName() + "QueryRunner";
                listOfQueryRunners.add(queryRunnerClassName);

                // *********** Query Runner [START] ***********//

                ps.println("     public static final class " + queryRunnerClassName + " extends QueryProcessor<" + resultRowClassName + ">");
                ps.println("     {");

                // *** Runner constructor [START] ***//

                ps.println("     private static final String " + name.toUpperCase() + " = " + "\"" + query.convert() + "\" ;");
                ps.println();
                ps.println("     private " + queryRunnerClassName + "()");
                ps.println("     {");
                ps.println("          super(" + name.toUpperCase() + ", " + resultRowClassName + "::new);");
                ps.println("     }");

                // *** Runner constructor [END] ***//

                // *** Runner connection setter [START] ***//

                ps.println("     public " + queryRunnerClassName + " connection(final Connection connection)");
                ps.println("     {");
                ps.println("          setConnection(connection);");
                ps.println("          return this;");
                ps.println("     }");
                ps.println();

                // *** Runner connection setter [END] *** //


                // *** Runner param setter methods [START] *** //
                if (query.getArguments() != null && query.getArguments().length != 0)
                {
                    final Map<String, Integer> argumentPositions = query.evaluateAndGetArgumentPositions();
                    for (QueryArgument param : query.getArguments())
                    {
                        final String bindVariableName = param.getName();
                        final int indexOfVariable = argumentPositions.get(bindVariableName);

                        if (!argumentPositions.containsKey(bindVariableName))
                        {
                            throw new RuntimeException("Illegal bind variable " + bindVariableName + " , variables " +
                                    "not present in the sql.");
                        }

                        // *** Set arguments [START] *** //

                        ps.println("     public " + queryRunnerClassName + " " + param.evaluateAndGetArgumentSetterMethodName() + "(final " + JavaTypeSupplier.get(param.getType()) + " value)");
                        ps.println("     {");
                        ps.println("          addParam(" + indexOfVariable + "," + param.getType() + "," + "value);");
                        ps.println("          return this;");
                        ps.println("     }");

                        // *** Set arguments [END] *** //

                        ps.println();
                    }
                }
                // *** Runner param setter methods [END] *** //


                ps.println("     }");
                // *********** Query Runner [END] *********** //
                ps.println();
            }

            // *** Add static Runners Accessor class [START] ** //

            ps.println("     public static class Runners");
            ps.println("     {");

            for (String runnerClass : listOfQueryRunners)
            {
                ps.println("     public static " + runnerClass + " " + runnerClass + "()");
                ps.println("     {");
                ps.println("          return new " + runnerClass + "();");
                ps.println("     }");
                ps.println();
            }
            ps.println("     }");

            // *** Add static Runners Accessor class [END] ** //

            ps.println("}");
            // *** Main query class [END] *** //
        }
        catch (Throwable e)
        {
            logger.error("Failed to generate java class for file config : " + sqlWrapperConfig.toString());
            e.printStackTrace();
        }
    }


    private String _createAndGetGeneratedClassesPath(final String packageName, final String relativeDirectoryPath, final Logger logger)
    {
        final String absoluteDirectoryPath = String.join(File.separator,
                Paths.get(relativeDirectoryPath).toAbsolutePath().toString(),
                packageName);

        final File directory = new File(absoluteDirectoryPath);
        if (!directory.exists())
        {
            boolean created = directory.mkdirs();
            if (!created)
            {
                throw new RuntimeException("Unable to create directory at : [" + directory + "]");
            }
        }

        logger.info("** ==== Writing generated classes to : [ " + absoluteDirectoryPath + "] ==== **");

        return absoluteDirectoryPath;
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
