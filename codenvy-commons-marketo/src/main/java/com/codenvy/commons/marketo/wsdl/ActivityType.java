
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActivityType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActivityType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="VisitWebpage"/>
 *     &lt;enumeration value="FillOutForm"/>
 *     &lt;enumeration value="ClickLink"/>
 *     &lt;enumeration value="RegisterForEvent"/>
 *     &lt;enumeration value="AttendEvent"/>
 *     &lt;enumeration value="SendEmail"/>
 *     &lt;enumeration value="EmailDelivered"/>
 *     &lt;enumeration value="EmailBounced"/>
 *     &lt;enumeration value="UnsubscribeEmail"/>
 *     &lt;enumeration value="OpenEmail"/>
 *     &lt;enumeration value="ClickEmail"/>
 *     &lt;enumeration value="NewLead"/>
 *     &lt;enumeration value="ChangeDataValue"/>
 *     &lt;enumeration value="LeadAssigned"/>
 *     &lt;enumeration value="NewSFDCOpprtnty"/>
 *     &lt;enumeration value="Wait"/>
 *     &lt;enumeration value="RunSubflow"/>
 *     &lt;enumeration value="RemoveFromFlow"/>
 *     &lt;enumeration value="PushLeadToSales"/>
 *     &lt;enumeration value="CreateTask"/>
 *     &lt;enumeration value="ConvertLead"/>
 *     &lt;enumeration value="ChangeScore"/>
 *     &lt;enumeration value="ChangeOwner"/>
 *     &lt;enumeration value="AddToList"/>
 *     &lt;enumeration value="RemoveFromList"/>
 *     &lt;enumeration value="SFDCActivity"/>
 *     &lt;enumeration value="EmailBouncedSoft"/>
 *     &lt;enumeration value="PushLeadUpdatesToSales"/>
 *     &lt;enumeration value="DeleteLeadFromSales"/>
 *     &lt;enumeration value="SFDCActivityUpdated"/>
 *     &lt;enumeration value="SFDCMergeLeads"/>
 *     &lt;enumeration value="MergeLeads"/>
 *     &lt;enumeration value="ResolveConflicts"/>
 *     &lt;enumeration value="AssocWithOpprtntyInSales"/>
 *     &lt;enumeration value="DissocFromOpprtntyInSales"/>
 *     &lt;enumeration value="UpdateOpprtntyInSales"/>
 *     &lt;enumeration value="DeleteLead"/>
 *     &lt;enumeration value="SendAlert"/>
 *     &lt;enumeration value="SendSalesEmail"/>
 *     &lt;enumeration value="OpenSalesEmail"/>
 *     &lt;enumeration value="ClickSalesEmail"/>
 *     &lt;enumeration value="AddtoSFDCCampaign"/>
 *     &lt;enumeration value="RemoveFromSFDCCampaign"/>
 *     &lt;enumeration value="ChangeStatusInSFDCCampaign"/>
 *     &lt;enumeration value="ReceiveSalesEmail"/>
 *     &lt;enumeration value="InterestingMoment"/>
 *     &lt;enumeration value="RequestCampaign"/>
 *     &lt;enumeration value="SalesEmailBounced"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActivityType")
@XmlEnum
public enum ActivityType {

    @XmlEnumValue("VisitWebpage")
    VISIT_WEBPAGE("VisitWebpage"),
    @XmlEnumValue("FillOutForm")
    FILL_OUT_FORM("FillOutForm"),
    @XmlEnumValue("ClickLink")
    CLICK_LINK("ClickLink"),
    @XmlEnumValue("RegisterForEvent")
    REGISTER_FOR_EVENT("RegisterForEvent"),
    @XmlEnumValue("AttendEvent")
    ATTEND_EVENT("AttendEvent"),
    @XmlEnumValue("SendEmail")
    SEND_EMAIL("SendEmail"),
    @XmlEnumValue("EmailDelivered")
    EMAIL_DELIVERED("EmailDelivered"),
    @XmlEnumValue("EmailBounced")
    EMAIL_BOUNCED("EmailBounced"),
    @XmlEnumValue("UnsubscribeEmail")
    UNSUBSCRIBE_EMAIL("UnsubscribeEmail"),
    @XmlEnumValue("OpenEmail")
    OPEN_EMAIL("OpenEmail"),
    @XmlEnumValue("ClickEmail")
    CLICK_EMAIL("ClickEmail"),
    @XmlEnumValue("NewLead")
    NEW_LEAD("NewLead"),
    @XmlEnumValue("ChangeDataValue")
    CHANGE_DATA_VALUE("ChangeDataValue"),
    @XmlEnumValue("LeadAssigned")
    LEAD_ASSIGNED("LeadAssigned"),
    @XmlEnumValue("NewSFDCOpprtnty")
    NEW_SFDC_OPPRTNTY("NewSFDCOpprtnty"),
    @XmlEnumValue("Wait")
    WAIT("Wait"),
    @XmlEnumValue("RunSubflow")
    RUN_SUBFLOW("RunSubflow"),
    @XmlEnumValue("RemoveFromFlow")
    REMOVE_FROM_FLOW("RemoveFromFlow"),
    @XmlEnumValue("PushLeadToSales")
    PUSH_LEAD_TO_SALES("PushLeadToSales"),
    @XmlEnumValue("CreateTask")
    CREATE_TASK("CreateTask"),
    @XmlEnumValue("ConvertLead")
    CONVERT_LEAD("ConvertLead"),
    @XmlEnumValue("ChangeScore")
    CHANGE_SCORE("ChangeScore"),
    @XmlEnumValue("ChangeOwner")
    CHANGE_OWNER("ChangeOwner"),
    @XmlEnumValue("AddToList")
    ADD_TO_LIST("AddToList"),
    @XmlEnumValue("RemoveFromList")
    REMOVE_FROM_LIST("RemoveFromList"),
    @XmlEnumValue("SFDCActivity")
    SFDC_ACTIVITY("SFDCActivity"),
    @XmlEnumValue("EmailBouncedSoft")
    EMAIL_BOUNCED_SOFT("EmailBouncedSoft"),
    @XmlEnumValue("PushLeadUpdatesToSales")
    PUSH_LEAD_UPDATES_TO_SALES("PushLeadUpdatesToSales"),
    @XmlEnumValue("DeleteLeadFromSales")
    DELETE_LEAD_FROM_SALES("DeleteLeadFromSales"),
    @XmlEnumValue("SFDCActivityUpdated")
    SFDC_ACTIVITY_UPDATED("SFDCActivityUpdated"),
    @XmlEnumValue("SFDCMergeLeads")
    SFDC_MERGE_LEADS("SFDCMergeLeads"),
    @XmlEnumValue("MergeLeads")
    MERGE_LEADS("MergeLeads"),
    @XmlEnumValue("ResolveConflicts")
    RESOLVE_CONFLICTS("ResolveConflicts"),
    @XmlEnumValue("AssocWithOpprtntyInSales")
    ASSOC_WITH_OPPRTNTY_IN_SALES("AssocWithOpprtntyInSales"),
    @XmlEnumValue("DissocFromOpprtntyInSales")
    DISSOC_FROM_OPPRTNTY_IN_SALES("DissocFromOpprtntyInSales"),
    @XmlEnumValue("UpdateOpprtntyInSales")
    UPDATE_OPPRTNTY_IN_SALES("UpdateOpprtntyInSales"),
    @XmlEnumValue("DeleteLead")
    DELETE_LEAD("DeleteLead"),
    @XmlEnumValue("SendAlert")
    SEND_ALERT("SendAlert"),
    @XmlEnumValue("SendSalesEmail")
    SEND_SALES_EMAIL("SendSalesEmail"),
    @XmlEnumValue("OpenSalesEmail")
    OPEN_SALES_EMAIL("OpenSalesEmail"),
    @XmlEnumValue("ClickSalesEmail")
    CLICK_SALES_EMAIL("ClickSalesEmail"),
    @XmlEnumValue("AddtoSFDCCampaign")
    ADDTO_SFDC_CAMPAIGN("AddtoSFDCCampaign"),
    @XmlEnumValue("RemoveFromSFDCCampaign")
    REMOVE_FROM_SFDC_CAMPAIGN("RemoveFromSFDCCampaign"),
    @XmlEnumValue("ChangeStatusInSFDCCampaign")
    CHANGE_STATUS_IN_SFDC_CAMPAIGN("ChangeStatusInSFDCCampaign"),
    @XmlEnumValue("ReceiveSalesEmail")
    RECEIVE_SALES_EMAIL("ReceiveSalesEmail"),
    @XmlEnumValue("InterestingMoment")
    INTERESTING_MOMENT("InterestingMoment"),
    @XmlEnumValue("RequestCampaign")
    REQUEST_CAMPAIGN("RequestCampaign"),
    @XmlEnumValue("SalesEmailBounced")
    SALES_EMAIL_BOUNCED("SalesEmailBounced");
    private final String value;

    ActivityType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ActivityType fromValue(String v) {
        for (ActivityType c: ActivityType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
