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
    "resultCD",
    "resultMSG",
    "stcAssignedIPList"
})

@XmlRootElement(name = "Sdn0003Response", namespace="http://ip.kt.com/interlocking/SDN_IPM_WS_0003")
public class Sdn0003Response {
	 @XmlElement(name = "Result_CD", required = true)
	    protected String resultCD;
	    @XmlElement(name = "Result_MSG")
	    protected String resultMSG;
	    @XmlElement(required = true)
	    protected List<Sdn0003AssignedIP> stcAssignedIPList;
	    

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
	     * {@link Nes0006AssignedIP }
	     * 
	     * 
	     */
	    public List<Sdn0003AssignedIP> getStcAssignedIPList() {
	        if (stcAssignedIPList == null) {
	            stcAssignedIPList = new ArrayList<Sdn0003AssignedIP>();
	        }
	        return this.stcAssignedIPList;
	    }
}
