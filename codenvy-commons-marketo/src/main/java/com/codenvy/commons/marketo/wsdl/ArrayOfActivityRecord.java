
package com.codenvy.commons.marketo.wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfActivityRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfActivityRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="activityRecord" type="{http://www.marketo.com/mktows/}ActivityRecord" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfActivityRecord", propOrder = {
    "activityRecord"
})
public class ArrayOfActivityRecord {

    protected List<ActivityRecord> activityRecord;

    /**
     * Gets the value of the activityRecord property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the activityRecord property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActivityRecord().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ActivityRecord }
     * 
     * 
     */
    public List<ActivityRecord> getActivityRecord() {
        if (activityRecord == null) {
            activityRecord = new ArrayList<ActivityRecord>();
        }
        return this.activityRecord;
    }

}
