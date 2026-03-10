package com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfStcAssignedIPList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfStcAssignedIPList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="stcAssignedIPList" type="{http://tempuri.org/}stcAssignedIPList" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfStcAssignedIPList", propOrder = {
    "stcAssignedIPList"
})
public class ArrayOfStcAssignedIPList {

    protected List<StcAssignedIPList> stcAssignedIPList;

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
     * {@link StcAssignedIPList }
     * 
     * 
     */
    public List<StcAssignedIPList> getStcAssignedIPList() {
        if (stcAssignedIPList == null) {
            stcAssignedIPList = new ArrayList<StcAssignedIPList>();
        }
        return this.stcAssignedIPList;
    }

}
