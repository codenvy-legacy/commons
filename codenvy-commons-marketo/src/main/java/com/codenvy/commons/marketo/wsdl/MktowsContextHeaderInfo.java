
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MktowsContextHeaderInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MktowsContextHeaderInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="targetWorkspace" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MktowsContextHeaderInfo", propOrder = {
    "targetWorkspace"
})
public class MktowsContextHeaderInfo {

    @XmlElement(required = true)
    protected String targetWorkspace;

    /**
     * Gets the value of the targetWorkspace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetWorkspace() {
        return targetWorkspace;
    }

    /**
     * Sets the value of the targetWorkspace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetWorkspace(String value) {
        this.targetWorkspace = value;
    }

}
