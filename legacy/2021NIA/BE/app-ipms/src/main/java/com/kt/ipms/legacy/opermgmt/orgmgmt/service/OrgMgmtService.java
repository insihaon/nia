package com.kt.ipms.legacy.opermgmt.orgmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.opermgmt.orgmgmt.dao.TbLvlBasDao;
import com.kt.ipms.legacy.opermgmt.orgmgmt.dao.TbLvlMstDao;
import com.kt.ipms.legacy.opermgmt.orgmgmt.dao.TbLvlRoleSubDao;
import com.kt.ipms.legacy.opermgmt.orgmgmt.dao.TbLvlSubCdDao;
import com.kt.ipms.legacy.opermgmt.orgmgmt.dao.TbOrgBasDao;
import com.kt.ipms.legacy.opermgmt.orgmgmt.dao.TbSvcLineTypeCdDao;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlCdListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlCdVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlRoleMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlRoleSubListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlRoleSubVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlSubCdListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlSubCdVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbOrgBasListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbOrgBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbSvcLineTypeCdListVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbSvcLineTypeCdVo;
import com.kt.log4kt.KTLogger;
import com.kt.log4kt.KTLoggerFactory;

@Component
public class OrgMgmtService {

	@Lazy @Autowired
	private TbSvcLineTypeCdDao tbSvcLineTypeCdDao;

	@Lazy @Autowired
	private TbLvlBasDao tbLvlBasDao;

	@Lazy @Autowired
	private TbOrgBasDao tbOrgBasDao;

	@Lazy @Autowired
	private TbLvlMstDao tbLvlMstDao;

	@Lazy @Autowired
	private TbLvlSubCdDao tbLvlSubCdDao;

	@Lazy @Autowired
	private TbLvlRoleSubDao tbLvlRoleSubDao;

	@Lazy @Autowired
	private OrgMgmtTxService orgMgmtTxService;

	protected KTLogger logger = KTLoggerFactory.getLogger(getClass());

	@Transactional(readOnly = true)
	public TbLvlCdListVo selectListTbLvlCdVo(TbLvlCdVo searchVo) {

		TbLvlCdListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {

			List<TbLvlCdVo> resultList = orgMgmtTxService.selectListPageTbLvlCdVo(searchVo);
			int totalCount = orgMgmtTxService.countListPageTbLvlCdVo(searchVo);
			resultListVo = new TbLvlCdListVo();
			resultListVo.setTbLvlCdVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위코드 리스트" });
		}
		return resultListVo;
	}

	@Transactional(readOnly = true)
	public TbLvlCdListVo searchTbLvlCd(TbLvlCdVo searchVo) {

		TbLvlCdListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {

			List<TbLvlCdVo> resultList = orgMgmtTxService.selectListTbLvlCdVo(searchVo);
			int totalCount = orgMgmtTxService.countListPageTbLvlCdVo(searchVo);
			resultListVo = new TbLvlCdListVo();
			resultListVo.setTbLvlCdVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위코드 리스트" });
		}
		return resultListVo;
	}

	@Transactional(readOnly = true)
	public TbLvlCdVo selectTbLvlCdVo(TbLvlCdVo searchVo) {

		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}

		TbLvlCdVo resultVo = null;

		try {
			resultVo = orgMgmtTxService.selectTbLvlCdVo(searchVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위코드 상세정보" });
		}
		return resultVo;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbLvlCdVo(TbLvlCdVo tbLvlCdVo) {

		int result;
		if (tbLvlCdVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}

		try {
			result = orgMgmtTxService.updateTbLvlCdVo(tbLvlCdVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[] { "계위코드" });
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int insertTbLvlCdVo(TbLvlCdVo tbLvlCdVo) {

		int result;
		if (tbLvlCdVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}

		try {
			String newLvlCd = "";
			newLvlCd = orgMgmtTxService.selectNewLvlCd();

			tbLvlCdVo.setSlvlCd(newLvlCd);
			result = orgMgmtTxService.insertTbLvlCdVo(tbLvlCdVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {

			throw new ServiceException("CMN.HIGH.00020", new String[] { "가상 계위코드" });
		}
		return result;
	}

	/*--------------------------------서비스 망 관리 strat ------------------------------------*/

	@Transactional(readOnly = true)
	public TbSvcLineTypeCdListVo selectListSvcLineType(TbSvcLineTypeCdVo searchVo) {
		TbSvcLineTypeCdListVo resultListVo = null;
		try {
			if (searchVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbSvcLineTypeCdVo> resultList = tbSvcLineTypeCdDao.selectListPageTbSvcLineTypeCdVo(searchVo);
			int totalCount = tbSvcLineTypeCdDao.countListPageTbSvcLineTypeCdVo(searchVo);
			resultListVo = new TbSvcLineTypeCdListVo();
			resultListVo.setTbSvcLineTypeCdVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "서비스망 내역 " });
		}
		return resultListVo;
	}

	@Transactional(readOnly = true)
	public TbSvcLineTypeCdVo selectLineType(TbSvcLineTypeCdVo tbSvcLineTypeCdVo) {
		if (tbSvcLineTypeCdVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}

		TbSvcLineTypeCdVo resultVo = null;
		try {
			resultVo = tbSvcLineTypeCdDao.selectTbSvcLineTypeCdVo(tbSvcLineTypeCdVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "서비스망 상세정보" });
		}
		return resultVo;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public TbSvcLineTypeCdVo insertSvcLineType(TbSvcLineTypeCdVo tbSvcLineTypeCdVo) {
		if (tbSvcLineTypeCdVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			String newLineTypeCd = null;
			/*LineTypeCd생성*/
			newLineTypeCd = tbSvcLineTypeCdDao.selectNewLinetypeCd();
			tbSvcLineTypeCdVo.setSsvcLineTypeCd(newLineTypeCd);

			tbSvcLineTypeCdDao.insertTbSvcLineTypeCdVo(tbSvcLineTypeCdVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[] { "서비스망" });
		}
		return tbSvcLineTypeCdVo;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public TbSvcLineTypeCdVo updateSvcLineType(TbSvcLineTypeCdVo tbSvcLineTypeCdVo) {
		if (tbSvcLineTypeCdVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			tbSvcLineTypeCdDao.updateTbSvcLineTypeCdVo(tbSvcLineTypeCdVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[] { "서비스망" });
		}
		return tbSvcLineTypeCdVo;
	}

	/*--------------------------------서비스 망 관리 end ------------------------------------*/
	@Transactional(readOnly = true)
	public TbLvlBasListVo selectListSvcLine(TbLvlBasVo searchVo) {
		TbLvlBasListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbLvlBasVo> resultList = tbLvlBasDao.selectListSvcLine(searchVo);
			resultListVo = new TbLvlBasListVo();
			resultListVo.setTbLvlBasVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "관리자권한 서비스망" });
		}
		return resultListVo;
	}

	@Transactional(readOnly = true)
	public TbLvlBasListVo selectlistCenter(TbLvlBasVo searchVo) {
		TbLvlBasListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbLvlBasVo> resultList = tbLvlBasDao.selectlistCenter(searchVo);
			resultListVo = new TbLvlBasListVo();
			resultListVo.setTbLvlBasVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "관리자권한 본부내역" });
		}
		return resultListVo;
	}

	@Transactional(readOnly = true)
	public TbLvlBasListVo selectlistNode(TbLvlBasVo searchVo) {
		TbLvlBasListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbLvlBasVo> resultList = tbLvlBasDao.selectlistNode(searchVo);
			resultListVo = new TbLvlBasListVo();
			resultListVo.setTbLvlBasVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "관리자권한 노드" });
		}
		return resultListVo;
	}

	//코드 전체보회 추가 

	@Transactional(readOnly = true)
	public TbLvlBasListVo selectListSvcLineAll() {
		TbLvlBasListVo resultListVo = null;
		try {
			List<TbLvlBasVo> resultList = tbLvlBasDao.selectListSvcLineAll();
			resultListVo = new TbLvlBasListVo();
			resultListVo.setTbLvlBasVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "관리자권한 서비스망" });
		}
		return resultListVo;
	}

	@Transactional(readOnly = true)
	public TbLvlBasListVo selectlistCenterAll() {
		TbLvlBasListVo resultListVo = null;
		try {
			List<TbLvlBasVo> resultList = tbLvlBasDao.selectlistCenterAll();
			resultListVo = new TbLvlBasListVo();
			resultListVo.setTbLvlBasVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "관리자권한 본부내역" });
		}
		return resultListVo;
	}

	@Transactional(readOnly = true)
	public TbLvlBasListVo selectlistNodeAll() {
		TbLvlBasListVo resultListVo = null;
		try {
			List<TbLvlBasVo> resultList = tbLvlBasDao.selectlistNodeAll();
			resultListVo = new TbLvlBasListVo();
			resultListVo.setTbLvlBasVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "관리자권한 노드" });
		}
		return resultListVo;
	}
	//

	@Transactional(readOnly = true)
	public TbLvlBasVo selectTbLvlBas(TbLvlBasVo searchVo) {
		TbLvlBasVo resultVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {

			if (!StringUtils.hasText(searchVo.getSsvcLineTypeCd())) {
				searchVo.setSsvcLineTypeCd(CommonCodeUtil.USER_LVL_CD_NA);
			}

			if (!StringUtils.hasText(searchVo.getSsvcGroupCd())) {
				searchVo.setSsvcGroupCd(CommonCodeUtil.USER_LVL_CD_NA);
			}

			if (!StringUtils.hasText(searchVo.getSsvcObjCd())) {
				searchVo.setSsvcObjCd(CommonCodeUtil.USER_LVL_CD_NA);
			}
			resultVo = tbLvlBasDao.selectTbLvlBasVo(searchVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 MST SEQ" });
		}
		return resultVo;
	}

	/*--------------------------------조직정보 관리  start------------------------------------*/

	@Transactional(readOnly = true)
	public TbOrgBasListVo selectListOrgBas(TbOrgBasVo searchVo) {
		TbOrgBasListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbOrgBasVo> resultList = tbOrgBasDao.selectListPageTbOrgBasVo(searchVo);
			int totalCount = tbOrgBasDao.countListPageTbOrgBasVo(searchVo);
			resultListVo = new TbOrgBasListVo();
			resultListVo.setTbOrgBasVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "IDMS  조직 기본정보" });
		}
		return resultListVo;
	}

	@Transactional(readOnly = true)
	public TbOrgBasListVo searchOrgBas(TbOrgBasVo searchVo) {
		TbOrgBasListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbOrgBasVo> resultList = tbOrgBasDao.selectListTbOrgBasVo(searchVo);
			int totalCount = tbOrgBasDao.countListPageTbOrgBasVo(searchVo);
			resultListVo = new TbOrgBasListVo();
			resultListVo.setTbOrgBasVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "운용조직 정보 검색" });
		}
		return resultListVo;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public TbOrgBasListVo updateSipmsOrgYn(TbOrgBasListVo tbOrgBasListVo) {
		if (tbOrgBasListVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			tbOrgBasDao.updateSipmsOrgYn(tbOrgBasListVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[] { "사용여부" });
		}
		return tbOrgBasListVo;
	}

	/*--------------------------------조직정보 관리  end------------------------------------*/

	/*--------------------------------조직계위정보 관리 start----------------------------------*/
	@Transactional(readOnly = true)
	public TbLvlBasListVo selectListLvlBas(TbLvlBasVo searchVo) {
		TbLvlBasListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbLvlBasVo> resultList = tbLvlBasDao.selectListPageTbLvlBasVo(searchVo);
			int totalCount = tbLvlBasDao.countListPageTbLvlBasVo(searchVo);
			resultListVo = new TbLvlBasListVo();
			resultListVo.setTbLvlBasVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 기본정보" });
		}
		return resultListVo;
	}

	/*--------------------------------조직계위정보 관리 end----------------------------------*/

	/*--------------------------------조직계위 MST 조회 start----------------------------------*/
	//admin  계위 조회 
	@Transactional(readOnly = true)
	public TbLvlMstListVo selectListTbLvlMstVo(TbLvlMstVo searchVo) {
		TbLvlMstListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbLvlMstVo> resultList = tbLvlMstDao.selectListTbLvlMstVo(searchVo);
			resultListVo = new TbLvlMstListVo();
			resultListVo.setTbLvlMstVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 MST  내역" });
		}
		return resultListVo;
	}

	//admin  계위 조회 
	@Transactional(readOnly = true)
	public TbLvlMstListVo selectListMstSeqBySvcLine(TbLvlMstVo searchVo) {
		TbLvlMstListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbLvlMstVo> resultList = tbLvlMstDao.selectListMstSeqBySvcLine(searchVo);
			resultListVo = new TbLvlMstListVo();
			resultListVo.setTbLvlMstVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 서비스망 MST  SEQ 리스트" });
		}
		return resultListVo;
	}

	@Transactional(readOnly = true)
	public TbLvlMstListVo selectListMstSeqByCenter(TbLvlMstVo searchVo) {
		TbLvlMstListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbLvlMstVo> resultList = tbLvlMstDao.selectListMstSeqByCenter(searchVo);
			resultListVo = new TbLvlMstListVo();
			resultListVo.setTbLvlMstVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 본부 MST  SEQ 리스트" });
		}
		return resultListVo;
	}

	@Transactional(readOnly = true)
	public TbLvlMstListVo selectListMstSeqByOper(TbLvlMstVo searchVo) {
		TbLvlMstListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbLvlMstVo> resultList = tbLvlMstDao.selectListMstSeqByOper(searchVo);
			resultListVo = new TbLvlMstListVo();
			resultListVo.setTbLvlMstVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "운용자 권한  MST  SEQ  리스트" });
		}
		return resultListVo;
	}

	/*--------------------------------조직계위 MST 조회 End----------------------------------*/

	/*--------------------------------시설수용국 관리 start----------------------------------*/
	@Transactional(readOnly = true)
	public TbLvlRoleSubListVo selectListLvlRoleSub(TbLvlRoleSubVo searchVo) {
		TbLvlRoleSubListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbLvlRoleSubVo> resultList = tbLvlRoleSubDao.selectListTbLvlRoleSubVo(searchVo);

			resultListVo = new TbLvlRoleSubListVo();
			resultListVo.setTotalCount(resultList.size());
			resultListVo.setTbLvlRoleSubVos(resultList);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 기본정보" });
		}
		return resultListVo;
	}

	/*--------------------------------시설수용국 관리 end----------------------------------*/

	/*--------------------------------시설수용국 등록 start----------------------------------*/

	public void insertLvlRoleSub(TbLvlRoleSubVo searchVo) {

		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {

			orgMgmtTxService.insertTbLvlRoleSubVo(searchVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 기본정보" });
		}
	}

	/*--------------------------------시설수용국 등록  end----------------------------------*/

	/*--------------------------------시설수용국 삭제 start----------------------------------*/

	public void deleteLvlRoleSub(TbLvlRoleSubVo searchVo) {

		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {

			orgMgmtTxService.deleteTbLvlRoleSubVo(searchVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 기본정보" });
		}
	}

	/*--------------------------------시설수용국 삭제 end----------------------------------*/

	/*--------------------------------오더노드국 관리 start----------------------------------*/
	public TbLvlSubCdListVo selectListLvlSubCd(TbLvlSubCdVo searchVo) {
		TbLvlSubCdListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbLvlSubCdVo> resultList = tbLvlSubCdDao.selectListTbLvlSubCdVo(searchVo);

			resultListVo = new TbLvlSubCdListVo();
			resultListVo.setTotalCount(resultList.size());
			resultListVo.setTbLvlSubCdVos(resultList);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 기본정보" });
		}
		return resultListVo;
	}

	/*--------------------------------오더노드국 관리 end----------------------------------*/

	/*--------------------------------오더노드국 수용국조회 start----------------------------------*/
	public int selectSloffice(TbLvlSubCdVo searchVo) {
		int result;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			result = tbLvlSubCdDao.selectSloffice(searchVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 기본정보" });
		}
		return result;
	}

	/*--------------------------------오더노드국 수용국조회  end----------------------------------*/

	/*--------------------------------오더노드국 등록 start----------------------------------*/

	public void insertTbLvlSubCd(TbLvlSubCdVo searchVo) {

		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {

			orgMgmtTxService.insertTbLvlSubCd(searchVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 기본정보" });
		}
	}

	/*--------------------------------오더노드국 등록  end----------------------------------*/

	/*--------------------------------오더노드국 삭제 start----------------------------------*/

	public void deleteTbLvlSubCd(TbLvlSubCdVo searchVo) {

		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {

			orgMgmtTxService.deleteTbLvlSubCd(searchVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 기본정보" });
		}
	}

	/*--------------------------------오더노드국 삭제 end----------------------------------*/

	/*-------------------------------- 조직계위 중복검사(노드) start----------------------------------*/

	public int countNodeTbLvlBasVo(TbLvlBasVo searchVo) {

		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		int result;
		try {

			//result = tbLvlMstDao.selectTbLvlMstVoCount(searchVo);	
			result = tbLvlBasDao.countNodeTbLvlBasVo(searchVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 기본정보" });
		}
		return result;
	}

	/*-------------------------------- 조직계위 중복검사end----------------------------------*/

	/*-------------------------------- 조직계위 중복검사(센터노드) start----------------------------------*/

	public int countSsvcGroupCd(TbLvlBasVo searchVo) {

		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		int result;
		try {

			//result = tbLvlMstDao.selectTbLvlMstVoCount(searchVo);	
			result = tbLvlBasDao.countSsvcGroupCd(searchVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 기본정보" });
		}
		return result;
	}

	/*-------------------------------- 조직계위 중복검사(센터노드) end----------------------------------*/

	/*-------------------------------- 조직계위 중복검사 start----------------------------------*/

	public int validTbLvlBas(TbLvlBasVo searchVo) {

		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		int result;
		try {

			//result = tbLvlMstDao.selectTbLvlMstVoCount(searchVo);	
			result = tbLvlBasDao.countListPageTbLvlBasVo(searchVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 기본정보" });
		}
		return result;
	}

	/*-------------------------------- 조직계위 중복검사end----------------------------------*/

	/*-------------------------------- 조직계위 등록 start----------------------------------*/

	public void insertTbLvlBas(TbLvlRoleMstVo searchVo) {

		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}

		try {

			if (searchVo.getSsvcObjCd().equals("000000")) {
				searchVo.setSlvlBasLevelCd("LL0002");
			} else {
				searchVo.setSlvlBasLevelCd("LL0003");
			}
			orgMgmtTxService.insertTbLvlMgmt(searchVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 기본정보" });
		}
	}

	/*-------------------------------- 조직계위 등록end----------------------------------*/

	/*-------------------------------- 조직계위 이동 start----------------------------------*/
	public void updateTbLvlMove(TbLvlMstVo searchVo) {

		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}

		try {

			orgMgmtTxService.updateTbLvlMove(searchVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 기본정보" });
		}
	}

	/*-------------------------------- 조직계위 이동 end----------------------------------*/

	/*------------------------------- 주노드 변경 start----------------------------------*/
	public void updatetblvlrolemst(TbLvlMstVo searchVo) {

		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}

		try {

			orgMgmtTxService.updateTbLvlRoleMstVo(searchVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "계위 기본정보" });
		}
	}

	/*-------------------------------- 주노드 변경 end----------------------------------*/

}
