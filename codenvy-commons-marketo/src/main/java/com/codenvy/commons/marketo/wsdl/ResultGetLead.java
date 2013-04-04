
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResultGetLead complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResultGetLead">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="count" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="leadRecordList" type="{http://www.marketo.com/mktows/}ArrayOfLeadRecord" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultGetLead", propOrder = {
    "count",
    "leadRecordList"
})
public class ResultGetLead {

    protected int count;
    @XmlElement(nillable = true)
    protected ArrayOfLeadRecord leadRecordList;

    /**
     * Gets the value of the count property.
     * 
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     * 
     */
    public void setCount(int value) {
        this.count = value;
    }

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

}
