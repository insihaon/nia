package com.kt.ipms.legacy.linkmgmt.batchmgmt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.linkmgmt.batchmgmt.vo.TbBatchLogVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbBatchSvcBasVo;

@Mapper
public interface TbBatchLogDao {
	
	public List<TbBatchLogVo> selectListTbBatchLog(TbBatchLogVo tbBatchLogVo);
	
	public TbBatchLogVo selectDetailTbBatchLogVo(TbBatchLogVo tbBatchLogVo);
	
	public int insertTbBatchLog(TbBatchLogVo tbBatchLogVo);
	
	public int updateTbBatchLog(TbBatchLogVo tbBatchLogVo);
	
	public TbBatchSvcBasVo selectTbBatchSvcBasByTbBatchLogSeq(TbBatchLogVo tbBatchLogVo);
	
	public int countListPageTbBatchLogVo(TbBatchLogVo tbBatchLogVo);
	
	public List<TbBatchLogVo> selectListPageTbBatchLogVo(TbBatchLogVo tbBatchLogVo);
	
	/**
	 * 배치 연동 이력현황 > 목록 조회
	 * @param tbBatchLogVo
	 * @return
	 */
	public List<TbBatchLogVo> selectListPageTbBatchHistVo(TbBatchLogVo tbBatchLogVo);
	
	/**
	 * 배치 연동 이력현황 > 건수 조회
	 * @param tbBatchLogVo
	 * @return
	 */
	public int countListTbBatchHistVo(TbBatchLogVo tbBatchLogVo);
}
