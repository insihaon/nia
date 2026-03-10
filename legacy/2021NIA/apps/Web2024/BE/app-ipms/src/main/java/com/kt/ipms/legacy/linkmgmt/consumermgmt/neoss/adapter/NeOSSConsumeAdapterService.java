package com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.adapter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstListVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstVo;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.service.ConsumeService;


@Component
@Transactional
public class NeOSSConsumeAdapterService {
	
	@Autowired	
	private ConsumeService consumeService;
	
	/**
	 * @MEthod 	: insertIpmsSvcInfoList
	 * @Date	: 2014. 9. 25.
	 * @Author	: neoargon
	 * @DESC	: NeOSS 상품 정보 연동
	 * @변경이력 	:
	 * @param strCurrentDate
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public TbIpAllocNeossMstVo insertIpmsSvcInfoList(String strCurrentDate){
		return consumeService.consumeNes0001(strCurrentDate);
	}
	
	/**
	 * @MEthod 	: insertReservedIPList
	 * @Date	: 2014. 9. 23.
	 * @Author	: neoargon
	 * @DESC	: NeOSS IP Block 예약 정보 연동
	 * @변경이력 	:
	 * @param tbIpAllocNeossMstListVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public boolean insertReservedIPList(TbIpAllocNeossMstListVo tbIpAllocNeossMstListVo){
		return consumeService.consumeNes0002(tbIpAllocNeossMstListVo);
	}
	

	/**
	 * @MEthod 	: insertFixedIPList
	 * @Date	: 2014. 9. 23.
	 * @Author	: neoargon
	 * @DESC	: NeOSS IP Block 확정 정보 연동
	 * @변경이력 	:
	 * @param tbIpAllocNeossMstListVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public boolean insertFixedIPList(TbIpAllocNeossMstListVo tbIpAllocNeossMstListVo){
		return consumeService.consumeNes0003(tbIpAllocNeossMstListVo);
	}
	
	/**
	 * @MEthod 	: selectSubscFixedIPList
	 * @Date	: 2014. 9. 24.
	 * @Author	: neoargon
	 * @DESC	: NeOSS IP 및 시설 할당 정보 조회
	 * @변경이력 	:
	 * @param tbIpAllocNeossMstVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public TbIpAllocNeossMstListVo selectSubscFixedIPList(TbIpAllocNeossMstVo tbIpAllocNeossMstVo){
		return consumeService.consumeNes0004(tbIpAllocNeossMstVo);
	}

	/**
	 * @MEthod 	: selectSVCInfo
	 * @Date	: 2014. 9. 24.
	 * @Author	: neoargon
	 * @DESC	:  NeOSS 회선 정보 조회
	 * @변경이력 	:
	 * @param tbIpAllocNeossMstVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public TbIpAllocNeossMstVo selectSVCInfo(TbIpAllocNeossMstVo tbIpAllocNeossMstVo){
		return consumeService.consumeNes0005(tbIpAllocNeossMstVo);
	}

}
