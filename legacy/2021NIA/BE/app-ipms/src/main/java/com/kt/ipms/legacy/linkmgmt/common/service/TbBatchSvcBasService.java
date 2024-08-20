package com.kt.ipms.legacy.linkmgmt.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.linkmgmt.common.dao.TbLnkSvcBasDao;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbBatchSvcBasListVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbBatchSvcBasVo;

@Component
public class TbBatchSvcBasService {
	@Autowired
	private TbLnkSvcBasDao tbLnkSvcBasDao;
	
	@Transactional(readOnly = true)
	public List<TbBatchSvcBasVo> selectListBatchConf(){
		List<TbBatchSvcBasVo> tbBatchSvcBasVos;
		try{
			tbBatchSvcBasVos = tbLnkSvcBasDao.selectListBatchConf();
			if(tbBatchSvcBasVos.size() < 1){
				throw new ServiceException("CMN.HIGH.00001");
			}
		}catch(ServiceException e) {
			throw e;
		}catch(Exception e) {
			throw new ServiceException(e.getMessage());
		}
		return tbBatchSvcBasVos;
	}
	
	@Transactional(readOnly=true)
	public TbBatchSvcBasListVo selectListTbBatchSvcBasVo(TbBatchSvcBasVo tbBatchSvcBasVo) throws ServiceException{
		List<TbBatchSvcBasVo> tbBatchSvcBasVos = null;
		TbBatchSvcBasListVo tbBatchSvcBasListVo = null;
		try{
			if(tbBatchSvcBasVo.getSearchCnd() != null) {
				if(tbBatchSvcBasVo.getSearchCnd().equals("sifId")){
					tbBatchSvcBasVo.setSifId(tbBatchSvcBasVo.getSearchWrd());
				} else if(tbBatchSvcBasVo.getSearchCnd().equals("ssystemNm")) {
					tbBatchSvcBasVo.setSsystemNm(tbBatchSvcBasVo.getSearchWrd());
				} else {
					tbBatchSvcBasVo.setSearchCnd("");
					tbBatchSvcBasVo.setSearchWrd("");
				}
			}
			tbBatchSvcBasVos = tbLnkSvcBasDao.selectListPageTbBatchSvcBasVo(tbBatchSvcBasVo);
			int totalCount = tbLnkSvcBasDao.countListPageTbBatchSvcBasVo(tbBatchSvcBasVo);
			tbBatchSvcBasListVo = new TbBatchSvcBasListVo();
			tbBatchSvcBasListVo.setTbBatchSvcBasVos(tbBatchSvcBasVos);
			tbBatchSvcBasListVo.setTotalCount(totalCount);
		}catch(ServiceException e) {
			throw e;
		}catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return tbBatchSvcBasListVo;
	}
	
	@Transactional(readOnly=true)
	public TbBatchSvcBasVo selectTbBatchSvcBasVo(TbBatchSvcBasVo tbBatchSvcBasVo) throws ServiceException {
		TbBatchSvcBasVo resultVo = null;
		try {
			resultVo = tbLnkSvcBasDao.selectTbBatchSvcBasVo(tbBatchSvcBasVo);
			if(resultVo == null){
				throw new ServiceException("CMN.HIGH.00001");
			}
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultVo;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public TbBatchSvcBasVo updateTbBatchSvcBasVo(TbBatchSvcBasVo updateVo) throws ServiceException {
		try {
			tbLnkSvcBasDao.updateTbBatchSvcBasVo(updateVo);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return updateVo;
	}
}
