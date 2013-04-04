/*
 * Copyright (C) 2013 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.codenvy.commons.marketo;

import com.codenvy.commons.marketo.client.MktServiceException;
import com.codenvy.commons.marketo.client.MktowsClient;
import com.codenvy.commons.marketo.client.MktowsClientException;
import com.codenvy.commons.marketo.client.MktowsUtil;
import com.codenvy.commons.marketo.client.StreamPostionHolder;
import com.codenvy.commons.marketo.wsdl.ActivityRecord;
import com.codenvy.commons.marketo.wsdl.ActivityType;
import com.codenvy.commons.marketo.wsdl.ArrayOfAttribute;
import com.codenvy.commons.marketo.wsdl.Attribute;
import com.codenvy.commons.marketo.wsdl.CampaignRecord;
import com.codenvy.commons.marketo.wsdl.LeadChangeRecord;
import com.codenvy.commons.marketo.wsdl.LeadKey;
import com.codenvy.commons.marketo.wsdl.LeadKeyRef;
import com.codenvy.commons.marketo.wsdl.LeadMergeStatusEnum;
import com.codenvy.commons.marketo.wsdl.LeadRecord;
import com.codenvy.commons.marketo.wsdl.LeadSyncStatus;
import com.codenvy.commons.marketo.wsdl.MergeStatus;
import com.codenvy.commons.marketo.wsdl.ResultSyncLead;
import com.codenvy.commons.marketo.wsdl.SyncStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MktowsUsageExamples {

    private static final Logger LOG        = LoggerFactory.getLogger(MktowsUsageExamples.class);
    protected MktowsClient      client     = null;
    public static final String  HOST_NAME  = "localhost";
    public static final String  ACCESS_KEY = "bigcorp1_543653174CEC4191431E50";
    public static final String  SECRET_KEY = "QRSTUVWXYZQ1";

    public MktowsUsageExamples() {

        client = new MktowsClient(MktowsUsageExamples.ACCESS_KEY, MktowsUsageExamples.SECRET_KEY, MktowsUsageExamples.HOST_NAME);
    }

    public void getCampaignsForSource() {

        List<CampaignRecord> campaignRecords = this.client.getCampaignsForSource();
        if (campaignRecords != null) {
            for (CampaignRecord item : campaignRecords) {
                LOG.info("Campaign name: " + item.getName() + ",  ID: " + item.getId());
            }
        }
    }

    public void getLead() {

        List<LeadRecord> leadRecords = null;
        try {
            leadRecords = this.client.getLead(LeadKeyRef.EMAIL, "scoobyzz@marketo.com");
        } catch (MktServiceException e) {
            LOG.error("Exception occurred: " + e.getLongMessage());
            return;
        }
        if (leadRecords != null) {
            Map<String, Object> attrMap = null;
            for (LeadRecord item : leadRecords) {
                LOG.info("Lead Id: " + item.getId() + ",  Email: " + item.getEmail());
                ArrayOfAttribute aoAttribute = item.getLeadAttributeList();
                if (aoAttribute != null) {
                    attrMap = MktowsUtil.getLeadAttributeMap(aoAttribute);
                    if (attrMap != null && !attrMap.isEmpty()) {
                        Set<String> keySet = attrMap.keySet();
                        for (String key : keySet) {
                            LOG.info("Attribute name: " + key + ", value: " + attrMap.get(key).toString());
                        }
                    }
                }
            }
        }
    }

    public void requestCampaign() {

        final String myCampName = "Product Seminar - Summer";
        final String leadEmail = "scooby1@marketo.com";
        // Find the available campaigns
        List<CampaignRecord> campaignRecords = this.client.getCampaignsForSource();
        int myCampId = 0;
        if (campaignRecords != null) {
            // Find ID for campaign of interest
            for (CampaignRecord item : campaignRecords) {
                if (item.getName() == myCampName) {
                    myCampId = item.getId();
                    break;
                }
            }
        }

        if (myCampId == 0) {
            LOG.info("Campaign not found: " + myCampName);
            return;
        }

        // Find the lead ID for lead(s) that need to be added to the campaign
        List<LeadRecord> leadRecords = null;
        try {
            leadRecords = this.client.getLead(LeadKeyRef.EMAIL, leadEmail);
        } catch (MktServiceException e) {
            LOG.info("Exception occurred: " + e.getMessage());
            return;
        }
        int leadId = 0;
        if (leadRecords != null) {
            for (LeadRecord item : leadRecords) {
                leadId = item.getId();
                break;
            }
        }

        if (leadId == 0) {
            LOG.info("Lead not found: " + leadEmail);
            return;
        }

        // Request that lead(s) be added to the campaign
        LeadKey leadKey = MktowsUtil.objectFactory.createLeadKey();
        leadKey.setKeyType(LeadKeyRef.IDNUM);
        leadKey.setKeyValue(new Integer(leadId).toString());
        List<LeadKey> leadList = new ArrayList<LeadKey>();
        leadList.add(leadKey);
        boolean success = this.client.requestCampaign(myCampId, leadList);
        if (success) {
            LOG.info("Lead " + leadId + " added to campaign " + myCampName);
        }
        else {
            LOG.info("Failed to add lead " + leadId + " to campaign " + myCampName);
        }
    }

    public void getLeadActivity() {

        StreamPostionHolder posHolder = new StreamPostionHolder();
        Date lastestCreatedAt = new Date();
        List<ActivityType> filter = new ArrayList<ActivityType>();
        filter.add(ActivityType.VISIT_WEBPAGE);
        filter.add(ActivityType.FILL_OUT_FORM);
        filter.add(ActivityType.OPEN_EMAIL);
        filter.add(ActivityType.OPEN_SALES_EMAIL);
        filter.add(ActivityType.CLICK_EMAIL);
        filter.add(ActivityType.CLICK_SALES_EMAIL);
        filter.add(ActivityType.NEW_LEAD);
        List<ActivityRecord> activityRecords = null;
        try {
            activityRecords = this.client.getLeadActivity(LeadKeyRef.IDNUM, "24", 10, lastestCreatedAt, null, filter, posHolder);
        } catch (MktowsClientException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return;
        } catch (MktServiceException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return;
        }
        if (activityRecords != null) {
            Map<String, Object> attrMap = null;
            for (ActivityRecord item : activityRecords) {
                String wsdlTS = item.getActivityDateTime().toString();
                Date localDT = MktowsUtil.w3cDateToDateObject(item.getActivityDateTime());
                DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String localTS = fmt.format(localDT);
                LOG.info("Activity: " + item.getActivityType() + ",  WSDL Timestamp: " + wsdlTS + ",  LOCAL Timestamp: "
                         + localTS);
                ArrayOfAttribute aoAttribute = item.getActivityAttributes();
                if (aoAttribute != null) {
                    attrMap = MktowsUtil.getLeadAttributeMap(aoAttribute);
                    if (attrMap != null && !attrMap.isEmpty()) {
                        Set<String> keySet = attrMap.keySet();
                        for (String key : keySet) {
                            LOG.info("Attribute name: " + key + ", value: " + attrMap.get(key).toString());
                        }
                    }
                }
            }
        }
    }

    public void getLeadChanges() {

        StreamPostionHolder posHolder = new StreamPostionHolder();
        Calendar cal = new GregorianCalendar(2010, Calendar.MAY, 1, 0, 0, 0);
        Date oldestCreatedAt = cal.getTime();
        List<ActivityType> filter = new ArrayList<ActivityType>();
        filter.add(ActivityType.VISIT_WEBPAGE);
        filter.add(ActivityType.FILL_OUT_FORM);
        filter.add(ActivityType.OPEN_EMAIL);
        filter.add(ActivityType.OPEN_SALES_EMAIL);
        filter.add(ActivityType.CLICK_EMAIL);
        filter.add(ActivityType.CLICK_SALES_EMAIL);
        filter.add(ActivityType.NEW_LEAD);
        List<LeadChangeRecord> leadChangeRecords = null;
        try {
            leadChangeRecords = this.client.getLeadChanges(100, null, oldestCreatedAt, filter, posHolder);
        } catch (MktowsClientException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return;
        }
        if (leadChangeRecords != null) {
            Map<String, Object> attrMap = null;
            for (LeadChangeRecord item : leadChangeRecords) {
                String wsdlTS = item.getActivityDateTime().toString();
                Date localDT = MktowsUtil.w3cDateToDateObject(item.getActivityDateTime());
                DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String localTS = fmt.format(localDT);
                LOG.info("Lead: " + item.getId() + ",  Activity: " + item.getActivityType() + ",  WSDL Timestamp: " + wsdlTS
                         + ",  LOCAL Timestamp: " + localTS);
                ArrayOfAttribute aoAttribute = item.getActivityAttributes();
                if (aoAttribute != null) {
                    attrMap = MktowsUtil.getLeadAttributeMap(aoAttribute);
                    if (attrMap != null && !attrMap.isEmpty()) {
                        Set<String> keySet = attrMap.keySet();
                        for (String key : keySet) {
                            LOG.info("Attribute name: " + key + ", value: " + attrMap.get(key).toString());
                        }
                    }
                }
            }
        }
    }

    public void getMultipleLeads() {

        StreamPostionHolder posHolder = new StreamPostionHolder();
        Calendar cal = new GregorianCalendar(2010, Calendar.MAY, 1, 0, 0, 0);
        Date lastUpdatedAt = cal.getTime();
        List<LeadRecord> leadRecords = null;
        try {
            leadRecords = this.client.getMultipleLeads(100, lastUpdatedAt, posHolder);
        } catch (MktowsClientException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return;
        }
        if (leadRecords != null) {
            Map<String, Object> attrMap = null;
            for (LeadRecord item : leadRecords) {
                LOG.info("Lead Id: " + item.getId() + ",  Email: " + item.getEmail());
                ArrayOfAttribute aoAttribute = item.getLeadAttributeList();
                if (aoAttribute != null) {
                    attrMap = MktowsUtil.getLeadAttributeMap(aoAttribute);
                    if (attrMap != null && !attrMap.isEmpty()) {
                        Set<String> keySet = attrMap.keySet();
                        for (String key : keySet) {
                            LOG.info("Attribute name: " + key + ", value: " + attrMap.get(key).toString());
                        }
                    }
                }
            }
        }
    }

    public void getMultipleLeadsUnsubscribedFlag() {

        StreamPostionHolder posHolder = new StreamPostionHolder();
        Calendar cal = new GregorianCalendar(2010, Calendar.MAY, 1, 0, 0, 0);
        Date lastUpdatedAt = cal.getTime();
        List<String> leadAttrs = new ArrayList<String>();
        leadAttrs.add("Unsubscribed");
        leadAttrs.add("UnsubscribedReason");
        List<LeadRecord> leadRecords = null;
        try {
            leadRecords = this.client.getMultipleLeads(100, lastUpdatedAt, posHolder, leadAttrs);
        } catch (MktowsClientException e) {
            LOG.error(e.getLocalizedMessage(), e);
            return;
        }
        if (leadRecords != null) {
            Map<String, Object> attrMap = null;
            for (LeadRecord item : leadRecords) {
                LOG.info("Lead Id: " + item.getId() + ",  Email: " + item.getEmail());
                ArrayOfAttribute aoAttribute = item.getLeadAttributeList();
                if (aoAttribute != null) {
                    attrMap = MktowsUtil.getLeadAttributeMap(aoAttribute);
                    if (attrMap != null && !attrMap.isEmpty()) {
                        Set<String> keySet = attrMap.keySet();
                        for (String key : keySet) {
                            LOG.info("Attribute name: " + key + ", value: " + attrMap.get(key).toString());
                        }
                    }
                }
            }
        }
    }

    public void syncLead() {

        HashMap<String, String> attrs = new HashMap<String, String>();
        attrs.put("FirstName", "Sam");
        attrs.put("LastName", "Haggy");
        LeadRecord leadRec = MktowsUtil.newLeadRecord(null, "shaggy@marketo.com", null, null, attrs);
        ResultSyncLead result = client.syncLead(leadRec, null, true);
        leadRec = result.getLeadRecord();
        SyncStatus sts = result.getSyncStatus();
        if (sts.getStatus() == LeadSyncStatus.CREATED) {
            LOG.info("Lead CREATED with Id " + sts.getLeadId());
        }
        else if (sts.getStatus() == LeadSyncStatus.UPDATED) {
            LOG.info("Lead UPDATED with Id " + sts.getLeadId());
        }
        else {
            LOG.info("Unexpected lead sync status");
        }
    }

    public void syncMultipleLeads() {

        List<LeadRecord> leadRecList = new ArrayList<LeadRecord>();
        Date dt = new Date();
        for (int i = 1; i <= 3; ++i) {
            HashMap<String, String> attrs = new HashMap<String, String>();
            attrs.put("FirstName", "FName" + dt + i);
            attrs.put("LastName", "LName" + dt + i);
            LeadRecord leadRec = MktowsUtil.newLeadRecord(null, "testemail" + dt + i + "@marketo.com", null, null, attrs);
            leadRecList.add(leadRec);
        }
        List<SyncStatus> syncStsList = client.syncMultipleLeads(leadRecList, true);
        for (SyncStatus sts : syncStsList) {
            if (sts.getStatus() == LeadSyncStatus.CREATED) {
                LOG.info("Lead CREATED with Id " + sts.getLeadId());
            }
            else if (sts.getStatus() == LeadSyncStatus.UPDATED) {
                LOG.info("Lead UPDATED with Id " + sts.getLeadId());
            }
            else {
                LOG.info("Unexpected lead sync status");
            }
        }
    }

    public void listMObjects() {

        List<String> objectNames = this.client.listMObjects();
        for (String name : objectNames) {
            LOG.info("Object name: " + name);
        }
    }

    public void mergeLeads() {

        String ID_ATTR = "FOOID"; // Lookup attribute name
        // The winning lead
        List<Attribute> winningLead = new ArrayList<Attribute>();
        winningLead.add(MktowsUtil.newAttribute(ID_ATTR, "1830856327"));

        // The losing leads
        List<List<Attribute>> losingLeadList = new ArrayList<List<Attribute>>();
        List<Attribute> loser = new ArrayList<Attribute>();
        // Lead 1
        loser = new ArrayList<Attribute>();
        loser.add(MktowsUtil.newAttribute(ID_ATTR, "1856585834"));
        losingLeadList.add(loser);
        // Lead 2
        loser = new ArrayList<Attribute>();
        loser.add(MktowsUtil.newAttribute(ID_ATTR, "1826211038"));
        losingLeadList.add(loser);

        MergeStatus status = this.client.mergeLeads(winningLead, losingLeadList);
        if (status.getStatus() == LeadMergeStatusEnum.MERGED) {
            LOG.info("Winning lead Id " + status.getWinningLeadId());
            List<Integer> loserIds = status.getLosingLeadIdList().getIntegerItem();
            for (Integer intItem : loserIds) {
                LOG.info("Losing leads Id " + intItem);
            }
        }
        else {
            LOG.error("Lead merge status is " + status.getStatus() + "\nError: " + status.getError());
        }
    }
}
