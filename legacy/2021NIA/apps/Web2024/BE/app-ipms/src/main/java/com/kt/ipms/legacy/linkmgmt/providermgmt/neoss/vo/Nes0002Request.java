

package com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo;

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
 *         &lt;element name="OdrRegDT" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OdrHopeDT" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="custname" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="reportopinion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="stcSVCInfo" type="{http://ip.kt.com/interlocking/Nes_IPM_WS_0002}Nes0002SVCInfo"/>
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
    "odrRegDT",
    "odrHopeDT",
    "custname",
    "reportopinion",
    "stcSVCInfo"
})
@XmlRootElement(name = "Nes0002Request", namespace="http://ip.kt.com/interlocking/Nes_IPM_WS_0002")
public class Nes0002Request {

    @XmlElement(name = "OdrNum", required = true)
    protected String odrNum;
    @XmlElement(name = "OdrRegDT", required = true)
    protected String odrRegDT;
    @XmlElement(name = "OdrHopeDT", required = true)
    protected String odrHopeDT;
    @XmlElement(required = true)
    protected String custname;
    @XmlElement(required = true)
    protected String reportopinion;
    @XmlElement(required = true)
    protected Nes0002SVCInfo stcSVCInfo;

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
     * Gets the value of the odrRegDT property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOdrRegDT() {
        return odrRegDT;
    }

    /**
     * Sets the value of the odrRegDT property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOdrRegDT(String value) {
        this.odrRegDT = value;
    }

    /**
     * Gets the value of the odrHopeDT property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOdrHopeDT() {
        return odrHopeDT;
    }

    /**
     * Sets the value of the odrHopeDT property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOdrHopeDT(String value) {
        this.odrHopeDT = value;
    }

    /**
     * Gets the value of the custname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustname() {
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
    public void setCustname(String value) {
        this.custname = value;
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
     * Gets the value of the stcSVCInfo property.
     * 
     * @return
     *     possible object is
     *     {@link Nes0002SVCInfo }
     *     
     */
    public Nes0002SVCInfo getStcSVCInfo() {
        return stcSVCInfo;
    }

    /**
     * Sets the value of the stcSVCInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Nes0002SVCInfo }
     *     
     */
    public void setStcSVCInfo(Nes0002SVCInfo value) {
        this.stcSVCInfo = value;
    }

}
