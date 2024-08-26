package com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Nes0005SVCInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Nes0005SVCInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Said" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LLNum" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SPEEDDESC" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ICISOfficescode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NodeOfficescode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LACPSaid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InstallAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InstallRoadAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Nes0005SVCInfo", propOrder = {
    "said",
    "llNum",
    "speeddesc",
    "icisOfficescode",
    "nodeOfficescode",
    "lacpSaid",
    "installAddress",
    "installRoadAddress"
})
public class Nes0005SVCInfo {

    @XmlElement(name = "Said", required = true)
    protected String said;
    @XmlElement(name = "LLNum", required = true)
    protected String llNum;
    @XmlElement(name = "SPEEDDESC", required = true)
    protected String speeddesc;
    @XmlElement(name = "ICISOfficescode", required = true)
    protected String icisOfficescode;
    @XmlElement(name = "NodeOfficescode", required = true)
    protected String nodeOfficescode;
    @XmlElement(name = "LACPSaid", required = true)
    protected String lacpSaid;
    @XmlElement(name = "InstallAddress", required = true)
    protected String installAddress;
    @XmlElement(name = "InstallRoadAddress", required = true)
    protected String installRoadAddress;

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
     * Gets the value of the speeddesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEEDDESC() {
        return speeddesc;
    }

    /**
     * Sets the value of the speeddesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEEDDESC(String value) {
        this.speeddesc = value;
    }

    /**
     * Gets the value of the icisOfficescode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getICISOfficescode() {
        return icisOfficescode;
    }

    /**
     * Sets the value of the icisOfficescode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setICISOfficescode(String value) {
        this.icisOfficescode = value;
    }

    /**
     * Gets the value of the nodeOfficescode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeOfficescode() {
        return nodeOfficescode;
    }

    /**
     * Sets the value of the nodeOfficescode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeOfficescode(String value) {
        this.nodeOfficescode = value;
    }

    /**
     * Gets the value of the lacpSaid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLACPSaid() {
        return lacpSaid;
    }

    /**
     * Sets the value of the lacpSaid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLACPSaid(String value) {
        this.lacpSaid = value;
    }

    /**
     * Gets the value of the installAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstallAddress() {
        return installAddress;
    }

    /**
     * Sets the value of the installAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstallAddress(String value) {
        this.installAddress = value;
    }

    /**
     * Gets the value of the installRoadAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstallRoadAddress() {
        return installRoadAddress;
    }

    /**
     * Sets the value of the installRoadAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstallRoadAddress(String value) {
        this.installRoadAddress = value;
    }

}
