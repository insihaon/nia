package com.kt.ipms.legacy.linkmgmt.providermgmt.sdn.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "resultCD",
    "resultMSG"
})

@XmlRootElement(name = "Sdn0005Response", namespace="http://ip.kt.com/interlocking/SDN_IPM_WS_0005")
public class Sdn0005Response {

	@XmlElement(name = "Result_CD", required = true)
    protected String resultCD;
    @XmlElement(name = "Result_MSG")
    protected String resultMSG;
    
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

}
