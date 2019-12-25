package org.jet.sql.codegen.plugin;

import org.gradle.api.file.ConfigurableFileTree;

import java.io.File;

public class SqlWrapperExtension
{
    public ConfigurableFileTree sources;
    public File generatedFileDirectory;
}
