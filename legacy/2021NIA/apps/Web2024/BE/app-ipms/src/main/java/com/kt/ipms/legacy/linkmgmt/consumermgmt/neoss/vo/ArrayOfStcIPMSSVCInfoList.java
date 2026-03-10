package com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.vo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfStcIPMSSVCInfoList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfStcIPMSSVCInfoList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="stcIPMSSVCInfoList" type="{http://tempuri.org/}stcIPMSSVCInfoList" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfStcIPMSSVCInfoList", propOrder = {
    "stcIPMSSVCInfoList"
})
public class ArrayOfStcIPMSSVCInfoList {

    protected List<StcIPMSSVCInfoList> stcIPMSSVCInfoList;

    /**
     * Gets the value of the stcIPMSSVCInfoList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stcIPMSSVCInfoList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStcIPMSSVCInfoList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StcIPMSSVCInfoList }
     * 
     * 
     */
    public List<StcIPMSSVCInfoList> getStcIPMSSVCInfoList() {
        if (stcIPMSSVCInfoList == null) {
            stcIPMSSVCInfoList = new ArrayList<StcIPMSSVCInfoList>();
        }
        return this.stcIPMSSVCInfoList;
    }

}
