
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParamsSyncMultipleLeads complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParamsSyncMultipleLeads">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="leadRecordList" type="{http://www.marketo.com/mktows/}ArrayOfLeadRecord"/>
 *         &lt;element name="dedupEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParamsSyncMultipleLeads", propOrder = {
    "leadRecordList",
    "dedupEnabled"
})
public class ParamsSyncMultipleLeads {

    @XmlElement(required = true)
    protected ArrayOfLeadRecord leadRecordList;
    @XmlElement(nillable = true)
    protected Boolean dedupEnabled;

    /**
     * Gets the value of the leadRecordList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfLeadRecord }
     *     
     */
    public ArrayOfLeadRecord getLeadRecordList() {
        return leadRecordList;
    }

    /**
     * Sets the value of the leadRecordList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfLeadRecord }
     *     
     */
    public void setLeadRecordList(ArrayOfLeadRecord value) {
        this.leadRecordList = value;
    }

    /**
     * Gets the value of the dedupEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDedupEnabled() {
        return dedupEnabled;
    }

    /**
     * Sets the value of the dedupEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDedupEnabled(Boolean value) {
        this.dedupEnabled = value;
    }

}
