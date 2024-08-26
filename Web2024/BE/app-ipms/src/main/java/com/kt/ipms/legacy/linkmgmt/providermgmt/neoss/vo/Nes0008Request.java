package com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Said" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RegYN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CUSTNAME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LLNUM" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ICISOfficescode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NodeOfficescode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InstallAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InstallRoadAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="reportopinion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WorkerID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WorkerName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EX_SVC_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SVC_USE_TYPE_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="stcVPNNeInfo" type="{http://ip.kt.com/interlocking/Nes_IPM_WS_0008}Nes0008VPNNeInfo"/>
 *         &lt;element name="stcVPNAssignedIPList" type="{http://ip.kt.com/interlocking/Nes_IPM_WS_0008}Nes0008VPNAssingdIp" maxOccurs="unbounded"/>
 *         &lt;element name="stcVPNHostAssignedIPList" type="{http://ip.kt.com/interlocking/Nes_IPM_WS_0008}Nes0008VPNHostAssignedIPList" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "said",
    "regYN",
    "custname",
    "llnum",
    "icisOfficescode",
    "nodeOfficescode",
    "installAddress",
    "installRoadAddress",
    "reportopinion",
    "workerID",
    "workerName",
    "exsvccd",
    "svcusetypecd",
    "stcVPNNeInfo",
    "stcVPNAssignedIPList",
    "stcVPNHostAssignedIPList"
})
@XmlRootElement(name = "Nes0008Request", namespace="http://ip.kt.com/interlocking/Nes_IPM_WS_0008")
public class Nes0008Request {

    @XmlElement(name = "Said", required = true)
    protected String said;
    @XmlElement(name = "RegYN", required = true)
    protected String regYN;
    @XmlElement(name = "CUSTNAME", required = true)
    protected String custname;
    @XmlElement(name = "LLNUM", required = true)
    protected String llnum;
    @XmlElement(name = "ICISOfficescode", required = true)
    protected String icisOfficescode;
    @XmlElement(name = "NodeOfficescode", required = true)
    protected String nodeOfficescode;
    @XmlElement(name = "InstallAddress", required = true)
    protected String installAddress;
    @XmlElement(name = "InstallRoadAddress", required = true)
    protected String installRoadAddress;
    @XmlElement(required = true)
    protected String reportopinion;
    @XmlElement(name = "WorkerID", required = true)
    protected String workerID;
    @XmlElement(name = "WorkerName", required = true)
    protected String workerName;
    @XmlElement(name = "EX_SVC_CD", required = true)
    protected String exsvccd;
    @XmlElement(name = "SVC_USE_TYPE_CD", required = true)
    protected String svcusetypecd;
    @XmlElement(required = true)
    protected Nes0008VPNNeInfo stcVPNNeInfo;
    @XmlElement(required = true)
    protected List<Nes0008VPNAssingdIp> stcVPNAssignedIPList;
    @XmlElement(required = true)
    protected List<Nes0008VPNHostAssignedIPList> stcVPNHostAssignedIPList;

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
     * Gets the value of the regYN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegYN() {
        return regYN;
    }

    /**
     * Sets the value of the regYN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegYN(String value) {
        this.regYN = value;
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
     * Gets the value of the llnum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLLNUM() {
        return llnum;
    }

    /**
     * Sets the value of the llnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLLNUM(String value) {
        this.llnum = value;
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

    /**
     * Gets the value of the reportopinion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReportopinion() {
        return reportopinion;
    }

    /**
     * Sets the value of the reportopinion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportopinion(String value) {
        this.reportopinion = value;
    }

    /**
     * Gets the value of the workerID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkerID() {
        return workerID;
    }

    /**
     * Sets the value of the workerID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkerID(String value) {
        this.workerID = value;
    }

    /**
     * Gets the value of the workerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkerName() {
        return workerName;
    }

    /**
     * Sets the value of the workerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkerName(String value) {
        this.workerName = value;
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
     * Gets the value of the stcVPNNeInfo property.
     * 
     * @return
     *     possible object is
     *     {@link Nes0008VPNNeInfo }
     *     
     */
    public Nes0008VPNNeInfo getStcVPNNeInfo() {
        return stcVPNNeInfo;
    }

    /**
     * Sets the value of the stcVPNNeInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Nes0008VPNNeInfo }
     *     
     */
    public void setStcVPNNeInfo(Nes0008VPNNeInfo value) {
        this.stcVPNNeInfo = value;
    }

    /**
     * Gets the value of the stcVPNAssignedIPList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stcVPNAssignedIPList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStcVPNAssignedIPList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Nes0008VPNAssingdIp }
     * 
     * 
     */
    public List<Nes0008VPNAssingdIp> getStcVPNAssignedIPList() {
        if (stcVPNAssignedIPList == null) {
            stcVPNAssignedIPList = new ArrayList<Nes0008VPNAssingdIp>();
        }
        return this.stcVPNAssignedIPList;
    }

    /**
     * Gets the value of the stcVPNHostAssignedIPList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stcVPNHostAssignedIPList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStcVPNHostAssignedIPList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Nes0008VPNHostAssignedIPList }
     * 
     * 
     */
    public List<Nes0008VPNHostAssignedIPList> getStcVPNHostAssignedIPList() {
        if (stcVPNHostAssignedIPList == null) {
            stcVPNHostAssignedIPList = new ArrayList<Nes0008VPNHostAssignedIPList>();
        }
        return this.stcVPNHostAssignedIPList;
    }

}
