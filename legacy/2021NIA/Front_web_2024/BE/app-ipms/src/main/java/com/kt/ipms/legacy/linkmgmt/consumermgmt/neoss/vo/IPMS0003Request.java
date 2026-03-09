package com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IPMS0003Request complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IPMS0003Request">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Said" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RegYN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IPList" type="{http://tempuri.org/}ArrayOfStcReservedIPList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IPMS0003Request", propOrder = {
    "said",
    "regYN",
    "ipList"
})
public class IPMS0003Request {

    @XmlElement(name = "Said")
    protected String said;
    @XmlElement(name = "RegYN")
    protected String regYN;
    @XmlElement(name = "IPList")
    protected ArrayOfStcReservedIPList ipList;

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
     * Gets the value of the ipList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfStcReservedIPList }
     *     
     */
    public ArrayOfStcReservedIPList getIPList() {
        return ipList;
    }

    /**
     * Sets the value of the ipList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfStcReservedIPList }
     *     
     */
    public void setIPList(ArrayOfStcReservedIPList value) {
        this.ipList = value;
    }

}
