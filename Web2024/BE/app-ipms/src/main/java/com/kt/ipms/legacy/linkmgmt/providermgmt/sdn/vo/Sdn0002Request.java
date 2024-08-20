package com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "regYN"
    , "stcAssignedIPList"
})


@XmlRootElement(name = "Sdn0002Request", namespace="http://ip.kt.com/interlocking/SDN_IPM_WS_0002")
public class Sdn0002Request {

	    @XmlElement(name = "RegYN", required = true)
	    protected String regYN;
	    @XmlElement(required = true)
	    protected List<Sdn0002AssingdIp> stcAssignedIPList;

	    /**
	     * Gets the value of the regYN property.
	     * 
	     * @return
	     *     possible object is
	     *     {@link String }
	     *     
	     */
	    public String getRegYN() {
	        return regYN;
	    }

	    /**
	     * Sets the value of the regYN property.
	     * 
	     * @param value
	     *     allowed object is
	     *     {@link String }
	     *     
	     */
	    public void setRegYN(String value) {
	        this.regYN = value;
	    }

	    /**
	     * Gets the value of the stcAssignedIPList property.
	     * 
	     * <p>
	     * This accessor method returns a reference to the live list,
	     * not a snapshot. Therefore any modification you make to the
	     * returned list will be present inside the JAXB object.
	     * This is why there is not a <CODE>set</CODE> method for the stcAssignedIPList property.
	     * 
	     * <p>
	     * For example, to add a new item, do as follows:
	     * <pre>
	     *    getStcAssignedIPList().add(newItem);
	     * </pre>
	     * 
	     * 
	     * <p>
	     * Objects of the following type(s) are allowed in the list
	     * {@link Sdn0002AssingdIp }
	     * 
	     * 
	     */
	    public List<Sdn0002AssingdIp> getStcAssignedIPList() {
	        if (stcAssignedIPList == null) {
	            stcAssignedIPList = new ArrayList<Sdn0002AssingdIp>();
	        }
	        return this.stcAssignedIPList;
	    }
}
