package com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo;

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
 *         &lt;element name="insertFixedIPListResult" type="{http://tempuri.org/}IPMS0003Response"/>
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
    "insertFixedIPListResult"
})
@XmlRootElement(name = "insertFixedIPListResponse")
public class InsertFixedIPListResponse {

    @XmlElement(required = true)
    protected IPMS0003Response insertFixedIPListResult;

    /**
     * Gets the value of the insertFixedIPListResult property.
     * 
     * @return
     *     possible object is
     *     {@link IPMS0003Response }
     *     
     */
    public IPMS0003Response getInsertFixedIPListResult() {
        return insertFixedIPListResult;
    }

    /**
     * Sets the value of the insertFixedIPListResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPMS0003Response }
     *     
     */
    public void setInsertFixedIPListResult(IPMS0003Response value) {
        this.insertFixedIPListResult = value;
    }

}
