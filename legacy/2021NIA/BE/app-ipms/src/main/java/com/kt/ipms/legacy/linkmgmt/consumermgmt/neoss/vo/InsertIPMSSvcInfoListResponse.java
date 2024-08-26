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
 *         &lt;element name="insertIPMSSvcInfoListResult" type="{http://tempuri.org/}IPMS0001Response"/>
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
    "insertIPMSSvcInfoListResult"
})
@XmlRootElement(name = "insertIPMSSvcInfoListResponse")
public class InsertIPMSSvcInfoListResponse {

    @XmlElement(required = true)
    protected IPMS0001Response insertIPMSSvcInfoListResult;

    /**
     * Gets the value of the insertIPMSSvcInfoListResult property.
     * 
     * @return
     *     possible object is
     *     {@link IPMS0001Response }
     *     
     */
    public IPMS0001Response getInsertIPMSSvcInfoListResult() {
        return insertIPMSSvcInfoListResult;
    }

    /**
     * Sets the value of the insertIPMSSvcInfoListResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPMS0001Response }
     *     
     */
    public void setInsertIPMSSvcInfoListResult(IPMS0001Response value) {
        this.insertIPMSSvcInfoListResult = value;
    }

}
