/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.maven.native2utf8;

//~--- non-JDK imports --------------------------------------------------------

import com.google.common.io.Closeables;

import org.apache.commons.lang.StringEscapeUtils;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.nio.CharBuffer;

/**
 *
 * @author th
 */
public class Native2UTF8
{

  /**
   * Constructs ...
   *
   */
  private Native2UTF8() {}

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param src
   * @param dst
   * @param encoding
   *
   * @throws IOException
   */
  public static void nativeToUTF8(File src, File dst, String encoding)
    throws IOException
  {
    BufferedReader input = null;
    BufferedWriter output = null;

    try
    {
      input =
        new BufferedReader(new InputStreamReader(new FileInputStream(src),
          encoding));
      output =
        new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dst),
          "UTF8"));

      char[] buffer = new char[65536];
      int len;

      while ((len = input.read(buffer)) != -1)
      {
        if (len > 0)
        {
          output.write(StringEscapeUtils.unescapeJava((CharBuffer.wrap(buffer,
            0, len)).toString()));
        }
      }
    }
    finally
    {
      Closeables.close(input, true);
      Closeables.close(output, true);
    }
  }
}
