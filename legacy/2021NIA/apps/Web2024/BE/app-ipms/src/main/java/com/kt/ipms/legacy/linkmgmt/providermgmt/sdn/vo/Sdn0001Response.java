package com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo.Nes0003IpSuggestList;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "resultCD"
    , "resultMSG"
    , "ipBlockCount"
    , "stcIpSuggestList"
})

@XmlRootElement(name = "Sdn0001Response", namespace="http://ip.kt.com/interlocking/SDN_IPM_WS_0001")
public class Sdn0001Response {
	
	@XmlElement(name = "Result_CD", required = true)
    protected String resultCD;
    @XmlElement(name = "Result_MSG")
    protected String resultMSG;
    @XmlElement(name = "IP_BLOCK_COUNT", required = true)
    protected String ipBlockCount;
    @XmlElement(nillable = true)
    protected List<Sdn0001IpSuggestList> stcIpSuggestList;
    
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
     * Gets the value of the ipBlockCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpBlockCount() {
        return ipBlockCount;
    }

    /**
     * Sets the value of the ipBlockCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpBlockCount(String value) {
        this.ipBlockCount = value;
    }

    /**
     * Gets the value of the stcIpSuggestList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stcIpSuggestList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStcIpSuggestList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Nes0003IpSuggestList }
     * 
     * 
     */
    public List<Sdn0001IpSuggestList> getStcIpSuggestList() {
        if (stcIpSuggestList == null) {
            stcIpSuggestList = new ArrayList<Sdn0001IpSuggestList>();
        }
        return this.stcIpSuggestList;
    }
}
