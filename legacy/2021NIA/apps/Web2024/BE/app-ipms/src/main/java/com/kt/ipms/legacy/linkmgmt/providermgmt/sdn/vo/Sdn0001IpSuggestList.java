package com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Sdn0001IpSuggestList", propOrder = {
		 "ipblock"
		  , "ipBitmask"
		  , "modifiedDate"
		  , "IpDetailStatus"
		  , "IpDetailStatusMsg"
})

public class Sdn0001IpSuggestList {
	
    @XmlElement(name = "IP_BLOCK", required = true)
    protected String ipblock;
    @XmlElement(name = "IpBitmask", required = true)
    protected String ipBitmask;
    @XmlElement(name = "Modified_Date")
    protected String modifiedDate;
    @XmlElement(name = "IpDetailStatus")
    protected String IpDetailStatus;
    @XmlElement(name = "IpDetailStatusMsg")
    protected String IpDetailStatusMsg;
    
    
    
    /**
     * Gets the value of the ipblock property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPBLOCK() {
        return ipblock;
    }

    /**
     * Sets the value of the ipblock property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPBLOCK(String value) {
        this.ipblock = value;
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
     * Gets the value of the modifiedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModifiedDate() {
        return modifiedDate;
    }

    /**
     * Sets the value of the modifiedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModifiedDate(String value) {
        this.modifiedDate = value;
    }
    
    /**
     * Gets the value of the modifiedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpDetailStatus() {
        return IpDetailStatus;
    }

    /**
     * Sets the value of the modifiedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpDetailStatus(String value) {
        this.IpDetailStatus = value;
    }
    
    /**
     * Gets the value of the modifiedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpDetailStatusMsg() {
        return IpDetailStatusMsg;
    }

    /**
     * Sets the value of the modifiedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpDetailStatusMsg(String value) {
        this.IpDetailStatusMsg = value;
    }
	
}
