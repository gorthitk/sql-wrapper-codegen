package org.jet.sql.codegen.plugin;

import org.gradle.api.file.ConfigurableFileTree;

import java.io.File;

/**
 * Contains Plugin configuration settings.
 *
 * @author tgorthi
 * @since December 2019
 */
public class SqlWrapperExtension
{
    public ConfigurableFileTree sources;
    public File generatedFileDirectory;
    public String userName;
    public String password;
    public String type;
    public String host;

    public ConfigurableFileTree getSources()
    {
        return sources;
    }

    public File getGeneratedFileDirectory()
    {
        return generatedFileDirectory;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getPassword()
    {
        return password;
    }

    public String getType()
    {
        return type;
    }

    public String getHost()
    {
        return host;
    }
}
