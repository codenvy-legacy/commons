
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParamsDeleteCustomObjects complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParamsDeleteCustomObjects">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="objTypeName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="customObjKeyLists" type="{http://www.marketo.com/mktows/}ArrayOfKeyList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParamsDeleteCustomObjects", propOrder = {
    "objTypeName",
    "customObjKeyLists"
})
public class ParamsDeleteCustomObjects {

    @XmlElement(required = true)
    protected String objTypeName;
    @XmlElement(required = true)
    protected ArrayOfKeyList customObjKeyLists;

    /**
     * Gets the value of the objTypeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjTypeName() {
        return objTypeName;
    }

    /**
     * Sets the value of the objTypeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjTypeName(String value) {
        this.objTypeName = value;
    }

    /**
     * Gets the value of the customObjKeyLists property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfKeyList }
     *     
     */
    public ArrayOfKeyList getCustomObjKeyLists() {
        return customObjKeyLists;
    }

    /**
     * Sets the value of the customObjKeyLists property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfKeyList }
     *     
     */
    public void setCustomObjKeyLists(ArrayOfKeyList value) {
        this.customObjKeyLists = value;
    }

}
