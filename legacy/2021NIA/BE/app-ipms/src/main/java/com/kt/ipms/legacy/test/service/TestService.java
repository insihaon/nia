package com.kt.ipms.legacy.test.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codej.base.dto.model.ResultMap;
import com.kt.ipms.legacy.test.dao.TestMapper;

@Service
public class TestService {
    
    @Autowired
	private TestMapper mapper;

    @Transactional(readOnly = true)
	public ResultMap SELECT_NOW(HashMap<String, Object> map){
		return mapper.SELECT_NOW(map);
	}
    
}
