package com.sun.corba.se.spi.activation;

/**
 * com/sun/corba/se/spi/activation/ServerAlreadyActiveHolder.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from c:/re/workspace/8-2-build-windows-amd64-cygwin/jdk8u221/13320/corba/src/share/classes/com/sun/corba/se/spi/activation/activation.idl
 * Thursday, July 4, 2019 4:41:44 AM PDT
 */

public final class ServerAlreadyActiveHolder implements org.omg.CORBA.portable.Streamable {
    public com.sun.corba.se.spi.activation.ServerAlreadyActive value = null;

    public ServerAlreadyActiveHolder() {
    }

    public ServerAlreadyActiveHolder(com.sun.corba.se.spi.activation.ServerAlreadyActive initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = com.sun.corba.se.spi.activation.ServerAlreadyActiveHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        com.sun.corba.se.spi.activation.ServerAlreadyActiveHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return com.sun.corba.se.spi.activation.ServerAlreadyActiveHelper.type();
    }

}
