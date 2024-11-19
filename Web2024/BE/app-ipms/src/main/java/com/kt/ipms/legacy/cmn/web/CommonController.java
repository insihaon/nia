package com.kt.ipms.legacy.cmn.web;

import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import com.codej.base.controller.BaseController;
import com.kt.ipms.legacy.cmn.service.CommonCodeService;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.service.ExcelDownloadService;
import com.kt.ipms.legacy.cmn.util.ExcelUtil;
import com.kt.ipms.legacy.cmn.util.JwtUtil;
import com.kt.ipms.legacy.cmn.util.SessionUtil;
import com.kt.ipms.legacy.cmn.util.SmtpUtil;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.service.TbCmnMsgMstService;
import com.kt.log4kt.KTLogger;
import com.kt.log4kt.KTLoggerFactory;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

public class CommonController extends BaseController {

	@Autowired
	protected ConfigPropertieService propertieService;

	@Autowired
	protected CommonCodeService commonCodeService;

	@Autowired
	protected TbCmnMsgMstService tbCmnMstService;
	@Autowired
	protected ExcelDownloadService excelDownloadService;

	@Autowired
	protected SessionUtil sessionUtil;

	@Autowired
	protected JwtUtil jwtUtil;

	@Autowired
	protected ExcelUtil excelUtil;

	@Autowired
	protected SmtpUtil smtpUtil;

	protected KTLogger logger = KTLoggerFactory.getLogger(getClass());

	private PaginationInfo paginationInfo;

	public PaginationInfo getPaginationInfo() {
		return paginationInfo;
	}

	public void setPaginationInfo(PaginationInfo paginationInfo) {
		this.paginationInfo = paginationInfo;
	}

	protected void setPagination(CommonVo commonVo) {
		setPagination(commonVo, null);
	}

	protected void setPagination(CommonVo commonVo, PaginationInfo paginationInfo) {
		if (this.paginationInfo == null) {
			this.paginationInfo = new PaginationInfo();
		}

		if (paginationInfo == null) {
			this.paginationInfo.setCurrentPageNo(commonVo.getPageIndex());
			// this.paginationInfo.setRecordCountPerPage(propertieService.getInt("Pagination.pageUnit"));
			if (commonVo.getPageUnit() == 0) {
				this.paginationInfo.setRecordCountPerPage(propertieService.getInt("Pagination.pageUnit"));
			} else {
				this.paginationInfo.setRecordCountPerPage(commonVo.getPageUnit());
			}
			this.paginationInfo.setPageSize(propertieService.getInt("Pagination.pageSize"));
		} else {
			this.paginationInfo.setCurrentPageNo(commonVo.getPageIndex());
			this.paginationInfo.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
			this.paginationInfo.setPageSize(paginationInfo.getPageSize());
			this.paginationInfo.setTotalRecordCount(paginationInfo.getTotalRecordCount());
		}
		commonVo.setPageUnit(this.paginationInfo.getRecordCountPerPage());
		commonVo.setPageSize(this.paginationInfo.getPageSize());
		commonVo.setFirstIndex(this.paginationInfo.getFirstRecordIndex() + 1);
		commonVo.setLastIndex(this.paginationInfo.getLastRecordIndex());
		commonVo.setRecordCountPerPage(this.paginationInfo.getRecordCountPerPage());
	}

	public String searchVoJson(Object searchVo) {
		String searchVoJson = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			searchVoJson = mapper.writeValueAsString(searchVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return searchVoJson;
	}

	public ModelMap createResult(CommonVo data, CommonVo data2) {
		ModelMap resultModel = new ModelMap();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("data", data);
		if (data2 != null) {
			map.put("data2", data2);
		}
		resultModel.addAttribute("result", map);

		return resultModel;
	}

	public ModelMap createResult(CommonVo data) {
		return createResult(data, null);
	}

	public ModelMap createResultList(List<?> data, int count) {
		ModelMap resultModel = new ModelMap();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("data", data);
		map.put("totalCount", count);
		resultModel.addAttribute("result", map);

		return resultModel;
	}

}
