
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResultGetCampaignsForSource complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResultGetCampaignsForSource">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="returnCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="campaignRecordList" type="{http://www.marketo.com/mktows/}ArrayOfCampaignRecord" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultGetCampaignsForSource", propOrder = {
    "returnCount",
    "campaignRecordList"
})
public class ResultGetCampaignsForSource {

    protected int returnCount;
    @XmlElement(nillable = true)
    protected ArrayOfCampaignRecord campaignRecordList;

    /**
     * Gets the value of the returnCount property.
     * 
     */
    public int getReturnCount() {
        return returnCount;
    }

    /**
     * Sets the value of the returnCount property.
     * 
     */
    public void setReturnCount(int value) {
        this.returnCount = value;
    }

    /**
     * Gets the value of the campaignRecordList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCampaignRecord }
     *     
     */
    public ArrayOfCampaignRecord getCampaignRecordList() {
        return campaignRecordList;
    }

    /**
     * Sets the value of the campaignRecordList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCampaignRecord }
     *     
     */
    public void setCampaignRecordList(ArrayOfCampaignRecord value) {
        this.campaignRecordList = value;
    }

}
