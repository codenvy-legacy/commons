package com.codenvy.commons.marketo.client;

import com.codenvy.commons.marketo.wsdl.ActivityRecord;
import com.codenvy.commons.marketo.wsdl.ActivityType;
import com.codenvy.commons.marketo.wsdl.ActivityTypeFilter;
import com.codenvy.commons.marketo.wsdl.ArrayOfActivityRecord;
import com.codenvy.commons.marketo.wsdl.ArrayOfActivityType;
import com.codenvy.commons.marketo.wsdl.ArrayOfAttribute;
import com.codenvy.commons.marketo.wsdl.ArrayOfCampaignRecord;
import com.codenvy.commons.marketo.wsdl.ArrayOfLeadChangeRecord;
import com.codenvy.commons.marketo.wsdl.ArrayOfLeadKey;
import com.codenvy.commons.marketo.wsdl.ArrayOfLeadRecord;
import com.codenvy.commons.marketo.wsdl.ArrayOfString;
import com.codenvy.commons.marketo.wsdl.Attribute;
import com.codenvy.commons.marketo.wsdl.AuthenticationHeaderInfo;
import com.codenvy.commons.marketo.wsdl.CampaignRecord;
import com.codenvy.commons.marketo.wsdl.LeadActivityList;
import com.codenvy.commons.marketo.wsdl.LeadChangeRecord;
import com.codenvy.commons.marketo.wsdl.LeadKey;
import com.codenvy.commons.marketo.wsdl.LeadKeyRef;
import com.codenvy.commons.marketo.wsdl.LeadRecord;
import com.codenvy.commons.marketo.wsdl.LeadStatus;
import com.codenvy.commons.marketo.wsdl.ListKey;
import com.codenvy.commons.marketo.wsdl.ListKeyType;
import com.codenvy.commons.marketo.wsdl.ListOperationType;
import com.codenvy.commons.marketo.wsdl.MergeStatus;
import com.codenvy.commons.marketo.wsdl.MktMktowsApiService;
import com.codenvy.commons.marketo.wsdl.MktowsPort;
import com.codenvy.commons.marketo.wsdl.ObjectFactory;
import com.codenvy.commons.marketo.wsdl.ParamsGetCampaignsForSource;
import com.codenvy.commons.marketo.wsdl.ParamsGetLead;
import com.codenvy.commons.marketo.wsdl.ParamsGetLeadActivity;
import com.codenvy.commons.marketo.wsdl.ParamsGetLeadChanges;
import com.codenvy.commons.marketo.wsdl.ParamsGetMultipleLeads;
import com.codenvy.commons.marketo.wsdl.ParamsListMObjects;
import com.codenvy.commons.marketo.wsdl.ParamsListOperation;
import com.codenvy.commons.marketo.wsdl.ParamsMergeLeads;
import com.codenvy.commons.marketo.wsdl.ParamsRequestCampaign;
import com.codenvy.commons.marketo.wsdl.ParamsSyncLead;
import com.codenvy.commons.marketo.wsdl.ParamsSyncMultipleLeads;
import com.codenvy.commons.marketo.wsdl.ReqCampSourceType;
import com.codenvy.commons.marketo.wsdl.ResultGetCampaignsForSource;
import com.codenvy.commons.marketo.wsdl.ResultGetLead;
import com.codenvy.commons.marketo.wsdl.ResultGetLeadChanges;
import com.codenvy.commons.marketo.wsdl.ResultGetMultipleLeads;
import com.codenvy.commons.marketo.wsdl.ResultListMObjects;
import com.codenvy.commons.marketo.wsdl.ResultListOperation;
import com.codenvy.commons.marketo.wsdl.ResultMergeLeads;
import com.codenvy.commons.marketo.wsdl.ResultRequestCampaign;
import com.codenvy.commons.marketo.wsdl.ResultSyncLead;
import com.codenvy.commons.marketo.wsdl.StreamPosition;
import com.codenvy.commons.marketo.wsdl.SuccessGetCampaignsForSource;
import com.codenvy.commons.marketo.wsdl.SuccessGetLead;
import com.codenvy.commons.marketo.wsdl.SuccessGetLeadActivity;
import com.codenvy.commons.marketo.wsdl.SuccessGetLeadChanges;
import com.codenvy.commons.marketo.wsdl.SuccessGetMultipleLeads;
import com.codenvy.commons.marketo.wsdl.SuccessListMObjects;
import com.codenvy.commons.marketo.wsdl.SuccessListOperation;
import com.codenvy.commons.marketo.wsdl.SuccessMergeLeads;
import com.codenvy.commons.marketo.wsdl.SuccessRequestCampaign;
import com.codenvy.commons.marketo.wsdl.SuccessSyncLead;
import com.codenvy.commons.marketo.wsdl.SuccessSyncMultipleLeads;
import com.codenvy.commons.marketo.wsdl.SyncStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.SignatureException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPFaultException;

/**
 * Client for Marketo SOAP API v2.0
 */
public class MktowsClient {
    public static final String  API_VERSION          = "2_0";
    public static final String  W3C_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final Logger LOG                  = LoggerFactory.getLogger(MktowsClient.class);
    protected String            mktowsUserId         = null;
    protected String            encryptionKey        = null;
    protected String            endPoint             = null;
    private MktowsPort          proxy                = null;

    public MktowsClient(String mktowsUserId, String encryptionKey, String host) {
        this.endPoint = "https://" + host + "/soap/mktows/" + MktowsClient.API_VERSION;

        this.mktowsUserId = mktowsUserId;
        this.encryptionKey = encryptionKey;
        MktowsUtil.objectFactory = new ObjectFactory();
        MktMktowsApiService service = null;
        try {
            service = new MktMktowsApiService(new URL(this.endPoint + "?WSDL"),
                                              new QName("http://www.marketo.com/mktows/", "MktMktowsApiService"));
        } catch (MalformedURLException e) {
            LOG.error(e.getMessage(), e);
            throw new IllegalStateException(e.getMessage(), e);
        }
        this.proxy = service.getMktowsApiSoapPort();
        Map<String, Object> ctxt = ((BindingProvider)this.proxy).getRequestContext();
        ctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.endPoint);
    }

    static public String formatAsW3C(Date dt) {
        DateFormat df = new SimpleDateFormat(MktowsClient.W3C_DATE_TIME_FORMAT);
        String text = df.format(dt);
        String w3cValue = text.substring(0, 22) + ":" + text.substring(22);
        return w3cValue;
    }

    static public String formatAsWSDL(Date dt) {
        return MktowsClient.formatAsW3C(dt);
    }

    protected String calcRequestTimestamp() {
        return MktowsClient.formatAsW3C(new Date());
    }

    protected String calcRequestSignature(String requestTimestamp) throws SignatureException {
        String encryptString = requestTimestamp + this.mktowsUserId;
        return Signature.calculateHMAC(encryptString, this.encryptionKey);
    }

    protected AuthenticationHeaderInfo createAuthenticationHeader() throws MktowsClientException {
        AuthenticationHeaderInfo authHeader = null;
        String requestTimestamp = this.calcRequestTimestamp();
        try {
            String requestSignature = this.calcRequestSignature(requestTimestamp);
            authHeader = MktowsUtil.objectFactory.createAuthenticationHeaderInfo();
            authHeader.setMktowsUserId(this.mktowsUserId);
            authHeader.setRequestSignature(requestSignature);
            authHeader.setRequestTimestamp(requestTimestamp);
        } catch (SignatureException e) {
            LOG.error(e.getMessage(), e);
            throw new MktowsClientException("Exception occurred while generating signature", e);
        }
        return authHeader;
    }

    public MktowsPort getSoapInterface() {
        return this.proxy;
    }

    /**
     * This function returns a list of available Marketo campaigns that can be used as input parameters into the requestCampaign function.
     * Campaigns are categorized by the source of the request, which are enumerated in the WSDL.
     */
    public List<CampaignRecord> getCampaignsForSource() {

        List<CampaignRecord> listCampaignRecord = null;
        ParamsGetCampaignsForSource params = MktowsUtil.objectFactory.createParamsGetCampaignsForSource();
        params.setSource(ReqCampSourceType.MKTOWS);
        MktowsPort soap = this.getSoapInterface();
        try {
            AuthenticationHeaderInfo authHdr = this.createAuthenticationHeader();
            SuccessGetCampaignsForSource success = soap.getCampaignsForSource(params, authHdr);
            ResultGetCampaignsForSource result = success.getResult();
            ArrayOfCampaignRecord aoCampaignRecord = result.getCampaignRecordList();
            if (aoCampaignRecord != null) {
                listCampaignRecord = aoCampaignRecord.getCampaignRecord();
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }

        return listCampaignRecord;
    }

    /**
     * This function retrieves a single lead record from Marketo, with all field values for the built-in and custom fields for a lead
     * identified by the provided key (LeadKey). Some of the input parameters for retrieving a lead include: Marketo ID, email address,
     * Marketo cookie ID. If the lead exists based on the input parameters, the lead record attributes will be returned in the result.
     */
    public List<LeadRecord> getLead(LeadKeyRef keyType, String keyValue) throws MktServiceException {

        List<LeadRecord> listLeadRecord = null;
        LeadKey leadKey = MktowsUtil.objectFactory.createLeadKey();
        leadKey.setKeyType(keyType);
        leadKey.setKeyValue(keyValue);
        ParamsGetLead params = MktowsUtil.objectFactory.createParamsGetLead();
        params.setLeadKey(leadKey);
        try {
            AuthenticationHeaderInfo authHdr = this.createAuthenticationHeader();
            MktowsPort soap = this.getSoapInterface();
            SuccessGetLead success = soap.getLead(params, authHdr);
            ResultGetLead result = success.getResult();
            ArrayOfLeadRecord aoLeadRecord = result.getLeadRecordList();
            if (aoLeadRecord != null) {
                listLeadRecord = aoLeadRecord.getLeadRecord();
            }
        } catch (SOAPFaultException ex) {
            LOG.error(ex.getMessage(), ex);
            MktServiceException mktEx = new MktServiceException(ex.getMessage(), ex);
            throw mktEx;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }

        return listLeadRecord;
    }

    /**
     * @see #getLeadActivity(LeadKeyRef, String, int, Date, Date, List, StreamPostionHolder)
     */
    public List<ActivityRecord> getLeadActivity(int batchSize, List<ActivityType> filter, StreamPostionHolder posHolder) throws MktowsClientException,
                                                                                                                        MktServiceException {
        return this.getLeadActivity(null, null, batchSize, null, null, filter, posHolder);
    }

    /**
     * This function retrieves the history of activity/event records for a single lead identified by the provided key. Some of the input
     * parameters for identifying which lead record to retrieve activity records for include: Marketo ID, email address, cookie ID. This
     * function also has an input parameter identifying which activity types you wish to be returned in the result. If you want all activity
     * types, a blank value needs to be passed (must have a parameter passed). For more than one activity time, pass in a list of activity
     * types. The complete list of activity types can be found in the API WSDL. Some example activity types are 'VisitWebPage',
     * 'FillOutForm', and 'ClickLink'.
     */
    public List<ActivityRecord> getLeadActivity(LeadKeyRef keyType,
                                                String keyValue,
                                                int batchSize,
                                                Date latestCreatedAt,
                                                Date oldestCreatedAt,
                                                List<ActivityType> filter,
                                                StreamPostionHolder posHolder) throws MktowsClientException, MktServiceException {

        List<ActivityRecord> listActivityRecord = null;
        ParamsGetLeadActivity params = MktowsUtil.objectFactory.createParamsGetLeadActivity();
        LeadKey leadKey = null;
        // If the holder has a stream position, then don't initialize a new stream position.
        Object lastPos = posHolder.getStreamPosition();
        if (lastPos != null) {
            leadKey = posHolder.getLeadKey();
            params.setStartPosition((StreamPosition)lastPos);
        }
        else {
            leadKey = MktowsUtil.objectFactory.createLeadKey();
            leadKey.setKeyType(keyType);
            leadKey.setKeyValue(keyValue);
            StreamPosition initPos = MktowsUtil.objectFactory.createStreamPosition();
            if (latestCreatedAt != null) {
                initPos.setLatestCreatedAt(MktowsUtil.dateObjectToW3cDate(latestCreatedAt));
            }
            if (oldestCreatedAt != null) {
                initPos.setOldestCreatedAt(MktowsUtil.dateObjectToW3cDate(oldestCreatedAt));
            }
            params.setStartPosition(initPos);
        }
        params.setLeadKey(leadKey);
        params.setBatchSize(new Integer(batchSize));
        ArrayOfActivityType aoActType = MktowsUtil.objectFactory.createArrayOfActivityType();
        aoActType.getActivityType().addAll(filter);
        ActivityTypeFilter actTypeFilter = MktowsUtil.objectFactory.createActivityTypeFilter();
        actTypeFilter.setIncludeTypes(aoActType);
        params.setActivityFilter(actTypeFilter);
        try {
            AuthenticationHeaderInfo authHdr = this.createAuthenticationHeader();
            MktowsPort soap = this.getSoapInterface();
            SuccessGetLeadActivity success = soap.getLeadActivity(params, authHdr);
            LeadActivityList result = success.getLeadActivityList();
            ArrayOfActivityRecord aoActivityRecord = result.getActivityRecordList();
            if (aoActivityRecord != null) {
                listActivityRecord = aoActivityRecord.getActivityRecord();
            }
            posHolder.setStreamPosition(result.getNewStartPosition());
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return listActivityRecord;
    }

    /**
     * This operation checks for changes that have occurred to leads and lead history in the Marketo system. The changes reported include
     * new leads created, leads updated with the changed fields, and new activity records for leads. The result contains activities that
     * caused the change. This API similar to the getLeadActivity API except that it operates on multiple leads instead of a single lead.
     * This function also has an input parameter identifying which activity types you wish to be returned in the result. If you want all
     * activity types, a blank value needs to be passed (must have a parameter passed). For more than one activity time, pass in a list of
     * activity types. The complete list of activity types can be found in the API WSDL. Some example activity types are: 'VisitWebPage',
     * 'FillOutForm', and 'ClickLink'.
     */
    public List<LeadChangeRecord> getLeadChanges(int batchSize,
                                                 Date latestCreatedAt,
                                                 Date oldestCreatedAt,
                                                 List<ActivityType> filter,
                                                 StreamPostionHolder posHolder) throws MktowsClientException {
        List<LeadChangeRecord> listChangeRecord = null;
        ParamsGetLeadChanges params = MktowsUtil.objectFactory.createParamsGetLeadChanges();
        // If the holder has a stream position, then don't initialize a new stream position.
        Object lastPos = posHolder.getStreamPosition();
        if (lastPos != null) {
            params.setStartPosition((StreamPosition)lastPos);
        }
        else {
            StreamPosition initPos = MktowsUtil.objectFactory.createStreamPosition();
            if (latestCreatedAt != null) {
                initPos.setLatestCreatedAt(MktowsUtil.dateObjectToW3cDate(latestCreatedAt));
            }
            if (oldestCreatedAt != null) {
                initPos.setOldestCreatedAt(MktowsUtil.dateObjectToW3cDate(oldestCreatedAt));
            }
            params.setStartPosition(initPos);
        }
        params.setBatchSize(new Integer(batchSize));
        ArrayOfActivityType aoActType = MktowsUtil.objectFactory.createArrayOfActivityType();
        aoActType.getActivityType().addAll(filter);
        ActivityTypeFilter actTypeFilter = MktowsUtil.objectFactory.createActivityTypeFilter();
        actTypeFilter.setIncludeTypes(aoActType);
        params.setActivityFilter(actTypeFilter);
        try {
            AuthenticationHeaderInfo authHdr = this.createAuthenticationHeader();
            MktowsPort soap = this.getSoapInterface();
            SuccessGetLeadChanges success = soap.getLeadChanges(params, authHdr);
            ResultGetLeadChanges result = success.getResult();
            ArrayOfLeadChangeRecord aoLeadChangeRecord = result.getLeadChangeRecordList();
            if (aoLeadChangeRecord != null) {
                listChangeRecord = aoLeadChangeRecord.getLeadChangeRecord();
            }
            posHolder.setStreamPosition(result.getNewStartPosition());
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return listChangeRecord;
    }

    /**
     * This operation retrieves the field values for all the built-in and custom fields for a group of leads which match the criteria passed
     * into the leadSelector parameter. The criteria can be a date range, such as the last updated date; an array of lead keys; or a static
     * list. If you use an array of lead keys, you will be limited to 100 per batch; additional keys will be ignored. Each getMultipleLeads
     * function call can only return batches of 1000 leads, maximum. If this call needs to return more than 1000 leads, the result will
     * return a start position, which should be used in subsequent calls to retrieve the next set of leads. Logic should be implemented to
     * check for this value to see if there are more leads to retrieve at this given time.
     */
    public List<LeadRecord> getMultipleLeads(int batchSize, Date lastUpdatedAt, StreamPostionHolder posHolder) throws MktowsClientException {
        List<LeadRecord> listLeadRecord = null;
        ParamsGetMultipleLeads params = MktowsUtil.objectFactory.createParamsGetMultipleLeads();
        // If the holder has a lead key, then don't initialize a new stream position.
        Object lastPos = posHolder.getStreamPosition();
        if (lastPos != null) {
            params.setStreamPosition((String)lastPos);
        }
        else {
            params.setLastUpdatedAt(MktowsUtil.dateObjectToW3cDate(lastUpdatedAt));
        }
        params.setBatchSize(new Integer(batchSize));
        try {
            AuthenticationHeaderInfo authHdr = this.createAuthenticationHeader();
            MktowsPort soap = this.getSoapInterface();
            SuccessGetMultipleLeads success = soap.getMultipleLeads(params, authHdr);
            ResultGetMultipleLeads result = success.getResult();
            ArrayOfLeadRecord aoLeadRecord = result.getLeadRecordList();
            if (aoLeadRecord != null) {
                listLeadRecord = aoLeadRecord.getLeadRecord();
            }
            posHolder.setStreamPosition(result.getNewStreamPosition());
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return listLeadRecord;
    }

    /**
     * If only a subset of the lead fields are required, then the includeAttributes parameter should be used to specify the desired fields.
     * Limiting the lead fields returned can improve the response time of the API.
     * 
     * @see #getMultipleLeads(int, Date, StreamPostionHolder)
     */
    public List<LeadRecord> getMultipleLeads(int batchSize, Date lastUpdatedAt, StreamPostionHolder posHolder, List<String> leadAttrs) throws MktowsClientException {
        List<LeadRecord> listLeadRecord = null;
        ParamsGetMultipleLeads params = MktowsUtil.objectFactory.createParamsGetMultipleLeads();
        // If the holder has a lead key, then don't initialize a new stream position.
        Object lastPos = posHolder.getStreamPosition();
        if (lastPos != null) {
            params.setStreamPosition((String)lastPos);
        }
        else {
            params.setLastUpdatedAt(MktowsUtil.dateObjectToW3cDate(lastUpdatedAt));
        }
        params.setBatchSize(new Integer(batchSize));
        if (leadAttrs != null && leadAttrs.size() > 0) {
            ArrayOfString aoString = MktowsUtil.objectFactory.createArrayOfString();
            aoString.getStringItem().addAll(leadAttrs);
            params.setIncludeAttributes(aoString);
        }
        try {
            AuthenticationHeaderInfo authHdr = this.createAuthenticationHeader();
            MktowsPort soap = this.getSoapInterface();
            SuccessGetMultipleLeads success = soap.getMultipleLeads(params, authHdr);
            ResultGetMultipleLeads result = success.getResult();
            ArrayOfLeadRecord aoLeadRecord = result.getLeadRecordList();
            if (aoLeadRecord != null) {
                listLeadRecord = aoLeadRecord.getLeadRecord();
            }
            posHolder.setStreamPosition(result.getNewStreamPosition());
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return listLeadRecord;
    }

    /**
     * This function returns the names of Marketo objects that can be used as input into the describeMObjects function for schema discovery
     * operations.
     */
    public List<String> listMObjects() {
        List<String> objectNames = null;
        ParamsListMObjects params = MktowsUtil.objectFactory.createParamsListMObjects();
        try {
            AuthenticationHeaderInfo authHdr = this.createAuthenticationHeader();
            MktowsPort soap = this.getSoapInterface();
            SuccessListMObjects success = soap.listMObjects(params, authHdr);
            ResultListMObjects result = success.getResult();
            objectNames = result.getObjects();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return objectNames;
    }

    /**
     * This operation is used to perform operations on a static list is a list of leads. The available operations are adding a lead to a
     * static list, removing a lead from a static list, or checking if a lead is a member of a static list. The input parameter for
     * identifying a lead when adding, removing and checking is the Marketo ID. The operation can be invoked in a strict or non-strict mode.
     * Strict means that if the operation is not performed or fails, an exception is returned. Non-strict means that a Boolean status is
     * returned to indicate the outcome of the operation.
     */
    public boolean listOperation(ListOperationType listop, String listName, List<LeadKey> leadList, HashMap<String, Boolean> opStatus) {
        boolean retVal = false;
        if (opStatus != null) {
            opStatus.clear();
        }
        opStatus.clear();
        ArrayOfLeadKey aoLeadKey = MktowsUtil.objectFactory.createArrayOfLeadKey();
        aoLeadKey.getLeadKey().addAll(leadList);
        ListKey listKey = MktowsUtil.objectFactory.createListKey();
        listKey.setKeyType(ListKeyType.MKTOLISTNAME);
        listKey.setKeyValue(listName);
        ParamsListOperation params = MktowsUtil.objectFactory.createParamsListOperation();
        params.setListKey(listKey);
        params.setListMemberList(aoLeadKey);
        params.setListOperation(listop);
        try {
            AuthenticationHeaderInfo authHdr = this.createAuthenticationHeader();
            MktowsPort soap = this.getSoapInterface();
            SuccessListOperation success = soap.listOperation(params, authHdr);
            ResultListOperation result = success.getResult();
            retVal = result.isSuccess();
            if (opStatus != null) {
                List<LeadStatus> statusList = result.getStatusList().getLeadStatus();
                for (LeadStatus leadStatus : statusList) {
                    opStatus.put(leadStatus.getLeadKey().getKeyValue(), new Boolean(leadStatus.isStatus()));
                }
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return retVal;
    }

    /**
     * Wrapper for mergeLeads API.
     */
    public MergeStatus mergeLeads(List<Attribute> winningLead, List<List<Attribute>> losingLeadList) {
        MergeStatus status = null;
        ParamsMergeLeads params = MktowsUtil.objectFactory.createParamsMergeLeads();

        // The winning lead
        params.setWinningLeadKeyList(MktowsUtil.objectFactory.createArrayOfAttribute());
        params.getWinningLeadKeyList().getAttribute().addAll(winningLead);

        // The losing leads
        params.setLosingLeadKeyLists(MktowsUtil.objectFactory.createArrayOfKeyList());
        ArrayOfAttribute loser = null;
        for (List<Attribute> losingLead : losingLeadList) {
            loser = MktowsUtil.objectFactory.createArrayOfAttribute();
            loser.getAttribute().addAll(losingLead);
            params.getLosingLeadKeyLists().getKeyList().add(loser);
        }
        try {
            AuthenticationHeaderInfo authHdr = this.createAuthenticationHeader();
            MktowsPort soap = this.getSoapInterface();
            SuccessMergeLeads success = soap.mergeLeads(params, authHdr);
            ResultMergeLeads result = success.getResult();
            status = result.getMergeStatus();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return status;
    }

    /**
     * This function allows the ability to add an existing Marketo lead to an existing Marketo campaign. The input parameter for identifying
     * which lead to add is the Marketo ID.
     */
    public boolean requestCampaign(int campId, List<LeadKey> leadList) {
        boolean retVal = false;
        ArrayOfLeadKey aoLeadKey = MktowsUtil.objectFactory.createArrayOfLeadKey();
        aoLeadKey.getLeadKey().addAll(leadList);
        ParamsRequestCampaign params = MktowsUtil.objectFactory.createParamsRequestCampaign();
        params.setCampaignId(campId);
        params.setLeadList(aoLeadKey);
        try {
            AuthenticationHeaderInfo authHdr = this.createAuthenticationHeader();
            MktowsPort soap = this.getSoapInterface();
            SuccessRequestCampaign success = soap.requestCampaign(params, authHdr);
            ResultRequestCampaign result = success.getResult();
            retVal = result.isSuccess();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return retVal;
    }

    /**
     * This function requests an insert or update (upsert) operation for a lead record. When updating an existing lead, the lead can be
     * identified one of the follow:
     * <ul>
     * <li>Marketo ID
     * <li>Foreign system ID
     * <li>Marketo Cookie
     * <li>Email
     * </ul>
     * If any of these attributes are set as a part of the input parameter and if there is a matching lead in Marketo with these values, the
     * existing lead record will be updated instead of created. Passing the Marketo cookie id as an input parameter helps in associating the
     * new lead with existing anonymous activity records. Except for Email, all of these identifiers are treated as unique keys. The Marketo
     * Id takes precedence over all other keys. If both ForeignSysPersonId and the Marketo Id are present in the lead record, then the
     * Marketo Id will take precedence and the ForeignSysPersonId will be updated for that lead. If the only ForeignSysPersonId is given,
     * then it will be used as a unique identifier. Optionally, a Context Header can be specified to name the target workspace. Refer to the
     * section titled “Definitions” for an explanation of the Context Header.
     */
    public ResultSyncLead syncLead(LeadRecord leadRec, String marketoCookie, boolean returnLead) {
        ResultSyncLead result = null;
        ParamsSyncLead params = MktowsUtil.objectFactory.createParamsSyncLead();
        params.setLeadRecord(leadRec);
        if (marketoCookie != null) {
            params.setMarketoCookie(marketoCookie);
        }
        params.setReturnLead(new Boolean(returnLead));
        try {
            AuthenticationHeaderInfo authHdr = this.createAuthenticationHeader();
            MktowsPort soap = this.getSoapInterface();
            SuccessSyncLead success = soap.syncLead(params, authHdr);
            result = success.getResult();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return result;
    }

    /**
     * This function requests an insert or update (upsert) operation for multiple lead records. If both ForeignSysPersonId and the Marketo
     * lead Id are present in the lead record, then the Marketo lead Id will take precedence and the ForeignSysPersonId will be updated. If
     * the only ForeignSysPersonId is given, then it will be used as a unique identifier. You are able to turn off the dedup feature with
     * this function call. If dedupEnabled is set to true and no other unique identifier is given (foreignSysPersonId or Marketo lead Id),
     * then the lead record will be de-duplicated using the email address. Keep in mind, passing in 'false' will create duplicates within
     * Marketo.
     */
    public List<SyncStatus> syncMultipleLeads(List<LeadRecord> leadRecList, boolean dedupEnabled) {
        List<SyncStatus> retVal = null;
        ArrayOfLeadRecord aoLeadRec = MktowsUtil.objectFactory.createArrayOfLeadRecord();
        aoLeadRec.getLeadRecord().addAll(leadRecList);
        ParamsSyncMultipleLeads params = MktowsUtil.objectFactory.createParamsSyncMultipleLeads();
        params.setDedupEnabled(new Boolean(dedupEnabled));
        try {
            AuthenticationHeaderInfo authHdr = this.createAuthenticationHeader();
            MktowsPort soap = this.getSoapInterface();
            SuccessSyncMultipleLeads success = soap.syncMultipleLeads(params, authHdr);
            retVal = success.getResult().getSyncStatusList().getSyncStatus();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return retVal;
    }

}
