package com.sun.corba.se.spi.activation;


/**
 * com/sun/corba/se/spi/activation/NoSuchEndPoint.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from c:/re/workspace/8-2-build-windows-amd64-cygwin/jdk8u221/13320/corba/src/share/classes/com/sun/corba/se/spi/activation/activation.idl
 * Thursday, July 4, 2019 4:41:44 AM PDT
 */

public final class NoSuchEndPoint extends org.omg.CORBA.UserException {

    public NoSuchEndPoint() {
        super(NoSuchEndPointHelper.id());
    } // ctor


    public NoSuchEndPoint(String $reason) {
        super(NoSuchEndPointHelper.id() + "  " + $reason);
    } // ctor

} // class NoSuchEndPoint
