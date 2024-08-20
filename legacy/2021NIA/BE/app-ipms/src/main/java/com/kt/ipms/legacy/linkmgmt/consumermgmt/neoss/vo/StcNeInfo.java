package com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for stcNeInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="stcNeInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="subscnescode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subscmstip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subsclgipportseq" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subsclgipportdescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subsclgipportip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subscrouterserialip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subscnealias" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="connalias" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modelName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stcNeInfo", propOrder = {
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
public class StcNeInfo {

    protected String subscnescode;
    protected String subscmstip;
    protected String subsclgipportseq;
    protected String subsclgipportdescription;
    protected String subsclgipportip;
    protected String subscrouterserialip;
    protected String subscnealias;
    protected String connalias;
    protected String modelName;

    /**
     * Gets the value of the subscnescode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubscnescode() {
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
    public void setSubscnescode(String value) {
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
    public String getSubscmstip() {
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
    public void setSubscmstip(String value) {
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
    public String getSubsclgipportseq() {
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
    public void setSubsclgipportseq(String value) {
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
    public String getSubsclgipportdescription() {
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
    public void setSubsclgipportdescription(String value) {
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
    public String getSubsclgipportip() {
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
    public void setSubsclgipportip(String value) {
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
    public String getSubscrouterserialip() {
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
    public void setSubscrouterserialip(String value) {
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
    public String getSubscnealias() {
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
    public void setSubscnealias(String value) {
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
    public String getConnalias() {
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
    public void setConnalias(String value) {
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
