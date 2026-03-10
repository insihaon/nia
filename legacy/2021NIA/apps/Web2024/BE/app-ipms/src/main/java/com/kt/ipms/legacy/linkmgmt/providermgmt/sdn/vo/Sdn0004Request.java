package com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "ipBlock"
	, "ipBitmask"
})

@XmlRootElement(name = "Sdn0004Request", namespace="http://ip.kt.com/interlocking/SDN_IPM_WS_0004")
public class Sdn0004Request {


    @XmlElement(name = "Ip_Block")
    protected String ipBlock;
    @XmlElement(name = "IpBitmask")
    protected String ipBitmask;
    
    /**
     * Gets the value of the ipBlock property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpBlock() {
        return ipBlock;
    }

    /**
     * Sets the value of the ipBlock property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpBlock(String value) {
        this.ipBlock = value;
    }
    
    /**
     * Gets the value of the ipBitmask property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpBistmask() {
        return ipBitmask;
    }

    /**
     * Sets the value of the ipBitmask property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpBistmask(String value) {
        this.ipBitmask = value;
    }
}
