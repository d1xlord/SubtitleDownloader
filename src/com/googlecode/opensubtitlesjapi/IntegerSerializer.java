package com.googlecode.opensubtitlesjapi;

import org.apache.xmlrpc.serializer.TypeSerializerImpl;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class IntegerSerializer extends TypeSerializerImpl {

  public IntegerSerializer() {
    super();
  }

  public void write(ContentHandler pHandler, Object pObject) throws SAXException {
    Integer i = (Integer) pObject;
    write(pHandler, "int", i.toString());
  }
}
