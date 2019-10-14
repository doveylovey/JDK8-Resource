package org.omg.IOP;


/**
 * org/omg/IOP/TaggedComponent.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from c:/re/workspace/8-2-build-windows-amd64-cygwin/jdk8u221/13320/corba/src/share/classes/org/omg/PortableInterceptor/IOP.idl
 * Thursday, July 4, 2019 4:41:47 AM PDT
 */

public final class TaggedComponent implements org.omg.CORBA.portable.IDLEntity {

    /**
     * The tag, represented as a component id.
     */
    public int tag = (int) 0;

    /**
     * The component data associated with the component id.
     */
    public byte component_data[] = null;

    public TaggedComponent() {
    } // ctor

    public TaggedComponent(int _tag, byte[] _component_data) {
        tag = _tag;
        component_data = _component_data;
    } // ctor

} // class TaggedComponent
