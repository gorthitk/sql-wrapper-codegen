package com.jet.plugin;

import com.jet.wrapper.SqlWrapperBuilder;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * @author tgorthi
 * @since Dec 2019
 */
public class SqlWrapperPlugin implements Plugin<Project>
{
    public static final String ID = "sql-wrapper-codegen";
    public static final String TASK_ID = "generateSqlWrapper";

    @Override
    public void apply(final Project project)
    {
        final SqlWrapperExtension extension = project.getExtensions().create("sqlWrapperConfig",
                SqlWrapperExtension.class);

        final SqlWrapperBuilder wrapperBuilder = new SqlWrapperBuilder();

        project.task(TASK_ID)
                .doFirst(task ->
                {
                    new CopyDependentSrcCode().execute(extension.generatedFileDirectory.toPath().toString());
                    final String relativeDirectoryPath = project.file(extension.generatedFileDirectory).getPath();
                    extension.sources.forEach(file -> wrapperBuilder.run(file.getPath(), relativeDirectoryPath, project.getLogger()));
                });
    }
}
