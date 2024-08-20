package com.kt.ipms.legacy.cmn.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.dao.CommonDao;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.IpCalculateUtil;
import com.kt.ipms.legacy.cmn.util.IpVoSortCompare;
import com.kt.ipms.legacy.cmn.vo.IpVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.ipmgmt.createmgmt.vo.TbIpBlockMstVo;
import com.kt.ipms.legacy.ipmgmt.linemgmt.vo.TbIpAssignSubVo;

@Component
public class IpCommonService {
	
	@Autowired
	private IpCalculateUtil ipCalculateUtil;
	
	@Autowired
	private CommonDao commonDao;
	/**
	 * IP 블럭 기준 정보 설정
	 * @param paramVo
	 * @throws Exception
	 */
	public void setBaseIpBlockMstInfo(IpVo paramVo) {
		
		if (paramVo == null 
			|| paramVo.getPipPrefix() == null || paramVo.getPipPrefix().equals("")
			|| paramVo.getSipVersionTypeCd() == null || paramVo.getSipVersionTypeCd().equals("")) {
			
			throw new ServiceException("CMN.HIGH.00001"); // 파라미터 Null
		} else if (!paramVo.getSipVersionTypeCd().equals(CommonCodeUtil.IPV4) 
			&& !paramVo.getSipVersionTypeCd().equals(CommonCodeUtil.IPV6)) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		String pipPrefix = paramVo.getPipPrefix();
		if (pipPrefix.lastIndexOf("/") == -1) {
			throw new ServiceException("APP.INFO.00034");
		}
		StringTokenizer st = new StringTokenizer(pipPrefix, "/");
		String sipBlock = (String) st.nextToken();
		int nbitmask = 0;
		try {
			nbitmask = Integer.parseInt(st.nextToken(), 10);
		} catch (NumberFormatException e) {
			throw new ServiceException("APP.INFO.00034");
		}
		String sipVersionTypeCd = paramVo.getSipVersionTypeCd();
		if (sipVersionTypeCd.equals(CommonCodeUtil.IPV4) && (nbitmask > 32 || nbitmask < 1)) {
			throw new ServiceException("APP.INFO.00034");
		} else if (sipVersionTypeCd.equals(CommonCodeUtil.IPV6) && (nbitmask > 128 || nbitmask < 1)) {
			throw new ServiceException("APP.INFO.00034");
		}
		if (!ipCalculateUtil.isIpPatternValidation(sipVersionTypeCd, sipBlock)) {
			throw new ServiceException("APP.INFO.00034");
		}
		
		/** First IP Infomation **/
		paramVo.setNfirstAddr(ipCalculateUtil.getStartIpAddressDecimal(sipBlock, nbitmask, sipVersionTypeCd));
		paramVo.setSfirstAddr(ipCalculateUtil.getDecimalToStringShort(paramVo.getNfirstAddr(), sipVersionTypeCd));
		paramVo.setSfirstIpPreferred(ipCalculateUtil.getDecimalToStringFull(paramVo.getNfirstAddr(), sipVersionTypeCd));
		paramVo.setSfirstAddrBinary(ipCalculateUtil.getDecimalToStringBinary(paramVo.getNfirstAddr(), sipVersionTypeCd));
		
		/** Last IP Information **/
		paramVo.setNlastAddr(ipCalculateUtil.getEndIpAddressDecimal(sipBlock, nbitmask, sipVersionTypeCd));
		paramVo.setSlastAddr(ipCalculateUtil.getDecimalToStringShort(paramVo.getNlastAddr(), sipVersionTypeCd));
		paramVo.setSlastIpPreferred(ipCalculateUtil.getDecimalToStringFull(paramVo.getNlastAddr(), sipVersionTypeCd));
		paramVo.setSlastAddrBinary(ipCalculateUtil.getDecimalToStringBinary(paramVo.getNlastAddr(), sipVersionTypeCd));
		
		/** NetMask & nClassCnt & ncnt Information **/
		paramVo.setNcnt(paramVo.getNlastAddr().subtract(paramVo.getNfirstAddr()).add(BigInteger.ONE));
		if (sipVersionTypeCd.equals(CommonCodeUtil.IPV4)) {
			BigInteger nnetmask = ipCalculateUtil.getNetMaskDecimal(nbitmask, sipVersionTypeCd);
			paramVo.setSnetmask(ipCalculateUtil.getDecimalToStringShort(nnetmask, sipVersionTypeCd));
			paramVo.setNclassCnt(new BigDecimal(Math.pow(2, CommonCodeUtil.IPV4_BASE_BIT - nbitmask)));
		} else {
			paramVo.setSnetmask(CommonCodeUtil.NOT_AVALIABLE);
			paramVo.setNclassCnt(new BigDecimal(Math.pow(2, CommonCodeUtil.IPV6_BASE_BIT - nbitmask)));
		}
		paramVo.setSipBlock(paramVo.getSfirstAddr());
		paramVo.setNbitmask(nbitmask);
		BigInteger paramBlockDecimal = ipCalculateUtil.getStringToDecimal(sipBlock, sipVersionTypeCd);
		if (paramVo.getNfirstAddr().compareTo(paramBlockDecimal) != 0) {
			throw new ServiceException("APP.INFO.00017", new String[]{paramVo.getPipPrefix(), paramVo.getSfirstAddr() + "/" + paramVo.getNbitmask()});
		}
		paramVo.setPipPrefix(paramVo.getSipBlock() + "/" + paramVo.getNbitmask());
		
	}
	
	/**
	 * IP SUBNETTING 블럭 기준 정보 목록 반환
	 * @param paramVo
	 * @param subnetMask
	 * @return
	 * @throws Exception
	 */
	public List<IpVo> getSubnetIpBlockMstInfo(IpVo paramVo, int subnetMask) {
		
		setBaseIpBlockMstInfo(paramVo);
		
		if (paramVo.getNbitmask() < subnetMask) {
			throw new ServiceException("APP.HIGH.00026");
		}
		
		List<IpVo> resultList = null;
		int count = 0;
		if (paramVo.getSipVersionTypeCd().equals(CommonCodeUtil.IPV4)) {
			BigInteger bitMaskCount = ipCalculateUtil.calculateSqrd(CommonCodeUtil.IPV4_MAX_PREFIX - paramVo.getNbitmask());
			BigInteger subnetMaskCount = ipCalculateUtil.calculateSqrd(CommonCodeUtil.IPV4_MAX_PREFIX - subnetMask);
			count = bitMaskCount.divide(subnetMaskCount).intValue();
			resultList = new ArrayList<IpVo>();
			for(Integer i = 0; i < count; i++) {
				String pipPrefix = "";
				if (i == 0) {
					pipPrefix = paramVo.getSipBlock() + "/" + subnetMask;
				} else {
					BigInteger nipAddr = paramVo.getNfirstAddr().add(subnetMaskCount.multiply(new BigInteger(i.toString())));
					pipPrefix = ipCalculateUtil.getDecimalToStringShort(nipAddr, CommonCodeUtil.IPV4) + "/" + subnetMask;
				}
				IpVo addIpVo = new IpVo();
				addIpVo.setPipPrefix(pipPrefix);
				addIpVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
				setBaseIpBlockMstInfo(addIpVo);
				resultList.add(addIpVo);
			}
		} else {
			BigInteger bitMaskCount = ipCalculateUtil.calculateSqrd(CommonCodeUtil.IPV6_MAX_PREFIX - paramVo.getNbitmask());
			BigInteger subnetMaskCount = ipCalculateUtil.calculateSqrd(CommonCodeUtil.IPV6_MAX_PREFIX - subnetMask);
			count = bitMaskCount.divide(subnetMaskCount).intValue();
			resultList = new ArrayList<IpVo>();
			for(Integer i = 0; i < count; i++) {
				String pipPrefix = "";
				if (i == 0) {
					pipPrefix = paramVo.getSipBlock() + "/" + subnetMask;
				} else {
					BigInteger nipAddr = paramVo.getNfirstAddr().add(subnetMaskCount.multiply(new BigInteger(i.toString())));
					pipPrefix = ipCalculateUtil.getDecimalToStringShort(nipAddr, CommonCodeUtil.IPV6) + "/" + subnetMask;
				}
				IpVo addIpVo = new IpVo();
				addIpVo.setPipPrefix(pipPrefix);
				addIpVo.setSipVersionTypeCd(CommonCodeUtil.IPV6);
				setBaseIpBlockMstInfo(addIpVo);
				resultList.add(addIpVo);
			}
		}
		return resultList;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getSubnetIpBlockMstInfo(Object paramVo) {
		
		IpVo paramIpVo = (IpVo) paramVo;
		setBaseIpBlockMstInfo(paramIpVo);
		List<Object> resultList = null;
		
		int count = 0;
		if (paramIpVo.getSipVersionTypeCd().equals(CommonCodeUtil.IPV4)) {
			BigInteger bitMaskCount = ipCalculateUtil.calculateSqrd(CommonCodeUtil.IPV4_MAX_PREFIX - paramIpVo.getNbitmask());
			BigInteger subnetMaskCount = ipCalculateUtil.calculateSqrd(CommonCodeUtil.IPV4_MAX_PREFIX - paramIpVo.getNsubnetmask());
			count = bitMaskCount.divide(subnetMaskCount).intValue();
			resultList = new ArrayList();
			for(Integer i = 0; i < count; i++) {
				String pipPrefix = "";
				if (i == 0) {
					pipPrefix = paramIpVo.getSipBlock() + "/" + paramIpVo.getNsubnetmask();
				} else {
					BigInteger nipAddr = paramIpVo.getNfirstAddr().add(subnetMaskCount.multiply(new BigInteger(i.toString())));
					pipPrefix = ipCalculateUtil.getDecimalToStringShort(nipAddr, CommonCodeUtil.IPV4) + "/" + paramIpVo.getNsubnetmask();
				}
				if (paramVo instanceof TbIpAssignSubVo) {
					TbIpAssignSubVo addIpVo = new TbIpAssignSubVo();
					addIpVo.setPipPrefix(pipPrefix);
					addIpVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
					setBaseIpBlockMstInfo(addIpVo);
					resultList.add(addIpVo);
				} else if (paramVo instanceof TbIpAssignMstVo) {
					TbIpAssignMstVo addIpVo = new TbIpAssignMstVo();
					addIpVo.setPipPrefix(pipPrefix);
					addIpVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
					setBaseIpBlockMstInfo(addIpVo);
					resultList.add(addIpVo);
				} else if (paramVo instanceof TbIpBlockMstVo) {
					TbIpBlockMstVo addIpVo = new TbIpBlockMstVo();
					addIpVo.setPipPrefix(pipPrefix);
					addIpVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
					setBaseIpBlockMstInfo(addIpVo);
					resultList.add(addIpVo);	
				} else {
					IpVo addIpVo = new IpVo();
					addIpVo.setPipPrefix(pipPrefix);
					addIpVo.setSipVersionTypeCd(CommonCodeUtil.IPV4);
					setBaseIpBlockMstInfo(addIpVo);
					resultList.add(addIpVo);
				}
			}
		} else {
			BigInteger bitMaskCount = ipCalculateUtil.calculateSqrd(CommonCodeUtil.IPV6_MAX_PREFIX - paramIpVo.getNbitmask());
			BigInteger subnetMaskCount = ipCalculateUtil.calculateSqrd(CommonCodeUtil.IPV6_MAX_PREFIX - paramIpVo.getNsubnetmask());
			count = bitMaskCount.divide(subnetMaskCount).intValue();
			resultList = new ArrayList<Object>();
			for(Integer i = 0; i < count; i++) {
				String pipPrefix = "";
				if (i == 0) {
					pipPrefix = paramIpVo.getSipBlock() + "/" + paramIpVo.getNsubnetmask();
				} else {
					BigInteger nipAddr = paramIpVo.getNfirstAddr().add(subnetMaskCount.multiply(new BigInteger(i.toString())));
					pipPrefix = ipCalculateUtil.getDecimalToStringShort(nipAddr, CommonCodeUtil.IPV6) + "/" + paramIpVo.getNsubnetmask();
				}
				if (paramVo instanceof TbIpAssignSubVo) {
					TbIpAssignSubVo addIpVo = new TbIpAssignSubVo();
					addIpVo.setPipPrefix(pipPrefix);
					addIpVo.setSipVersionTypeCd(CommonCodeUtil.IPV6);
					setBaseIpBlockMstInfo(addIpVo);
					resultList.add(addIpVo);
				} else if (paramVo instanceof TbIpAssignMstVo) {
					TbIpAssignMstVo addIpVo = new TbIpAssignMstVo();
					addIpVo.setPipPrefix(pipPrefix);
					addIpVo.setSipVersionTypeCd(CommonCodeUtil.IPV6);
					setBaseIpBlockMstInfo(addIpVo);
					resultList.add(addIpVo);
				} else if (paramVo instanceof TbIpBlockMstVo) {
					TbIpBlockMstVo addIpVo = new TbIpBlockMstVo();
					addIpVo.setPipPrefix(pipPrefix);
					addIpVo.setSipVersionTypeCd(CommonCodeUtil.IPV6);
					setBaseIpBlockMstInfo(addIpVo);
					resultList.add(addIpVo);
				} else {
					IpVo addIpVo = new IpVo();
					addIpVo.setPipPrefix(pipPrefix);
					addIpVo.setSipVersionTypeCd(CommonCodeUtil.IPV6);
					setBaseIpBlockMstInfo(addIpVo);
					resultList.add(addIpVo);
				}
			}
		}
		return resultList;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean getMergeIpBlockMstInfo(List paramListVo) {
		Collections.sort(paramListVo, new IpVoSortCompare());
		if (paramListVo.size() > 1) {
			IpVo ipVo1 = (IpVo)paramListVo.get(0);
			IpVo ipVo2 = (IpVo)paramListVo.get(1);
			if (ipVo1.getNbitmask() != ipVo2.getNbitmask()) {
				return false;
			} else {
				int compare = ipVo1.getNlastAddr().add(BigInteger.ONE).compareTo(ipVo2.getNfirstAddr());
				if (compare == 0) {
					String pipPrefix = ipVo1.getSfirstAddr() + "/" + (ipVo2.getNbitmask() - 1);
					String sipVersionTypeCd = ipVo1.getSipVersionTypeCd();
					try {
						if (paramListVo.get(0) instanceof TbIpAssignSubVo) {
							TbIpAssignSubVo mergeVo = new TbIpAssignSubVo();
							mergeVo.setPipPrefix(pipPrefix);
							mergeVo.setSipVersionTypeCd(sipVersionTypeCd);
							setBaseIpBlockMstInfo(mergeVo);
							paramListVo.remove(ipVo1);
							paramListVo.remove(ipVo2);
							paramListVo.add(mergeVo);
							return getMergeIpBlockMstInfo(paramListVo);
						} else if (paramListVo.get(0) instanceof TbIpAssignMstVo) {
							TbIpAssignMstVo mergeVo = new TbIpAssignMstVo();
							mergeVo.setPipPrefix(pipPrefix);
							mergeVo.setSipVersionTypeCd(sipVersionTypeCd);
							setBaseIpBlockMstInfo(mergeVo);
							paramListVo.remove(ipVo1);
							paramListVo.remove(ipVo2);
							paramListVo.add(mergeVo);
							return getMergeIpBlockMstInfo(paramListVo);
						} else if (paramListVo.get(0) instanceof TbIpBlockMstVo) {
							TbIpBlockMstVo mergeVo = new TbIpBlockMstVo();
							mergeVo.setPipPrefix(pipPrefix);
							mergeVo.setSipVersionTypeCd(sipVersionTypeCd);
							setBaseIpBlockMstInfo(mergeVo);
							paramListVo.remove(ipVo1);
							paramListVo.remove(ipVo2);
							paramListVo.add(mergeVo);
							return getMergeIpBlockMstInfo(paramListVo);
						} else {
							IpVo mergeVo = new IpVo();
							mergeVo.setPipPrefix(pipPrefix);
							mergeVo.setSipVersionTypeCd(sipVersionTypeCd);
							setBaseIpBlockMstInfo(mergeVo);
							paramListVo.remove(ipVo1);
							paramListVo.remove(ipVo2);
							paramListVo.add(mergeVo);
							return getMergeIpBlockMstInfo(paramListVo);
						}	
					} catch (ServiceException e) {
						return false;
					} catch (Exception e) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * @param srcIpVo
	 * @param destIpVo
	 * @return
	 * @throws Exception
	 */
	public boolean checkIpRange(IpVo srcIpVo, IpVo destIpVo) {
		boolean isValidate = false;
		int compare = srcIpVo.getNfirstAddr().compareTo(destIpVo.getNlastAddr());
		int compare2= srcIpVo.getNlastAddr().compareTo(destIpVo.getNfirstAddr());
		if (compare == 1 || compare2 == -1) {
			isValidate = true;
		} else {
			isValidate = false;
		}
		return isValidate;
	}
	
	public boolean isIpVersionValidation(String ipAddress) {
		boolean isValidation = false;
		try {
			int ipVersion = commonDao.selectIpVersionValidation(ipAddress);
			if (ipVersion == 4 || ipVersion == 6) {
				return true;
			}
		} catch (Exception e) {
			isValidation = false;
		}
		return isValidation;
	}

}
