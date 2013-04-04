
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MergeStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MergeStatus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="winningLeadId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="losingLeadIdList" type="{http://www.marketo.com/mktows/}ArrayOfInteger" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.marketo.com/mktows/}LeadMergeStatusEnum"/>
 *         &lt;element name="error" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MergeStatus", propOrder = {
    "winningLeadId",
    "losingLeadIdList",
    "status",
    "error"
})
public class MergeStatus {

    @XmlElement(nillable = true)
    protected Integer winningLeadId;
    @XmlElement(nillable = true)
    protected ArrayOfInteger losingLeadIdList;
    @XmlElement(required = true)
    protected LeadMergeStatusEnum status;
    @XmlElement(nillable = true)
    protected String error;

    /**
     * Gets the value of the winningLeadId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getWinningLeadId() {
        return winningLeadId;
    }

    /**
     * Sets the value of the winningLeadId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setWinningLeadId(Integer value) {
        this.winningLeadId = value;
    }

    /**
     * Gets the value of the losingLeadIdList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfInteger }
     *     
     */
    public ArrayOfInteger getLosingLeadIdList() {
        return losingLeadIdList;
    }

    /**
     * Sets the value of the losingLeadIdList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfInteger }
     *     
     */
    public void setLosingLeadIdList(ArrayOfInteger value) {
        this.losingLeadIdList = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link LeadMergeStatusEnum }
     *     
     */
    public LeadMergeStatusEnum getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link LeadMergeStatusEnum }
     *     
     */
    public void setStatus(LeadMergeStatusEnum value) {
        this.status = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setError(String value) {
        this.error = value;
    }

}
