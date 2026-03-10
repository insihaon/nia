package com.kt.ipms.legacy.opermgmt.boardmgmt.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.boardmgmt.service.BoardService;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardListVo;

@Component
@Transactional
public class BoardMgmtAdapterService {
	
	@Autowired
	private BoardService  boardService;
	
	@Transactional(readOnly = true)
	public  TbBoardListVo selectListTbBoardByMain() {
		return boardService.selectListTbBoardByMain();
	}
	

}
