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
 *         &lt;element name="ODRNUM" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="REGYN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WORKERID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WORKERNAME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="stcNoKTSvcInfo" type="{http://ip.kt.com/interlocking/Nes_IPM_WS_0007}Nes0007SVCINFO"/>
 *         &lt;element name="stcNoKTAssignedIPList" type="{http://ip.kt.com/interlocking/Nes_IPM_WS_0007}Nes0007AssignedIP" maxOccurs="unbounded"/>
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
    "odrnum",
    "regyn",
    "workerid",
    "workername",
    "stcNoKTSvcInfo",
    "stcNoKTAssignedIPList"
})
@XmlRootElement(name = "Nes0007Request", namespace="http://ip.kt.com/interlocking/Nes_IPM_WS_0007")
public class Nes0007Request {

    @XmlElement(name = "ODRNUM", required = true)
    protected String odrnum;
    @XmlElement(name = "REGYN", required = true)
    protected String regyn;
    @XmlElement(name = "WORKERID", required = true)
    protected String workerid;
    @XmlElement(name = "WORKERNAME", required = true)
    protected String workername;
    @XmlElement(required = true)
    protected Nes0007SVCINFO stcNoKTSvcInfo;
    @XmlElement(required = true)
    protected List<Nes0007AssignedIP> stcNoKTAssignedIPList;

    /**
     * Gets the value of the odrnum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getODRNUM() {
        return odrnum;
    }

    /**
     * Sets the value of the odrnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setODRNUM(String value) {
        this.odrnum = value;
    }

    /**
     * Gets the value of the regyn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGYN() {
        return regyn;
    }

    /**
     * Sets the value of the regyn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGYN(String value) {
        this.regyn = value;
    }

    /**
     * Gets the value of the workerid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWORKERID() {
        return workerid;
    }

    /**
     * Sets the value of the workerid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWORKERID(String value) {
        this.workerid = value;
    }

    /**
     * Gets the value of the workername property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWORKERNAME() {
        return workername;
    }

    /**
     * Sets the value of the workername property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWORKERNAME(String value) {
        this.workername = value;
    }

    /**
     * Gets the value of the stcNoKTSvcInfo property.
     * 
     * @return
     *     possible object is
     *     {@link Nes0007SVCINFO }
     *     
     */
    public Nes0007SVCINFO getStcNoKTSvcInfo() {
        return stcNoKTSvcInfo;
    }

    /**
     * Sets the value of the stcNoKTSvcInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Nes0007SVCINFO }
     *     
     */
    public void setStcNoKTSvcInfo(Nes0007SVCINFO value) {
        this.stcNoKTSvcInfo = value;
    }

    /**
     * Gets the value of the stcNoKTAssignedIPList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stcNoKTAssignedIPList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStcNoKTAssignedIPList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Nes0007AssignedIP }
     * 
     * 
     */
    public List<Nes0007AssignedIP> getStcNoKTAssignedIPList() {
        if (stcNoKTAssignedIPList == null) {
            stcNoKTAssignedIPList = new ArrayList<Nes0007AssignedIP>();
        }
        return this.stcNoKTAssignedIPList;
    }

}
