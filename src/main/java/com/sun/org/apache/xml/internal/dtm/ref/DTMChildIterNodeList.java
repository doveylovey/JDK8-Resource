/*
 * Copyright (c) 2007, 2019, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/*
 * Copyright 1999-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: DTMChildIterNodeList.java,v 1.2.4.1 2005/09/15 08:15:00 suresh_emailid Exp $
 */
package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xml.internal.dtm.DTM;
import org.w3c.dom.Node;

/**
 * <code>DTMNodeList</code> gives us an implementation of the DOM's
 * NodeList interface wrapped around a DTM Iterator. The author
 * considers this something of an abominations, since NodeList was not
 * intended to be a general purpose "list of nodes" API and is
 * generally considered by the DOM WG to have be a mistake... but I'm
 * told that some of the XPath/XSLT folks say they must have this
 * solution.
 * <p>
 * Please note that this is not necessarily equivlaent to a DOM
 * NodeList operating over the same document. In particular:
 * <ul>
 *
 * <li>If there are several Text nodes in logical succession (ie,
 * across CDATASection and EntityReference boundaries), we will return
 * only the first; the caller is responsible for stepping through
 * them.
 * (%REVIEW% Provide a convenience routine here to assist, pending
 * proposed DOM Level 3 getAdjacentText() operation?) </li>
 *
 * <li>Since the whole XPath/XSLT architecture assumes that the source
 * document is not altered while we're working with it, we do not
 * promise to implement the DOM NodeList's "live view" response to
 * document mutation. </li>
 *
 * </ul>
 *
 * <p>State: In progress!!</p>
 */
public class DTMChildIterNodeList extends DTMNodeListBase {
    private int m_firstChild;
    private DTM m_parentDTM;

    //================================================================
    // Methods unique to this class
    private DTMChildIterNodeList() {
    }

    /**
     * Public constructor: Create a NodeList to support
     * DTMNodeProxy.getChildren().
     * <p>
     * Unfortunately AxisIterators and DTMIterators don't share an API,
     * so I can't use the existing Axis.CHILD iterator. Rather than
     * create Yet Another Class, let's set up a special case of this
     * one.
     *
     * @param parentDTM    The DTM containing this node
     * @param parentHandle DTM node-handle integer
     */
    public DTMChildIterNodeList(DTM parentDTM, int parentHandle) {
        m_parentDTM = parentDTM;
        m_firstChild = parentDTM.getFirstChild(parentHandle);
    }


    //================================================================
    // org.w3c.dom.NodeList API follows

    /**
     * Returns the <code>index</code>th item in the collection. If
     * <code>index</code> is greater than or equal to the number of nodes in
     * the list, this returns <code>null</code>.
     *
     * @param index Index into the collection.
     * @return The node at the <code>index</code>th position in the
     * <code>NodeList</code>, or <code>null</code> if that is not a valid
     * index.
     */
    public Node item(int index) {
        int handle = m_firstChild;
        while (--index >= 0 && handle != DTM.NULL) {
            handle = m_parentDTM.getNextSibling(handle);
        }
        if (handle == DTM.NULL) {
            return null;
        }
        return m_parentDTM.getNode(handle);
    }

    /**
     * The number of nodes in the list. The range of valid child node indices
     * is 0 to <code>length-1</code> inclusive.
     */
    public int getLength() {
        int count = 0;
        for (int handle = m_firstChild;
             handle != DTM.NULL;
             handle = m_parentDTM.getNextSibling(handle)) {
            ++count;
        }
        return count;
    }
}
