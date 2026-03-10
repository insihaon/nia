package com.nia.engine.singleton;

import java.util.ArrayList;
import java.util.List;

import com.nia.engine.vo.DomainCodeVo;

public class SingletoneRcaCodeInfo {
	
	private List<DomainCodeVo> singleDomainCodeVo = new ArrayList<DomainCodeVo>();
	
	private static SingletoneRcaCodeInfo one;
	
    private SingletoneRcaCodeInfo() {
    }
    public synchronized static SingletoneRcaCodeInfo getInstance() {
        if(one==null) {
            one = new SingletoneRcaCodeInfo();
        }
        return one;
    }
    
	public List<DomainCodeVo> getSingleDomainCodeVo() {
		return singleDomainCodeVo;
	}
	public void setSingleDomainCodeVo(List<DomainCodeVo> singleDomainCodeVo) {
		this.singleDomainCodeVo = singleDomainCodeVo;
	}
	
}
