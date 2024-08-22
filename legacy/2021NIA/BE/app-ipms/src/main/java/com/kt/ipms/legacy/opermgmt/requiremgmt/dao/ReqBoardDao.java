package com.kt.ipms.legacy.opermgmt.requiremgmt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.requiremgmt.vo.ReqAdminEmailVo;
import com.kt.ipms.legacy.opermgmt.requiremgmt.vo.ReqBoardSubVo;
import com.kt.ipms.legacy.opermgmt.requiremgmt.vo.ReqBoardVo;

@Mapper
public interface ReqBoardDao {
	public List<ReqBoardVo> selectListPageReqBoardVo(ReqBoardVo reqBoardVo);
	
	public int countListPageReqBoardVo(ReqBoardVo reqBoardVo);
	
	public List<ReqBoardSubVo> selectListReqSubVo(ReqBoardSubVo reqBoardSubVo);
	
	public ReqBoardVo selectReqBoard(ReqBoardVo reqBoardVo);
	
	public int insertReqBoardVo(ReqBoardVo reqBoardVo);
	
	public int insertReqBoardReply(ReqBoardVo reqBoardVo);
	
	public int insertReqBoardUpload(ReqBoardVo reqBoardVo);
	
	public int deleteReqBoardVo(ReqBoardVo reqBoardVo);
	
	public int updateReqBoardVo(ReqBoardVo reqBoardVo);
	
	public ReqBoardVo selectReqBoardUpload(ReqBoardVo reqBoardVo);
	
	public int updateReqBoardReply(ReqBoardVo reqBoardVo);
	
	public int updateReqBoardUpload(ReqBoardVo reqBoardVo);
	
	public int deleteReqBoardReply(ReqBoardVo reqBoardVo);
	
	public int deleteReqBoardUpload(ReqBoardVo reqBoardVo);
	
	public ReqBoardVo selectEmailContent(ReqBoardVo reqBoardVo);
	
	public int selectReqBoardUploadCount(ReqBoardVo reqBoardVo);
	
	public List<ReqAdminEmailVo> selectAdminEmailList();
}
