package com.codenvy.commons.marketo.client;

import com.codenvy.commons.marketo.wsdl.LeadKey;
import com.codenvy.commons.marketo.wsdl.LeadKeyRef;
import com.codenvy.commons.marketo.wsdl.StreamPosition;

import java.util.StringTokenizer;

import javax.xml.datatype.XMLGregorianCalendar;

public class StreamPostionHolder {

    private LeadKey leadKey        = null;
    private Object  streamPosition = null;

    public void StreamPositionHolder(String stringValue) throws MktowsClientException {

        if (stringValue == null) {
            throw new MktowsClientException("Constructor requires not null value for parameter stringValue");
        }
        StringTokenizer tokens = new StringTokenizer(stringValue, ";");
        if (tokens.hasMoreTokens()) {
            String posType = tokens.nextToken();
            if (posType == "0") {
                throw new MktowsClientException("String value type '0' indicates a bad StreamPosition");
            }
            if (posType == "1") {
                boolean formatOk = false;
                if (tokens.hasMoreTokens()) {
                    this.leadKey = MktowsUtil.objectFactory.createLeadKey();
                    this.leadKey.setKeyType(LeadKeyRef.fromValue(tokens.nextToken()));
                    if (tokens.hasMoreTokens()) {
                        this.leadKey.setKeyValue(tokens.nextToken());
                        if (tokens.hasMoreTokens()) {
                            StreamPosition sp = MktowsUtil.objectFactory.createStreamPosition();
                            XMLGregorianCalendar xmlCal = MktowsUtil.getDatatypeFactory().newXMLGregorianCalendar(tokens.nextToken());
                            sp.setLatestCreatedAt(xmlCal);
                            if (tokens.hasMoreTokens()) {
                                xmlCal = MktowsUtil.getDatatypeFactory().newXMLGregorianCalendar(tokens.nextToken());
                                sp.setOldestCreatedAt(xmlCal);
                                if (tokens.hasMoreTokens()) {
                                    sp.setOffset(tokens.nextToken());
                                }
                            }
                        }
                    }
                }
                if (!formatOk) {
                    throw new MktowsClientException("Format error in StreamPosition string value type '1'");
                }
            }
        }
    }

    boolean isEmpty() {

        return (this.streamPosition == null);
    }

    @Override
    public String toString() {

        String value = null;
        if (this.streamPosition != null) {
            if (this.streamPosition instanceof com.codenvy.commons.marketo.wsdl.StreamPosition) {
                if (this.leadKey != null) {
                    StreamPosition streamPos = (StreamPosition)this.streamPosition;
                    value = "1;";
                    value += leadKey.getKeyType().value() + ";";
                    value += leadKey.getKeyValue() + ";";
                    value += streamPos.getLatestCreatedAt().toXMLFormat() + ";";
                    value += streamPos.getOldestCreatedAt().toXMLFormat() + ";";
                    value += streamPos.getOffset();
                }
                else {
                    value = "0;";
                }
            }
            else {
                value = "2;" + this.streamPosition.toString();
            }
        }
        else {
            value = "0;";
        }
        return value;
    }

    LeadKey getLeadKey() {
        return leadKey;
    }

    void setLeadKey(LeadKey leadKey) {
        this.leadKey = leadKey;
    }

    Object getStreamPosition() {
        return streamPosition;
    }

    void setStreamPosition(Object streamPosition) {
        this.streamPosition = streamPosition;
    }
}
