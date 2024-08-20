package com.kt.ipms.legacy.opermgmt.nonktipmgmt.service;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.opermgmt.nonktipmgmt.vo.TbNonKtIpblockListVo;
import com.kt.ipms.legacy.opermgmt.nonktipmgmt.vo.TbNonKtIpblockVo;
import com.kt.ipms.legacy.opermgmt.nonktipmgmt.vo.TbNonKtSvcMstListVo;
import com.kt.ipms.legacy.opermgmt.nonktipmgmt.vo.TbNonKtSvcMstVo;

@Component
public class NonKtIpMgmtService {
	@Autowired
	private NonKtIpMgmtTxService nonKtIpMgmtTxService;

	/* Non-KT IP 목록 리스트 조회  */
	@Transactional(readOnly = true)
	public TbNonKtSvcMstListVo viewListNonKtIpSvcMst(TbNonKtSvcMstVo searchVo) {
		TbNonKtSvcMstListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbNonKtSvcMstVo> resultList = nonKtIpMgmtTxService.selectListPageTbNonKtSvcMstVo(searchVo);
			int totalCount = nonKtIpMgmtTxService.countListTbNonKtSvcMstVo(searchVo);
			resultListVo = new TbNonKtSvcMstListVo();
			resultListVo.setTbNonKtSvcMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Non-KT IP 내역"});
		}
		
		return resultListVo;
	}
	/* Non-KT IP 상세조회 */
	@Transactional(readOnly = true)
	public TbNonKtSvcMstVo viewDetailNonKtIpSvcMst(TbNonKtSvcMstVo tbNonKtSvcMstVo) {
		TbNonKtSvcMstVo resultVo = null;
		if(tbNonKtSvcMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			resultVo = nonKtIpMgmtTxService.selectTbNonKtSvcMstVo(tbNonKtSvcMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Non-KT IP 상세 내역"});
		}
		return resultVo;
	}
	
	/* 노드국  리스트 조회 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectIcisOfficesCodeList(TbNonKtSvcMstVo tbNonKtSvcMstVo) {
		List<CommonCodeVo> resultListVo = null;
		try {
			if(tbNonKtSvcMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultListVo = nonKtIpMgmtTxService.selectIcisOfficesCodeList(tbNonKtSvcMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"노드국 목록"});
		}
		return resultListVo;
	}
	
	/* 수용국 리스트 조회 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectsOfficeCodeList(TbNonKtSvcMstVo tbNonKtSvcMstVo) {
		List<CommonCodeVo> resultListVo = null;
		try {
			if(tbNonKtSvcMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultListVo = nonKtIpMgmtTxService.selectsOfficeCodeList(tbNonKtSvcMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"수용국 목록"});
		}
		return resultListVo;
	}
	
	/* Non-KT IP Block 목록 리스트 조회 */
	@Transactional(readOnly = true)
	public TbNonKtIpblockListVo selectListNonKtIpBlock(TbNonKtIpblockVo searchVo) {
		TbNonKtIpblockListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbNonKtIpblockVo> resultList = nonKtIpMgmtTxService.selectListTbNonKtIpblockVo(searchVo);
			int totalCount = nonKtIpMgmtTxService.countListTbNonKtIpblockVo(searchVo);
			resultListVo = new TbNonKtIpblockListVo();
			resultListVo.setTbNonKtIpblockVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Non-KT IP Block 내역"});
		}
		
		return resultListVo;
	}
	
	/* Non-KT IP블록 작업완료 호출 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void workCompletionIpBlock(TbNonKtIpblockListVo tbNonKtIpblockListVo) {
		try {
			
			if(tbNonKtIpblockListVo.getTbNonKtIpblockVos() !=null && tbNonKtIpblockListVo.getTbNonKtIpblockVos().size() != 0 ){
				
				List<TbNonKtIpblockVo> baseNonKtIpblockVos = tbNonKtIpblockListVo.getTbNonKtIpblockVos();
				int rCnt = tbNonKtIpblockListVo.getTbNonKtIpblockVos().size();
				String checkSregyn = "D"; //기본 D, 회선에 묶인 IP블럭 중 C(작업완료)상태가 있으면 C로 변경
				String tbNonKtSvcMstFlag = "N"; //IP Block 상태가 변경될때만 BIF.TB_NON_KT_SVC_MST의 상태 Update
				BigInteger nonKtSvcMstSeq = tbNonKtIpblockListVo.getNnonKtSvcMstSeq(); //IP블록 작업완료후 BIF.TB_NON_KT_SVC_MST의 상태를 변경을 위한 SEQ
				TbNonKtSvcMstVo tbNonKtSvcMstVo = new TbNonKtSvcMstVo();
				String userId = null;
				
				for(int i=0; i <rCnt; i++){
					TbNonKtIpblockVo tempVo = baseNonKtIpblockVos.get(i);
					
					TbNonKtIpblockVo searchVo = new TbNonKtIpblockVo();
					TbNonKtIpblockVo updateVo = new TbNonKtIpblockVo();
					searchVo.setNnonKtSvcMstSeq(tempVo.getNnonKtSvcMstSeq());
					searchVo.setSipBlock(tempVo.getSipBlock());
					userId = tempVo.getScreateId();
					TbNonKtIpblockVo checkVo = nonKtIpMgmtTxService.selectCheckTbNonKtIpblockVo(searchVo);
					
					String sFlag = checkVo.getSflag(); // DB IPBlock 상태 조회
					
					if(sFlag.equals("Y") || sFlag.equals("N")){
						//tempVo.setNnonKtSvcMstSeq(nonKtSvcMstSeq);
						if(sFlag.equals("Y")) {
							tempVo.setSflag("C"); // C:작업완료
							checkSregyn = "C";
						} else if(sFlag.equals("N")) {
							tempVo.setSflag("D"); // D:삭제완료
						}
						tbNonKtSvcMstFlag = "Y"; // BIF.TB_NON_KT_SVC_MST 상태변경
						nonKtIpMgmtTxService.insertTbNonKtIpblockVo(tempVo); //상태확인후 IP블록 Insert
					} else if(sFlag.equals("C")){
						updateVo.setNnonKtIpblockSeq(checkVo.getNnonKtIpblockSeq());
						updateVo.setSmodifyId(tempVo.getSmodifyId());
						updateVo.setScomment(tempVo.getScomment());
						nonKtIpMgmtTxService.updateTbNonKtIpblockVo(updateVo); //상태확인후 IP블록 update(비고 수정)
						checkSregyn = "C";
						tbNonKtSvcMstFlag = "Y";
					}
				}//for end
				if(tbNonKtSvcMstFlag.equals("Y")) {
					tbNonKtSvcMstVo.setNnonKtSvcMstSeq(nonKtSvcMstSeq);
					tbNonKtSvcMstVo.setSregyn(checkSregyn);
					tbNonKtSvcMstVo.setSmodifyId(userId);
					nonKtIpMgmtTxService.updateTbNonKtSvcMstVo(tbNonKtSvcMstVo); //BIF.TB_NON_KT_SVC_MST 상태 변경
				}
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"Non-KT IP Block"});
		}
	}
	
	/* Non KT IP 목록 리스트 엑셀 다운 */
	@Transactional(readOnly = true)
	public TbNonKtSvcMstListVo selectListNonKtIpSvcMstExcel(TbNonKtSvcMstVo searchVo) {
		TbNonKtSvcMstListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			int totalCount = nonKtIpMgmtTxService.countListTbNonKtSvcMstVo(searchVo);
			List<TbNonKtSvcMstVo> resultList = null;
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			if(totalCount > 0) {
				searchVo.setFirstIndex(1);
				searchVo.setLastIndex(totalCount);
				resultList = nonKtIpMgmtTxService.selectListPageTbNonKtSvcMstVo(searchVo);
			}
			
			resultListVo = new TbNonKtSvcMstListVo();
			resultListVo.setTbNonKtSvcMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Non-KT IP 관리 리스트 정보 엑셀저장"});
		}
		return resultListVo;
	}
}
