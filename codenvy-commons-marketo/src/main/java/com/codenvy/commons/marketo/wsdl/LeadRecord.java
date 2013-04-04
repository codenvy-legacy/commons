
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LeadRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LeadRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ForeignSysPersonId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ForeignSysType" type="{http://www.marketo.com/mktows/}ForeignSysType" minOccurs="0"/>
 *         &lt;element name="leadAttributeList" type="{http://www.marketo.com/mktows/}ArrayOfAttribute" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LeadRecord", propOrder = {
    "id",
    "email",
    "foreignSysPersonId",
    "foreignSysType",
    "leadAttributeList"
})
public class LeadRecord {

    @XmlElement(name = "Id", nillable = true)
    protected Integer id;
    @XmlElement(name = "Email", nillable = true)
    protected String email;
    @XmlElement(name = "ForeignSysPersonId", nillable = true)
    protected String foreignSysPersonId;
    @XmlElement(name = "ForeignSysType", nillable = true)
    protected ForeignSysType foreignSysType;
    @XmlElement(nillable = true)
    protected ArrayOfAttribute leadAttributeList;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the foreignSysPersonId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForeignSysPersonId() {
        return foreignSysPersonId;
    }

    /**
     * Sets the value of the foreignSysPersonId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForeignSysPersonId(String value) {
        this.foreignSysPersonId = value;
    }

    /**
     * Gets the value of the foreignSysType property.
     * 
     * @return
     *     possible object is
     *     {@link ForeignSysType }
     *     
     */
    public ForeignSysType getForeignSysType() {
        return foreignSysType;
    }

    /**
     * Sets the value of the foreignSysType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ForeignSysType }
     *     
     */
    public void setForeignSysType(ForeignSysType value) {
        this.foreignSysType = value;
    }

    /**
     * Gets the value of the leadAttributeList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAttribute }
     *     
     */
    public ArrayOfAttribute getLeadAttributeList() {
        return leadAttributeList;
    }

    /**
     * Sets the value of the leadAttributeList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAttribute }
     *     
     */
    public void setLeadAttributeList(ArrayOfAttribute value) {
        this.leadAttributeList = value;
    }

}
