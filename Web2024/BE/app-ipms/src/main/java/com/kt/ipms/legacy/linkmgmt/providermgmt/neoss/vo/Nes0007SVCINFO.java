package com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Nes0007SVCINFO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Nes0007SVCINFO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Said" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LLNum" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CUSTNAME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IPMS_SVC_SEQ" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SVC_USE_TYPE_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EX_SVC_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="INSTALLADDRESS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="INSTALLROADADDRESS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ICISOFFICESCODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NODEOFFICESCODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BGPENCUSTNAME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ASNUM" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Nes0007SVCINFO", propOrder = {
    "said",
    "llNum",
    "custname",
    "ipmssvcseq",
    "svcusetypecd",
    "exsvccd",
    "installaddress",
    "installroadaddress",
    "icisofficescode",
    "nodeofficescode",
    "bgpencustname",
    "asnum"
})
public class Nes0007SVCINFO {

    @XmlElement(name = "Said", required = true)
    protected String said;
    @XmlElement(name = "LLNum", required = true)
    protected String llNum;
    @XmlElement(name = "CUSTNAME", required = true)
    protected String custname;
    @XmlElement(name = "IPMS_SVC_SEQ", required = true)
    protected String ipmssvcseq;
    @XmlElement(name = "SVC_USE_TYPE_CD", required = true)
    protected String svcusetypecd;
    @XmlElement(name = "EX_SVC_CD", required = true)
    protected String exsvccd;
    @XmlElement(name = "INSTALLADDRESS", required = true)
    protected String installaddress;
    @XmlElement(name = "INSTALLROADADDRESS", required = true)
    protected String installroadaddress;
    @XmlElement(name = "ICISOFFICESCODE", required = true)
    protected String icisofficescode;
    @XmlElement(name = "NODEOFFICESCODE", required = true)
    protected String nodeofficescode;
    @XmlElement(name = "BGPENCUSTNAME", required = true)
    protected String bgpencustname;
    @XmlElement(name = "ASNUM", required = true)
    protected String asnum;

    /**
     * Gets the value of the said property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaid() {
        return said;
    }

    /**
     * Sets the value of the said property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaid(String value) {
        this.said = value;
    }

    /**
     * Gets the value of the llNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLLNum() {
        return llNum;
    }

    /**
     * Sets the value of the llNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLLNum(String value) {
        this.llNum = value;
    }

    /**
     * Gets the value of the custname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUSTNAME() {
        return custname;
    }

    /**
     * Sets the value of the custname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUSTNAME(String value) {
        this.custname = value;
    }

    /**
     * Gets the value of the ipmssvcseq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPMSSVCSEQ() {
        return ipmssvcseq;
    }

    /**
     * Sets the value of the ipmssvcseq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPMSSVCSEQ(String value) {
        this.ipmssvcseq = value;
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
     * Gets the value of the installaddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSTALLADDRESS() {
        return installaddress;
    }

    /**
     * Sets the value of the installaddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSTALLADDRESS(String value) {
        this.installaddress = value;
    }

    /**
     * Gets the value of the installroadaddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSTALLROADADDRESS() {
        return installroadaddress;
    }

    /**
     * Sets the value of the installroadaddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSTALLROADADDRESS(String value) {
        this.installroadaddress = value;
    }

    /**
     * Gets the value of the icisofficescode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getICISOFFICESCODE() {
        return icisofficescode;
    }

    /**
     * Sets the value of the icisofficescode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setICISOFFICESCODE(String value) {
        this.icisofficescode = value;
    }

    /**
     * Gets the value of the nodeofficescode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNODEOFFICESCODE() {
        return nodeofficescode;
    }

    /**
     * Sets the value of the nodeofficescode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNODEOFFICESCODE(String value) {
        this.nodeofficescode = value;
    }

    /**
     * Gets the value of the bgpencustname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBGPENCUSTNAME() {
        return bgpencustname;
    }

    /**
     * Sets the value of the bgpencustname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBGPENCUSTNAME(String value) {
        this.bgpencustname = value;
    }

    /**
     * Gets the value of the asnum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getASNUM() {
        return asnum;
    }

    /**
     * Sets the value of the asnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setASNUM(String value) {
        this.asnum = value;
    }

}
