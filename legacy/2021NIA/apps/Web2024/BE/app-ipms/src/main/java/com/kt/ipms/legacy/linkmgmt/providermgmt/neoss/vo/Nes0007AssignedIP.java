package com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Nes0007AssignedIP complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Nes0007AssignedIP">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IP_VERSION_TYPE_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IP_CREATE_TYPE_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IP_BLOCK" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IPBITMASK" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Nes0007AssignedIP", propOrder = {
    "ipversiontypecd",
    "ipcreatetypecd",
    "ipblock",
    "ipbitmask"
})
public class Nes0007AssignedIP {

    @XmlElement(name = "IP_VERSION_TYPE_CD", required = true)
    protected String ipversiontypecd;
    @XmlElement(name = "IP_CREATE_TYPE_CD", required = true)
    protected String ipcreatetypecd;
    @XmlElement(name = "IP_BLOCK", required = true)
    protected String ipblock;
    @XmlElement(name = "IPBITMASK", required = true)
    protected String ipbitmask;

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
     * Gets the value of the ipbitmask property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPBITMASK() {
        return ipbitmask;
    }

    /**
     * Sets the value of the ipbitmask property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPBITMASK(String value) {
        this.ipbitmask = value;
    }

}
