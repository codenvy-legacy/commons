
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ActivityRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ActivityRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="activityDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="activityType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mktgAssetName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="activityAttributes" type="{http://www.marketo.com/mktows/}ArrayOfAttribute" minOccurs="0"/>
 *         &lt;element name="campaign" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="personName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mktPersonId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="foreignSysId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orgName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="foreignSysOrgId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActivityRecord", propOrder = {
    "id",
    "activityDateTime",
    "activityType",
    "mktgAssetName",
    "activityAttributes",
    "campaign",
    "personName",
    "mktPersonId",
    "foreignSysId",
    "orgName",
    "foreignSysOrgId"
})
public class ActivityRecord {

    protected int id;
    @XmlElement(required = true)
    protected XMLGregorianCalendar activityDateTime;
    @XmlElement(required = true)
    protected String activityType;
    protected String mktgAssetName;
    @XmlElement(nillable = true)
    protected ArrayOfAttribute activityAttributes;
    @XmlElement(nillable = true)
    protected String campaign;
    @XmlElement(nillable = true)
    protected String personName;
    @XmlElement(required = true)
    protected String mktPersonId;
    @XmlElement(nillable = true)
    protected String foreignSysId;
    @XmlElement(nillable = true)
    protected String orgName;
    @XmlElement(nillable = true)
    protected String foreignSysOrgId;

    /**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the activityDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getActivityDateTime() {
        return activityDateTime;
    }

    /**
     * Sets the value of the activityDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setActivityDateTime(XMLGregorianCalendar value) {
        this.activityDateTime = value;
    }

    /**
     * Gets the value of the activityType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivityType() {
        return activityType;
    }

    /**
     * Sets the value of the activityType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivityType(String value) {
        this.activityType = value;
    }

    /**
     * Gets the value of the mktgAssetName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMktgAssetName() {
        return mktgAssetName;
    }

    /**
     * Sets the value of the mktgAssetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMktgAssetName(String value) {
        this.mktgAssetName = value;
    }

    /**
     * Gets the value of the activityAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAttribute }
     *     
     */
    public ArrayOfAttribute getActivityAttributes() {
        return activityAttributes;
    }

    /**
     * Sets the value of the activityAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAttribute }
     *     
     */
    public void setActivityAttributes(ArrayOfAttribute value) {
        this.activityAttributes = value;
    }

    /**
     * Gets the value of the campaign property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCampaign() {
        return campaign;
    }

    /**
     * Sets the value of the campaign property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCampaign(String value) {
        this.campaign = value;
    }

    /**
     * Gets the value of the personName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * Sets the value of the personName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonName(String value) {
        this.personName = value;
    }

    /**
     * Gets the value of the mktPersonId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMktPersonId() {
        return mktPersonId;
    }

    /**
     * Sets the value of the mktPersonId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMktPersonId(String value) {
        this.mktPersonId = value;
    }

    /**
     * Gets the value of the foreignSysId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForeignSysId() {
        return foreignSysId;
    }

    /**
     * Sets the value of the foreignSysId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForeignSysId(String value) {
        this.foreignSysId = value;
    }

    /**
     * Gets the value of the orgName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * Sets the value of the orgName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgName(String value) {
        this.orgName = value;
    }

    /**
     * Gets the value of the foreignSysOrgId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForeignSysOrgId() {
        return foreignSysOrgId;
    }

    /**
     * Sets the value of the foreignSysOrgId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForeignSysOrgId(String value) {
        this.foreignSysOrgId = value;
    }

}
