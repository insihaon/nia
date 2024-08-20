package com.kt.ipms.legacy.linkmgmt.common.util;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.service.CommonService;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.linkmgmt.batchmgmt.dao.TbBatchLogDao;
import com.kt.ipms.legacy.linkmgmt.batchmgmt.vo.TbBatchLogVo;
import com.kt.ipms.legacy.linkmgmt.common.dao.TbIpAllocNeossMstDao;
import com.kt.ipms.legacy.linkmgmt.common.dao.TbLnkIpAllocOrderMstDao;
import com.kt.ipms.legacy.linkmgmt.common.dao.TbLnkSvcBasDao;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstListVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbLnkIpAllocOrderMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbLnkSvcBasVo;

/**
 * @FileName 	: LinkUtil.java
 * @Project  	: KT_IPMS
 * @Package  	: com.kt.ipms.legacy.linkmgmt.common.util
 * @Date 		: 2014. 9. 12.
 * @Description :
 */
@Component
public class LinkUtil {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private TbLnkSvcBasDao tbLnkSvcBasDao;
	
	@Autowired
	private TbIpAllocNeossMstDao tbIpAllocNeossMstDao;
	
	@Autowired
	private TbLnkIpAllocOrderMstDao tbLnkIpAllocOrderMstDao;
	
	@Autowired
	private TbBatchLogDao tbBatchLogDao;
	
	/* consumer URI Selecter*/
	/**
	 * @MEthod 	: getconsumerURI
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: Consumer용 타켓 시스템 URI 정보 조회
	 * @변경이력 	:
	 * @param Ifid
	 * @return
	 * @throws Exception
	 */
	public String getconsumerURI(String sIfid){
		
		String reteunValue = "";
		
		TbLnkSvcBasVo requestVo = new TbLnkSvcBasVo();
		TbLnkSvcBasVo responseVo = null;
		
		requestVo.setSifId(sIfid);
				
		try{
		
			responseVo = tbLnkSvcBasDao.getWSURIConfig(requestVo);
			
			if(responseVo != null){
				
				reteunValue = responseVo.getSaddress()+responseVo.getSuri(); 
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("LNK.HIGH.00005");
		}
		
		return reteunValue;		
	}
	
	/* consumer CAllBackURI Selecter*/
	/**
	 * @MEthod 	: getCAllBackURI
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: Consumer용 CallBackURI 조회
	 * @변경이력 	:
	 * @param Ifid
	 * @return
	 * @throws Exception
	 */
	public String getCAllBackURI(String sIfid){
		
		String reteunValue = "";
		
		TbLnkSvcBasVo requestVo = new TbLnkSvcBasVo();
		TbLnkSvcBasVo responseVo = null;
		
		requestVo.setSifId(sIfid);
				
		try{
		
			responseVo = tbLnkSvcBasDao.getWSURIConfig(requestVo);
			
			if(responseVo != null){
				
				reteunValue = "http://tempuri.org/"+responseVo.getSuriMethod(); 
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("LNK.HIGH.00005");
		}
		
		return reteunValue;		
	}
	
	/* String Null Checker */
	/**
	 * @MEthod 	: getString
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: String checker null--> empty 치환
	 * @변경이력 	:
	 * @param Value
	 * @return
	 */
	public  String getString(String sValue){
		
		String returnValue = "";
		
		if(sValue !=null && !sValue.trim().isEmpty())			
			returnValue = sValue;
		
		return returnValue;		
	}
	
	/* 시스템 로그 처리 대상 - consumeNeoss0001Service,consumeNeoss0006Service */
	/**
	 * @MEthod 	: setSystemlog
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: 인터페이스 시스템 로그 저장
	 * @변경이력 	:
	 * @param ifID
	 * @param inputKeyValue
	 * @param resultCD
	 * @param resultMSG
	 */
	public void setSystemlog(String ifID, String inputKeyValue, String resultCD, String resultMSG){
		/*To-Do : Type System Logging */	
		try	{
			if(!resultCD.equals(CommonCodeUtil.SUCCESS_MSG)){
				String logHeader ="/*-------"+this.getString(ifID)+"-------*/";
				String logKey ="/*-------"+this.getString(inputKeyValue)+"-------*/";
				String logCD 	 = "/* C D: "+this.getString(resultCD)+"  */";
				String logMSG 	 = "/* MSG: "+this.getString(resultMSG)+"  */";
				String logFooter 	 = "/*---------------------------------*/";
				
				PrintLogUtil.printLogInfo(logHeader);
				PrintLogUtil.printLogInfo(logKey);
				PrintLogUtil.printLogInfo(logCD);
				PrintLogUtil.printLogInfo(logMSG);
				PrintLogUtil.printLogInfo(logFooter);
			}
		}catch(Exception e){
			setSystemERR(e);
		}
		
		
	}
	
	/**
	 * @MEthod 	: setSystemERR
	 * @Date	: 2014. 9. 25.
	 * @Author	: neoargon
	 * @DESC	: 인터페이스 시스템 ERR 저장
	 * @변경이력 	:
	 * @param e
	 */
	public void setSystemERR(Throwable e){
		PrintLogUtil.printLogInfo(e.toString());
		PrintLogUtil.printError(e);		
	}
		

	/**
	 * @MEthod 	: getBIntFromString
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: BigInt to String Convertor
	 * @변경이력 	:
	 * @param value
	 * @return
	 */
	public BigInteger getBIntFromString(String value){
		
		BigInteger returnValue = null;
		try{		
			if(!getString(value).isEmpty())		
				returnValue = new BigInteger(value);
		}catch(Exception e){
			this.setSystemERR(e);
		}
		
		return returnValue;		
	}
		
	/**
	 * @MEthod 	: getIntgerFromString
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: Int to String Convertor
	 * @변경이력 	:
	 * @param value
	 * @return
	 */
	public Integer getIntgerFromString(String value){
			
			Integer returnValue = null;
			
			try{
				if(!getString(value).isEmpty())		
					returnValue = Integer.parseInt(value);
			}catch(Exception e){
				this.setSystemERR(e);
			}
			
			return returnValue;		
	}
	
	/**
	 * @MEthod 	: getIntgerFromString
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: Int to String Convertor
	 * @변경이력 	:
	 * @param value
	 * @return
	 */
	public BigInteger getBigIntegerFromString(int value){
			
			BigInteger returnValue = null;
			
			try{
				if(0 != value)		
					returnValue = BigInteger.valueOf(value);
			}catch(Exception e){
				this.setSystemERR(e);
			}
			
			return returnValue;		
	}
	
	/**
	 * @MEthod 	: getStringFromDate
	 * @Date	: 2014. 9. 24.
	 * @Author	: neoargon
	 * @DESC	: Date to String Convertor
	 * @변경이력 	:
	 * @param date
	 * @param format
	 * @return
	 */
	public String getStringFromDate(java.util.Date date,String format){
		
		String returnValue = "";
		String sFormst = format;
		
			if(date != null){
				
				if(sFormst.isEmpty())
					sFormst ="yyyy-MM-dd HH:mm:ss";
				
				SimpleDateFormat transFormat = new SimpleDateFormat(sFormst,Locale.KOREA);
				
				returnValue = transFormat.format(date);
			}
		
		return returnValue;		
	}	
	
	/**
	 * @MEthod 	: setIpAllocDBLog
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: IP 정보 연동 로그 등록
	 * @변경이력 	:
	 * @param reqTbIpAllocNeossMstVo
	 */
	
	public void setIpAllocDBLog(TbIpAllocNeossMstVo reqTbIpAllocNeossMstVo){
		
		try
		{
			if(!StringUtils.hasText(reqTbIpAllocNeossMstVo.getScreateId()))
			{
				reqTbIpAllocNeossMstVo.setScreateId("IPM_"+reqTbIpAllocNeossMstVo.getSaid());
				reqTbIpAllocNeossMstVo.setSmodifyId("IPM_"+reqTbIpAllocNeossMstVo.getSaid());
			}
			
			reqTbIpAllocNeossMstVo.setDcreateDt(commonService.selectSysDate());
			reqTbIpAllocNeossMstVo.setDmodifyDt(commonService.selectSysDate());
			tbIpAllocNeossMstDao.insertTbIpAllocNeossMstVo(reqTbIpAllocNeossMstVo);
		} catch (Exception e){
			this.setSystemERR(e);
		}		
	}
	
	/**
	 * @MEthod 	: setIpAllocDBListLog
	 * @Date	: 2014. 9. 12.
	 * @Author	: neoargon
	 * @DESC	: IP 정보 List 연동 로그 등록
	 * @변경이력 	:
	 * @param reqTbIpAllocNeossMstListVo
	 */	
	public void setIpAllocDBListLog(TbIpAllocNeossMstListVo reqTbIpAllocNeossMstListVo){
					
		List<TbIpAllocNeossMstVo> tbIpAllocNeossMstListVos = reqTbIpAllocNeossMstListVo.getTbIpAllocNeossMstVos();
					
		for(TbIpAllocNeossMstVo tbIpAllocNeossMstVo : tbIpAllocNeossMstListVos ){				
			setIpAllocDBLog(tbIpAllocNeossMstVo);	
		}
	}
	
	/**
	 * @MEthod 	: setIpAllockOrder
	 * @Date	: 2014. 10. 24.
	 * @Author	: neoargon
	 * @DESC	: NeOSS UI용 ODR 생성
	 * @변경이력 	:
	 * @param reqTbLnkIpAllocOrderMstVo
	 */	
	public void setIpAllockOrder(TbLnkIpAllocOrderMstVo reqTbLnkIpAllocOrderMstVo){
		
		try{
			
			tbLnkIpAllocOrderMstDao.insertTbLnkIpAllocOrderMstVo(reqTbLnkIpAllocOrderMstVo);			
			
		} catch(Exception e) {
			this.setSystemERR(e);
		}	 
	}
	
	/**
	 * @MEthod 	: updateIpAllockOrder
	 * @Date	: 2014. 10. 24.
	 * @Author	: neoargon
	 * @DESC	: NeOSS UI용 ODR 진행 설정
	 * @변경이력 	:
	 * @param reqTbLnkIpAllocOrderMstVo
	 */	
	public void updateIpAllockOrder(TbLnkIpAllocOrderMstVo reqTbLnkIpAllocOrderMstVo){
		
		try{
			
			reqTbLnkIpAllocOrderMstVo.setDmodifyDt(commonService.selectSysDate());
			tbLnkIpAllocOrderMstDao.insertTbLnkIpAllocOrderMstVo(reqTbLnkIpAllocOrderMstVo);
		} catch(Exception e) {
			this.setSystemERR(e);
		}	 
	}
	
	/**
	 * @MEthod 	: deleteTbLnkIpAllocOrder
	 * @Date	: 2014. 10. 24.
	 * @Author	: neoargon
	 * @DESC	: NeOSS UI용 ODR 삭제
	 * @변경이력 	:
	 * @param reqTbLnkIpAllocOrderMstVo
	 */	
	public void deleteTbLnkIpAllocOrder(TbLnkIpAllocOrderMstVo reqTbLnkIpAllocOrderMstVo){
		
		try{
			tbLnkIpAllocOrderMstDao.deleteTbLnkIpAllocOrderMstVo(reqTbLnkIpAllocOrderMstVo);
			
		} catch(Exception e){
			this.setSystemERR(e);
		}
	}
	

	/**
	 * @MEthod 	: insertBatchLog
	 * @Date	: 2014. 10. 24.
	 * @Author	: neoargon
	 * @DESC	: Batch Log Insert
	 * @변경이력 	:
	 * @param tbBatchLogVo
	 */	
	public void insertBatchLog(TbBatchLogVo tbBatchLogVo){
		
		try{			
			tbBatchLogDao.insertTbBatchLog(tbBatchLogVo);
		} catch(Exception e){
			this.setSystemERR(e);
		}		
	}
	
	/**
	 * @MEthod 	: updateBatchLog
	 * @Date	: 2014. 10. 24.
	 * @Author	: neoargon
	 * @DESC	: Batch Log Update
	 * @변경이력 	:
	 * @param tbBatchLogVo
	 */	
	public void updateBatchLog(TbBatchLogVo tbBatchLogVo){
		
		try{
			tbBatchLogDao.updateTbBatchLog(tbBatchLogVo);			
		} catch(Exception e){
			this.setSystemERR(e);
		}		
	}
	
	/**
	 * @MEthod 	: getRandID
	 * @Date	: 2014. 10. 24.
	 * @Author	: neoargon
	 * @DESC	: Batch Seq 생성기
	 * @변경이력 	:
	 */	
	public int getRandID()
	{
		int nreturn =0;
		
		Random rand = new Random();
		
		nreturn = rand.nextInt(50000)+1;		
		
		return nreturn;
	}
	
	/**
	 * @MEthod 	: getDateTimeAge
	 * @Date	: 2014. 10. 24.
	 * @Author	: neoargon
	 * @DESC	: DateDiff
	 * @변경이력 	:
	 * @param dts,dte
	 */	
	public String getDateTimeAge(Date dts, Date dte){
		
		long diff = 0;
		
		String sreturnValue = "0";
		
		if(dts != null || dte != null){
			
			diff = dte.getTime() - dts.getTime();
			
			sreturnValue =  String.valueOf(diff *1000);
		}
		return sreturnValue;
	}	
}
