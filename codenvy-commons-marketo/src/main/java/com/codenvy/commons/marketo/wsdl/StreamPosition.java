
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for StreamPosition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StreamPosition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="latestCreatedAt" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="oldestCreatedAt" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="activityCreatedAt" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="offset" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StreamPosition", propOrder = {
    "latestCreatedAt",
    "oldestCreatedAt",
    "activityCreatedAt",
    "offset"
})
public class StreamPosition {

    @XmlElement(nillable = true)
    protected XMLGregorianCalendar latestCreatedAt;
    @XmlElement(nillable = true)
    protected XMLGregorianCalendar oldestCreatedAt;
    @XmlElement(nillable = true)
    protected XMLGregorianCalendar activityCreatedAt;
    @XmlElement(nillable = true)
    protected String offset;

    /**
     * Gets the value of the latestCreatedAt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLatestCreatedAt() {
        return latestCreatedAt;
    }

    /**
     * Sets the value of the latestCreatedAt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLatestCreatedAt(XMLGregorianCalendar value) {
        this.latestCreatedAt = value;
    }

    /**
     * Gets the value of the oldestCreatedAt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOldestCreatedAt() {
        return oldestCreatedAt;
    }

    /**
     * Sets the value of the oldestCreatedAt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOldestCreatedAt(XMLGregorianCalendar value) {
        this.oldestCreatedAt = value;
    }

    /**
     * Gets the value of the activityCreatedAt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getActivityCreatedAt() {
        return activityCreatedAt;
    }

    /**
     * Sets the value of the activityCreatedAt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setActivityCreatedAt(XMLGregorianCalendar value) {
        this.activityCreatedAt = value;
    }

    /**
     * Gets the value of the offset property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOffset() {
        return offset;
    }

    /**
     * Sets the value of the offset property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOffset(String value) {
        this.offset = value;
    }

}
