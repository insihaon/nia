package com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Sdn0002AssingdIp", propOrder = {
    "said"
    , "svcLineTypeCd" 
    , "ipBlock"
    , "ipBitmask"
    , "gatewayIP"
})

public class Sdn0002AssingdIp {

	@XmlElement(name = "Said", required = true)
    protected String said;
   @XmlElement(name = "SVC_LINE_TYPE_CD", required = true)
    protected String svcLineTypeCd;
	@XmlElement(name = "Ip_Block", required = true)
    protected String ipBlock;
    @XmlElement(name = "IpBitmask", required = true)
    protected String ipBitmask;
    @XmlElement(name = "GatewayIP", required = true)
    protected String gatewayIP;
    
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
    public String getIpBitmask() {
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
    public void setIpBitmask(String value) {
        this.ipBitmask = value;
    }
    
    /**
     * Gets the value of the svcLineTypeCd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSvcLineTypeCd() {
        return svcLineTypeCd;
    }

    /**
     * Sets the value of the svcLineTypeCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSvcLineTypeCd(String value) {
        this.svcLineTypeCd = value;
    }
    
    /**
     * Gets the value of the gatewayIP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGatewayIP() {
        return gatewayIP;
    }

    /**
     * Sets the value of the gatewayIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGatewayIP(String value) {
        this.gatewayIP = value;
    }
}
