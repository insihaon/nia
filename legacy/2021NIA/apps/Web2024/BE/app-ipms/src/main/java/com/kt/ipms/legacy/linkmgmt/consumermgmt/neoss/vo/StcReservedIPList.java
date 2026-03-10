package com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for stcReservedIPList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="stcReservedIPList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IPMS_SVC_Seq" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Assign_Type_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EX_SVC_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SVC_USE_TYPE_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IP_VERSION_TYPE_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IP_CREATE_TYPE_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Ip_Block" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IPBitmask" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIpAddr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EIpAddr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IP_ASSIGN_SEQ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GatewayIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LACPBLOCKYN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SVC_LINE_TYPE_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stcReservedIPList", propOrder = {
    "ipmssvcSeq",
    "assignTypeCD",
    "exsvccd",
    "svcusetypecd",
    "ipversiontypecd",
    "ipcreatetypecd",
    "ipBlock",
    "ipBitmask",
    "sIpAddr",
    "eIpAddr",
    "ipassignseq",
    "gatewayIP",
    "lacpblockyn",
    "svclinetypecd"
    
})
public class StcReservedIPList {

    @XmlElement(name = "IPMS_SVC_Seq")
    protected String ipmssvcSeq;
    @XmlElement(name = "Assign_Type_CD")
    protected String assignTypeCD;
    @XmlElement(name = "EX_SVC_CD")
    protected String exsvccd;
    @XmlElement(name = "SVC_USE_TYPE_CD")
    protected String svcusetypecd;
    @XmlElement(name = "IP_VERSION_TYPE_CD")
    protected String ipversiontypecd;
    @XmlElement(name = "IP_CREATE_TYPE_CD")
    protected String ipcreatetypecd;
    @XmlElement(name = "Ip_Block")
    protected String ipBlock;
    @XmlElement(name = "IPBitmask")
    protected String ipBitmask;
    @XmlElement(name = "SIpAddr")
    protected String sIpAddr;
    @XmlElement(name = "EIpAddr")
    protected String eIpAddr;
    @XmlElement(name = "IP_ASSIGN_SEQ")
    protected String ipassignseq;
    @XmlElement(name = "GatewayIP")
    protected String gatewayIP;
    @XmlElement(name = "LACPBLOCKYN")
    protected String lacpblockyn;
    @XmlElement(name = "SVC_LINE_TYPE_CD")
    protected String svclinetypecd;

    
    public String getLACPBLOCKYN(){
    	return lacpblockyn;
    }
    
    public void setLACPBLOCKYN(String value){
    	this.lacpblockyn = value;
    }
    
    public String getSVCLINETYPECD(){
    	return svclinetypecd;
    }
    
    public void setSVCLINETYPECD(String value){
    	this.svclinetypecd = value;
    }
    
    /**
     * Gets the value of the ipmssvcSeq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPMSSVCSeq() {
        return ipmssvcSeq;
    }

    /**
     * Sets the value of the ipmssvcSeq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPMSSVCSeq(String value) {
        this.ipmssvcSeq = value;
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
     * Gets the value of the exsvccd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEXSVCCD() {
        return exsvccd;
    }

    /**
     * Sets the value of the exsvccd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEXSVCCD(String value) {
        this.exsvccd = value;
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
    public String getIPBitmask() {
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
    public void setIPBitmask(String value) {
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
     * Gets the value of the ipassignseq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPASSIGNSEQ() {
        return ipassignseq;
    }

    /**
     * Sets the value of the ipassignseq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPASSIGNSEQ(String value) {
        this.ipassignseq = value;
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

}
