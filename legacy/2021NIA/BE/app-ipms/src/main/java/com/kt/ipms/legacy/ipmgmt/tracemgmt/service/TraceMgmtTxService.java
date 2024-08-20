package com.kt.ipms.legacy.ipmgmt.tracemgmt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.TraceParserUtil;
import com.kt.ipms.legacy.ipmgmt.tracemgmt.dao.TbTraceIPHostMstDao;
import com.kt.ipms.legacy.ipmgmt.tracemgmt.vo.TbTraceIPListVo;
import com.kt.ipms.legacy.ipmgmt.tracemgmt.vo.TbTraceIPVo;
import com.kt.ipms.legacy.ipmgmt.tracemgmt.vo.TbTraceIpAssignMstVo;
import com.kt.ipms.legacy.ipmgmt.tracemgmt.vo.TbTraceIpHostMstVo;
import com.kt.ipms.legacy.linkmgmt.socketmgmt.adapter.SocketMgmtAdapterService;

@Component
@Transactional
public class TraceMgmtTxService {
	
	@Autowired
	private SocketMgmtAdapterService socketMgmtAdapterService;
	
	@Autowired
	private TbTraceIPHostMstDao tbTraceIPHostMstDao;
	
	@Autowired
	private TraceParserUtil traceParserUtil;
	
	
	/*Trace Route 실행*/
	/**
	 * @Method	:	excuteTrace
	 * @Date	:	2014. 12. 7.
	 * @Author	:	neoargon
	 * @DESC	:	traceRoute 싱행 요청
	 * @변경이력	:
	 * @param sparam
	 * @return String
	 */
	@Transactional(readOnly = true)
	public String excuteTraceRoute(String sinput){
		String sreturn = "";
		
		try{					
			sreturn = socketMgmtAdapterService.executeTraceRoute(sinput);			
		} catch (ServiceException e){
			throw e;
		} catch (Exception e) {
			throw new ServiceException("LNK.HIGH.00023", new String[]{"Trace Route"}); //CODE 변경 필요
		}
		return sreturn;
	}
	
	@Transactional(readOnly = true)
	public String getMakeTraceCmdQuery(TbTraceIPVo paramVo) {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("traceroute6 ");
			if (paramVo.getSipVersionTypeCd().equals(CommonCodeUtil.IPV4)) {
				sb.append("-4 ");
			} else if (paramVo.getSipVersionTypeCd().equals(CommonCodeUtil.IPV6)) {
				sb.append("-6 ");
			} else {
				throw new ServiceException("CMN.HIGH.00001");
			}
			sb.append("-n -A -m ");
			sb.append(CommonCodeUtil.TRACE_MAX_HOPCNT);
			sb.append(" -w ");
			sb.append(CommonCodeUtil.TRACE_TTL_LIMIT);
			if (StringUtils.hasText(paramVo.getSportNum())) {
				sb.append(" -p ");
				sb.append(paramVo.getSportNum());
			}
			if (StringUtils.hasText(paramVo.getSprotocolTypeCd())) {
				sb.append(" -");
				sb.append(paramVo.getSprotocolTypeCd());
			}
			sb.append(" ");
			sb.append(paramVo.getSipAddress());
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("APP.HIGH.00033", new String[]{"Trace Route 명령어 생성"});
		}
		return sb.toString();
	}
	
	
	@Transactional(readOnly = true)
	public TbTraceIPListVo parseTraceRouteResult(String sipVersionTypeCd, String resultTraceRoute) {
		TbTraceIPListVo resultListVo = null;
		try {
			if (!StringUtils.hasText(sipVersionTypeCd) || !StringUtils.hasText(resultTraceRoute)) {
				throw new ServiceException("CMN.HIGH.00001");
			} else if (!sipVersionTypeCd.equals(CommonCodeUtil.IPV4) && !sipVersionTypeCd.equals(CommonCodeUtil.IPV6)) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			StringTokenizer st = new StringTokenizer(resultTraceRoute, "\r\n");
			if (st.countTokens() > 0) {
				List<TbTraceIPVo> listVo = new ArrayList<TbTraceIPVo>();
				while (st.hasMoreTokens()) {
					String seqData = st.nextToken();
					TbTraceIPVo itemVo = new TbTraceIPVo();
					if (traceParserUtil.checkEmptyTrace(seqData)) {
						itemVo.setSipVersionTypeCd(sipVersionTypeCd);
						itemVo.setNtraceIpSeq(traceParserUtil.matchSeqTrace(seqData));
						if (sipVersionTypeCd.equals(CommonCodeUtil.IPV4)) {
							itemVo.setSipAddress(traceParserUtil.matchIPV4Address(seqData));
						} else {
							itemVo.setSipAddress(traceParserUtil.matchIPV6Address(seqData));
						}
						itemVo.setSasOrgCd(traceParserUtil.matchASOrgCd(seqData));
						
						listVo.add(itemVo);
					}
				}
				resultListVo = new TbTraceIPListVo();
				resultListVo.setTbTraceIPVos(listVo);
				resultListVo.setTotalCount(listVo.size());
			} else {
				throw new ServiceException("LNK.HIGH.00032", new String[]{"Trace Route"});
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("LNK.HIGH.00032", new String[]{"Trace Route"});
		}
		return resultListVo;
	}
	
	// 장비 정보 조회
	/**
	 * @Method	:	selectHostInfo
	 * @Date	:	2014. 12. 7.
	 * @Author	:	neoargon
	 * @DESC	:	Trace Route Host 정보 검색
	 * @변경이력	:
	 * @param tbTraceIPVo
	 * @return TbTraceIPListVo
	 */
	@Transactional(readOnly = true)
	public TbTraceIPListVo selectHostInfo(TbTraceIPListVo tbTraceIPVo){
		
		TbTraceIPListVo returnListVo = null; 
		TbTraceIPVo searchVo = new TbTraceIPVo(); 
		
		StringBuilder sbIPaddr = new StringBuilder();
		String sIpInput ="";
		StringBuilder sbAsOrgCd = new StringBuilder();
		String sAsInput ="";
		
		try{
			if(tbTraceIPVo != null && tbTraceIPVo.getTbTraceIPVos() != null && tbTraceIPVo.getTbTraceIPVos().size() > 0){
				List<TbTraceIPVo> inputListvo =  tbTraceIPVo.getTbTraceIPVos();
				
				for (TbTraceIPVo foreachVo : inputListvo) {
					sbIPaddr.append(foreachVo.getSipAddress());
					sbIPaddr.append("|");
					
					sbAsOrgCd.append(foreachVo.getSasOrgCd());
					sbAsOrgCd.append("|");
				}
				
				sIpInput = sbIPaddr.toString().substring(0, sbIPaddr.toString().length()-1);
				sAsInput = sbAsOrgCd.toString().substring(0, sbAsOrgCd.toString().length()-1);
				
				
				searchVo.setSipaddresslist(sIpInput);
				searchVo.setSasOrgCdList(sAsInput);
				
				List<TbTraceIPVo> resultVos = tbTraceIPHostMstDao.selectTraceHostInfo(searchVo);
				
				if (resultVos !=null)
				{
					returnListVo = new TbTraceIPListVo();
					int totalCnt = resultVos.size();
					returnListVo.setTbTraceIPVos(resultVos);
					returnListVo.setTotalCount(totalCnt);				
				}
				else
				{				
					returnListVo = new TbTraceIPListVo();
					returnListVo.setTotalCount(0);
				}
			}
		} catch (ServiceException e){
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Trace Route 시설정보 "}); //CODE 변경 필요
		}
		
		return returnListVo;
	}
	
	/**
	 * @Method	:	selectHostDetailInfo
	 * @Date	:	2014. 12. 7.
	 * @Author	:	neoargon
	 * @DESC	:	TraceRoute Host 상세 정보 검색
	 * @변경이력	:
	 * @param tbTraceIpHostMstVo
	 * @return TbTraceIpHostMstListVo
	 */
	@Transactional(readOnly = true)
	public TbTraceIpHostMstVo selectHostDetailInfo(TbTraceIpHostMstVo tbTraceIpHostMstVo){	
		
		TbTraceIpHostMstVo resultVos =null;
		
		try {		
			resultVos = tbTraceIPHostMstDao.selectTraceDetailHostInfo(tbTraceIpHostMstVo);				
		} catch (ServiceException e){
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Trace Route 호스트정보"}); //CODE 변경 필요
		}
		
		return resultVos;	
	}
	
	/**
	 * @Method	:	selectTraceDetailAssignInfo
	 * @Date	:	2014. 12. 7.
	 * @Author	:	neoargon
	 * @DESC	:	TraceRoute 할당  상세 정보 검색
	 * @변경이력	:
	 * @param tbTraceIpAssignMstVo
	 * @return TbTraceIpAssignMstListVo
	 */
	@Transactional(readOnly = true)
	public TbTraceIpAssignMstVo selectAssignDetailInfo(TbTraceIpAssignMstVo tbTraceIpAssignMstVo){
		TbTraceIpAssignMstVo resultVos = null;
		
		try{
				resultVos = tbTraceIPHostMstDao.selectTraceDetailAssignInfo(tbTraceIpAssignMstVo);			
			
			
		} catch (ServiceException e){
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Trace Route 할당정보"}); //CODE 변경 필요
		}
		
		return resultVos;
	}
}
