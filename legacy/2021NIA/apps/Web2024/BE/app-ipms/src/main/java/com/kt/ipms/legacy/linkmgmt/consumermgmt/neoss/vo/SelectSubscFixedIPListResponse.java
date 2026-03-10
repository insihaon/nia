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
 *         &lt;element name="selectSubscFixedIPListResult" type="{http://tempuri.org/}IPMS0004Response"/>
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
    "selectSubscFixedIPListResult"
})
@XmlRootElement(name = "selectSubscFixedIPListResponse")
public class SelectSubscFixedIPListResponse {

    @XmlElement(required = true)
    protected IPMS0004Response selectSubscFixedIPListResult;

    /**
     * Gets the value of the selectSubscFixedIPListResult property.
     * 
     * @return
     *     possible object is
     *     {@link IPMS0004Response }
     *     
     */
    public IPMS0004Response getSelectSubscFixedIPListResult() {
        return selectSubscFixedIPListResult;
    }

    /**
     * Sets the value of the selectSubscFixedIPListResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPMS0004Response }
     *     
     */
    public void setSelectSubscFixedIPListResult(IPMS0004Response value) {
        this.selectSubscFixedIPListResult = value;
    }

}
