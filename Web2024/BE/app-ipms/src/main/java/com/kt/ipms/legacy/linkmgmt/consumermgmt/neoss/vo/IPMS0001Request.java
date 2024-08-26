package com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IPMS0001Request complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IPMS0001Request">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="svcList" type="{http://tempuri.org/}ArrayOfStcIPMSSVCInfoList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IPMS0001Request", propOrder = {
    "svcList"
})
public class IPMS0001Request {

    protected ArrayOfStcIPMSSVCInfoList svcList;

    /**
     * Gets the value of the svcList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfStcIPMSSVCInfoList }
     *     
     */
    public ArrayOfStcIPMSSVCInfoList getSvcList() {
        return svcList;
    }

    /**
     * Sets the value of the svcList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfStcIPMSSVCInfoList }
     *     
     */
    public void setSvcList(ArrayOfStcIPMSSVCInfoList value) {
        this.svcList = value;
    }

}
