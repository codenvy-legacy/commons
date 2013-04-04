
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customObjKeyList" type="{http://www.marketo.com/mktows/}ArrayOfAttribute" minOccurs="0"/>
 *         &lt;element name="customObjAttributeList" type="{http://www.marketo.com/mktows/}ArrayOfAttribute" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomObj", propOrder = {
    "customObjKeyList",
    "customObjAttributeList"
})
public class CustomObj {

    @XmlElement(nillable = true)
    protected ArrayOfAttribute customObjKeyList;
    @XmlElement(nillable = true)
    protected ArrayOfAttribute customObjAttributeList;

    /**
     * Gets the value of the customObjKeyList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAttribute }
     *     
     */
    public ArrayOfAttribute getCustomObjKeyList() {
        return customObjKeyList;
    }

    /**
     * Sets the value of the customObjKeyList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAttribute }
     *     
     */
    public void setCustomObjKeyList(ArrayOfAttribute value) {
        this.customObjKeyList = value;
    }

    /**
     * Gets the value of the customObjAttributeList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAttribute }
     *     
     */
    public ArrayOfAttribute getCustomObjAttributeList() {
        return customObjAttributeList;
    }

    /**
     * Sets the value of the customObjAttributeList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAttribute }
     *     
     */
    public void setCustomObjAttributeList(ArrayOfAttribute value) {
        this.customObjAttributeList = value;
    }

}
