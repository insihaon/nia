package com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "officescode"
    , "assignTypeCD"
    , "svcusetypecd"
    , "ipversiontypecd"
    , "ipcreatetypecd"
    , "SVC_LINE_TYPE_CD"
    , "ipaddress"
    , "ipBitmask"
    , "limit"
})

@XmlRootElement(name = "Sdn0001Request", namespace="http://ip.kt.com/interlocking/SDN_IPM_WS_0001")
public class Sdn0001Request {

	
    @XmlElement(name = "Officescode", required = true)
    protected String officescode;
    @XmlElement(name = "Assign_Type_CD", required = true)
    protected String assignTypeCD;
    @XmlElement(name = "SVC_USE_TYPE_CD", required = true)
    protected String svcusetypecd;
    @XmlElement(name = "IP_VERSION_TYPE_CD", required = true)
    protected String ipversiontypecd;
    @XmlElement(name = "IP_CREATE_TYPE_CD", required = true)
    protected String ipcreatetypecd;
    @XmlElement(name = "SVC_LINE_TYPE_CD", required = true)
    protected String SVC_LINE_TYPE_CD;
    @XmlElement(name = "IPADDRESS")
    protected String ipaddress;
    @XmlElement(name = "IpBitmask")
    protected String ipBitmask;
    @XmlElement(name = "Limit")
    protected String limit;
    
  

    /**
     * Gets the value of the officescode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOfficescode() {
        return officescode;
    }

    /**
     * Sets the value of the officescode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOfficescode(String value) {
        this.officescode = value;
    }

    /**
     * Gets the value of the assignTypeCD property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssignTypeCD() {
        return assignTypeCD;
    }

    /**
     * Sets the value of the assignTypeCD property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssignTypeCD(String value) {
        this.assignTypeCD = value;
    }

    /**
     * Gets the value of the svcusetypecd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSVCUSETYPECD() {
        return svcusetypecd;
    }

    /**
     * Sets the value of the svcusetypecd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSVCUSETYPECD(String value) {
        this.svcusetypecd = value;
    }

    /**
     * Gets the value of the ipversiontypecd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPVERSIONTYPECD() {
        return ipversiontypecd;
    }

    /**
     * Sets the value of the ipversiontypecd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPVERSIONTYPECD(String value) {
        this.ipversiontypecd = value;
    }

    /**
     * Gets the value of the ipcreatetypecd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPCREATETYPECD() {
        return ipcreatetypecd;
    }

    /**
     * Sets the value of the ipcreatetypecd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPCREATETYPECD(String value) {
        this.ipcreatetypecd = value;
    }

    /**
     * Gets the value of the svcLineTypeCd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSvcLineTypeCd() {
        return SVC_LINE_TYPE_CD;
    }

    /**
     * Sets the value of the svcLineTypeCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSvcLineTypeCd(String value) {
        this.SVC_LINE_TYPE_CD = value;
    }

    /**
     * Gets the value of the ipaddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPADDRESS() {
        return ipaddress;
    }

    /**
     * Sets the value of the ipaddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPADDRESS(String value) {
        this.ipaddress = value;
    }
    
    /**
     * Gets the value of the ipBitmask property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpBitmask() {
        return ipBitmask;
    }

    /**
     * Sets the value of the ipBitmask property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpBitmask(String value) {
        this.ipBitmask = value;
    }
    
    /**
     * Gets the value of the limit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLimit() {
        return limit;
    }

    /**
     * Sets the value of the limit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLimit(String value) {
        this.limit = value;
    }

}
