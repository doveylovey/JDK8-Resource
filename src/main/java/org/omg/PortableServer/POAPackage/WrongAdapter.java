package org.omg.PortableServer.POAPackage;


/**
 * org/omg/PortableServer/POAPackage/WrongAdapter.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from c:/re/workspace/8-2-build-windows-amd64-cygwin/jdk8u221/13320/corba/src/share/classes/org/omg/PortableServer/poa.idl
 * Thursday, July 4, 2019 4:41:49 AM PDT
 */

public final class WrongAdapter extends org.omg.CORBA.UserException {

    public WrongAdapter() {
        super(WrongAdapterHelper.id());
    } // ctor


    public WrongAdapter(String $reason) {
        super(WrongAdapterHelper.id() + "  " + $reason);
    } // ctor

} // class WrongAdapter
