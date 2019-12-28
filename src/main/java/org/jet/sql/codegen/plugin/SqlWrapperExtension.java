package org.jet.sql.codegen.plugin;

import org.gradle.api.file.ConfigurableFileTree;

import java.io.File;

/**
 * @author tgorthi
 * @since December 2019
 */
public class SqlWrapperExtension
{
    public ConfigurableFileTree sources;
    public File generatedFileDirectory;
}
