/* Copyright (c) 2018 OpenJAX
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

package org.openjax.classic.xml.sax;

import org.xml.sax.Attributes;

/**
 * Utility functions for operations pertaining to the {@code org.xml.sax} package.
 */
public final class SAXUtil {
  /**
   * Returns a string encoding of the specified {@code attributes}. The encoding
   * is of the form:
   *
   * <pre>
   * name = "value"
   * </pre>
   *
   * @param attributes The {@code Attributes}.
   * @return A string encoding of the specified {@code attributes}.
   */
  public static String toString(final Attributes attributes) {
    if (attributes == null || attributes.getLength() == 0)
      return null;

    final StringBuilder builder = new StringBuilder();
    for (int i = 0; i < attributes.getLength(); ++i) {
      if (i > 0)
        builder.append(' ');

      builder.append(attributes.getLocalName(i)).append("=\"").append(attributes.getValue(i)).append('"');
    }

    return builder.toString();
  }

  private SAXUtil() {
  }
}