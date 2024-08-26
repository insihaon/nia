package com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Sdn0003AssignedIP", propOrder = {
    "assignTypeCD"
    , "ipversiontypecd"
    , "ipcreatetypecd"
    , "ipBlock"
    , "ipBitmask"
    , "sIpAddr"
    , "eIpAddr"
    , "gatewayIP"
    , "svclinetypecd"
})

public class Sdn0003AssignedIP {
    @XmlElement(name = "Assign_Type_CD", required = true)
    protected String assignTypeCD;
    @XmlElement(name = "IP_VERSION_TYPE_CD", required = true)
    protected String ipversiontypecd;
    @XmlElement(name = "IP_CREATE_TYPE_CD", required = true)
    protected String ipcreatetypecd;
    @XmlElement(name = "Ip_Block", required = true)
    protected String ipBlock;
    @XmlElement(name = "IpBitmask", required = true)
    protected String ipBitmask;
    @XmlElement(name = "SIpAddr", required = true)
    protected String sIpAddr;
    @XmlElement(name = "EIpAddr", required = true)
    protected String eIpAddr;
    @XmlElement(name = "GatewayIP", required = true)
    protected String gatewayIP;
    @XmlElement(name = "SVC_LINE_TYPE_CD", required = true)
    protected String svclinetypecd;
  

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
     * Gets the value of the ipBlock property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpBlock() {
        return ipBlock;
    }

    /**
     * Sets the value of the ipBlock property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpBlock(String value) {
        this.ipBlock = value;
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
     * Gets the value of the sIpAddr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIpAddr() {
        return sIpAddr;
    }

    /**
     * Sets the value of the sIpAddr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIpAddr(String value) {
        this.sIpAddr = value;
    }

    /**
     * Gets the value of the eIpAddr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEIpAddr() {
        return eIpAddr;
    }

    /**
     * Sets the value of the eIpAddr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEIpAddr(String value) {
        this.eIpAddr = value;
    }

    /**
     * Gets the value of the gatewayIP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGatewayIP() {
        return gatewayIP;
    }

    /**
     * Sets the value of the gatewayIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGatewayIP(String value) {
        this.gatewayIP = value;
    }

    /**
     * Gets the value of the svclinetypecd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSVCLINETYPECD() {
        return svclinetypecd;
    }

    /**
     * Sets the value of the svclinetypecd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSVCLINETYPECD(String value) {
        this.svclinetypecd = value;
    }

}
