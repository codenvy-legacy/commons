
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.marketo.mktows.wsdl package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ParamsListOperation_QNAME = new QName("http://www.marketo.com/mktows/", "paramsListOperation");
    private final static QName _MktowsContextHeader_QNAME = new QName("http://www.marketo.com/mktows/", "MktowsContextHeader");
    private final static QName _SuccessMergeLeads_QNAME = new QName("http://www.marketo.com/mktows/", "successMergeLeads");
    private final static QName _ParamsGetMultipleLeads_QNAME = new QName("http://www.marketo.com/mktows/", "paramsGetMultipleLeads");
    private final static QName _ParamsSyncLead_QNAME = new QName("http://www.marketo.com/mktows/", "paramsSyncLead");
    private final static QName _SuccessGetLead_QNAME = new QName("http://www.marketo.com/mktows/", "successGetLead");
    private final static QName _ParamsGetLeadChanges_QNAME = new QName("http://www.marketo.com/mktows/", "paramsGetLeadChanges");
    private final static QName _SuccessListMObjects_QNAME = new QName("http://www.marketo.com/mktows/", "successListMObjects");
    private final static QName _SuccessSyncMultipleLeads_QNAME = new QName("http://www.marketo.com/mktows/", "successSyncMultipleLeads");
    private final static QName _ParamsGetLead_QNAME = new QName("http://www.marketo.com/mktows/", "paramsGetLead");
    private final static QName _SuccessDeleteCustomObjects_QNAME = new QName("http://www.marketo.com/mktows/", "successDeleteCustomObjects");
    private final static QName _SuccessGetLeadActivity_QNAME = new QName("http://www.marketo.com/mktows/", "successGetLeadActivity");
    private final static QName _ParamsRequestCampaign_QNAME = new QName("http://www.marketo.com/mktows/", "paramsRequestCampaign");
    private final static QName _SuccessGetCustomObjects_QNAME = new QName("http://www.marketo.com/mktows/", "successGetCustomObjects");
    private final static QName _ParamsMergeLeads_QNAME = new QName("http://www.marketo.com/mktows/", "paramsMergeLeads");
    private final static QName _ParamsDescribeMObject_QNAME = new QName("http://www.marketo.com/mktows/", "paramsDescribeMObject");
    private final static QName _SuccessGetCampaignsForSource_QNAME = new QName("http://www.marketo.com/mktows/", "successGetCampaignsForSource");
    private final static QName _ParamsGetCustomObjects_QNAME = new QName("http://www.marketo.com/mktows/", "paramsGetCustomObjects");
    private final static QName _SuccessRequestCampaign_QNAME = new QName("http://www.marketo.com/mktows/", "successRequestCampaign");
    private final static QName _ParamsGetLeadActivity_QNAME = new QName("http://www.marketo.com/mktows/", "paramsGetLeadActivity");
    private final static QName _ParamsDeleteCustomObjects_QNAME = new QName("http://www.marketo.com/mktows/", "paramsDeleteCustomObjects");
    private final static QName _SuccessListOperation_QNAME = new QName("http://www.marketo.com/mktows/", "successListOperation");
    private final static QName _ParamsListMObjects_QNAME = new QName("http://www.marketo.com/mktows/", "paramsListMObjects");
    private final static QName _SuccessSyncCustomObjects_QNAME = new QName("http://www.marketo.com/mktows/", "successSyncCustomObjects");
    private final static QName _ParamsSyncCustomObjects_QNAME = new QName("http://www.marketo.com/mktows/", "paramsSyncCustomObjects");
    private final static QName _AuthenticationHeader_QNAME = new QName("http://www.marketo.com/mktows/", "AuthenticationHeader");
    private final static QName _SuccessGetMultipleLeads_QNAME = new QName("http://www.marketo.com/mktows/", "successGetMultipleLeads");
    private final static QName _SuccessSyncLead_QNAME = new QName("http://www.marketo.com/mktows/", "successSyncLead");
    private final static QName _SuccessGetLeadChanges_QNAME = new QName("http://www.marketo.com/mktows/", "successGetLeadChanges");
    private final static QName _ParamsGetCampaignsForSource_QNAME = new QName("http://www.marketo.com/mktows/", "paramsGetCampaignsForSource");
    private final static QName _SuccessDescribeMObject_QNAME = new QName("http://www.marketo.com/mktows/", "successDescribeMObject");
    private final static QName _ParamsSyncMultipleLeads_QNAME = new QName("http://www.marketo.com/mktows/", "paramsSyncMultipleLeads");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.marketo.mktows.wsdl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResultMergeLeads }
     * 
     */
    public ResultMergeLeads createResultMergeLeads() {
        return new ResultMergeLeads();
    }

    /**
     * Create an instance of {@link SyncStatus }
     * 
     */
    public SyncStatus createSyncStatus() {
        return new SyncStatus();
    }

    /**
     * Create an instance of {@link ParamsMergeLeads }
     * 
     */
    public ParamsMergeLeads createParamsMergeLeads() {
        return new ParamsMergeLeads();
    }

    /**
     * Create an instance of {@link ArrayOfLeadRecord }
     * 
     */
    public ArrayOfLeadRecord createArrayOfLeadRecord() {
        return new ArrayOfLeadRecord();
    }

    /**
     * Create an instance of {@link ArrayOfString }
     * 
     */
    public ArrayOfString createArrayOfString() {
        return new ArrayOfString();
    }

    /**
     * Create an instance of {@link ArrayOfLeadStatus }
     * 
     */
    public ArrayOfLeadStatus createArrayOfLeadStatus() {
        return new ArrayOfLeadStatus();
    }

    /**
     * Create an instance of {@link SuccessListOperation }
     * 
     */
    public SuccessListOperation createSuccessListOperation() {
        return new SuccessListOperation();
    }

    /**
     * Create an instance of {@link LeadChangeRecord }
     * 
     */
    public LeadChangeRecord createLeadChangeRecord() {
        return new LeadChangeRecord();
    }

    /**
     * Create an instance of {@link ActivityRecord }
     * 
     */
    public ActivityRecord createActivityRecord() {
        return new ActivityRecord();
    }

    /**
     * Create an instance of {@link ArrayOfMObjFieldMetadata }
     * 
     */
    public ArrayOfMObjFieldMetadata createArrayOfMObjFieldMetadata() {
        return new ArrayOfMObjFieldMetadata();
    }

    /**
     * Create an instance of {@link ResultDeleteCustomObjects }
     * 
     */
    public ResultDeleteCustomObjects createResultDeleteCustomObjects() {
        return new ResultDeleteCustomObjects();
    }

    /**
     * Create an instance of {@link MObjFieldMetadata }
     * 
     */
    public MObjFieldMetadata createMObjFieldMetadata() {
        return new MObjFieldMetadata();
    }

    /**
     * Create an instance of {@link ParamsGetLead }
     * 
     */
    public ParamsGetLead createParamsGetLead() {
        return new ParamsGetLead();
    }

    /**
     * Create an instance of {@link LeadActivityList }
     * 
     */
    public LeadActivityList createLeadActivityList() {
        return new LeadActivityList();
    }

    /**
     * Create an instance of {@link ResultRequestCampaign }
     * 
     */
    public ResultRequestCampaign createResultRequestCampaign() {
        return new ResultRequestCampaign();
    }

    /**
     * Create an instance of {@link ArrayOfCampaignRecord }
     * 
     */
    public ArrayOfCampaignRecord createArrayOfCampaignRecord() {
        return new ArrayOfCampaignRecord();
    }

    /**
     * Create an instance of {@link SuccessGetLeadActivity }
     * 
     */
    public SuccessGetLeadActivity createSuccessGetLeadActivity() {
        return new SuccessGetLeadActivity();
    }

    /**
     * Create an instance of {@link ResultSyncMultipleLeads }
     * 
     */
    public ResultSyncMultipleLeads createResultSyncMultipleLeads() {
        return new ResultSyncMultipleLeads();
    }

    /**
     * Create an instance of {@link ParamsGetCampaignsForSource }
     * 
     */
    public ParamsGetCampaignsForSource createParamsGetCampaignsForSource() {
        return new ParamsGetCampaignsForSource();
    }

    /**
     * Create an instance of {@link ParamsSyncMultipleLeads }
     * 
     */
    public ParamsSyncMultipleLeads createParamsSyncMultipleLeads() {
        return new ParamsSyncMultipleLeads();
    }

    /**
     * Create an instance of {@link ResultGetCustomObjects }
     * 
     */
    public ResultGetCustomObjects createResultGetCustomObjects() {
        return new ResultGetCustomObjects();
    }

    /**
     * Create an instance of {@link ArrayOfSyncStatus }
     * 
     */
    public ArrayOfSyncStatus createArrayOfSyncStatus() {
        return new ArrayOfSyncStatus();
    }

    /**
     * Create an instance of {@link LeadRecord }
     * 
     */
    public LeadRecord createLeadRecord() {
        return new LeadRecord();
    }

    /**
     * Create an instance of {@link ResultGetLead }
     * 
     */
    public ResultGetLead createResultGetLead() {
        return new ResultGetLead();
    }

    /**
     * Create an instance of {@link ResultSyncLead }
     * 
     */
    public ResultSyncLead createResultSyncLead() {
        return new ResultSyncLead();
    }

    /**
     * Create an instance of {@link SuccessRequestCampaign }
     * 
     */
    public SuccessRequestCampaign createSuccessRequestCampaign() {
        return new SuccessRequestCampaign();
    }

    /**
     * Create an instance of {@link SuccessSyncLead }
     * 
     */
    public SuccessSyncLead createSuccessSyncLead() {
        return new SuccessSyncLead();
    }

    /**
     * Create an instance of {@link ActivityTypeFilter }
     * 
     */
    public ActivityTypeFilter createActivityTypeFilter() {
        return new ActivityTypeFilter();
    }

    /**
     * Create an instance of {@link ResultDescribeMObject }
     * 
     */
    public ResultDescribeMObject createResultDescribeMObject() {
        return new ResultDescribeMObject();
    }

    /**
     * Create an instance of {@link ResultListMObjects }
     * 
     */
    public ResultListMObjects createResultListMObjects() {
        return new ResultListMObjects();
    }

    /**
     * Create an instance of {@link ArrayOfAttribute }
     * 
     */
    public ArrayOfAttribute createArrayOfAttribute() {
        return new ArrayOfAttribute();
    }

    /**
     * Create an instance of {@link ArrayOfBase64Binary }
     * 
     */
    public ArrayOfBase64Binary createArrayOfBase64Binary() {
        return new ArrayOfBase64Binary();
    }

    /**
     * Create an instance of {@link ArrayOfSyncCustomObjStatus }
     * 
     */
    public ArrayOfSyncCustomObjStatus createArrayOfSyncCustomObjStatus() {
        return new ArrayOfSyncCustomObjStatus();
    }

    /**
     * Create an instance of {@link SuccessGetLead }
     * 
     */
    public SuccessGetLead createSuccessGetLead() {
        return new SuccessGetLead();
    }

    /**
     * Create an instance of {@link ResultListOperation }
     * 
     */
    public ResultListOperation createResultListOperation() {
        return new ResultListOperation();
    }

    /**
     * Create an instance of {@link ArrayOfLeadChangeRecord }
     * 
     */
    public ArrayOfLeadChangeRecord createArrayOfLeadChangeRecord() {
        return new ArrayOfLeadChangeRecord();
    }

    /**
     * Create an instance of {@link SuccessMergeLeads }
     * 
     */
    public SuccessMergeLeads createSuccessMergeLeads() {
        return new SuccessMergeLeads();
    }

    /**
     * Create an instance of {@link ParamsRequestCampaign }
     * 
     */
    public ParamsRequestCampaign createParamsRequestCampaign() {
        return new ParamsRequestCampaign();
    }

    /**
     * Create an instance of {@link ArrayOfKeyList }
     * 
     */
    public ArrayOfKeyList createArrayOfKeyList() {
        return new ArrayOfKeyList();
    }

    /**
     * Create an instance of {@link ParamsSyncLead }
     * 
     */
    public ParamsSyncLead createParamsSyncLead() {
        return new ParamsSyncLead();
    }

    /**
     * Create an instance of {@link MergeStatus }
     * 
     */
    public MergeStatus createMergeStatus() {
        return new MergeStatus();
    }

    /**
     * Create an instance of {@link SuccessSyncMultipleLeads }
     * 
     */
    public SuccessSyncMultipleLeads createSuccessSyncMultipleLeads() {
        return new SuccessSyncMultipleLeads();
    }

    /**
     * Create an instance of {@link StreamPosition }
     * 
     */
    public StreamPosition createStreamPosition() {
        return new StreamPosition();
    }

    /**
     * Create an instance of {@link ParamsListMObjects }
     * 
     */
    public ParamsListMObjects createParamsListMObjects() {
        return new ParamsListMObjects();
    }

    /**
     * Create an instance of {@link SyncCustomObjStatus }
     * 
     */
    public SyncCustomObjStatus createSyncCustomObjStatus() {
        return new SyncCustomObjStatus();
    }

    /**
     * Create an instance of {@link ListKey }
     * 
     */
    public ListKey createListKey() {
        return new ListKey();
    }

    /**
     * Create an instance of {@link LeadStatus }
     * 
     */
    public LeadStatus createLeadStatus() {
        return new LeadStatus();
    }

    /**
     * Create an instance of {@link SuccessGetCampaignsForSource }
     * 
     */
    public SuccessGetCampaignsForSource createSuccessGetCampaignsForSource() {
        return new SuccessGetCampaignsForSource();
    }

    /**
     * Create an instance of {@link ParamsDeleteCustomObjects }
     * 
     */
    public ParamsDeleteCustomObjects createParamsDeleteCustomObjects() {
        return new ParamsDeleteCustomObjects();
    }

    /**
     * Create an instance of {@link ResultGetLeadChanges }
     * 
     */
    public ResultGetLeadChanges createResultGetLeadChanges() {
        return new ResultGetLeadChanges();
    }

    /**
     * Create an instance of {@link SuccessGetLeadChanges }
     * 
     */
    public SuccessGetLeadChanges createSuccessGetLeadChanges() {
        return new SuccessGetLeadChanges();
    }

    /**
     * Create an instance of {@link ArrayOfVersionedItem }
     * 
     */
    public ArrayOfVersionedItem createArrayOfVersionedItem() {
        return new ArrayOfVersionedItem();
    }

    /**
     * Create an instance of {@link ArrayOfActivityType }
     * 
     */
    public ArrayOfActivityType createArrayOfActivityType() {
        return new ArrayOfActivityType();
    }

    /**
     * Create an instance of {@link MObjectMetadata }
     * 
     */
    public MObjectMetadata createMObjectMetadata() {
        return new MObjectMetadata();
    }

    /**
     * Create an instance of {@link ResultSyncCustomObjects }
     * 
     */
    public ResultSyncCustomObjects createResultSyncCustomObjects() {
        return new ResultSyncCustomObjects();
    }

    /**
     * Create an instance of {@link SuccessGetCustomObjects }
     * 
     */
    public SuccessGetCustomObjects createSuccessGetCustomObjects() {
        return new SuccessGetCustomObjects();
    }

    /**
     * Create an instance of {@link CampaignRecord }
     * 
     */
    public CampaignRecord createCampaignRecord() {
        return new CampaignRecord();
    }

    /**
     * Create an instance of {@link SuccessDescribeMObject }
     * 
     */
    public SuccessDescribeMObject createSuccessDescribeMObject() {
        return new SuccessDescribeMObject();
    }

    /**
     * Create an instance of {@link ParamsSyncCustomObjects }
     * 
     */
    public ParamsSyncCustomObjects createParamsSyncCustomObjects() {
        return new ParamsSyncCustomObjects();
    }

    /**
     * Create an instance of {@link CustomObj }
     * 
     */
    public CustomObj createCustomObj() {
        return new CustomObj();
    }

    /**
     * Create an instance of {@link ParamsDescribeMObject }
     * 
     */
    public ParamsDescribeMObject createParamsDescribeMObject() {
        return new ParamsDescribeMObject();
    }

    /**
     * Create an instance of {@link ArrayOfInteger }
     * 
     */
    public ArrayOfInteger createArrayOfInteger() {
        return new ArrayOfInteger();
    }

    /**
     * Create an instance of {@link LeadKey }
     * 
     */
    public LeadKey createLeadKey() {
        return new LeadKey();
    }

    /**
     * Create an instance of {@link VersionedItem }
     * 
     */
    public VersionedItem createVersionedItem() {
        return new VersionedItem();
    }

    /**
     * Create an instance of {@link SuccessSyncCustomObjects }
     * 
     */
    public SuccessSyncCustomObjects createSuccessSyncCustomObjects() {
        return new SuccessSyncCustomObjects();
    }

    /**
     * Create an instance of {@link ResultGetMultipleLeads }
     * 
     */
    public ResultGetMultipleLeads createResultGetMultipleLeads() {
        return new ResultGetMultipleLeads();
    }

    /**
     * Create an instance of {@link ResultGetCampaignsForSource }
     * 
     */
    public ResultGetCampaignsForSource createResultGetCampaignsForSource() {
        return new ResultGetCampaignsForSource();
    }

    /**
     * Create an instance of {@link ArrayOfLeadKey }
     * 
     */
    public ArrayOfLeadKey createArrayOfLeadKey() {
        return new ArrayOfLeadKey();
    }

    /**
     * Create an instance of {@link Attribute }
     * 
     */
    public Attribute createAttribute() {
        return new Attribute();
    }

    /**
     * Create an instance of {@link ParamsGetLeadActivity }
     * 
     */
    public ParamsGetLeadActivity createParamsGetLeadActivity() {
        return new ParamsGetLeadActivity();
    }

    /**
     * Create an instance of {@link ArrayOfCustomObj }
     * 
     */
    public ArrayOfCustomObj createArrayOfCustomObj() {
        return new ArrayOfCustomObj();
    }

    /**
     * Create an instance of {@link ParamsGetCustomObjects }
     * 
     */
    public ParamsGetCustomObjects createParamsGetCustomObjects() {
        return new ParamsGetCustomObjects();
    }

    /**
     * Create an instance of {@link SuccessListMObjects }
     * 
     */
    public SuccessListMObjects createSuccessListMObjects() {
        return new SuccessListMObjects();
    }

    /**
     * Create an instance of {@link SuccessDeleteCustomObjects }
     * 
     */
    public SuccessDeleteCustomObjects createSuccessDeleteCustomObjects() {
        return new SuccessDeleteCustomObjects();
    }

    /**
     * Create an instance of {@link SuccessGetMultipleLeads }
     * 
     */
    public SuccessGetMultipleLeads createSuccessGetMultipleLeads() {
        return new SuccessGetMultipleLeads();
    }

    /**
     * Create an instance of {@link ParamsGetMultipleLeads }
     * 
     */
    public ParamsGetMultipleLeads createParamsGetMultipleLeads() {
        return new ParamsGetMultipleLeads();
    }

    /**
     * Create an instance of {@link ArrayOfActivityRecord }
     * 
     */
    public ArrayOfActivityRecord createArrayOfActivityRecord() {
        return new ArrayOfActivityRecord();
    }

    /**
     * Create an instance of {@link AuthenticationHeaderInfo }
     * 
     */
    public AuthenticationHeaderInfo createAuthenticationHeaderInfo() {
        return new AuthenticationHeaderInfo();
    }

    /**
     * Create an instance of {@link ParamsListOperation }
     * 
     */
    public ParamsListOperation createParamsListOperation() {
        return new ParamsListOperation();
    }

    /**
     * Create an instance of {@link ParamsGetLeadChanges }
     * 
     */
    public ParamsGetLeadChanges createParamsGetLeadChanges() {
        return new ParamsGetLeadChanges();
    }

    /**
     * Create an instance of {@link MktowsContextHeaderInfo }
     * 
     */
    public MktowsContextHeaderInfo createMktowsContextHeaderInfo() {
        return new MktowsContextHeaderInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsListOperation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsListOperation")
    public JAXBElement<ParamsListOperation> createParamsListOperation(ParamsListOperation value) {
        return new JAXBElement<ParamsListOperation>(_ParamsListOperation_QNAME, ParamsListOperation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MktowsContextHeaderInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "MktowsContextHeader")
    public JAXBElement<MktowsContextHeaderInfo> createMktowsContextHeader(MktowsContextHeaderInfo value) {
        return new JAXBElement<MktowsContextHeaderInfo>(_MktowsContextHeader_QNAME, MktowsContextHeaderInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessMergeLeads }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successMergeLeads")
    public JAXBElement<SuccessMergeLeads> createSuccessMergeLeads(SuccessMergeLeads value) {
        return new JAXBElement<SuccessMergeLeads>(_SuccessMergeLeads_QNAME, SuccessMergeLeads.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsGetMultipleLeads }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsGetMultipleLeads")
    public JAXBElement<ParamsGetMultipleLeads> createParamsGetMultipleLeads(ParamsGetMultipleLeads value) {
        return new JAXBElement<ParamsGetMultipleLeads>(_ParamsGetMultipleLeads_QNAME, ParamsGetMultipleLeads.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsSyncLead }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsSyncLead")
    public JAXBElement<ParamsSyncLead> createParamsSyncLead(ParamsSyncLead value) {
        return new JAXBElement<ParamsSyncLead>(_ParamsSyncLead_QNAME, ParamsSyncLead.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessGetLead }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successGetLead")
    public JAXBElement<SuccessGetLead> createSuccessGetLead(SuccessGetLead value) {
        return new JAXBElement<SuccessGetLead>(_SuccessGetLead_QNAME, SuccessGetLead.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsGetLeadChanges }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsGetLeadChanges")
    public JAXBElement<ParamsGetLeadChanges> createParamsGetLeadChanges(ParamsGetLeadChanges value) {
        return new JAXBElement<ParamsGetLeadChanges>(_ParamsGetLeadChanges_QNAME, ParamsGetLeadChanges.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessListMObjects }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successListMObjects")
    public JAXBElement<SuccessListMObjects> createSuccessListMObjects(SuccessListMObjects value) {
        return new JAXBElement<SuccessListMObjects>(_SuccessListMObjects_QNAME, SuccessListMObjects.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessSyncMultipleLeads }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successSyncMultipleLeads")
    public JAXBElement<SuccessSyncMultipleLeads> createSuccessSyncMultipleLeads(SuccessSyncMultipleLeads value) {
        return new JAXBElement<SuccessSyncMultipleLeads>(_SuccessSyncMultipleLeads_QNAME, SuccessSyncMultipleLeads.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsGetLead }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsGetLead")
    public JAXBElement<ParamsGetLead> createParamsGetLead(ParamsGetLead value) {
        return new JAXBElement<ParamsGetLead>(_ParamsGetLead_QNAME, ParamsGetLead.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessDeleteCustomObjects }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successDeleteCustomObjects")
    public JAXBElement<SuccessDeleteCustomObjects> createSuccessDeleteCustomObjects(SuccessDeleteCustomObjects value) {
        return new JAXBElement<SuccessDeleteCustomObjects>(_SuccessDeleteCustomObjects_QNAME, SuccessDeleteCustomObjects.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessGetLeadActivity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successGetLeadActivity")
    public JAXBElement<SuccessGetLeadActivity> createSuccessGetLeadActivity(SuccessGetLeadActivity value) {
        return new JAXBElement<SuccessGetLeadActivity>(_SuccessGetLeadActivity_QNAME, SuccessGetLeadActivity.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsRequestCampaign }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsRequestCampaign")
    public JAXBElement<ParamsRequestCampaign> createParamsRequestCampaign(ParamsRequestCampaign value) {
        return new JAXBElement<ParamsRequestCampaign>(_ParamsRequestCampaign_QNAME, ParamsRequestCampaign.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessGetCustomObjects }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successGetCustomObjects")
    public JAXBElement<SuccessGetCustomObjects> createSuccessGetCustomObjects(SuccessGetCustomObjects value) {
        return new JAXBElement<SuccessGetCustomObjects>(_SuccessGetCustomObjects_QNAME, SuccessGetCustomObjects.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsMergeLeads }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsMergeLeads")
    public JAXBElement<ParamsMergeLeads> createParamsMergeLeads(ParamsMergeLeads value) {
        return new JAXBElement<ParamsMergeLeads>(_ParamsMergeLeads_QNAME, ParamsMergeLeads.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsDescribeMObject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsDescribeMObject")
    public JAXBElement<ParamsDescribeMObject> createParamsDescribeMObject(ParamsDescribeMObject value) {
        return new JAXBElement<ParamsDescribeMObject>(_ParamsDescribeMObject_QNAME, ParamsDescribeMObject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessGetCampaignsForSource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successGetCampaignsForSource")
    public JAXBElement<SuccessGetCampaignsForSource> createSuccessGetCampaignsForSource(SuccessGetCampaignsForSource value) {
        return new JAXBElement<SuccessGetCampaignsForSource>(_SuccessGetCampaignsForSource_QNAME, SuccessGetCampaignsForSource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsGetCustomObjects }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsGetCustomObjects")
    public JAXBElement<ParamsGetCustomObjects> createParamsGetCustomObjects(ParamsGetCustomObjects value) {
        return new JAXBElement<ParamsGetCustomObjects>(_ParamsGetCustomObjects_QNAME, ParamsGetCustomObjects.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessRequestCampaign }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successRequestCampaign")
    public JAXBElement<SuccessRequestCampaign> createSuccessRequestCampaign(SuccessRequestCampaign value) {
        return new JAXBElement<SuccessRequestCampaign>(_SuccessRequestCampaign_QNAME, SuccessRequestCampaign.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsGetLeadActivity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsGetLeadActivity")
    public JAXBElement<ParamsGetLeadActivity> createParamsGetLeadActivity(ParamsGetLeadActivity value) {
        return new JAXBElement<ParamsGetLeadActivity>(_ParamsGetLeadActivity_QNAME, ParamsGetLeadActivity.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsDeleteCustomObjects }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsDeleteCustomObjects")
    public JAXBElement<ParamsDeleteCustomObjects> createParamsDeleteCustomObjects(ParamsDeleteCustomObjects value) {
        return new JAXBElement<ParamsDeleteCustomObjects>(_ParamsDeleteCustomObjects_QNAME, ParamsDeleteCustomObjects.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessListOperation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successListOperation")
    public JAXBElement<SuccessListOperation> createSuccessListOperation(SuccessListOperation value) {
        return new JAXBElement<SuccessListOperation>(_SuccessListOperation_QNAME, SuccessListOperation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsListMObjects }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsListMObjects")
    public JAXBElement<ParamsListMObjects> createParamsListMObjects(ParamsListMObjects value) {
        return new JAXBElement<ParamsListMObjects>(_ParamsListMObjects_QNAME, ParamsListMObjects.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessSyncCustomObjects }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successSyncCustomObjects")
    public JAXBElement<SuccessSyncCustomObjects> createSuccessSyncCustomObjects(SuccessSyncCustomObjects value) {
        return new JAXBElement<SuccessSyncCustomObjects>(_SuccessSyncCustomObjects_QNAME, SuccessSyncCustomObjects.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsSyncCustomObjects }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsSyncCustomObjects")
    public JAXBElement<ParamsSyncCustomObjects> createParamsSyncCustomObjects(ParamsSyncCustomObjects value) {
        return new JAXBElement<ParamsSyncCustomObjects>(_ParamsSyncCustomObjects_QNAME, ParamsSyncCustomObjects.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthenticationHeaderInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "AuthenticationHeader")
    public JAXBElement<AuthenticationHeaderInfo> createAuthenticationHeader(AuthenticationHeaderInfo value) {
        return new JAXBElement<AuthenticationHeaderInfo>(_AuthenticationHeader_QNAME, AuthenticationHeaderInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessGetMultipleLeads }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successGetMultipleLeads")
    public JAXBElement<SuccessGetMultipleLeads> createSuccessGetMultipleLeads(SuccessGetMultipleLeads value) {
        return new JAXBElement<SuccessGetMultipleLeads>(_SuccessGetMultipleLeads_QNAME, SuccessGetMultipleLeads.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessSyncLead }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successSyncLead")
    public JAXBElement<SuccessSyncLead> createSuccessSyncLead(SuccessSyncLead value) {
        return new JAXBElement<SuccessSyncLead>(_SuccessSyncLead_QNAME, SuccessSyncLead.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessGetLeadChanges }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successGetLeadChanges")
    public JAXBElement<SuccessGetLeadChanges> createSuccessGetLeadChanges(SuccessGetLeadChanges value) {
        return new JAXBElement<SuccessGetLeadChanges>(_SuccessGetLeadChanges_QNAME, SuccessGetLeadChanges.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsGetCampaignsForSource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsGetCampaignsForSource")
    public JAXBElement<ParamsGetCampaignsForSource> createParamsGetCampaignsForSource(ParamsGetCampaignsForSource value) {
        return new JAXBElement<ParamsGetCampaignsForSource>(_ParamsGetCampaignsForSource_QNAME, ParamsGetCampaignsForSource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuccessDescribeMObject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "successDescribeMObject")
    public JAXBElement<SuccessDescribeMObject> createSuccessDescribeMObject(SuccessDescribeMObject value) {
        return new JAXBElement<SuccessDescribeMObject>(_SuccessDescribeMObject_QNAME, SuccessDescribeMObject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParamsSyncMultipleLeads }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marketo.com/mktows/", name = "paramsSyncMultipleLeads")
    public JAXBElement<ParamsSyncMultipleLeads> createParamsSyncMultipleLeads(ParamsSyncMultipleLeads value) {
        return new JAXBElement<ParamsSyncMultipleLeads>(_ParamsSyncMultipleLeads_QNAME, ParamsSyncMultipleLeads.class, null, value);
    }

}
