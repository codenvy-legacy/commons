package com.codenvy.commons.marketo.client;

import com.codenvy.commons.marketo.wsdl.ArrayOfAttribute;
import com.codenvy.commons.marketo.wsdl.Attribute;
import com.codenvy.commons.marketo.wsdl.LeadRecord;
import com.codenvy.commons.marketo.wsdl.ObjectFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class MktowsUtil {
    private static final Logger    LOG             = LoggerFactory.getLogger(MktowsUtil.class);
    static public ObjectFactory    objectFactory   = new ObjectFactory();
    static private DatatypeFactory dataTypeFactory = null;

    static public Attribute newAttribute(String name, String value) {

        Attribute attr = new Attribute();
        attr.setAttrName(name);
        attr.setAttrValue(value);
        return attr;
    }

    static public LeadRecord newLeadRecord(Integer Id,
                                           String Email,
                                           String fsysIdType,
                                           String fsysIdValue,
                                           HashMap<String, String> attributes) {

        LeadRecord lr = MktowsUtil.objectFactory.createLeadRecord();
        if (Id != null) {
            lr.setId(Id);
        }
        if (Email != null) {
            lr.setEmail(Email);
        }
        if (attributes != null && attributes.size() > 0) {
            ArrayOfAttribute aoAttr = MktowsUtil.objectFactory.createArrayOfAttribute();
            List<Attribute> attrList = aoAttr.getAttribute();
            String attrName = null;
            Iterator<String> it = attributes.keySet().iterator();
            while (it.hasNext()) {
                attrName = it.next();
                attrList.add(MktowsUtil.newAttribute(attrName, attributes.get(attrName)));
            }
            lr.setLeadAttributeList(aoAttr);
        }
        return lr;
    }

    static public Map<String, Object> getLeadAttributeMap(ArrayOfAttribute aoAttrs) {

        Map<String, Object> map = new HashMap<String, Object>();
        if (aoAttrs != null) {
            List<Attribute> tempAttrList = aoAttrs.getAttribute();
            if (tempAttrList != null) {
                String name = null;
                String value = null;
                // String type = null;
                // JAXBElement<String> elem = null;
                for (Attribute attr : tempAttrList) {
                    name = attr.getAttrName();
                    value = attr.getAttrValue();
                    // type = attr.getAttrType();
                    map.put(name, value);
                }
            }
        }
        return map;
    }

    static public DatatypeFactory getDatatypeFactory() throws MktowsClientException {

        if (MktowsUtil.dataTypeFactory == null) {
            try {
                MktowsUtil.dataTypeFactory = DatatypeFactory.newInstance();
                if (MktowsUtil.dataTypeFactory == null) {
                    throw new MktowsClientException("DatatypeFactory returned null handle");
                }
            } catch (DatatypeConfigurationException ex) {
                LOG.error(ex.getLocalizedMessage(), ex);
                throw new MktowsClientException("Error getting handle to DatatypeFactory", ex);
            }
        }

        return MktowsUtil.dataTypeFactory;
    }

    static public String elemToString(JAXBElement<String> elem) {

        String value = (elem != null) ? elem.getValue() : null;
        return value;
    }

    static public Date w3cDateToDateObject(XMLGregorianCalendar w3cDate) {

        GregorianCalendar cal = w3cDate.toGregorianCalendar();
        Date dateObj = cal.getTime();
        return dateObj;
    }

    static public XMLGregorianCalendar dateObjectToW3cDate(Date dateObj) throws MktowsClientException {

        Calendar cal = GregorianCalendar.getInstance(TimeZone.getDefault());
        cal.setTime(dateObj);
        XMLGregorianCalendar w3cDate = MktowsUtil.getDatatypeFactory().newXMLGregorianCalendar();
        w3cDate.setYear(cal.get(GregorianCalendar.YEAR));
        w3cDate.setMonth(cal.get(GregorianCalendar.MONTH) + 1);
        w3cDate.setDay(cal.get(GregorianCalendar.DAY_OF_MONTH));
        w3cDate.setHour(cal.get(GregorianCalendar.HOUR_OF_DAY));
        w3cDate.setMinute(cal.get(GregorianCalendar.MINUTE));
        w3cDate.setSecond(cal.get(GregorianCalendar.SECOND));
        w3cDate.setTimezone(cal.get(GregorianCalendar.ZONE_OFFSET) / 60000);
        return w3cDate;
    }
}
