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

package org.fastjax.xml.dom;

import org.junit.Assert;
import org.junit.Test;

public class DOMStyleTest {
  @Test
  public void testConsolidate() {
    Assert.assertNull(DOMStyle.merge((DOMStyle[])null));

    // Condition: default
    DOMStyle option = DOMStyle.merge();
    Assert.assertFalse(option.isIndent());
    Assert.assertFalse(option.isIgnoreNamespaces());

    // Condition: indent
    option = DOMStyle.merge(DOMStyle.INDENT);
    Assert.assertTrue(option.isIndent());
    Assert.assertFalse(option.isIgnoreNamespaces());

    // Condition: ignoreNamespases
    option = DOMStyle.merge(DOMStyle.IGNORE_NAMESPACES);
    Assert.assertTrue(option.isIgnoreNamespaces());
    Assert.assertFalse(option.isIndent());

    // Condition: indent & ignoreNamespases
    option = DOMStyle.merge(DOMStyle.INDENT, DOMStyle.IGNORE_NAMESPACES);
    Assert.assertTrue(option.isIgnoreNamespaces());
    Assert.assertTrue(option.isIndent());
  }
}