package com.kt.ipms.legacy.opermgmt.whoismgmt.service;

import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.irms.epp.xml.EPPResponseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.DateUtils;
import com.kt.ipms.legacy.cmn.service.CommonService;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.EPPCodeUtil;
import com.kt.ipms.legacy.cmn.util.EPPCodeUtil.EPPErrorTypeCodes;
import com.kt.ipms.legacy.cmn.util.EPPParserUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.cmn.util.WhoisFileFilterUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.linkmgmt.whois.adapter.WhoisAdapterService;
import com.kt.ipms.legacy.linkmgmt.whois.service.WhoisService;
import com.kt.ipms.legacy.linkmgmt.whois.service.WhoisTxService;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbNewZipcodeListVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbNewZipcodeVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisComplexListVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisComplexVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisKeywordListVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisKeywordVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisListVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisModifyListVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisModifyVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisUserVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbZipcodeListVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbZipcodeVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.WhoisStatusVo;

@Component
@Transactional
public class WhoisMgmtService {
	
@Lazy @Autowired
	private ConfigPropertieService configPropertieService;
	
@Lazy @Autowired
	private WhoisMgmtTxService whoisMgmtTxService;
	
@Lazy @Autowired
	private WhoisTxService whoisTxService;
	
@Lazy @Autowired
	private WhoisService whoisService;
	
@Lazy @Autowired
	private CommonService commonService;
	
	@Autowired
	private EPPParserUtil eppParserUtil;
	
@Lazy @Autowired
	private WhoisAdapterService whoisAdapterService;
	
	@Transactional(readOnly = true)
	public TbWhoisListVo selectListPageWhois(TbWhoisVo tbWhoisVo) {
		TbWhoisListVo resultListVo = null;
		try {
			if (tbWhoisVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbWhoisVo> resultList = whoisMgmtTxService.selectListPageWhois(tbWhoisVo);
			int totalCount = whoisMgmtTxService.countListPageWhois(tbWhoisVo);
			resultListVo = new TbWhoisListVo();
			resultListVo.setTbWhoisVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public WhoisStatusVo countWhoisByStatus() {
		WhoisStatusVo resultVo;
		try {
			resultVo = whoisMgmtTxService.countWhoisByStatus();
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois통계"});
		}
		return resultVo;
	}
	
	@Transactional(readOnly = true)
	public TbWhoisListVo selectListWhois(TbWhoisVo tbWhoisVo) {
		TbWhoisListVo resultListVo = null;
		try {
			if (tbWhoisVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbWhoisVo> resultList = whoisMgmtTxService.selectListWhois(tbWhoisVo);
			int totalCount = whoisMgmtTxService.countListWhois(tbWhoisVo);
			resultListVo = new TbWhoisListVo();
			resultListVo.setTbWhoisVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbWhoisVo selectWhois(TbWhoisVo tbWhoisVo) {
		TbWhoisVo resultVo = null;
		try {
			if(tbWhoisVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = whoisMgmtTxService.selectWhois(tbWhoisVo);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois목록"});
		}
		return resultVo;
	}

	@Transactional(readOnly = true)
	public TbWhoisUserVo selectWhoisUser(TbWhoisUserVo tbWhoisUserVo) {
		TbWhoisUserVo resultVo = null;
		try {
			if(tbWhoisUserVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = whoisMgmtTxService.selectWhoisUser(tbWhoisUserVo);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois목록"});
		}
		return resultVo;
	}

	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectListVTbWhoisTransStatusCd() {
		List<CommonCodeVo> codeList = null;
		try{
			codeList =  whoisMgmtTxService.selectListVTbWhoisTransStatusCd();
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois 전송 상태 목록"});
		}
		return codeList;
	}

	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectListVTbWhoisReqTypeCd() {
		List<CommonCodeVo> codeList = null;
		try{
			codeList =  whoisMgmtTxService.selectListVTbWhoisReqTypeCd();
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois 요청 형태 목록"});
		}
		return codeList;
	}
	
	@Transactional(readOnly = true)
	public TbZipcodeListVo selectListTbZipcode(TbZipcodeVo tbZipcodeVo) {
		if(tbZipcodeVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		TbZipcodeListVo resultListVo = null;
		try {
			resultListVo = new TbZipcodeListVo();
			List<TbZipcodeVo> tbZipcodeVos = whoisMgmtTxService.selectListTbZipcode(tbZipcodeVo);
			resultListVo.setTbZipcodeVos(tbZipcodeVos);
			resultListVo.setTotalCount(tbZipcodeVos.size());
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Zipcode 목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbNewZipcodeListVo selectListPageTbNewZipcode(TbNewZipcodeVo tbNewZipcodeVo) {
		if(tbNewZipcodeVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		TbNewZipcodeListVo resultListVo = null;
		try {
			resultListVo = new TbNewZipcodeListVo();
			List<TbNewZipcodeVo> tbZipcodeVos = whoisMgmtTxService.selectListPageTbNewZipcode(tbNewZipcodeVo);
			
			int totalCount = whoisMgmtTxService.countListPageTbNewZipcode(tbNewZipcodeVo);
			
			resultListVo.setTbZipcodeVos(tbZipcodeVos);
			resultListVo.setTotalCount(totalCount);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Zipcode 목록"});
		}
		return resultListVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateWhois(TbWhoisVo tbWhoisVo) {
		if(tbWhoisVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		int resultCode;
		try{
			resultCode = whoisMgmtTxService.updateWhois(tbWhoisVo);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultCode;
	}
	
	@Transactional(readOnly = true)
	public TbWhoisComplexListVo selectListWhoisComplex(TbWhoisComplexVo tbWhoisComplexVo) {
		TbWhoisComplexListVo resultListVo = null;
		try {
			if (tbWhoisComplexVo == null || tbWhoisComplexVo.getTbWhoisVo() == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbWhoisComplexVo> resultList = whoisMgmtTxService.selectListTbWhoisComplexVo(tbWhoisComplexVo);
			int totalCount = whoisMgmtTxService.countListTbWhoisComplexVo(tbWhoisComplexVo);
			resultListVo = new TbWhoisComplexListVo();
			resultListVo.setTbWhoisComplexVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois Complex 목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbWhoisComplexListVo selectListBatchWhoisComplex(TbWhoisComplexVo tbWhoisComplexVo) {
		TbWhoisComplexListVo resultListVo = null;
		try {
			if (tbWhoisComplexVo == null || tbWhoisComplexVo.getTbWhoisVo() == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbWhoisComplexVo> resultList = whoisMgmtTxService.selectListBatchWhoisComplexVo(tbWhoisComplexVo);
			int totalCount = whoisMgmtTxService.countListBatchWhoisComplexVo(tbWhoisComplexVo);
			resultListVo = new TbWhoisComplexListVo();
			resultListVo.setTbWhoisComplexVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois Complex 목록"});
		}
		return resultListVo;
	}
	
	
	@Transactional(readOnly = true)
	public List<String> selectListScity() {
		return whoisMgmtTxService.selectListScity();
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateWhoisUser(TbWhoisUserVo ktInfoVo) {
		if(ktInfoVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		int resultCode;
		try{
			resultCode = whoisMgmtTxService.updateWhoisUser(ktInfoVo);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultCode;
	}
	
	@Transactional(readOnly = true)
	public String selectEaddrDetail(String addrDetail) {
		String result = null;
		try {
			result = whoisMgmtTxService.selectEaddrDetail(addrDetail);
		}catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateWhoisComplex(TbWhoisComplexVo tbWhoisComplexVo) {
		int result;
		if(tbWhoisComplexVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		if(tbWhoisComplexVo.isNullTbWhoisVo() || tbWhoisComplexVo.isNullTbWhoisUserVo()) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			int whoisResult = whoisMgmtTxService.updateWhois(tbWhoisComplexVo.getTbWhoisVo());
			if(whoisResult == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			int whoisUserResult = whoisMgmtTxService.updateWhoisUser(tbWhoisComplexVo.getTbWhoisUserVo());
			if(whoisUserResult == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			result = whoisResult & whoisUserResult;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois 정보"});
		}
		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateResultCd(TbWhoisVo tbWhoisVo) {
		return whoisMgmtTxService.updateResultCd(tbWhoisVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateWhoisResultList() {
		PrintLogUtil.printLogInfo("START WHOIS RESULT BATCH SCHEDULER===========================================================");
		int result = 0;
		File basePath = new File(configPropertieService.getString("Whois.path"));
		WhoisFileFilterUtil filenameFilter = new WhoisFileFilterUtil();
		File fileList[] = basePath.listFiles(filenameFilter);
		if(fileList == null) {
			PrintLogUtil.printLogInfo("WHOIS RESULT FILE : 0");
			return 0;
		}
		List<EPPResponseAction> eppResponseActions = eppParserUtil.parserResultFiles(fileList);
		PrintLogUtil.printLogInfo("EPP RESPONSE COUNT : " + eppResponseActions.size());
		for (EPPResponseAction eppResponseAction : eppResponseActions) {
			try {
				int resultCode = eppResponseAction.getCode();
				String clientTRID = eppResponseAction.getClientTRID();
				String swhoisresultMsg = eppResponseAction.getMsg();
				
				TbWhoisVo searchVo = new TbWhoisVo();
				/** 오류 검즘 **/
				EPPErrorTypeCodes errCode = EPPCodeUtil.findEPPErrorTypeCode(resultCode);
//				String swhoisresultMsg =  EPPCodeUtil.findEPPErrorTypeMsg(resultCode);
				
				if (!errCode.equals(EPPErrorTypeCodes.NONE)) {
					PrintLogUtil.printLogInfo("REJECT : TRID = " + eppResponseAction.getClientTRID() + ", RESULTCODE = " + resultCode);
					/** 반송 처리 **/
					if (clientTRID.startsWith("CL")) {
						searchVo.setSwhoisrequestid(clientTRID);
						BigInteger whoisSeq = whoisMgmtTxService.selectTbWhoisVoSeq(searchVo);
						if (whoisSeq != null) {
							Date dbDate = commonService.selectSysDate();
							TbWhoisVo updateVo = new TbWhoisVo();
							updateVo.setNwhoisSeq(whoisSeq);
							updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_REJECT);
							updateVo.setSwhoisresultMsg(swhoisresultMsg);
							updateVo.setDmodifyDt(dbDate);
							result += whoisMgmtTxService.updateResultCdTransactionNew(updateVo);
						}
					}
				} else {
					PrintLogUtil.printLogInfo("REGIST : TRID = " + eppResponseAction.getClientTRID() + ", RESULTCODE = " + resultCode);
					/** 등록완료 처리 **/
					if (clientTRID.startsWith("CL")) {
						searchVo.setSwhoisrequestid(clientTRID);
						BigInteger whoisSeq = whoisMgmtTxService.selectTbWhoisVoSeq(searchVo);
						if (whoisSeq != null) {
							Date dbDate = commonService.selectSysDate();
							TbWhoisVo updateVo = new TbWhoisVo();
							updateVo.setNwhoisSeq(whoisSeq);
							updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_REGIST);
							updateVo.setSwhoisresultMsg(swhoisresultMsg);
							updateVo.setDmodifyDt(dbDate);
							result += whoisMgmtTxService.updateResultCdTransactionNew(updateVo);
						}	
					}
				}
			} catch (ServiceException e) {
				continue;
			} catch (Exception e) {
				continue;
			}
		}
		String backupPath = basePath + File.separator + DateUtils.getCurrentYyyymmdd();
		for(File file : fileList) {
			File dir = new File(backupPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File dest = new File(backupPath + File.separator + file.getName());
			file.renameTo(dest);
		}
		PrintLogUtil.printLogInfo("WHOIS RESULT FILE TOTAL : " + fileList.length + ", UPDATE TOTAL : " + result);
		PrintLogUtil.printLogInfo("END WHOIS RESULT BATCH SCHEDULER===========================================================");
		return result;
	}
	
//	@Transactional(propagation = Propagation.REQUIRED)
//	public int updateWhoisResultList() {
//		PrintLogUtil.printLogInfo("START WHOIS RESULT BATCH SCHEDULER===========================================================");
//		int result = 0;
//		File basePath = new File(configPropertieService.getString("Whois.path"));
//		WhoisFileFilterUtil filenameFilter = new WhoisFileFilterUtil();
//		File fileList[] = basePath.listFiles(filenameFilter);
//		if(fileList == null) {
//			PrintLogUtil.printLogInfo("WHOIS RESULT FILE : 0");
//			return 0;
//		}
//		for(File temp : fileList){
//			try {
//				EPPResponseAction eppResponseAction = eppParserUtil.parserResultFile(temp);
//				if (eppResponseAction == null) {
//					continue;
//				}
//				
//				int resultCode = eppResponseAction.getCode();
//				String clientTRID = eppResponseAction.getClientTRID();
//				String swhoisresultMsg = eppResponseAction.getMsg();
//				TbWhoisVo searchVo = new TbWhoisVo();
//				String backupPath = null;
//				/** 오류 검즘 **/
//				EPPErrorTypeCodes errCode = EPPCodeUtil.findEPPErrorTypeCode(resultCode);
//				if (!errCode.equals(EPPErrorTypeCodes.NONE)) {
//					/** 반송 처리 **/
//					if (clientTRID.startsWith("CL")) {
//						searchVo.setSwhoisrequestid(clientTRID);
//						BigInteger whoisSeq = whoisMgmtTxService.selectTbWhoisVoSeq(searchVo);
//						if (whoisSeq != null) {
//							Date dbDate = commonService.selectSysDate();
//							TbWhoisVo updateVo = new TbWhoisVo();
//							updateVo.setNwhoisSeq(whoisSeq);
//							updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_REJECT);
//							updateVo.setSwhoisresultMsg(swhoisresultMsg);
//							updateVo.setDmodifyDt(dbDate);
//							result += whoisMgmtTxService.updateResultCdTransactionNew(updateVo);
//						}
//					}
//					backupPath = basePath + File.separator + DateUtils.getCurrentYyyymmdd() + File.separator + errCode.getPath();
//				} else {
//					/** 등록완료 처리 **/
//					if (clientTRID.startsWith("CL")) {
//						searchVo.setSwhoisrequestid(clientTRID);
//						BigInteger whoisSeq = whoisMgmtTxService.selectTbWhoisVoSeq(searchVo);
//						if (whoisSeq != null) {
//							Date dbDate = commonService.selectSysDate();
//							TbWhoisVo updateVo = new TbWhoisVo();
//							updateVo.setNwhoisSeq(whoisSeq);
//							updateVo.setSwhoisresultCd(CommonCodeUtil.WHOIS_TRANS_TYPE_REGIST);
//							updateVo.setSwhoisresultMsg(swhoisresultMsg);
//							updateVo.setDmodifyDt(dbDate);
//							result += whoisMgmtTxService.updateResultCdTransactionNew(updateVo);
//						}	
//					}
//					backupPath = basePath + File.separator + DateUtils.getCurrentYyyymmdd();
//				}
//				File dir = new File(backupPath);
//				if (!dir.exists()) {
//					dir.mkdirs();
//				}
//				File dest = new File(backupPath + File.separator + temp.getName());
//				temp.renameTo(dest);
//			} catch (ServiceException e) {
//				continue;
//			}
//		}
//		PrintLogUtil.printLogInfo("WHOIS RESULT FILE TOTAL : " + fileList.length + ", UPDATE TOTAL : " + result);
//		PrintLogUtil.printLogInfo("END WHOIS RESULT BATCH SCHEDULER===========================================================");
//		return result;
//	}
	
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//	public int updateWhoisResultList() {
//		int result = 0;
//		File basePath = new File(configPropertieService.getString("Whois.path"));
//		WhoisFileFilterUtil filenameFilter = new WhoisFileFilterUtil();
//		File fileList[] = basePath.listFiles(filenameFilter);
//		if(fileList == null) {
//			return 0;
//		}
//		EPPXMLParser eppXmlParser = new EPPXMLParser();
//		EPPResponseAction eppResponseAction = null;
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
//		for(File temp : fileList){
//			try {
//				Document doc = eppXmlParser.createDocument(temp, true, true);
//				eppResponseAction = new EPPResponseAction();
//				eppResponseAction.mapping(doc);
//				int resultCode = eppResponseAction.getCode();
//				if(resultCode != 2000 && eppResponseAction.getServiceObject() != null){
//					TbWhoisVo tbWhoisVo = new TbWhoisVo();
//					if(resultCode == 11) {
//						tbWhoisVo.setSwhoisresultCd("04");
//					} else if(resultCode >= 1000 && resultCode < 1600){
//						tbWhoisVo.setSwhoisresultCd("03");
//					}
//					if(eppResponseAction.getServiceObject().getNamespace().equals(EPPCommand.NS_IPV4RTN)) {
//						EPPIPv4RtnObject eppIPv4RtnObject = ((EPPIPv4RtnObject)eppResponseAction.getServiceObject());
//						tbWhoisVo.setSfirstAddr(eppIPv4RtnObject.getStartIP());
//						tbWhoisVo.setSlastAddr(eppIPv4RtnObject.getEndIP());
//					} else if(eppResponseAction.getServiceObject().getNamespace().equals(EPPCommand.NS_IPV4INF)) {
//						EPPIPv4InfObject eppIPv4InfObject = ((EPPIPv4InfObject)eppResponseAction.getServiceObject());
//						tbWhoisVo.setSfirstAddr(eppIPv4InfObject.getStartIP());
//						tbWhoisVo.setSlastAddr(eppIPv4InfObject.getEndIP());
//					} else if(eppResponseAction.getServiceObject().getNamespace().equals(EPPCommand.NS_IPV4MOD)) {
//						EPPIPv4ModObject eppIPv4ModObject = ((EPPIPv4ModObject)eppResponseAction.getServiceObject());
//						tbWhoisVo.setSfirstAddr(eppIPv4ModObject.getStartIP());
//						tbWhoisVo.setSlastAddr(eppIPv4ModObject.getEndIP());
//						tbWhoisVo.setSnetNm(eppIPv4ModObject.getNetName());
//					} else if(eppResponseAction.getServiceObject().getNamespace().equals(EPPCommand.NS_IPV4NEW)) {
//						EPPIPv4NewObject eppIPv4NewObject = ((EPPIPv4NewObject)eppResponseAction.getServiceObject());
//						tbWhoisVo.setSfirstAddr(eppIPv4NewObject.getStartIP());
//						tbWhoisVo.setSlastAddr(eppIPv4NewObject.getEndIP());
//						tbWhoisVo.setSnetNm(eppIPv4NewObject.getNetName());
//					} else if(eppResponseAction.getServiceObject().getNamespace().equals(EPPCommand.NS_IPV4ADD)) {
//						EPPIPv4AddObject eppIPv4AddObject = ((EPPIPv4AddObject)eppResponseAction.getServiceObject());
//						tbWhoisVo.setSfirstAddr(eppIPv4AddObject.getStartIP());
//						tbWhoisVo.setSlastAddr(eppIPv4AddObject.getEndIP());
//						tbWhoisVo.setSnetNm(eppIPv4AddObject.getNetName());
//					}
//					String clientTRID = eppResponseAction.getClientTRID();
//					String swhoisresultMsg = eppResponseAction.getMsg();
//					tbWhoisVo.setSwhoisrequestid(clientTRID);
//					tbWhoisVo.setSwhoisresultMsg(swhoisresultMsg);
//					BigInteger seq = whoisMgmtTxService.selectTbWhoisVoSeq(tbWhoisVo);
//					tbWhoisVo.setNwhoisSeq(seq);
//					int updateCount = whoisMgmtTxService.updateWhois(tbWhoisVo);
//					if(updateCount == 1) {
//						
//						if(result == 0){
//							File dir = new File(temp.getParent() + File.separator + simpleDateFormat.format(new Date()));
//							if(!dir.exists()) {
//								dir.mkdir();
//							}
//						}
//						File destFile = new File(temp.getParent() + File.separator + simpleDateFormat.format(new Date()) + File.separator + temp.getName());
//						temp.renameTo(destFile);
//					}
//					result += updateCount;
//				} else if(resultCode == 2000){
//					temp.delete();
//				} else {
//					File dir = new File(temp.getParent() + File.separator + simpleDateFormat.format(new Date()) + "_not_welformed");
//					if(!dir.exists()){
//						dir.mkdir();
//						File dst = new File(temp.getParent() + File.separator + simpleDateFormat.format(new Date()) + "_not_welformed" + File.separator + temp.getName());
//						temp.renameTo(dst);
//					}
//				}
//			} catch (FileNotFoundException e) {
//				continue;
//			} catch (EPPInvalidException e) {
//				continue;
//			} catch (EPPXMLParseException e) {
//				continue;
//			}
//		}
//		return result;
//	}
	
	@Transactional(readOnly=true)
	public TbWhoisKeywordListVo selectListTbWhoisKeyword(TbWhoisKeywordVo tbWhoisKeywordVo){
		if(tbWhoisKeywordVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		TbWhoisKeywordListVo resultListVo = null;
		try {
			List<TbWhoisKeywordVo> tbWhoisKeywordVos =  whoisMgmtTxService.selectListTbWhoisKeyword(tbWhoisKeywordVo);
			int totalCount = whoisMgmtTxService.countListPageTbWhoisKeyword(tbWhoisKeywordVo);
			resultListVo = new TbWhoisKeywordListVo();
			resultListVo.setTbWhoisKeywordVos(tbWhoisKeywordVos);
			resultListVo.setTotalCount(totalCount);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"대체 키워드 목록"});
		}
		return resultListVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertWhoisKeywordVo(TbWhoisKeywordVo tbWhoisKeywordVo){
		int resultCount;
		if(tbWhoisKeywordVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			int count = whoisMgmtTxService.countListTbWhoisKeyword(tbWhoisKeywordVo);
			if(count > 0){
				throw new ServiceException("CMN.HIGH.00001");
			}
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		try {
			resultCount = whoisMgmtTxService.insertTbWhoisKeyword(tbWhoisKeywordVo);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"대체 키워드"});
		}
		return resultCount;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteWhoisKeyword(TbWhoisKeywordListVo tbWhoisKeywordListVo) {
		List<TbWhoisKeywordVo> tbWhoisKeywordVos = tbWhoisKeywordListVo.getTbWhoisKeywordVos();
		int resultCount = 0;
		try {
			for(TbWhoisKeywordVo temp : tbWhoisKeywordVos) {
				int resultCode = whoisMgmtTxService.deleteWhoisKeyword(temp);
				if(resultCode == 1) {
					resultCount = resultCount + resultCode;
				} else {
					throw new ServiceException("CMN.HIGH.00000");
				}
			}
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"대체 키워드"});
		}
		return resultCount;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int insertWhoisComplex(TbWhoisComplexVo tbWhoisComplexVo) {
		int result;
		if(tbWhoisComplexVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			int whoisResult = whoisMgmtTxService.insertWhois(tbWhoisComplexVo.getTbWhoisVo());
			if(whoisResult == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			int whoisUserResult = updateWhoisUser(tbWhoisComplexVo.getTbWhoisUserVo());
			if(whoisUserResult == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			result = whoisResult & whoisUserResult;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"Whois 정보"});
		}
		return result;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * WHOIS 대체 키워드 목록 조회
	 * @param tbWhoisKeywordVo
	 * @return
	 */
	@Transactional(readOnly=true)
	public TbWhoisKeywordListVo selectListTbWhoisKeywordNew(TbWhoisKeywordVo tbWhoisKeywordVo){
		if(tbWhoisKeywordVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		TbWhoisKeywordListVo resultListVo = null;
		try {
			List<TbWhoisKeywordVo> tbWhoisKeywordVos =  whoisMgmtTxService.selectListTbWhoisKeywordNew(tbWhoisKeywordVo);
			int totalCount = whoisMgmtTxService.countListPageTbWhoisKeywordNew(tbWhoisKeywordVo);
			resultListVo = new TbWhoisKeywordListVo();
			resultListVo.setTbWhoisKeywordVos(tbWhoisKeywordVos);
			resultListVo.setTotalCount(totalCount);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"대체 키워드 목록"});
		}
		return resultListVo;
	}
	
	/**
	 * WHOIS 대체 키워드 등록 
	 * @param tbWhoisKeywordVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertWhoisKeywordNewVo(TbWhoisKeywordVo tbWhoisKeywordVo){
		int resultCount;
		if(tbWhoisKeywordVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			int count = whoisMgmtTxService.countListTbWhoisKeywordNew(tbWhoisKeywordVo);
			if(count > 0){
				throw new ServiceException("CMN.HIGH.00001");
			}
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		try {
			resultCount = whoisMgmtTxService.insertTbWhoisKeywordNew(tbWhoisKeywordVo);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"대체 키워드"});
		}
		return resultCount;
	}
	
	/**
	 * WHOIS 대체 키워드 삭제
	 * @param tbWhoisKeywordListVo
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteWhoisKeywordNew(TbWhoisKeywordListVo tbWhoisKeywordListVo) {
		List<TbWhoisKeywordVo> tbWhoisKeywordVos = tbWhoisKeywordListVo.getTbWhoisKeywordVos();
		int resultCount = 0;
		try {
			for(TbWhoisKeywordVo temp : tbWhoisKeywordVos) {
				int resultCode = whoisMgmtTxService.deleteWhoisKeywordNew(temp);
				if(resultCode == 1) {
					resultCount = resultCount + resultCode;
				} else {
					throw new ServiceException("CMN.HIGH.00000");
				}
			}
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"대체 키워드"});
		}
		return resultCount;
	}
	
	/**
	 * WHOIS 정보수정 팝업 수정
	 * @param tbWhoisComplexVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateWhoisComplexNew(TbWhoisComplexVo tbWhoisComplexVo) {
		int result;
		
		if(tbWhoisComplexVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		if(tbWhoisComplexVo.isNullTbWhoisVo() || tbWhoisComplexVo.isNullTbWhoisUserVo()) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			int whoisResult = whoisMgmtTxService.updateWhoisNew(tbWhoisComplexVo.getTbWhoisVo());
			if(whoisResult == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			int whoisUserResult = whoisMgmtTxService.updateWhoisUserNew(tbWhoisComplexVo.getTbWhoisUserVo());
			if(whoisUserResult == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			result = whoisResult & whoisUserResult;
			
			// WHOIS 전송
			com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo tbWhoisVo = new com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo();
			tbWhoisVo.setNwhoisSeq(tbWhoisComplexVo.getTbWhoisVo().getNwhoisSeq());
			tbWhoisVo.setSmodifyId(tbWhoisComplexVo.getTbWhoisVo().getSmodifyId());
			tbWhoisVo.setType("DEFAULT");
			
			whoisAdapterService.sendToWhois(tbWhoisVo);
			
			String srequestCd = tbWhoisComplexVo.getTbWhoisVo().getSrequestCd();
			
			String strCount = null;
			// 신규/변경신청서 변경 시 추가신청서가 있을 경우 추가신청서 정보도 변경
			if("01".equals(srequestCd) || "04".equals(srequestCd)) {
			
				TbWhoisComplexVo selTbWhoisVo = whoisMgmtTxService.selectListTbWhoisControlIp(tbWhoisComplexVo.getTbWhoisVo());
				
				if(!selTbWhoisVo.isNullTbWhoisVo()) {
				
					if("04".equals(selTbWhoisVo.getTbWhoisVo().getSwhoisresultCd())) {
						TbWhoisVo vo = new TbWhoisVo();
						vo.setType("NEW_MOD");
						vo.setNwhoisSeq(tbWhoisComplexVo.getTbWhoisVo().getNwhoisSeq());
						vo.setSsaid(tbWhoisComplexVo.getTbWhoisVo().getSsaid());
						strCount =  whoisMgmtTxService.selectWhoisComplexNew(vo);
					}
				}
				
			}
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"Whois 정보"});
		}
		return result;
	}
	
	/**
	 * WHOIS정보변경신청 - IP주소 조회
	 * @param tbWhoisModifyVo
	 * @return
	 */
	@Transactional(readOnly=true)
	public TbWhoisModifyListVo selectListTbWhoisModReqIp(TbWhoisModifyVo tbWhoisModifyVo){
		if(tbWhoisModifyVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		TbWhoisModifyListVo resultListVo = null;
		try {
			List<TbWhoisModifyVo> tbWhoisModifyVos =  whoisMgmtTxService.selectListTbWhoisModReqIp(tbWhoisModifyVo);
			int totalCount = whoisMgmtTxService.countListPageTbWhoisModReqIp(tbWhoisModifyVo);
			resultListVo = new TbWhoisModifyListVo();
			resultListVo.setTbWhoisModifyVos(tbWhoisModifyVos);
			resultListVo.setTotalCount(totalCount);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois IP 정보"});
		}
		
		return resultListVo;
	}
	
	/**
	 * WHOIS정보변경신청 - 등록(변경신청)
	 * @param tbWhoisModifyVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertWhoisModifyVo(TbWhoisModifyVo tbWhoisModifyVo){
		
		int resultCount;
		if(tbWhoisModifyVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try {
			int count = whoisMgmtTxService.countTbWhoisModifyVo(tbWhoisModifyVo);
			
			if(count > 0){
				throw new ServiceException("CMN.INFO.00054", new String[]{"진행중인 변경신청 내역이 존재합니다."});
				// throw new ServiceException("CMN.HIGH.00001");
			}
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		try {
			if(tbWhoisModifyVo.getsStatCd().equals("10")) {			// 신청
				tbWhoisModifyVo.setsStatNm(CommonCodeUtil.WHOIS_MODIFY_REQ);
			}
			
			resultCount = whoisMgmtTxService.insertTbWhoisModifyVo(tbWhoisModifyVo);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"Whois정보변경신청"});
		}
		return resultCount;
	}
	
	/**
	 *  WHOIS 정보 변경 신청 목록 조회
	 * @param searchVo
	 * @return
	 */
	public TbWhoisModifyListVo selectListPageTbWhoisModifyVo(TbWhoisModifyVo searchVo) {
		TbWhoisModifyListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbWhoisModifyVo> resultList = whoisMgmtTxService.selectListPageTbWhoisModifyVo(searchVo);
			int totalCount = whoisMgmtTxService.countListTbWhoisModifyVo(searchVo);
			resultListVo = new TbWhoisModifyListVo();
			resultListVo.setTbWhoisModifyVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois정보변경신청 내역"});
		}
		
		return resultListVo;
	}
	
	/**
	 * WHOIS 정보 변경 신청 목록 엑셀 다운로드
	 * @param searchVo
	 * @return
	 */
	public TbWhoisModifyListVo viewListWhoisModReqExcel(TbWhoisModifyVo searchVo) {
		TbWhoisModifyListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			int totalCount = whoisMgmtTxService.countListTbWhoisModifyVo(searchVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<TbWhoisModifyVo> resultList = null;
			
			if(totalCount > 0){
				searchVo.setFirstIndex(1);
				searchVo.setLastIndex(totalCount);
				resultList = whoisMgmtTxService.selectListPageTbWhoisModifyVo(searchVo);
			}
			
			resultListVo = new TbWhoisModifyListVo();
			resultListVo.setTbWhoisModifyVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois정보변경신청 내역 엑셀저장"});
		}
		
		return resultListVo;
	}
	
	
	/**
	 * WHOIS 정보 변경 신청 상세조회
	 * @param tbWhoisModifyVo
	 * @return
	 */
	public TbWhoisModifyVo selectTbWhoisModifyVo(TbWhoisModifyVo tbWhoisModifyVo) {
		TbWhoisModifyVo resultVo = null;
		if(tbWhoisModifyVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			resultVo = whoisMgmtTxService.selectTbWhoisModifyVo(tbWhoisModifyVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois정보변경신청 상세정보"});
		}
		
		return resultVo;
	}
	
	/**
	 *  WHOIS정보변경신청 - 수정
	 * @param updateVo
	 * @return
	 */
	public TbWhoisModifyVo updateWhoisModReqVo(TbWhoisModifyVo updateVo) {
		if(updateVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			whoisMgmtTxService.updateWhoisModReqVo(updateVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"Whois정보변경신청"});
		}
		
		return updateVo;
	}
	
	/**
	 * WHOIS정보변경신청 - 수정(변경신청취소)
	 * @param deleteVo
	 * @return
	 */
	public TbWhoisModifyVo deleteWhoisModReqVo(TbWhoisModifyVo deleteVo) {
		if(deleteVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			whoisMgmtTxService.deleteWhoisModReqVo(deleteVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00022", new String[]{"Whois정보변경신청"});
		}
		
		return deleteVo;
	}
	
	/**
	 * WHOIS정보변경신청 - 승인/반려
	 * @param tbWhoisModifyVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateWhoisModReqAppr(TbWhoisModifyVo tbWhoisModifyVo) {
		int resultCount;
		String whoisResultMsg = "";
		String whoisResultCode = "";
		com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo resultSocketVo = new com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo();
		if(tbWhoisModifyVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		TbWhoisModifyVo socketVo = new TbWhoisModifyVo();
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			
			if(tbWhoisModifyVo.getsStatCd().equals("20")) {	// 승인
				
				socketVo = whoisMgmtTxService.selectTbWhoisModifyVo(tbWhoisModifyVo);
				String newClTrid = socketVo.getSwhoisrequestid() + "-" + simpleDateFormat.format(new Date());
				socketVo.setSwhoisrequestid(newClTrid);
				socketVo.setsModifyId(tbWhoisModifyVo.getsModifyId());
				// int cnt = whoisAdapterService.sendToWhoisModify(socketVo);
				
				resultSocketVo = whoisAdapterService.sendToWhoisModify(socketVo);
				
				whoisResultMsg = resultSocketVo.getSwhoisresultMsg();
				whoisResultCode =resultSocketVo.getSwhoisresultCd();
				
//				if(whoisResultCode != "" && whoisResultCode.equals(CommonCodeUtil.WHOIS_TRANS_TYPE_REJECT)) { //Codeeyes-Urgent-객체 비교시 == , != 사용제한
				if(whoisResultCode.equals("") && whoisResultCode.equals(CommonCodeUtil.WHOIS_TRANS_TYPE_REJECT)) {
					whoisResultMsg = whoisResultMsg.substring(whoisResultMsg.indexOf("]") +2, whoisResultMsg.length());
					throw new ServiceException("CMN.INFO.00054", new String[]{whoisResultMsg});
				}
				
				tbWhoisModifyVo.setsStatNm(CommonCodeUtil.WHOIS_MODIFY_APPR);		// 승인
				tbWhoisModifyVo.setSreject_rsn("");
				
			} else if(tbWhoisModifyVo.getsStatCd().equals("30")) {	// 반려
				
				
				tbWhoisModifyVo.setsStatNm(CommonCodeUtil.WHOIS_MODIFY_REJECT);
			}
			
		
			resultCount = whoisMgmtTxService.updateWhoisModReqAppr(tbWhoisModifyVo);
			
			if(resultCount == 0) {
				throw new ServiceException("CMN.HIGH.00000");
			} 
			
			if(tbWhoisModifyVo.getsStatCd().equals("20")) {	// 승인
				// TbWhoisUser 반영
				TbWhoisUserVo tbWhoisUserVo = new TbWhoisUserVo();
				tbWhoisUserVo.setSorgname(socketVo.getsAftOrgName());
				tbWhoisUserVo.setSaddr(socketVo.getsAftOrgAddr());
				tbWhoisUserVo.setSaddrDetail(socketVo.getsAftOrgAddrDetail());
				tbWhoisUserVo.setSzipcode(socketVo.getsAftZipCode());
				tbWhoisUserVo.setSeorgname(socketVo.getsAftEOrgName());
				tbWhoisUserVo.setSeaddr(socketVo.getsAftEOrgAddr());
				tbWhoisUserVo.setSeaddrDetail(socketVo.getsAftEOrgAddrDetail());
				tbWhoisUserVo.setSmodifyId(socketVo.getsModifyId());
				tbWhoisUserVo.setStransKind("USER");
				tbWhoisUserVo.setSsaid(socketVo.getSsaid());
				whoisMgmtTxService.updateTbWhoisUserTrans(tbWhoisUserVo);
				
				// TbWhois 반영
				TbWhoisVo tbWhoisVo = new TbWhoisVo();
				
				tbWhoisVo.setNwhoisSeq(socketVo.getNwhoisseq());
				tbWhoisVo.setSmodifyId(socketVo.getsModifyId());
				tbWhoisVo.setStransKind("USER");
				whoisMgmtTxService.updateTbWhoisTrans(tbWhoisVo);
			
			}
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"Whois정보변경신청"});
		}
		
		return resultCount;
	}
	
	/**
	 * WHOIS정보공개관리 - 삭제
	 * @param deleteVo
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteTbWhoisVo(TbWhoisVo deleteVo) {
		//List<TbWhoisKeywordVo> tbWhoisKeywordVos = deleteVo.getTbWhoisKeywordVos();
		List<BigInteger> list = deleteVo.getDelList();
		int resultCount = 0;
		
		TbWhoisVo tbWhoisVo = new TbWhoisVo();
		try {
			
			for(BigInteger seq : list) {
				tbWhoisVo.setNwhoisSeq(seq);
				int resultCode = whoisMgmtTxService.deleteTbWhoisUserVo(tbWhoisVo);
				resultCode = whoisMgmtTxService.deleteTbWhoisVo(tbWhoisVo);
				
				if(resultCode == 1) {
					resultCount = resultCount + resultCode;
				} else {
					throw new ServiceException("CMN.HIGH.00000");
				}
			}
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00022", new String[]{"Whois 정보"});
		}
		return resultCount;
	}
	
	/**
	 * WHOIS정보공개관리 - DB 현행화
	 * @param pageType
	 * @param matchVo
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public String matchTbWhoisVo(String pageType, TbWhoisVo matchVo) {
	
		String resultCode = null;
		TbWhoisVo tbWhoisVo = new TbWhoisVo();
		
		try {
			
			if("LIST".equals(pageType)) {
				List<BigInteger> list = matchVo.getMatchList();
				for(BigInteger seq : list) {
					
					tbWhoisVo.setType("DB_MATCH_UPDATE");
					tbWhoisVo.setNwhoisSeq(seq);
					// db 업데이트 후
					resultCode = whoisMgmtTxService.matchTbWhoisVo(tbWhoisVo);

					if(resultCode.equals("SUCCESS")) {
						resultCode = null;
						// 같은 said 건들 정보 수정(추가신청서)
						tbWhoisVo.setType("DB_MATCH_UPDATE_ADD");
						resultCode = whoisMgmtTxService.matchTbWhoisVo(tbWhoisVo);
						
						if(!resultCode.equals("SUCCESS")) {
							throw new ServiceException("CMN.HIGH.00000");
						} 
						
						// whois 전송
						com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo submitVo = new com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo();
						submitVo.setType(null);
						submitVo.setNwhoisSeq(seq);
						submitVo.setScreateId(matchVo.getSmodifyId());
						submitVo.setSmodifyId(matchVo.getSmodifyId());
						whoisService.whoisSubmit(submitVo);
						
						TbWhoisComplexVo selTbWhoisVo = whoisMgmtTxService.selectListTbWhoisControlIp(tbWhoisVo);
						
						if(selTbWhoisVo.isNullTbWhoisVo()) {
							throw new ServiceException("CMN.HIGH.00000");
						}
						
						if("04".equals(selTbWhoisVo.getTbWhoisVo().getSwhoisresultCd())) {
							resultCode = "SUCCESS";
						} else {
							resultCode = selTbWhoisVo.getTbWhoisVo().getSwhoisresultCd();
						}
						
					} else {
						throw new ServiceException("CMN.HIGH.00000");
					}
				}
			} else {
				tbWhoisVo.setType("DB_MATCH_UPDATE");
				tbWhoisVo.setNwhoisSeq(matchVo.getNwhoisSeq());
				// db 업데이트 후
				resultCode = whoisMgmtTxService.matchTbWhoisVo(tbWhoisVo);

				if(resultCode.equals("SUCCESS")) {
					resultCode = null;
					// 같은 said 건들 정보 수정(추가신청서)
					tbWhoisVo.setType("DB_MATCH_UPDATE_ADD");
					resultCode = whoisMgmtTxService.matchTbWhoisVo(tbWhoisVo);
					
					if(!resultCode.equals("SUCCESS")) {
						throw new ServiceException("CMN.HIGH.00000");
					} 
					
					// whois 전송
					com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo submitVo = new com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo();
					submitVo.setType(null);
					submitVo.setNwhoisSeq(matchVo.getNwhoisSeq());
					submitVo.setScreateId(matchVo.getSmodifyId());
					submitVo.setSmodifyId(matchVo.getSmodifyId());
					whoisService.whoisSubmit(submitVo);
					
					TbWhoisComplexVo selTbWhoisVo = whoisMgmtTxService.selectListTbWhoisControlIp(tbWhoisVo);
					
					if(selTbWhoisVo.isNullTbWhoisVo()) {
						throw new ServiceException("CMN.HIGH.00000");
					}
					
					if("04".equals(selTbWhoisVo.getTbWhoisVo().getSwhoisresultCd())) {
						resultCode = "SUCCESS";
					} else {
						resultCode = selTbWhoisVo.getTbWhoisVo().getSwhoisresultCd();
					}
					
				} else {
					throw new ServiceException("CMN.HIGH.00000");
				}
			}
			
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00022", new String[]{"Whois 정보"});
		}
		return resultCode;
	}
	
	/**
	 * TB_WHOIS  Update
	 * @param updateVo
	 * @return
	 */
	public TbWhoisVo updateTbWhoisTrans (TbWhoisVo updateVo) {
		if(updateVo == null) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		try {
			whoisMgmtTxService.updateTbWhoisTrans(updateVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		
		return updateVo;
	}
	
	/**
	 * TB_WHOIS_USER  Update
	 * @param updateVo
	 * @return
	 */
	public TbWhoisUserVo updateTbWhoisUserTrans (TbWhoisUserVo updateVo) {
		if(updateVo == null) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		try {
			whoisMgmtTxService.updateTbWhoisUserTrans(updateVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		
		return updateVo;
	}
	
	
	/**
	 * Whois 정보공개관리 엑셀 다운로드
	 * @param searchVo
	 * @return
	 */
	public TbWhoisVo viewListWhoisExcel (TbWhoisVo searchVo) {
		
		TbWhoisVo resultListVo = null;
		try {
			if (searchVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbWhoisVo> resultList = null; 
			int totalCount = whoisMgmtTxService.countListPageWhois(searchVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			
			if(totalCount > 0){
				searchVo.setFirstIndex(1);
				searchVo.setLastIndex(totalCount);
				resultList = whoisMgmtTxService.selectListPageWhois(searchVo);
			}

			resultListVo = new TbWhoisVo();
			resultListVo.setTbWhoisVos(resultList);
			resultListVo.setTotalCount(totalCount);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois정보공개관리 내역 엑셀저장"});
		}
		return resultListVo;
		
	}
	
	/**
	 * whois kisa 정보 조회 및 관리 - IP주소 조회
	 * @param tbWhoisVo
	 * @return
	 */
	@Transactional(readOnly=true)
	public TbWhoisComplexVo selectListTbWhoisControl(TbWhoisVo tbWhoisVo){
		if(tbWhoisVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		TbWhoisComplexVo result = null;
		try {
			result =  whoisMgmtTxService.selectListTbWhoisControlIp(tbWhoisVo);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois IP 정보"});
		}
		
		return result;
	}
	
	/**
	 * Whois 반송 건 조회
	 * @param tbWhoisVo
	 * @return
	 */
	@Transactional(readOnly=true)
	public String selectWhoisComplexNew(TbWhoisVo tbWhoisVo){
		if(tbWhoisVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		String strCount = null;
		try {
			strCount =  whoisMgmtTxService.selectWhoisComplexNew(tbWhoisVo);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois 정보"});
		}
		
		return strCount;
	}
	
	/**
	 * WHOIS 정보수정 팝업 수정
	 * @param tbWhoisComplexVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateWhoisComplexNew2 (TbWhoisComplexVo tbWhoisComplexVo) {
		int result;
		
		if(tbWhoisComplexVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		if(tbWhoisComplexVo.isNullTbWhoisVo() || tbWhoisComplexVo.isNullTbWhoisUserVo()) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			int whoisResult = whoisMgmtTxService.updateWhoisNew(tbWhoisComplexVo.getTbWhoisVo());
			if(whoisResult == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			int whoisUserResult = whoisMgmtTxService.updateWhoisUserNew(tbWhoisComplexVo.getTbWhoisUserVo());
			if(whoisUserResult == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			result = whoisResult & whoisUserResult;
			
			if(result != 1) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			// WHOIS 전송
			com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo tbWhoisVo = new com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo();
			tbWhoisVo.setNwhoisSeq(tbWhoisComplexVo.getTbWhoisVo().getNwhoisSeq());
			tbWhoisVo.setSmodifyId(tbWhoisComplexVo.getTbWhoisVo().getSmodifyId());
			tbWhoisVo.setType(tbWhoisComplexVo.getTbWhoisVo().getType());
			
			whoisAdapterService.sendToWhois(tbWhoisVo); 
			
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"Whois 정보"});
		}
		return result;
	}
	
	/**
	 * Whois Kisa ip 해지
	 * @param deleteVo
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo deleteKisaIp(TbWhoisVo deleteVo) {
		
		com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo result = new com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo();
		try {
		
			com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisComplexVo tbWhoisComplexVo = new com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisComplexVo();
			
			com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo tbWhoisVo = new com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo();
			tbWhoisVo.setSfirstAddr(deleteVo.getSfirstAddr());
			tbWhoisVo.setSlastAddr(deleteVo.getSlastAddr());
			tbWhoisVo.setType("DEL_KISA_IP");
			tbWhoisVo.setSrequestCd(CommonCodeUtil.WHOIS_REQUEST_TYPE_RTN);
			tbWhoisVo.setSsvccommnet("kisa ip 회선 해지");
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String newClTrid = simpleDateFormat.format(new Date());
			
			tbWhoisVo.setSwhoisrequestid(newClTrid);
			
			tbWhoisComplexVo.setTbWhoisVo(tbWhoisVo);
			
			
			result = whoisService.whoisRtnSubmit(tbWhoisComplexVo);
			
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00022", new String[]{"Whois 정보"});
		}
		return result;
	}
	
	@Transactional(readOnly = true)
	public TbWhoisComplexVo selectWhoisAlloc(TbWhoisVo tbWhoisVo) {
		TbWhoisComplexVo resultVo = null;
		try {
			if(tbWhoisVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = whoisMgmtTxService.selectWhoisAlloc(tbWhoisVo);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Whois"});
		}
		return resultVo;
	}
	
	/**
	 *  DB 현행화 전송 건 목록 화면 로드 & 조회
	 * @param tbWhoisVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbWhoisListVo selectListDbMatch(TbWhoisVo tbWhoisVo) {
		TbWhoisListVo resultListVo = null;
		try {
			if (tbWhoisVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbWhoisVo> resultList = whoisMgmtTxService.selectListDbMatch(tbWhoisVo);
			int totalCount = whoisMgmtTxService.countListDbMatch(tbWhoisVo);
			resultListVo = new TbWhoisListVo();
			resultListVo.setTbWhoisVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"DB현행화 전송 목록"});
		}
		return resultListVo;
	}
	
	
}
