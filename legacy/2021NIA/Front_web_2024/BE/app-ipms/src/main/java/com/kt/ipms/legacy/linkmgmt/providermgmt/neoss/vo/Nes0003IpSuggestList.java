

package com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Nes0003IpSuggestList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Nes0003IpSuggestList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Assign_Type_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IP_VERSION_TYPE_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IP_CREATE_TYPE_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IP_ASSIGN_SEQ" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IP_BLOCK" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IpBitmask" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SIpAddr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EIpAddr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AssignDT" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Priority" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Nes0003IpSuggestList", propOrder = {
    "assignTypeCD",
    "ipversiontypecd",
    "ipcreatetypecd",
    "ipassignseq",
    "ipblock",
    "ipBitmask",
    "sIpAddr",
    "eIpAddr",
    "assignDT",
    "priority"
})
public class Nes0003IpSuggestList {

    @XmlElement(name = "Assign_Type_CD", required = true)
    protected String assignTypeCD;
    @XmlElement(name = "IP_VERSION_TYPE_CD", required = true)
    protected String ipversiontypecd;
    @XmlElement(name = "IP_CREATE_TYPE_CD", required = true)
    protected String ipcreatetypecd;
    @XmlElement(name = "IP_ASSIGN_SEQ", required = true)
    protected String ipassignseq;
    @XmlElement(name = "IP_BLOCK", required = true)
    protected String ipblock;
    @XmlElement(name = "IpBitmask", required = true)
    protected String ipBitmask;
    @XmlElement(name = "SIpAddr", required = true)
    protected String sIpAddr;
    @XmlElement(name = "EIpAddr", required = true)
    protected String eIpAddr;
    @XmlElement(name = "AssignDT", required = true)
    protected String assignDT;
    @XmlElement(name = "Priority", required = true)
    protected String priority;

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
     * Gets the value of the ipblock property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPBLOCK() {
        return ipblock;
    }

    /**
     * Sets the value of the ipblock property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPBLOCK(String value) {
        this.ipblock = value;
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
     * Gets the value of the assignDT property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssignDT() {
        return assignDT;
    }

    /**
     * Sets the value of the assignDT property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssignDT(String value) {
        this.assignDT = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriority(String value) {
        this.priority = value;
    }

}
