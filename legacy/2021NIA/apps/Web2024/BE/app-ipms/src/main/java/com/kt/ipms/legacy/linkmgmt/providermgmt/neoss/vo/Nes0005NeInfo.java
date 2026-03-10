

package com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Nes0005NeInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Nes0005NeInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SUBSCNESCODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SUBSCMSTIP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SUBSCLGIPPORTSEQ" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SUBSCLGIPPORTDESCRIPTION" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SUBSCLGIPPORTIP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SUBSCROUTERSERIALIP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SUBSCNEALIAS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CONNALIAS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ModelName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Nes0005NeInfo", propOrder = {
    "subscnescode",
    "subscmstip",
    "subsclgipportseq",
    "subsclgipportdescription",
    "subsclgipportip",
    "subscrouterserialip",
    "subscnealias",
    "connalias",
    "modelName"
})
public class Nes0005NeInfo {

    @XmlElement(name = "SUBSCNESCODE", required = true)
    protected String subscnescode;
    @XmlElement(name = "SUBSCMSTIP", required = true)
    protected String subscmstip;
    @XmlElement(name = "SUBSCLGIPPORTSEQ", required = true)
    protected String subsclgipportseq;
    @XmlElement(name = "SUBSCLGIPPORTDESCRIPTION", required = true)
    protected String subsclgipportdescription;
    @XmlElement(name = "SUBSCLGIPPORTIP", required = true)
    protected String subsclgipportip;
    @XmlElement(name = "SUBSCROUTERSERIALIP", required = true)
    protected String subscrouterserialip;
    @XmlElement(name = "SUBSCNEALIAS", required = true)
    protected String subscnealias;
    @XmlElement(name = "CONNALIAS", required = true)
    protected String connalias;
    @XmlElement(name = "ModelName", required = true)
    protected String modelName;

    /**
     * Gets the value of the subscnescode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUBSCNESCODE() {
        return subscnescode;
    }

    /**
     * Sets the value of the subscnescode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUBSCNESCODE(String value) {
        this.subscnescode = value;
    }

    /**
     * Gets the value of the subscmstip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUBSCMSTIP() {
        return subscmstip;
    }

    /**
     * Sets the value of the subscmstip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUBSCMSTIP(String value) {
        this.subscmstip = value;
    }

    /**
     * Gets the value of the subsclgipportseq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUBSCLGIPPORTSEQ() {
        return subsclgipportseq;
    }

    /**
     * Sets the value of the subsclgipportseq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUBSCLGIPPORTSEQ(String value) {
        this.subsclgipportseq = value;
    }

    /**
     * Gets the value of the subsclgipportdescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUBSCLGIPPORTDESCRIPTION() {
        return subsclgipportdescription;
    }

    /**
     * Sets the value of the subsclgipportdescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUBSCLGIPPORTDESCRIPTION(String value) {
        this.subsclgipportdescription = value;
    }

    /**
     * Gets the value of the subsclgipportip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUBSCLGIPPORTIP() {
        return subsclgipportip;
    }

    /**
     * Sets the value of the subsclgipportip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUBSCLGIPPORTIP(String value) {
        this.subsclgipportip = value;
    }

    /**
     * Gets the value of the subscrouterserialip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUBSCROUTERSERIALIP() {
        return subscrouterserialip;
    }

    /**
     * Sets the value of the subscrouterserialip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUBSCROUTERSERIALIP(String value) {
        this.subscrouterserialip = value;
    }

    /**
     * Gets the value of the subscnealias property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUBSCNEALIAS() {
        return subscnealias;
    }

    /**
     * Sets the value of the subscnealias property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUBSCNEALIAS(String value) {
        this.subscnealias = value;
    }

    /**
     * Gets the value of the connalias property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONNALIAS() {
        return connalias;
    }

    /**
     * Sets the value of the connalias property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONNALIAS(String value) {
        this.connalias = value;
    }

    /**
     * Gets the value of the modelName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * Sets the value of the modelName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelName(String value) {
        this.modelName = value;
    }

}
