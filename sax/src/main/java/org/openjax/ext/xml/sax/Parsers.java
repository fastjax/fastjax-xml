package org.openjax.ext.xml.sax;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public final class Parsers {
  private static final Logger logger = LoggerFactory.getLogger(Parsers.class);
  private static SAXParserFactory factory;

  static {
    try {
      factory = SAXParserFactory.newInstance("org.apache.xerces.jaxp.SAXParserFactoryImpl", null);
    }
    catch (final FactoryConfigurationError e) {
      factory = SAXParserFactory.newInstance();
      logger.warn("Unable to create SAXParserFactory of type org.apache.xerces.jaxp.SAXParserFactoryImpl. Factory of " + factory.getClass().getName() + " created instead.", e);
    }
  }

  public static SAXParser newParser(final boolean validating) throws SAXException {
    factory.setNamespaceAware(true);
    factory.setValidating(true);
    try {
      factory.setFeature("http://xml.org/sax/features/validation", validating);
      factory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
      factory.setFeature("http://apache.org/xml/features/validation/schema-full-checking", validating);
      factory.setFeature("http://apache.org/xml/features/honour-all-schemaLocations", true);
      factory.setFeature("http://apache.org/xml/features/continue-after-fatal-error", true);
      return factory.newSAXParser();
    }
    catch (final ParserConfigurationException e) {
      throw new SAXException(e);
    }
  }

  private Parsers() {
  }
}