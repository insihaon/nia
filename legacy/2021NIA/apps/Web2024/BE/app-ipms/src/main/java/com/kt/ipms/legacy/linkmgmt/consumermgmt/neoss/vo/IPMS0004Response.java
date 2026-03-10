package com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IPMS0004Response complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IPMS0004Response">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Result_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Result_MSG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="svcInfo" type="{http://tempuri.org/}IPMS0004SVCInfo"/>
 *         &lt;element name="neInfo" type="{http://tempuri.org/}stcNeInfo"/>
 *         &lt;element name="ipList" type="{http://tempuri.org/}ArrayOfStcAssignedIPList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IPMS0004Response", propOrder = {
    "resultCD",
    "resultMSG",
    "svcInfo",
    "neInfo",
    "ipList"
})
public class IPMS0004Response {

    @XmlElement(name = "Result_CD")
    protected String resultCD;
    @XmlElement(name = "Result_MSG")
    protected String resultMSG;
    @XmlElement(required = true)
    protected IPMS0004SVCInfo svcInfo;
    @XmlElement(required = true)
    protected StcNeInfo neInfo;
    protected ArrayOfStcAssignedIPList ipList;

    /**
     * Gets the value of the resultCD property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultCD() {
        return resultCD;
    }

    /**
     * Sets the value of the resultCD property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultCD(String value) {
        this.resultCD = value;
    }

    /**
     * Gets the value of the resultMSG property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultMSG() {
        return resultMSG;
    }

    /**
     * Sets the value of the resultMSG property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultMSG(String value) {
        this.resultMSG = value;
    }

    /**
     * Gets the value of the svcInfo property.
     * 
     * @return
     *     possible object is
     *     {@link IPMS0004SVCInfo }
     *     
     */
    public IPMS0004SVCInfo getSvcInfo() {
        return svcInfo;
    }

    /**
     * Sets the value of the svcInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPMS0004SVCInfo }
     *     
     */
    public void setSvcInfo(IPMS0004SVCInfo value) {
        this.svcInfo = value;
    }

    /**
     * Gets the value of the neInfo property.
     * 
     * @return
     *     possible object is
     *     {@link StcNeInfo }
     *     
     */
    public StcNeInfo getNeInfo() {
        return neInfo;
    }

    /**
     * Sets the value of the neInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link StcNeInfo }
     *     
     */
    public void setNeInfo(StcNeInfo value) {
        this.neInfo = value;
    }

    /**
     * Gets the value of the ipList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfStcAssignedIPList }
     *     
     */
    public ArrayOfStcAssignedIPList getIpList() {
        return ipList;
    }

    /**
     * Sets the value of the ipList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfStcAssignedIPList }
     *     
     */
    public void setIpList(ArrayOfStcAssignedIPList value) {
        this.ipList = value;
    }

}
