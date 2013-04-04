
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ParamsGetMultipleLeads complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParamsGetMultipleLeads">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lastUpdatedAt" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="streamPosition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="batchSize" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="includeAttributes" type="{http://www.marketo.com/mktows/}ArrayOfString" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParamsGetMultipleLeads", propOrder = {
    "lastUpdatedAt",
    "streamPosition",
    "batchSize",
    "includeAttributes"
})
public class ParamsGetMultipleLeads {

    @XmlElement(required = true)
    protected XMLGregorianCalendar lastUpdatedAt;
    @XmlElement(nillable = true)
    protected String streamPosition;
    @XmlElement(nillable = true)
    protected Integer batchSize;
    protected ArrayOfString includeAttributes;

    /**
     * Gets the value of the lastUpdatedAt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    /**
     * Sets the value of the lastUpdatedAt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastUpdatedAt(XMLGregorianCalendar value) {
        this.lastUpdatedAt = value;
    }

    /**
     * Gets the value of the streamPosition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStreamPosition() {
        return streamPosition;
    }

    /**
     * Sets the value of the streamPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStreamPosition(String value) {
        this.streamPosition = value;
    }

    /**
     * Gets the value of the batchSize property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBatchSize() {
        return batchSize;
    }

    /**
     * Sets the value of the batchSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBatchSize(Integer value) {
        this.batchSize = value;
    }

    /**
     * Gets the value of the includeAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getIncludeAttributes() {
        return includeAttributes;
    }

    /**
     * Sets the value of the includeAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setIncludeAttributes(ArrayOfString value) {
        this.includeAttributes = value;
    }

}
