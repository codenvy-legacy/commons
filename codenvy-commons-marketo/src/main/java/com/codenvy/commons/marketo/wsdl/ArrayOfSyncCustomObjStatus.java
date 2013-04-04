
package com.codenvy.commons.marketo.wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfSyncCustomObjStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfSyncCustomObjStatus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="syncCustomObjStatus" type="{http://www.marketo.com/mktows/}SyncCustomObjStatus" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfSyncCustomObjStatus", propOrder = {
    "syncCustomObjStatus"
})
public class ArrayOfSyncCustomObjStatus {

    protected List<SyncCustomObjStatus> syncCustomObjStatus;

    /**
     * Gets the value of the syncCustomObjStatus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the syncCustomObjStatus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSyncCustomObjStatus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SyncCustomObjStatus }
     * 
     * 
     */
    public List<SyncCustomObjStatus> getSyncCustomObjStatus() {
        if (syncCustomObjStatus == null) {
            syncCustomObjStatus = new ArrayList<SyncCustomObjStatus>();
        }
        return this.syncCustomObjStatus;
    }

}
