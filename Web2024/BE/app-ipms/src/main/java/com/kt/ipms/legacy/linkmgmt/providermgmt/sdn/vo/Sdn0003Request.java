package com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "said"
})

@XmlRootElement(name = "Sdn0003Request", namespace="http://ip.kt.com/interlocking/SDN_IPM_WS_0003")
public class Sdn0003Request {


    @XmlElement(name = "Said")
    protected String said;
    
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
     * Sets the value of the officescode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaid(String value) {
        this.said = value;
    }
}
