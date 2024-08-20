package com.kt.ipms.legacy.linkmgmt.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.linkmgmt.common.service.TbBatchSvcBasService;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbBatchSvcBasVo;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.service.TbCmnMsgMstService;

@Component
public class ShellScriptUtil {
	@Autowired
	private TbBatchSvcBasService tbBatchSvcBasService;
	
	@Autowired
	private TbCmnMsgMstService tbCmnMsgMstService;
	
	@Autowired
	private ConfigPropertieService configPropertieService;
	
	public void execute(TbBatchSvcBasVo tbBatchSvcBasVo){
		
		Date dDate = new Date();
		String url;
		PrintLogUtil.printLogInfo("======================================" + tbBatchSvcBasVo.getSifId() + "======================================");
		try{
			
			TbBatchSvcBasVo tbBatchSvcBasVo2 = new TbBatchSvcBasVo();
			tbBatchSvcBasVo2.setBatMstSeq(tbBatchSvcBasVo.getBatMstSeq());
			tbBatchSvcBasVo2 = tbBatchSvcBasService.selectTbBatchSvcBasVo(tbBatchSvcBasVo2);
			
			String sDate = new SimpleDateFormat("yyyyMMdd", Locale.KOREA).format(dDate);
			String port = configPropertieService.getString("Database.Url").split(":")[3].split("/")[0];
			String databaseNm = configPropertieService.getString("Database.Url").split(":")[3].split("/")[1];
			url = configPropertieService.getString("Database.Url").replace("jdbc:edb://", "").split(":")[0];
			ProcessBuilder pb = new ProcessBuilder("." + tbBatchSvcBasVo.getSscriptNm(), url, port, configPropertieService.getString("Database.UserName"),
													configPropertieService.getString("Database.Password"), databaseNm, sDate, tbBatchSvcBasVo2.getSinterlockPath(),
													tbBatchSvcBasVo2.getStableNm(), configPropertieService.getString("Batch.PsqlPath"));
			pb.directory(new File(tbBatchSvcBasVo.getSscriptPath()));
			Process p = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String lines = "";
			StringBuffer stringBuffer = new StringBuffer();
			while((lines = reader.readLine()) != null)
			{
				stringBuffer.append(lines);
				stringBuffer.append("\n");
			}
			PrintLogUtil.printLog(stringBuffer.toString());
			PrintLogUtil.printLogInfo("======================================" + tbBatchSvcBasVo.getSifId() + "======================================");
			p.destroy();
		}catch(ServiceException e){
			tbCmnMsgMstService.selectMsgDesc(e);
		}catch(Exception e){
			PrintLogUtil.printError(e);
			PrintLogUtil.printLogInfo("======================================" + tbBatchSvcBasVo.getSifId() + "======================================");
		}		
	}
}
