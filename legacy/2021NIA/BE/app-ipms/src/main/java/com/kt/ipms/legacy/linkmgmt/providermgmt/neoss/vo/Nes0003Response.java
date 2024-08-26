

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
 *         &lt;element name="stcIpSuggestList" type="{http://ip.kt.com/interlocking/Nes_IPM_WS_0003}Nes0003IpSuggestList" maxOccurs="unbounded" minOccurs="0"/>
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
    "stcIpSuggestList"
})
@XmlRootElement(name = "Nes0003Response", namespace="http://ip.kt.com/interlocking/Nes_IPM_WS_0003")
public class Nes0003Response {

    @XmlElement(name = "Result_CD", required = true)
    protected String resultCD;
    @XmlElement(name = "Result_MSG")
    protected String resultMSG;
    @XmlElement(nillable = true)
    protected List<Nes0003IpSuggestList> stcIpSuggestList;

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
     * Gets the value of the stcIpSuggestList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stcIpSuggestList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStcIpSuggestList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Nes0003IpSuggestList }
     * 
     * 
     */
    public List<Nes0003IpSuggestList> getStcIpSuggestList() {
        if (stcIpSuggestList == null) {
            stcIpSuggestList = new ArrayList<Nes0003IpSuggestList>();
        }
        return this.stcIpSuggestList;
    }

}
