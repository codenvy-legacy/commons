
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LeadSyncStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LeadSyncStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CREATED"/>
 *     &lt;enumeration value="UPDATED"/>
 *     &lt;enumeration value="FAILED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LeadSyncStatus")
@XmlEnum
public enum LeadSyncStatus {

    CREATED,
    UPDATED,
    FAILED;

    public String value() {
        return name();
    }

    public static LeadSyncStatus fromValue(String v) {
        return valueOf(v);
    }

}
