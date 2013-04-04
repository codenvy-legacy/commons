
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ForeignSysType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ForeignSysType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CUSTOM"/>
 *     &lt;enumeration value="SFDC"/>
 *     &lt;enumeration value="NETSUITE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ForeignSysType")
@XmlEnum
public enum ForeignSysType {

    CUSTOM,
    SFDC,
    NETSUITE;

    public String value() {
        return name();
    }

    public static ForeignSysType fromValue(String v) {
        return valueOf(v);
    }

}
