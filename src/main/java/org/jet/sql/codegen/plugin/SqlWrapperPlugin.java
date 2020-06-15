package org.jet.sql.codegen.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jet.sql.codegen.wrapper.ProcessorCodeGen;
import org.jet.sql.codegen.wrapper.SqlWrapperCodeGen;
import org.jet.sql.codegen.wrapper.util.ConnectionSupplier;
import org.jet.sql.codegen.wrapper.util.JDBCConnectionSupplier;

/**
 * Implementation of the Plugin.
 * - Contains two tasks
 *   1. Generate the required abstract classes or interface implementation that execute a query on the db servers and to provider a simpler interface for prepared statements.
 *   2. Generate the required type safe Java Classes to set prepared statement bind variables (also called parameters , arguments) and type safe methods to extract result sets.
 *
 * @author tgorthi
 * @since Dec 2019
 */
public class SqlWrapperPlugin implements Plugin<Project>
{
    public static final String ID = "org.jet.sql.codegen";
    public static final String TASK_ID = "generateSqlWrapper";

    @Override
    public void apply(final Project project)
    {
        final SqlWrapperExtension extension = project.getExtensions().create("sqlWrapperConfig",
                SqlWrapperExtension.class);


        project.task(TASK_ID)
                .doFirst(task ->
                {
                    final String relativeDirectoryPath = project.file(extension.getGeneratedFileDirectory()).getPath();
                    ProcessorCodeGen codeGen = new ProcessorCodeGen();
                    codeGen.run(relativeDirectoryPath, project.getLogger());
                })
                .doLast(task ->
                {
                    final String relativeDirectoryPath = project.file(extension.getGeneratedFileDirectory()).getPath();
                    final SqlWrapperCodeGen sqlWrapperCodeGen = new SqlWrapperCodeGen();
                    final ConnectionSupplier connectionSupplier = JDBCConnectionSupplier.create(extension);

                    extension.getSources().forEach(file -> sqlWrapperCodeGen.run(file, relativeDirectoryPath,
                            connectionSupplier, project.getLogger()));
                });
    }
}
