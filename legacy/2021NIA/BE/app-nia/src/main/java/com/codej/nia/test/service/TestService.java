package com.codej.nia.test.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codej.base.dto.model.ResultMap;
import com.codej.nia.test.dao.TestMapper;

@Service
public class TestService {
    
    @Autowired(required = false)
	private TestMapper mapper;

    @Transactional(readOnly = true)
	public ResultMap SELECT_NOW(HashMap<String, Object> map){
		return mapper.SELECT_NOW(map);
	}
    
}
