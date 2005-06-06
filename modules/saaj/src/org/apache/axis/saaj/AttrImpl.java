/*
 * Created on May 23, 2005
 *
 */
package org.apache.axis.saaj;

import org.apache.axis.om.OMAttribute;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 * @author Jayachandra
 *
 */
public class AttrImpl extends NodeImpl implements Attr {
	
	private boolean specified = false;
	private OMAttribute omAttr;
	private Element ownerElement=null;

	/**
	 * 
	 */
	public AttrImpl() {
		super();
	}	

	/**
	 * @param attrib
	 */
	public AttrImpl(OMAttribute attrib, Element owner) {
		super(attrib);
		this.omAttr = attrib;
		this.ownerElement = owner;
	}
	
	/**
	 * Method getSpecified
	 * Returns true if this attribute value is set by user in the original document,
	 * if so far user didn't set some value to this OR if the user removed this
	 * attribute (may be by calling removeAttribute() on owner element) the specified 
	 * value returned should be false
	 * @see org.w3c.dom.Attr#getSpecified()
	 */
	public boolean getSpecified() {	
		return specified;
	}
	
	/**
	 * Method setSpecified
	 * This method sets the value of the private datamember <code>specified</code>
	 * equals to that of the passed input parameter <code>val</code> and returns the
	 * final value of <code>specified</code> flag
	 * @param val
	 * @return boolean
	 */
	public boolean setSpecified(boolean val) {
		this.specified = val;
		return specified;
	}

	/**
	 * Method getName
	 * returns the localName of the attribute
	 * @return String
	 * @see org.w3c.dom.Attr#getName()
	 */
	public String getName() {		
		return omAttr.getLocalName();
	}

	/**
	 * Method getOwnerElement
	 * returns the <code>Element</code> object to which this attribute is attached
	 * @return Element
	 * @see org.w3c.dom.Attr#getOwnerElement()
	 */
	public Element getOwnerElement() {
		return ownerElement;
	}

	/**
	 * Method getValue
	 * returns the value of this attribute
	 * @return String
	 */
	public String getValue() {
		return omAttr.getValue();
	}
	
	/**
	 * Method setValue
	 * This method sets the value of this attribute to the provided input 
	 * <code>value</code>
	 * @param value
	 * @return 
	 */
	public void setValue(String value) {
		omAttr.setValue(value);
		setSpecified(true);
	}	
}
