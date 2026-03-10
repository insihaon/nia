package com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Nes0004SVCInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Nes0004SVCInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Said" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LLNum" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Assign_Type_NM" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ICISOfficescode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NodeOfficescode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Nes0004SVCInfo", propOrder = {
    "said",
    "llNum",
    "assignTypeNM",
    "icisOfficescode",
    "nodeOfficescode"
})
public class Nes0004SVCInfo {

    @XmlElement(name = "Said", required = true)
    protected String said;
    @XmlElement(name = "LLNum", required = true)
    protected String llNum;
    @XmlElement(name = "Assign_Type_NM", required = true)
    protected String assignTypeNM;
    @XmlElement(name = "ICISOfficescode", required = true)
    protected String icisOfficescode;
    @XmlElement(name = "NodeOfficescode", required = true)
    protected String nodeOfficescode;

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
     * Gets the value of the llNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLLNum() {
        return llNum;
    }

    /**
     * Sets the value of the llNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLLNum(String value) {
        this.llNum = value;
    }

    /**
     * Gets the value of the assignTypeNM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssignTypeNM() {
        return assignTypeNM;
    }

    /**
     * Sets the value of the assignTypeNM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssignTypeNM(String value) {
        this.assignTypeNM = value;
    }

    /**
     * Gets the value of the icisOfficescode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getICISOfficescode() {
        return icisOfficescode;
    }

    /**
     * Sets the value of the icisOfficescode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setICISOfficescode(String value) {
        this.icisOfficescode = value;
    }

    /**
     * Gets the value of the nodeOfficescode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeOfficescode() {
        return nodeOfficescode;
    }

    /**
     * Sets the value of the nodeOfficescode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeOfficescode(String value) {
        this.nodeOfficescode = value;
    }

}
