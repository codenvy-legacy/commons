
package com.codenvy.commons.marketo.wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfLeadChangeRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfLeadChangeRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="leadChangeRecord" type="{http://www.marketo.com/mktows/}LeadChangeRecord" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfLeadChangeRecord", propOrder = {
    "leadChangeRecord"
})
public class ArrayOfLeadChangeRecord {

    protected List<LeadChangeRecord> leadChangeRecord;

    /**
     * Gets the value of the leadChangeRecord property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the leadChangeRecord property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLeadChangeRecord().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LeadChangeRecord }
     * 
     * 
     */
    public List<LeadChangeRecord> getLeadChangeRecord() {
        if (leadChangeRecord == null) {
            leadChangeRecord = new ArrayList<LeadChangeRecord>();
        }
        return this.leadChangeRecord;
    }

}
