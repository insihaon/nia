
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
 *         &lt;element name="OdrNum" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RegYN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WorkerID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WorkerName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Odr_close_Type_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="stcSVCInfo" type="{http://ip.kt.com/interlocking/Nes_IPM_WS_0005}Nes0005SVCInfo"/>
 *         &lt;element name="stcNeInfo" type="{http://ip.kt.com/interlocking/Nes_IPM_WS_0005}Nes0005NeInfo"/>
 *         &lt;element name="stcAssignedIPList" type="{http://ip.kt.com/interlocking/Nes_IPM_WS_0005}Nes0005AssingdIp" maxOccurs="unbounded"/>
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
    "odrNum",
    "regYN",
    "workerID",
    "workerName",
    "odrCloseTypeCD",
    "stcSVCInfo",
    "stcNeInfo",
    "stcAssignedIPList"
})
@XmlRootElement(name = "Nes0005Request", namespace="http://ip.kt.com/interlocking/Nes_IPM_WS_0005")
public class Nes0005Request {

    @XmlElement(name = "OdrNum", required = true)
    protected String odrNum;
    @XmlElement(name = "RegYN", required = true)
    protected String regYN;
    @XmlElement(name = "WorkerID", required = true)
    protected String workerID;
    @XmlElement(name = "WorkerName", required = true)
    protected String workerName;
    @XmlElement(name = "Odr_Close_Type_CD", required = true)
    protected String odrCloseTypeCD;
    @XmlElement(required = true)
    protected Nes0005SVCInfo stcSVCInfo;
    @XmlElement(required = true)
    protected Nes0005NeInfo stcNeInfo;
    @XmlElement(required = true)
    protected List<Nes0005AssingdIp> stcAssignedIPList;

    /**
     * Gets the value of the odrNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOdrNum() {
        return odrNum;
    }

    /**
     * Sets the value of the odrNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOdrNum(String value) {
        this.odrNum = value;
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
     * Gets the value of the odrCloseTypeCD property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOdrCloseTypeCD() {
        return odrCloseTypeCD;
    }

    /**
     * Sets the value of the odrCloseTypeCD property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOdrCloseTypeCD(String value) {
        this.odrCloseTypeCD = value;
    }

    /**
     * Gets the value of the stcSVCInfo property.
     * 
     * @return
     *     possible object is
     *     {@link Nes0005SVCInfo }
     *     
     */
    public Nes0005SVCInfo getStcSVCInfo() {
        return stcSVCInfo;
    }

    /**
     * Sets the value of the stcSVCInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Nes0005SVCInfo }
     *     
     */
    public void setStcSVCInfo(Nes0005SVCInfo value) {
        this.stcSVCInfo = value;
    }

    /**
     * Gets the value of the stcNeInfo property.
     * 
     * @return
     *     possible object is
     *     {@link Nes0005NeInfo }
     *     
     */
    public Nes0005NeInfo getStcNeInfo() {
        return stcNeInfo;
    }

    /**
     * Sets the value of the stcNeInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Nes0005NeInfo }
     *     
     */
    public void setStcNeInfo(Nes0005NeInfo value) {
        this.stcNeInfo = value;
    }

    /**
     * Gets the value of the stcAssignedIPList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stcAssignedIPList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStcAssignedIPList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Nes0005AssingdIp }
     * 
     * 
     */
    public List<Nes0005AssingdIp> getStcAssignedIPList() {
        if (stcAssignedIPList == null) {
            stcAssignedIPList = new ArrayList<Nes0005AssingdIp>();
        }
        return this.stcAssignedIPList;
    }

}
