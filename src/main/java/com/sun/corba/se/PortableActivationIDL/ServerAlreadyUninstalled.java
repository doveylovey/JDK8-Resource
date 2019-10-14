package com.sun.corba.se.PortableActivationIDL;


/**
 * com/sun/corba/se/PortableActivationIDL/ServerAlreadyUninstalled.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from c:/re/workspace/8-2-build-windows-amd64-cygwin/jdk8u221/13320/corba/src/share/classes/com/sun/corba/se/PortableActivationIDL/activation.idl
 * Thursday, July 4, 2019 4:41:44 AM PDT
 */

public final class ServerAlreadyUninstalled extends org.omg.CORBA.UserException {
    public String serverId = null;

    public ServerAlreadyUninstalled() {
        super(ServerAlreadyUninstalledHelper.id());
    } // ctor

    public ServerAlreadyUninstalled(String _serverId) {
        super(ServerAlreadyUninstalledHelper.id());
        serverId = _serverId;
    } // ctor


    public ServerAlreadyUninstalled(String $reason, String _serverId) {
        super(ServerAlreadyUninstalledHelper.id() + "  " + $reason);
        serverId = _serverId;
    } // ctor

} // class ServerAlreadyUninstalled
