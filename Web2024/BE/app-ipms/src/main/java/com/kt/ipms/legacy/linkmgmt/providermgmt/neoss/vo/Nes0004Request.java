

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
 *         &lt;element name="stcSVCInfo" type="{http://ip.kt.com/interlocking/Nes_IPM_WS_0004}Nes0004SVCInfo"/>
 *         &lt;element name="ReservedIPList" type="{http://ip.kt.com/interlocking/Nes_IPM_WS_0004}Nes0004ReservedIP" maxOccurs="unbounded"/>
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
    "stcSVCInfo",
    "reservedIPList"
})
@XmlRootElement(name = "Nes0004Request", namespace="http://ip.kt.com/interlocking/Nes_IPM_WS_0004")
public class Nes0004Request {

    @XmlElement(name = "OdrNum", required = true)
    protected String odrNum;
    @XmlElement(name = "RegYN", required = true)
    protected String regYN;
    @XmlElement(required = true)
    protected Nes0004SVCInfo stcSVCInfo;
    @XmlElement(name = "ReservedIPList", required = true)
    protected List<Nes0004ReservedIP> reservedIPList;

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
     * Gets the value of the stcSVCInfo property.
     * 
     * @return
     *     possible object is
     *     {@link Nes0004SVCInfo }
     *     
     */
    public Nes0004SVCInfo getStcSVCInfo() {
        return stcSVCInfo;
    }

    /**
     * Sets the value of the stcSVCInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Nes0004SVCInfo }
     *     
     */
    public void setStcSVCInfo(Nes0004SVCInfo value) {
        this.stcSVCInfo = value;
    }

    /**
     * Gets the value of the reservedIPList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reservedIPList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReservedIPList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Nes0004ReservedIP }
     * 
     * 
     */
    public List<Nes0004ReservedIP> getReservedIPList() {
        if (reservedIPList == null) {
            reservedIPList = new ArrayList<Nes0004ReservedIP>();
        }
        return this.reservedIPList;
    }

}
