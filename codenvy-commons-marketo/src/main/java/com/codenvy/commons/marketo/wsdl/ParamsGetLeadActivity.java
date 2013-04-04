
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParamsGetLeadActivity complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParamsGetLeadActivity">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="leadKey" type="{http://www.marketo.com/mktows/}LeadKey"/>
 *         &lt;element name="activityFilter" type="{http://www.marketo.com/mktows/}ActivityTypeFilter" minOccurs="0"/>
 *         &lt;element name="startPosition" type="{http://www.marketo.com/mktows/}StreamPosition" minOccurs="0"/>
 *         &lt;element name="batchSize" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParamsGetLeadActivity", propOrder = {
    "leadKey",
    "activityFilter",
    "startPosition",
    "batchSize"
})
public class ParamsGetLeadActivity {

    @XmlElement(required = true)
    protected LeadKey leadKey;
    @XmlElement(nillable = true)
    protected ActivityTypeFilter activityFilter;
    @XmlElement(nillable = true)
    protected StreamPosition startPosition;
    @XmlElement(nillable = true)
    protected Integer batchSize;

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

    /**
     * Gets the value of the activityFilter property.
     * 
     * @return
     *     possible object is
     *     {@link ActivityTypeFilter }
     *     
     */
    public ActivityTypeFilter getActivityFilter() {
        return activityFilter;
    }

    /**
     * Sets the value of the activityFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivityTypeFilter }
     *     
     */
    public void setActivityFilter(ActivityTypeFilter value) {
        this.activityFilter = value;
    }

    /**
     * Gets the value of the startPosition property.
     * 
     * @return
     *     possible object is
     *     {@link StreamPosition }
     *     
     */
    public StreamPosition getStartPosition() {
        return startPosition;
    }

    /**
     * Sets the value of the startPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link StreamPosition }
     *     
     */
    public void setStartPosition(StreamPosition value) {
        this.startPosition = value;
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

}
