package com.codenvy.commons.marketo.client;

import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

public class MktServiceException extends Exception {

    protected SOAPFault fault;

    public MktServiceException(String msg, SOAPFaultException faultEx) {

        this(msg, faultEx.getFault());
    }

    public MktServiceException(String msg, SOAPFault fault) {

        super(msg);
        this.fault = fault;
    }

    public String getShortMessage() {

        if (this.fault != null) {
            return this.fault.getFaultString();
        }
        return null;
    }

    public String getLongMessage() {

        if (this.fault != null) {
            return this.fault.getDetail().toString();
        }
        return null;
    }
}
