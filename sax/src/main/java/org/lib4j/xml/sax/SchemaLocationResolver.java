/* Copyright (c) 2008 lib4j
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

package org.lib4j.xml.sax;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.xml.XMLConstants;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class SchemaLocationResolver implements LSResourceResolver {
  private static URL xmlSchemaXsd;
  private static URL xmlXsd;

  private final XMLCatalog catalog;

  public SchemaLocationResolver(final XMLCatalog catalog) {
    this.catalog = catalog;
  }

  @Override
  public LSInput resolveResource(final String type, final String namespaceURI, final String publicId, String systemId, final String baseURI) {
    if (namespaceURI == null && systemId == null)
      return new LSInputImpl(systemId, publicId, baseURI);

    if (systemId == null)
      systemId = namespaceURI;
    else if (baseURI != null)
      systemId = SchemaLocationHandler.getPath(baseURI, systemId);

    try {
      SchemaLocation schemaLocation = catalog.getSchemaLocation(namespaceURI);
      final Map<String,URL> directory;
      if (schemaLocation == null) {
        if (namespaceURI == null) {
          SchemaLocation nullLocation = catalog.getSchemaLocation(null);
          if (nullLocation == null)
            catalog.putSchemaLocation(null, nullLocation = new SchemaLocation(null));

          directory = nullLocation.getDirectory();
        }
        else if (XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(namespaceURI)) {
          if (xmlSchemaXsd == null)
            xmlSchemaXsd = Thread.currentThread().getContextClassLoader().getResource("xmlschema/XMLSchema.xsd");

          catalog.putSchemaLocation(XMLConstants.W3C_XML_SCHEMA_NS_URI, schemaLocation = new SchemaLocation(namespaceURI));
          schemaLocation.getDirectory().put(namespaceURI, xmlSchemaXsd);
          directory = schemaLocation.getDirectory();
        }
        else if (XMLConstants.XML_NS_URI.equals(namespaceURI) && "http://www.w3.org/2001/xml.xsd".equals(systemId)) {
          if (xmlXsd == null)
            xmlXsd = Thread.currentThread().getContextClassLoader().getResource("xmlschema/xml.xsd");

          catalog.putSchemaLocation(namespaceURI, schemaLocation = new SchemaLocation(systemId));
          schemaLocation.getDirectory().put(systemId, xmlXsd);
          directory = schemaLocation.getDirectory();
        }
        else {
          return new LSInputImpl(systemId, publicId, baseURI);
        }
      }
      else {
        directory = schemaLocation.getDirectory();
      }

      URL url = directory.get(systemId);
      if (url == null) {
        if (namespaceURI != null)
          return new LSInputImpl(systemId, publicId, baseURI);

        if ("http://www.w3.org/2001/XMLSchema.dtd".equals(systemId)) {
          final URL resource = Thread.currentThread().getContextClassLoader().getResource("xmlschema/XMLSchema.dtd");
          directory.put(systemId, url = resource);
        }
        else if ("http://www.w3.org/2001/datatypes.dtd".equals(systemId)) {
          final URL resource = Thread.currentThread().getContextClassLoader().getResource("xmlschema/datatypes.dtd");
          directory.put(systemId, url = resource);
        }
        else {
          return new LSInputImpl(systemId, publicId, baseURI);
        }
      }

      final LSInput input = new LSInputImpl(systemId, publicId, baseURI);
      input.setByteStream(url.openStream());
      return input;
    }
    catch (final IOException e) {
      throw new IllegalStateException(e);
    }
  }
}