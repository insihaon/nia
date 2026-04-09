
package com.kt.ipms.legacy.linkmgmt.providermgmt.neoss.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Said" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LACPYN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "said",
    "lacpyn"
})
@XmlRootElement(name = "Nes0006Request", namespace="http://ip.kt.com/interlocking/Nes_IPM_WS_0006")
public class Nes0006Request {

    @XmlElement(name = "Said", required = true)
    protected String said;
    @XmlElement(name = "LACPYN", required = true)
    protected String lacpyn;

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
     * Gets the value of the lacpyn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLACPYN() {
        return lacpyn;
    }

    /**
     * Sets the value of the lacpyn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLACPYN(String value) {
        this.lacpyn = value;
    }

}
