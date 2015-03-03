package com.googlecode.opensubtitlesjapi;

import org.apache.xmlrpc.serializer.TypeSerializerImpl;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class DoubleSerializer extends TypeSerializerImpl {

  public DoubleSerializer() {
    super();
  }

  public void write(ContentHandler pHandler, Object pObject) throws SAXException {
    Double d = (Double) pObject;
    Long l = new Long(d.longValue());
    write(pHandler, "double", l.toString());
  }
}
