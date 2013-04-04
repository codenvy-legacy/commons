
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResultListOperation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResultListOperation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="success" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="statusList" type="{http://www.marketo.com/mktows/}ArrayOfLeadStatus" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultListOperation", propOrder = {
    "success",
    "statusList"
})
public class ResultListOperation {

    protected boolean success;
    @XmlElement(nillable = true)
    protected ArrayOfLeadStatus statusList;

    /**
     * Gets the value of the success property.
     * 
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the value of the success property.
     * 
     */
    public void setSuccess(boolean value) {
        this.success = value;
    }

    /**
     * Gets the value of the statusList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfLeadStatus }
     *     
     */
    public ArrayOfLeadStatus getStatusList() {
        return statusList;
    }

    /**
     * Sets the value of the statusList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfLeadStatus }
     *     
     */
    public void setStatusList(ArrayOfLeadStatus value) {
        this.statusList = value;
    }

}
