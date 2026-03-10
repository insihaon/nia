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
 *         &lt;element name="insertReservedIPListResult" type="{http://tempuri.org/}IPMS0002Response"/>
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
    "insertReservedIPListResult"
})
@XmlRootElement(name = "insertReservedIPListResponse")
public class InsertReservedIPListResponse {

    @XmlElement(required = true)
    protected IPMS0002Response insertReservedIPListResult;

    /**
     * Gets the value of the insertReservedIPListResult property.
     * 
     * @return
     *     possible object is
     *     {@link IPMS0002Response }
     *     
     */
    public IPMS0002Response getInsertReservedIPListResult() {
        return insertReservedIPListResult;
    }

    /**
     * Sets the value of the insertReservedIPListResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPMS0002Response }
     *     
     */
    public void setInsertReservedIPListResult(IPMS0002Response value) {
        this.insertReservedIPListResult = value;
    }

}
