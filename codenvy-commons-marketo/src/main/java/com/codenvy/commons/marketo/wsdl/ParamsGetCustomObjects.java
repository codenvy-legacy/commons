
package com.codenvy.commons.marketo.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParamsGetCustomObjects complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParamsGetCustomObjects">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="objTypeName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="streamPosition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="batchSize" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="customObjKeyList" type="{http://www.marketo.com/mktows/}ArrayOfAttribute" minOccurs="0"/>
 *         &lt;element name="includeAttributes" type="{http://www.marketo.com/mktows/}ArrayOfString"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParamsGetCustomObjects", propOrder = {
    "objTypeName",
    "streamPosition",
    "batchSize",
    "customObjKeyList",
    "includeAttributes"
})
public class ParamsGetCustomObjects {

    @XmlElement(required = true)
    protected String objTypeName;
    @XmlElement(nillable = true)
    protected String streamPosition;
    @XmlElement(nillable = true)
    protected Integer batchSize;
    @XmlElement(nillable = true)
    protected ArrayOfAttribute customObjKeyList;
    @XmlElement(required = true)
    protected ArrayOfString includeAttributes;

    /**
     * Gets the value of the objTypeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjTypeName() {
        return objTypeName;
    }

    /**
     * Sets the value of the objTypeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjTypeName(String value) {
        this.objTypeName = value;
    }

    /**
     * Gets the value of the streamPosition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStreamPosition() {
        return streamPosition;
    }

    /**
     * Sets the value of the streamPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStreamPosition(String value) {
        this.streamPosition = value;
    }

    /**
     * Gets the value of the batchSize property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBatchSize() {
        return batchSize;
    }

    /**
     * Sets the value of the batchSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBatchSize(Integer value) {
        this.batchSize = value;
    }

    /**
     * Gets the value of the customObjKeyList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAttribute }
     *     
     */
    public ArrayOfAttribute getCustomObjKeyList() {
        return customObjKeyList;
    }

    /**
     * Sets the value of the customObjKeyList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAttribute }
     *     
     */
    public void setCustomObjKeyList(ArrayOfAttribute value) {
        this.customObjKeyList = value;
    }

    /**
     * Gets the value of the includeAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getIncludeAttributes() {
        return includeAttributes;
    }

    /**
     * Sets the value of the includeAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setIncludeAttributes(ArrayOfString value) {
        this.includeAttributes = value;
    }

}
