

package com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Nes0005AssingdIp complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Nes0005AssingdIp">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IP_VERSION_TYPE_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IP_CREATE_TYPE_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IP_ASSIGN_SEQ" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Ip_Block" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IpBitmask" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SIpAddr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EIpAddr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IPMS_SVC_Seq" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Assign_Type_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EX_SVC_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SVC_USE_TYPE_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="GatewayIP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LACPBLOCKYN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Nes0005AssingdIp", propOrder = {
    "ipversiontypecd",
    "ipcreatetypecd",
    "ipassignseq",
    "ipBlock",
    "ipBitmask",
    "sIpAddr",
    "eIpAddr",
    "ipmssvcSeq",
    "assignTypeCD",
    "exsvccd",
    "svcusetypecd",
    "gatewayIP",
    "lacpblockyn"
})
public class Nes0005AssingdIp {

    @XmlElement(name = "IP_VERSION_TYPE_CD", required = true)
    protected String ipversiontypecd;
    @XmlElement(name = "IP_CREATE_TYPE_CD", required = true)
    protected String ipcreatetypecd;
    @XmlElement(name = "IP_ASSIGN_SEQ", required = true)
    protected String ipassignseq;
    @XmlElement(name = "Ip_Block", required = true)
    protected String ipBlock;
    @XmlElement(name = "IpBitmask", required = true)
    protected String ipBitmask;
    @XmlElement(name = "SIpAddr", required = true)
    protected String sIpAddr;
    @XmlElement(name = "EIpAddr", required = true)
    protected String eIpAddr;
    @XmlElement(name = "IPMS_SVC_Seq", required = true)
    protected String ipmssvcSeq;
    @XmlElement(name = "Assign_Type_CD", required = true)
    protected String assignTypeCD;
    @XmlElement(name = "EX_SVC_CD", required = true)
    protected String exsvccd;
    @XmlElement(name = "SVC_USE_TYPE_CD", required = true)
    protected String svcusetypecd;
    @XmlElement(name = "GatewayIP", required = true)
    protected String gatewayIP;
    @XmlElement(name = "LACPBLOCKYN", required = true)
    protected String lacpblockyn;

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
     * Gets the value of the lacpblockyn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLACPBLOCKYN() {
        return lacpblockyn;
    }

    /**
     * Sets the value of the lacpblockyn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLACPBLOCKYN(String value) {
        this.lacpblockyn = value;
    }

}
