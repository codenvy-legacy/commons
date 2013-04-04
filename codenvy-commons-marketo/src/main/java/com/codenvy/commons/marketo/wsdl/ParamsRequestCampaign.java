
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParamsRequestCampaign complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParamsRequestCampaign">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="source" type="{http://www.marketo.com/mktows/}ReqCampSourceType"/>
 *         &lt;element name="campaignId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="leadList" type="{http://www.marketo.com/mktows/}ArrayOfLeadKey"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParamsRequestCampaign", propOrder = {
    "source",
    "campaignId",
    "leadList"
})
public class ParamsRequestCampaign {

    @XmlElement(required = true)
    protected ReqCampSourceType source;
    protected int campaignId;
    @XmlElement(required = true)
    protected ArrayOfLeadKey leadList;

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link ReqCampSourceType }
     *     
     */
    public ReqCampSourceType getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReqCampSourceType }
     *     
     */
    public void setSource(ReqCampSourceType value) {
        this.source = value;
    }

    /**
     * Gets the value of the campaignId property.
     * 
     */
    public int getCampaignId() {
        return campaignId;
    }

    /**
     * Sets the value of the campaignId property.
     * 
     */
    public void setCampaignId(int value) {
        this.campaignId = value;
    }

    /**
     * Gets the value of the leadList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfLeadKey }
     *     
     */
    public ArrayOfLeadKey getLeadList() {
        return leadList;
    }

    /**
     * Sets the value of the leadList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfLeadKey }
     *     
     */
    public void setLeadList(ArrayOfLeadKey value) {
        this.leadList = value;
    }

}
