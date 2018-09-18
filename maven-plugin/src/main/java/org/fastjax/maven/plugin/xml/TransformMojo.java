/* Copyright (c) 2008 FastJAX
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.fastjax.maven.plugin.xml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashSet;

import javax.xml.transform.TransformerException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.fastjax.io.Files;
import org.fastjax.maven.mojo.MojoUtil;
import org.fastjax.net.URLs;
import org.fastjax.util.Dates;
import org.fastjax.xml.OfflineValidationException;
import org.fastjax.xml.transform.Transformer;

@Mojo(name="transform", defaultPhase=LifecyclePhase.COMPILE)
@Execute(goal="transform")
public final class TransformMojo extends XmlMojo {
  @Parameter(property="destDir", required=true)
  private String destDir;

  @Parameter(property="rename")
  private String rename;

  @Parameter(property="stylesheet")
  private File stylesheet;

  @Override
  public void executeMojo(final LinkedHashSet<URL> urls) throws MojoExecutionException, MojoFailureException {
    try {
      for (final URL url : urls) {
        final String outFileName = MojoUtil.getRenamedFileName(url, rename);
        final File destFile = new File(destDir, outFileName);
        final String inFilePath = URLs.isFile(url) ? Files.relativePath(XmlMojo.CWD.getAbsoluteFile(), new File(url.getFile()).getAbsoluteFile()) : url.toExternalForm();

        long lastModified;
        if (destFile.exists() && destFile.lastModified() >= (lastModified = URLs.getLastModified(url)) && destFile.lastModified() < lastModified + Dates.MILLISECONDS_IN_DAY) {
          getLog().info("Pre-transformed: " + inFilePath);
        }
        else {
          final String outFilePath = Files.relativePath(XmlMojo.CWD.getAbsoluteFile(), destFile.getAbsoluteFile());
          getLog().info("   Transforming: " + inFilePath + " -> " + outFilePath);

          Transformer.transform(stylesheet.toURI().toURL(), url, destFile);
        }
      }
    }
    catch (final OfflineValidationException e) {
      throw new MojoFailureException(e.getMessage(), e);
    }
    catch (final IOException | TransformerException e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }
}