package org.omg.CosNaming.NamingContextPackage;


/**
 * org/omg/CosNaming/NamingContextPackage/InvalidName.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from c:/re/workspace/8-2-build-windows-amd64-cygwin/jdk8u221/13320/corba/src/share/classes/org/omg/CosNaming/nameservice.idl
 * Thursday, July 4, 2019 4:41:44 AM PDT
 */

public final class InvalidName extends org.omg.CORBA.UserException {

    public InvalidName() {
        super(InvalidNameHelper.id());
    } // ctor


    public InvalidName(String $reason) {
        super(InvalidNameHelper.id() + "  " + $reason);
    } // ctor

} // class InvalidName
