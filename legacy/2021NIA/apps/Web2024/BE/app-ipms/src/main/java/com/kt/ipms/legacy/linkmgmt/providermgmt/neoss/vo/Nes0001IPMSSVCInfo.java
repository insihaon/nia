
package com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Nes0001IPMSSVCInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Nes0001IPMSSVCInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IPMS_SVC_Seq" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Assign_Type_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Assign_Type_NM" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EX_SVC_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SVC_USE_TYPE_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Nes0001IPMSSVCInfo", propOrder = {
    "ipmssvcSeq",
    "assignTypeCD",
    "assignTypeNM",
    "exsvccd",
    "svcusetypecd",
    "usetypecd"
})
public class Nes0001IPMSSVCInfo {

    @XmlElement(name = "IPMS_SVC_Seq", required = true)
    protected String ipmssvcSeq;
    @XmlElement(name = "Assign_Type_CD", required = true)
    protected String assignTypeCD;
    @XmlElement(name = "Assign_Type_NM", required = true)
    protected String assignTypeNM;
    @XmlElement(name = "EX_SVC_CD", required = true)
    protected String exsvccd;
    @XmlElement(name = "SVC_USE_TYPE_CD", required = true)
    protected String svcusetypecd;
    @XmlElement(name = "USE_TYPE_CD", required = true)
    protected String usetypecd;
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
     * Gets the value of the assignTypeNM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssignTypeNM() {
        return assignTypeNM;
    }

    /**
     * Sets the value of the assignTypeNM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssignTypeNM(String value) {
        this.assignTypeNM = value;
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

    public void setUSETYPECD(String value) {
		this.usetypecd = value;
	}
    
	public String getUSETYPECD() {
		return usetypecd;
	}
}
