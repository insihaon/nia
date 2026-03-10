package com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0006AssignedIP;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "resultCD"
    , "resultMSG"
    , "said"
    , "officescode"
})

@XmlRootElement(name = "Sdn0004Response", namespace="http://ip.kt.com/interlocking/SDN_IPM_WS_0004")
public class Sdn0004Response {
	
	    @XmlElement(name = "Result_CD", required = true)
	    protected String resultCD;
	    @XmlElement(name = "Result_MSG")
	    protected String resultMSG;
	    @XmlElement(name = "Said")
	    protected String said;
	    @XmlElement(name = "Officescode")
	    protected String officescode;
	  

	    /**
	     * Gets the value of the resultCD property.
	     * 
	     * @return
	     *     possible object is
	     *     {@link String }
	     *     
	     */
	    public String getResultCD() {
	        return resultCD;
	    }

	    /**
	     * Sets the value of the resultCD property.
	     * 
	     * @param value
	     *     allowed object is
	     *     {@link String }
	     *     
	     */
	    public void setResultCD(String value) {
	        this.resultCD = value;
	    }

	    /**
	     * Gets the value of the resultMSG property.
	     * 
	     * @return
	     *     possible object is
	     *     {@link String }
	     *     
	     */
	    public String getResultMSG() {
	        return resultMSG;
	    }

	    /**
	     * Sets the value of the resultMSG property.
	     * 
	     * @param value
	     *     allowed object is
	     *     {@link String }
	     *     
	     */
	    public void setResultMSG(String value) {
	        this.resultMSG = value;
	    }

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
	     * Gets the value of the officescode property.
	     * 
	     * @return
	     *     possible object is
	     *     {@link String }
	     *     
	     */
	    public String getOfficescode() {
	        return officescode;
	    }

	    /**
	     * Sets the value of the officescode property.
	     * 
	     * @param value
	     *     allowed object is
	     *     {@link String }
	     *     
	     */
	    public void setOfficescode(String value) {
	        this.officescode = value;
	    }
}
