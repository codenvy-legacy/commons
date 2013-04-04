
package com.codenvy.commons.marketo.wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfLeadKey complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfLeadKey">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="leadKey" type="{http://www.marketo.com/mktows/}LeadKey" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfLeadKey", propOrder = {
    "leadKey"
})
public class ArrayOfLeadKey {

    protected List<LeadKey> leadKey;

    /**
     * Gets the value of the leadKey property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the leadKey property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLeadKey().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LeadKey }
     * 
     * 
     */
    public List<LeadKey> getLeadKey() {
        if (leadKey == null) {
            leadKey = new ArrayList<LeadKey>();
        }
        return this.leadKey;
    }

}
