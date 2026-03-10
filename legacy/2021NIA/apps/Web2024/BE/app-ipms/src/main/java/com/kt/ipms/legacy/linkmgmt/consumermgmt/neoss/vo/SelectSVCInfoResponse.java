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
 *         &lt;element name="selectSVCInfoResult" type="{http://tempuri.org/}IPMS0005Response"/>
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
    "selectSVCInfoResult"
})
@XmlRootElement(name = "selectSVCInfoResponse")
public class SelectSVCInfoResponse {

    @XmlElement(required = true)
    protected IPMS0005Response selectSVCInfoResult;

    /**
     * Gets the value of the selectSVCInfoResult property.
     * 
     * @return
     *     possible object is
     *     {@link IPMS0005Response }
     *     
     */
    public IPMS0005Response getSelectSVCInfoResult() {
        return selectSVCInfoResult;
    }

    /**
     * Sets the value of the selectSVCInfoResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPMS0005Response }
     *     
     */
    public void setSelectSVCInfoResult(IPMS0005Response value) {
        this.selectSVCInfoResult = value;
    }

}
