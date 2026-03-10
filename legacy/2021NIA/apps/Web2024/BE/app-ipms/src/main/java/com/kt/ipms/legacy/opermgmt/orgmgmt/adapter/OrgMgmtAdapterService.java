package com.kt.ipms.legacy.opermgmt.orgmgmt.adapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.orgmgmt.service.OrgMgmtService;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;


@Component
@Transactional
public class OrgMgmtAdapterService {
	
	@Autowired
	OrgMgmtService orgMgmtService;
	
	
	@Transactional(readOnly = true)
	public  TbLvlBasListVo selectListSvcLine(TbLvlBasVo searchVo)  {
		
		return orgMgmtService.selectListSvcLine(searchVo);
	
	}
	
	@Transactional(readOnly = true)
	public  TbLvlBasListVo selectlistCenter(TbLvlBasVo searchVo)  {
		
		return orgMgmtService.selectlistCenter(searchVo);
	
	}
	
	@Transactional(readOnly = true)
	public  TbLvlBasListVo selectlistNode(TbLvlBasVo searchVo)  {
		
		return orgMgmtService.selectlistNode(searchVo);
	
	}
	
	
	@Transactional(readOnly = true)
	public  TbLvlMstListVo selectListTbLvlMstVo(TbLvlMstVo searchVo)  {
		
		return orgMgmtService.selectListTbLvlMstVo(searchVo);
	
	}
	
	@Transactional(readOnly = true)
	public  TbLvlMstListVo selectListMstSeqBySvcLine(TbLvlMstVo searchVo)  {
		
		return orgMgmtService.selectListMstSeqBySvcLine(searchVo);
	
	}
	
	@Transactional(readOnly = true)
	public  TbLvlMstListVo selectListMstSeqByCenter(TbLvlMstVo searchVo)  {
		
		return orgMgmtService.selectListMstSeqByCenter(searchVo);
	
	}
	
	@Transactional(readOnly = true)
	public  TbLvlMstListVo selectListMstSeqByOper(TbLvlMstVo searchVo)  {
		
		return orgMgmtService.selectListMstSeqByOper(searchVo);
	
	}
	
	@Transactional(readOnly = true)
	public TbLvlBasVo selectTbLvlBas(TbLvlBasVo searchVo)  {
		return orgMgmtService.selectTbLvlBas(searchVo);
	}
	

}
