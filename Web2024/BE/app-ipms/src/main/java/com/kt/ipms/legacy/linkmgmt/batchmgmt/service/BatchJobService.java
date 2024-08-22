package com.kt.ipms.legacy.linkmgmt.batchmgmt.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.linkmgmt.common.service.TbBatchSvcBasService;
import com.kt.ipms.legacy.linkmgmt.common.util.ShellScriptUtil;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbBatchSvcBasVo;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.adapter.NeOSSConsumeAdapterService;
import com.kt.ipms.legacy.linkmgmt.socketmgmt.adapter.SocketMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.whoismgmt.adapter.WhoisMgmtAdapterService;

@Component
public class BatchJobService {
@Lazy @Autowired
	private TbBatchSvcBasService tbBatchSvcBasService;
	
	@Autowired
	private SchedulerFactoryBean batchSchedulerFactoryBean;
	
@Lazy @Autowired
	private ConfigPropertieService configPropertieService;
	
@Lazy @Autowired
	private NeOSSConsumeAdapterService neOSSConsumeAdapterService;
	
@Lazy @Autowired
	private WhoisMgmtAdapterService whoisMgmtAdapterService;
	
	@Autowired
	private ShellScriptUtil shellScriptUtil;
	
@Lazy @Autowired
	private SocketMgmtAdapterService socketMgmtAdapterService;
	
@Lazy @Autowired
	private CallableBatchService callableBatchService;
	
	@Autowired
	ExcelParseBatchService excelParseBatchService;
	
	
	/** Service Start시 Batch가 실행 여부 체크 후 BatchJob Start
	 * @author gaeco0
	 * @category BatchJobDetroyer
	 * @return void
	 * @exception Print Logs*/
	@PostConstruct
	public void autoStartBatchJob() {
		try {
			if(configPropertieService.getBoolean("Batch.run")){
				
				PrintLogUtil.printLogInfo("AUTO START BATCH SCHEDULER");
				startBatchJobServices();
			}
		} catch (Exception e) {
			PrintLogUtil.printError(e);
		}
	}
	
	/** Service Stop시 Batch가 실행중이면 끄고 Stop진행
	 * @author gaeco0
	 * @category BatchJobDetroyer
	 * @return void
	 * @exception Print Logs*/
	@PreDestroy
	public void autoDestroyBatchJob(){
		try{
			if(batchSchedulerFactoryBean.isRunning()) {
				PrintLogUtil.printLogInfo("AUTO STOP BATCH SCHEDULER");
				stopBatchJobServices();
			}
		} catch(Exception e) {
			PrintLogUtil.printError(e);
		}
	}
	/** BatchJob Starter
	 * @author gaeco0
	 * @category BatchJob
	 * @return void
	 * @exception Print Logs
	 * */
	public void startBatchJobServices() {
		// try {
		// 	PrintLogUtil.printLogInfo("=========START BATCH SCHEDULER=========");
		// 	if (batchSchedulerFactoryBean.isRunning()) {
		// 		stopBatchJobServices();
		// 	}
		// 	stopBatchJobServices();
		// 	List<TbBatchSvcBasVo> tbBatchSvcBasVos = tbBatchSvcBasService.selectListBatchConf();
		// 	CronTriggerBean[] triggers = createBatchJobDetail(tbBatchSvcBasVos);
		// 	batchSchedulerFactoryBean.setTriggers(triggers);
		// 	batchSchedulerFactoryBean.afterPropertiesSet();
		// 	batchSchedulerFactoryBean.start();
		// } catch (ServiceException e) {
		// 	PrintLogUtil.printError(e);
		// } catch (Exception e) {
		// 	PrintLogUtil.printError(e);
		// }
		
	}
	/** BatchJob Reloader
	 * @author gaeco0
	 * @category BatchJob
	 * @return void
	 * @exception Print Logs
	 * */
	public void updateBatchJobs(){
		try{
			PrintLogUtil.printLogInfo("=========UPDATE BATCH SCHEDULER=========");
			if(batchSchedulerFactoryBean.isRunning()){
				startBatchJobServices();
				
//				List<TbBatchSvcBasVo> tbBatchSvcBasVos = tbBatchSvcBasService.selectListBatchConf();
//				CronTriggerBean[] triggers = createBatchJobDetail(tbBatchSvcBasVos);
//				batchSchedulerFactoryBean.getObject().
//				
//				
//				for(int i = 0; i < triggers.length; i++) {
//					batchSchedulerFactoryBean.getScheduler().rescheduleJob(triggers[i].getName(), triggers[i].getGroup(), triggers[i]);
//				}
//				batchSchedulerFactoryBean.afterPropertiesSet();
//				//batchSchedulerFactoryBean.notifyAll();
//				batchSchedulerFactoryBean.start();
			}else{
				throw new ServiceException("CMN.HIGH.00000");
			}
		} catch(ServiceException e){
			PrintLogUtil.printError(e);
		} catch(Exception e){
			PrintLogUtil.printError(e);
		}
	}
	/** BatchJob Stopper
	 * @author gaeco0
	 * @category BatchJob
	 * @return void
	 * @exception Print Logs
	 * */
	public void stopBatchJobServices() {
// 		try {
// 			/*
// 			 * 일반 stop 및 destroy로 정지 시에 null Thread Running 수정 */
// 			PrintLogUtil.printLogInfo("=========STOP BATCH SCHEDULER=========");
// 			String[] jobGroups = batchSchedulerFactoryBean.getObject().getJobGroupNames();
// 			for(int i = 0; i < jobGroups.length; i++) {
// 				PrintLogUtil.printLog("JOB GROUPS IDX : " + i + ", STR : " + jobGroups[i]);
// 				String[] jobList = batchSchedulerFactoryBean.getObject().getJobNames(jobGroups[i]);
// 				for(int j = 0; j < jobList.length; j++){
// 					PrintLogUtil.printLog("\tJOB LIST IDX : " + j + ", STR : " + jobList[j]);
// 					batchSchedulerFactoryBean.getObject().unscheduleJob(jobList[j], jobGroups[i]);
					
// 				}
// 			}
// 			batchSchedulerFactoryBean.getScheduler().shutdown(false);
// //			batchSchedulerFactoryBean.afterPropertiesSet();
// 			//batchSchedulerFactoryBean.notifyAll();
// //			batchSchedulerFactoryBean.destroy();
// 			batchSchedulerFactoryBean.stop();
// 		} catch(ServiceException e){
// 			PrintLogUtil.printError(e);
// 		} catch (Exception e) {
// 			PrintLogUtil.printError(e);
// 		}
	}
	/** BatchJob Generator
	 * @author gaeco0
	 * @category BatchJob
	 * @return void
	 * @exception throws to Batch Job Starter
	 * */
	// private CronTriggerBean[] createBatchJobDetail(List<TbBatchSvcBasVo> tbBatchSvcBasVos) throws ServiceException{
	// 	CronTriggerBean[] result = new CronTriggerBean[tbBatchSvcBasVos.size()];
	// 	for(int i = 0; i < tbBatchSvcBasVos.size(); i++){
	// 		try{
	// 			if(tbBatchSvcBasVos.get(i).getSopstate().equals("U")){
					
	// 				if(tbBatchSvcBasVos.get(i).getLinktype().equals("WEBSERVICE")) {
	// 					PrintLogUtil.printLogInfo("=========WEBSERVICE BATCH CRON CREATE=========:" + tbBatchSvcBasVos.get(i).getSifId());
	// 					/** NEOSS WEBSERVICE BATCH **/
	// 					MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
	// 					methodInvokingJobDetailFactoryBean.setTargetObject(neOSSConsumeAdapterService);
	// 					methodInvokingJobDetailFactoryBean.setGroup(tbBatchSvcBasVos.get(i).getLinktype());
	// 					methodInvokingJobDetailFactoryBean.setName(tbBatchSvcBasVos.get(i).getSifId());
	// 					methodInvokingJobDetailFactoryBean.setTargetMethod(tbBatchSvcBasVos.get(i).getSscriptNm());
	// 					methodInvokingJobDetailFactoryBean.setArguments(new Object[]{new String()});
	// 					methodInvokingJobDetailFactoryBean.setConcurrent(false);
	// 					methodInvokingJobDetailFactoryBean.afterPropertiesSet();
	// 					CronTriggerBean cronTriggerBean = new CronTriggerBean();
	// 					cronTriggerBean.setJobDetail(methodInvokingJobDetailFactoryBean.getObject());
	// 					cronTriggerBean.setCronExpression(tbBatchSvcBasVos.get(i).getSperiod());
	// 					cronTriggerBean.setName(tbBatchSvcBasVos.get(i).getSifId());
	// 					cronTriggerBean.afterPropertiesSet();
	// 					result[i] = new CronTriggerBean();
	// 					result[i] = cronTriggerBean;
	// 				} else if (tbBatchSvcBasVos.get(i).getLinktype().equals("SOCKET")) {
	// 					PrintLogUtil.printLogInfo("=========SOCKET BATCH CRON CREATE=========:" + tbBatchSvcBasVos.get(i).getSifId());
	// 					/** WHOIS SOCKET BATCH **/
	// 					MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
	// 					methodInvokingJobDetailFactoryBean.setTargetObject(socketMgmtAdapterService);
	// 					methodInvokingJobDetailFactoryBean.setGroup(tbBatchSvcBasVos.get(i).getLinktype());
	// 					methodInvokingJobDetailFactoryBean.setName(tbBatchSvcBasVos.get(i).getSifId());
	// 					methodInvokingJobDetailFactoryBean.setTargetMethod(tbBatchSvcBasVos.get(i).getSscriptNm());
	// 					methodInvokingJobDetailFactoryBean.setConcurrent(false);
	// 					methodInvokingJobDetailFactoryBean.afterPropertiesSet();
	// 					CronTriggerBean cronTriggerBean = new CronTriggerBean();
	// 					cronTriggerBean.setJobDetail(methodInvokingJobDetailFactoryBean.getObject());
	// 					cronTriggerBean.setCronExpression(tbBatchSvcBasVos.get(i).getSperiod());
	// 					cronTriggerBean.setName(tbBatchSvcBasVos.get(i).getSifId());
	// 					cronTriggerBean.afterPropertiesSet();
	// 					result[i] = new CronTriggerBean();
	// 					result[i] = cronTriggerBean;
	// 				} else if (tbBatchSvcBasVos.get(i).getLinktype().equals("PARSE")) {
	// 					PrintLogUtil.printLogInfo("=========PARSE BATCH CRON CREATE=========:" + tbBatchSvcBasVos.get(i).getSifId());
	// 					/** WHOIS FILE PARSING **/
	// 					MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
	// 					methodInvokingJobDetailFactoryBean.setTargetObject(whoisMgmtAdapterService);
	// 					methodInvokingJobDetailFactoryBean.setGroup(tbBatchSvcBasVos.get(i).getLinktype());
	// 					methodInvokingJobDetailFactoryBean.setName(tbBatchSvcBasVos.get(i).getSifId());
	// 					methodInvokingJobDetailFactoryBean.setTargetMethod(tbBatchSvcBasVos.get(i).getSscriptNm());
	// 					methodInvokingJobDetailFactoryBean.setConcurrent(false);
	// 					methodInvokingJobDetailFactoryBean.afterPropertiesSet();
	// 					CronTriggerBean cronTriggerBean = new CronTriggerBean();
	// 					cronTriggerBean.setJobDetail(methodInvokingJobDetailFactoryBean.getObject());
	// 					cronTriggerBean.setCronExpression(tbBatchSvcBasVos.get(i).getSperiod());
	// 					cronTriggerBean.setName(tbBatchSvcBasVos.get(i).getSifId());
	// 					cronTriggerBean.afterPropertiesSet();
	// 					result[i] = new CronTriggerBean();
	// 					result[i] = cronTriggerBean;
	// 				} else if (tbBatchSvcBasVos.get(i).getLinktype().equals("DBCALL")) {
	// 					PrintLogUtil.printLogInfo("=========DBCALL CRON CREATE=========:" + tbBatchSvcBasVos.get(i).getSifId());
	// 					/** DB FUNTION CALLABLE **/
	// 					MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
	// 					methodInvokingJobDetailFactoryBean.setTargetObject(callableBatchService);
	// 					methodInvokingJobDetailFactoryBean.setGroup(tbBatchSvcBasVos.get(i).getLinktype());
	// 					methodInvokingJobDetailFactoryBean.setName(tbBatchSvcBasVos.get(i).getSifId());
	// 					methodInvokingJobDetailFactoryBean.setTargetMethod(tbBatchSvcBasVos.get(i).getSscriptNm());
	// 					methodInvokingJobDetailFactoryBean.setConcurrent(false);
	// 					methodInvokingJobDetailFactoryBean.afterPropertiesSet();
	// 					CronTriggerBean cronTriggerBean = new CronTriggerBean();
	// 					cronTriggerBean.setJobDetail(methodInvokingJobDetailFactoryBean.getObject());
	// 					cronTriggerBean.setCronExpression(tbBatchSvcBasVos.get(i).getSperiod());
	// 					cronTriggerBean.setName(tbBatchSvcBasVos.get(i).getSifId());
	// 					cronTriggerBean.afterPropertiesSet();
	// 					result[i] = new CronTriggerBean();
	// 					result[i] = cronTriggerBean;
	// 				} else if (tbBatchSvcBasVos.get(i).getLinktype().equals("EXCEL_PARSE")) {
	// 					PrintLogUtil.printLogInfo("=========EXCEL_PARSE CRON CREATE=========:" + tbBatchSvcBasVos.get(i).getSifId());
	// 					/** EXCEL PARSE BATCH **/
	// 					MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
	// 					methodInvokingJobDetailFactoryBean.setTargetObject(excelParseBatchService);
	// 					methodInvokingJobDetailFactoryBean.setGroup(tbBatchSvcBasVos.get(i).getLinktype());
	// 					methodInvokingJobDetailFactoryBean.setName(tbBatchSvcBasVos.get(i).getSifId());
	// 					methodInvokingJobDetailFactoryBean.setTargetMethod(tbBatchSvcBasVos.get(i).getSscriptNm());
	// 					methodInvokingJobDetailFactoryBean.setConcurrent(false);
	// 					methodInvokingJobDetailFactoryBean.afterPropertiesSet();
	// 					CronTriggerBean cronTriggerBean = new CronTriggerBean();
	// 					cronTriggerBean.setJobDetail(methodInvokingJobDetailFactoryBean.getObject());
	// 					cronTriggerBean.setCronExpression(tbBatchSvcBasVos.get(i).getSperiod());
	// 					cronTriggerBean.setName(tbBatchSvcBasVos.get(i).getSifId());
	// 					cronTriggerBean.afterPropertiesSet();
	// 					result[i] = new CronTriggerBean();
	// 					result[i] = cronTriggerBean;
	// 				} else {
	// 					/** SHELL SCRIPT BATCH **/
	// 					PrintLogUtil.printLogInfo("=========SHELL BATCH CRON CREATE=========:" + tbBatchSvcBasVos.get(i).getSifId());
	// 					/* JobTrigger 작업*/
	// 					MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
	// 					methodInvokingJobDetailFactoryBean.setName(tbBatchSvcBasVos.get(i).getSifId());
	// 					methodInvokingJobDetailFactoryBean.setGroup(tbBatchSvcBasVos.get(i).getLinktype());
	// 					methodInvokingJobDetailFactoryBean.setTargetObject(shellScriptUtil);
	// 					methodInvokingJobDetailFactoryBean.setTargetMethod("execute");
	// 					methodInvokingJobDetailFactoryBean.setArguments(new Object[]{tbBatchSvcBasVos.get(i)});
	// 					methodInvokingJobDetailFactoryBean.setConcurrent(false);
	// 					methodInvokingJobDetailFactoryBean.afterPropertiesSet();
	// 					CronTriggerBean cronTriggerBean = new CronTriggerBean();
	// 					cronTriggerBean.setJobDetail(methodInvokingJobDetailFactoryBean.getObject());
	// 					cronTriggerBean.setCronExpression(tbBatchSvcBasVos.get(i).getSperiod());
	// 					cronTriggerBean.setName(tbBatchSvcBasVos.get(i).getSifId());
	// 					cronTriggerBean.afterPropertiesSet();
	// 					result[i] = new CronTriggerBean();
	// 					result[i] = cronTriggerBean;
	// 				}
	// 			}
	// 		} catch (ServiceException e){
	// 			throw e;
	// 		} catch (Exception e){
	// 			throw new ServiceException("CMN.HIGH.00000", new String[]{e.getMessage()});
	// 		}
	// 	}
	// 	return result;
	// }
	
	public TbBatchSvcBasVo redoBatchProccess(TbBatchSvcBasVo tbBatchSvcBasVo){
		shellScriptUtil.execute(tbBatchSvcBasVo);
		return tbBatchSvcBasVo;
	}
}
