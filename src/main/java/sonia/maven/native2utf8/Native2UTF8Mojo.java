/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.maven.native2utf8;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;

//~--- JDK imports ------------------------------------------------------------

import java.io.*;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Converts files with characters in any supported character encoding to Unicode
 * 
 * @goal native2utf8
 * @phase process-classes
 */
public class Native2UTF8Mojo extends AbstractMojo
{

  /**
   * Method description
   *
   *
   * @throws MojoExecutionException
   * @throws MojoFailureException
   */
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException
  {
    if (!workDir.exists())
    {
      return;
    }

    if (StringUtils.isEmpty(encoding))
    {
      encoding = System.getProperty("file.encoding");
      getLog().warn("Using platform encoding (" + encoding
        + " actually) to convert resources!");
    }

    if ((includes == null) || (includes.length == 0))
    {
      includes = new String[] { "**/*.properties" };
    }

    if (excludes == null)
    {
      excludes = new String[0];
    }

    Iterator files;

    try
    {
      getLog().info("Includes: " + Arrays.asList(includes));
      getLog().info("Excludes: " + Arrays.asList(excludes));

      String incl = StringUtils.join(includes, ",");
      String excl = StringUtils.join(excludes, ",");

      files = FileUtils.getFiles(workDir, incl, excl).iterator();
    }
    catch (IOException e)
    {
      throw new MojoExecutionException("Unable to get list of files");
    }

    while (files.hasNext())
    {
      File file = (File) files.next();

      getLog().info("Processing " + file.getAbsolutePath());

      try
      {

        // Convert file in-place
        File tempFile = File.createTempFile(file.getName(), "native2utf8",
                          tempDir);

        Native2UTF8.nativeToUTF8(file, tempFile, encoding);
        FileUtils.rename(tempFile, file);
      }
      catch (IOException e)
      {
        throw new MojoExecutionException("Unable to convert "
          + file.getAbsolutePath(), e);
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /**
   * The native encoding the files are in.
   *
   * @parameter default-value="${project.build.sourceEncoding}"
   * @since 1.0-alpha-1
   */
  protected String encoding;

  /**
   * Patterns of files that must be excluded.
   *
   * @parameter
   * @since 1.0-alpha-2
   */
  protected String[] excludes;

  /**
   * Patterns of files that must be included. Default is "**\/*.properties".
   *
   * @parameter
   * @since 1.0-alpha-2
   */
  protected String[] includes;

  /**
   * Directory for temporary files.
   *
   * @parameter default-value="${project.build.directory}
   * @since 1.0-alpha-2
   */
  protected File tempDir;

  /**
   * The directory to find files in.
   *
   * @parameter default-value="${project.build.outputDirectory}"
   * @since 1.0-alpha-2
   */
  protected File workDir;
}
