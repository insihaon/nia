

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
 *         &lt;element name="Result_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Result_MSG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stcIPMSSVCInfo" type="{http://ip.kt.com/interlocking/Nes_IPM_WS_0001}Nes0001IPMSSVCInfo"/>
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
    "resultCD",
    "resultMSG",
    "stcIPMSSVCInfoList"
})
@XmlRootElement(name = "Nes0001Response", namespace="http://ip.kt.com/interlocking/Nes_IPM_WS_0001")
public class Nes0001Response {

    @XmlElement(name = "Result_CD", required = true)
    protected String resultCD;
    @XmlElement(name = "Result_MSG")
    protected String resultMSG;
    @XmlElement(nillable = true)
    protected List<Nes0001IPMSSVCInfo> stcIPMSSVCInfoList;

    /**
     * Gets the value of the resultCD property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultCD() {
        return resultCD;
    }

    /**
     * Sets the value of the resultCD property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultCD(String value) {
        this.resultCD = value;
    }

    /**
     * Gets the value of the resultMSG property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultMSG() {
        return resultMSG;
    }

    /**
     * Sets the value of the resultMSG property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultMSG(String value) {
        this.resultMSG = value;
    }

    /**
     * Gets the value of the stcIPMSSVCInfoList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stcIPMSSVCInfoList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStcIPMSSVCInfoList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Nes0001IPMSSVCInfo }
     * 
     * 
     */
    public List<Nes0001IPMSSVCInfo> getStcIPMSSVCInfoList() {
        if (stcIPMSSVCInfoList == null) {
            stcIPMSSVCInfoList = new ArrayList<Nes0001IPMSSVCInfo>();
        }
        return this.stcIPMSSVCInfoList;
    }
    public void setStcIPMSSVCInfoList(List<Nes0001IPMSSVCInfo> nes0001IPMSSVCInfoList){
    	this.stcIPMSSVCInfoList = nes0001IPMSSVCInfoList;
    }
}
