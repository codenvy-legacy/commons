
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParamsGetLead complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParamsGetLead">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="leadKey" type="{http://www.marketo.com/mktows/}LeadKey"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParamsGetLead", propOrder = {
    "leadKey"
})
public class ParamsGetLead {

    @XmlElement(required = true)
    protected LeadKey leadKey;

    /**
     * Gets the value of the leadKey property.
     * 
     * @return
     *     possible object is
     *     {@link LeadKey }
     *     
     */
    public LeadKey getLeadKey() {
        return leadKey;
    }

    /**
     * Sets the value of the leadKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link LeadKey }
     *     
     */
    public void setLeadKey(LeadKey value) {
        this.leadKey = value;
    }

}
