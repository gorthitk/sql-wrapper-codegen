package org.jet.sql.codegen.plugin;

import org.jet.sql.codegen.wrapper.ProcessorCodeGen;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jet.sql.codegen.wrapper.SqlWrapperCodeGen;

/**
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
        final SqlWrapperExtension extension = project.getExtensions().create("sqlWrapperConfig", SqlWrapperExtension.class);


        project.task(TASK_ID)
                .doFirst(task -> {
                    final String relativeDirectoryPath = project.file(extension.generatedFileDirectory).getPath();
                    ProcessorCodeGen codeGen = new ProcessorCodeGen();
                    codeGen.run(relativeDirectoryPath, project.getLogger());
                })
                .doLast(task ->
                {
                    final String relativeDirectoryPath = project.file(extension.generatedFileDirectory).getPath();
                    final SqlWrapperCodeGen sqlWrapperCodeGen = new SqlWrapperCodeGen();
                    extension.sources.forEach(file -> sqlWrapperCodeGen.run(file.getPath(), relativeDirectoryPath, project.getLogger()));
                });
    }
}
