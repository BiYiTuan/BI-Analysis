package com.sxit.mgt.pmp.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxit.common.action.BaseAction;
import com.sxit.common.dto.ResultMessage;
import com.sxit.common.dto.SearchVo;
import com.sxit.common.pagehelper.Page;
import com.sxit.common.pagehelper.PageVo;
import com.sxit.mgt.pmp.dto.BProjModel;
import com.sxit.mgt.pmp.dto.IndexBaseModel;
import com.sxit.mgt.pmp.dto.SFProjModel;
import com.sxit.model.pmp.BProjSum;
import com.sxit.model.pmp.IndexBase;
import com.sxit.model.pmp.IndexFlow;
import com.sxit.model.pmp.FormatConvert;
import com.sxit.model.pmp.RptCustomerBoughtCount;
import com.sxit.model.pmp.RptCustomerCountMain;
import com.sxit.model.pmp.RptKfIndexFlow;
import com.sxit.model.pmp.RptKfKeyIndex;
import com.sxit.model.pmp.RptXsFormatIndex;
import com.sxit.model.pmp.RptProjCstArea;
import com.sxit.model.pmp.RptProjCstIndustry;
import com.sxit.model.pmp.RptCustomerAttachCount;
import com.sxit.model.pmp.RptKfFormatIndex;
import com.sxit.model.pmp.RptZyFormatConvert;
import com.sxit.model.pmp.RptZyFormatIndex;
import com.sxit.model.pmp.RptZyIndexFlow;
import com.sxit.model.pmp.RptZyKeyIndex;
import com.sxit.mgt.pmp.service.IndexBaseService;
import com.sxit.mgt.pmp.service.IndexFlowService;
import com.sxit.mgt.pmp.service.FormatConvertService;
import com.sxit.mgt.pmp.service.BProjSumService;
import com.sxit.mgt.pmp.service.RptCustomerAttachCountService;
import com.sxit.mgt.pmp.service.RptCustomerBoughtCountService;
import com.sxit.mgt.pmp.service.RptCustomerCountMainService;
import com.sxit.mgt.pmp.service.RptKfIndexFlowService;
import com.sxit.mgt.pmp.service.RptKfKeyIndexService;
import com.sxit.mgt.pmp.service.RptKfFormatIndexService;
import com.sxit.mgt.pmp.service.RptProjCstAreaService;
import com.sxit.mgt.pmp.service.RptProjCstIndustryService;
import com.sxit.mgt.pmp.service.RptXsFormatIndexService;
import com.sxit.mgt.pmp.service.RptZyFormatConvertService;
import com.sxit.mgt.pmp.service.RptZyFormatIndexService;
import com.sxit.mgt.pmp.service.RptZyIndexFlowService;
import com.sxit.mgt.pmp.service.RptZyKeyIndexService;
import com.sxit.mgt.pmp.service.SFProjRecordService;

/**
 * @公司:深讯信科
 * @功能:IndexBase Action
 * @作者:Edson.di
 * @日期:2015-09-23 09:43:07
 * @版本:1.0
 * @修改:
 */

@Controller
@RequestMapping("/pmp")
public class IndexBaseAction extends BaseAction {

	private final static Logger log = Logger.getLogger(IndexBaseAction.class);

	@Autowired
	private IndexBaseService indexBaseService;
	@Autowired
	private IndexFlowService indexFlowService;
	@Autowired
	private FormatConvertService formatConvertService;
	@Autowired
	private BProjSumService bProjSumService;
	@Autowired
	private RptCustomerCountMainService rptCustomerCountMainService;
	@Autowired
	private RptCustomerBoughtCountService rptCustomerBoughtCountService;
	@Autowired
	private RptProjCstAreaService rptProjCstAreaService;
	@Autowired
	private RptProjCstIndustryService rptProjCstIndustryService;
	@Autowired
	private RptCustomerAttachCountService rptCustomerAttachCountService;
	@Autowired
	private SFProjRecordService sFollowRecotdService;
	@Autowired
	private RptXsFormatIndexService rptXsFormatIndexService;	
	@Autowired
	private RptKfKeyIndexService rptKfKeyIndexService;
	@Autowired
	private RptKfFormatIndexService rptKfFormatIndexService;
	@Autowired
	private RptKfIndexFlowService rptKfIndexFlowService;	
	@Autowired
	private RptZyIndexFlowService rptZyIndexFlowService;
	@Autowired
	private RptZyKeyIndexService rptZyKeyIndexService;
	@Autowired
	private RptZyFormatIndexService rptZyFormatIndexService;
	@Autowired
	private RptZyFormatConvertService rptZyFormatConvertService;
	

	/**
	 * 列表 查询所有
	 * 
	 * @param searchTxt
	 * @param page
	 * @param model
	 * @return
	 */
	// @AuthPassport(rightCode = "pmp.IndexBase")
	@RequestMapping(value = "/indexList")
	public @ResponseBody
	ResultMessage list(@ModelAttribute SearchVo vo, PageVo pagevo) {

		// 列表查询
		if (pagevo == null) {
			pagevo = new PageVo(0, 10);
		}

		Page page = indexBaseService.getIndexBaseList(pagevo, vo.getMap());

		return ResultMessage.successPage(page);
	}

	/**
	 * 增加 项目汇总信息
	 * 
	 * @return
	 */
	// @AuthPassport(rightCode = "pmp.IndexBase")
	@RequestMapping(value = "/indexBaseAdd")
	public @ResponseBody
	ResultMessage add(@Valid @RequestBody IndexBaseModel indexBaseModel,
			Errors errors) {
		IndexBase indexBase = new IndexBase();
		BeanUtils.copyProperties(indexBaseModel, indexBase);
		indexBase.setCreate_time((Timestamp) new Date());
		indexBaseService.insert(indexBase);
		return ResultMessage.successMsg("添加成功");
	}

	/**
	 * 明细
	 * 
	 * @param userId
	 * @return
	 */
	// @AuthPassport(rightCode = "pmp.IndexBase")
	@RequestMapping(value = "/indexBaseDetail")
	public @ResponseBody
	ResultMessage detail(@RequestParam String proj_guid) {
		// String pguid = request.getParameter("proj_guid");
		String message = "";
		if (proj_guid == null) {
			message = "此项目id不存在！";
			return ResultMessage.errorMsg(message);
		}

		IndexBase indexbase = indexBaseService.getIndexBaseById(proj_guid);
		// 带写工具类 亿转万，金额，数字为空默认为零
		if (indexbase != null) {
			indexbase.setConfess_amount_sum((double) Math.round(indexbase
					.getConfess_amount_sum() / 10000));
			indexbase.setSubscribe_amount_sum((double) Math.round(indexbase
					.getSubscribe_amount_sum() / 10000));
			indexbase.setContract_amount_sum((double) Math.round(indexbase
					.getContract_amount_sum() / 10000));
			indexbase.setReturned_amount_sum((double) Math.round(indexbase
					.getReturned_amount_sum() / 10000));
		}

		IndexFlow indexFlow = indexFlowService.getIndexFlowById(proj_guid);
		if (indexFlow != null) {
			if (indexFlow.getVisit_days() == null)
				indexFlow.setVisit_days(0);
			if (indexFlow.getAverage_baseline_days() == null)
				indexFlow.setAverage_baseline_days(0);
			if (indexFlow.getOverstep_days() == null)
				indexFlow.setOverstep_days(0);
			if (indexFlow.getConfess_days() == null)
				indexFlow.setConfess_days(0);
			if (indexFlow.getAverage_order_baseline_days() == null)
				indexFlow.setAverage_order_baseline_days(0);
		}
		
		RptXsFormatIndex rptXsFormatIndex = rptXsFormatIndexService.queryById(proj_guid);
		if(rptXsFormatIndex != null){
			rptXsFormatIndex.setDeal_money((double) Math.round(rptXsFormatIndex
					.getDeal_money() / 10000));
			rptXsFormatIndex.setEsti_Deal_money((double) Math.round(rptXsFormatIndex
					.getEsti_Deal_money() / 10000));
		}
		

		FormatConvert formatConvert = formatConvertService
				.getIndexBaseById(proj_guid);
		

		BProjSum bProjSum = bProjSumService.getBProjSumById(proj_guid);
		if (bProjSum != null) {
			bProjSum.setSaleamountsum((double) Math.round(bProjSum
					.getSaleamountsum() / 10000));
		}

		RptCustomerCountMain rptCustomerCountMain = rptCustomerCountMainService
				.getRptCustomerCountMainById(proj_guid);
		if (rptCustomerCountMain != null) {
			if (rptCustomerCountMain.getCustomer_bought_sum() == null)
				rptCustomerCountMain.setCustomer_bought_sum(0);
		}

		RptCustomerBoughtCount rptCustomerBoughtCount = rptCustomerBoughtCountService
				.getIndexFlowById(proj_guid);
		if (rptCustomerBoughtCount != null) {
			if (rptCustomerBoughtCount.getCustomer_change_sum() == null)
				rptCustomerBoughtCount.setCustomer_change_sum(0);
			if (rptCustomerBoughtCount.getCustomer_expect_sum() == null)
				rptCustomerBoughtCount.setCustomer_expect_sum(0);
			if (rptCustomerBoughtCount.getCustomer_follow_sum() == null)
				rptCustomerBoughtCount.setCustomer_follow_sum(0);
			if (rptCustomerBoughtCount.getCustomer_forward_sum() == null)
				rptCustomerBoughtCount.setCustomer_forward_sum(0);
			if (rptCustomerBoughtCount.getCustomer_invest_sum() == null)
				rptCustomerBoughtCount.setCustomer_invest_sum(0);
			if (rptCustomerBoughtCount.getCustomer_self_sum() == null)
				rptCustomerBoughtCount.setCustomer_self_sum(0);
			if (rptCustomerBoughtCount.getCustomer_away_sum() == null)
				rptCustomerBoughtCount.setCustomer_away_sum(0);
		}

		List<RptProjCstArea> RptArea = rptProjCstAreaService
				.RptProjCstAreaById(proj_guid);
		// 转换为List,String[] 数组都可以
		List listareaName = new ArrayList();
		List listareaValue = new ArrayList();
		for (RptProjCstArea object : RptArea) {
			listareaName.add(object.getArea_id());
			listareaValue.add(object.getCst_num());
		}

		List<RptProjCstIndustry> RptCst = rptProjCstIndustryService
				.RptProjCstIndustryById(proj_guid);
		// 转换为List,String[] 数组都可以
		List listRptCstName = new ArrayList();
		List listRptCstValue = new ArrayList();
		for (RptProjCstIndustry object : RptCst) {
			listRptCstName.add(object.getIndustry_id());
			listRptCstValue.add(object.getCst_num());
		}

		List<RptCustomerAttachCount> RptAttachCount = rptCustomerAttachCountService
				.RptCustomerAttachCountById(proj_guid);

		List listData = new ArrayList();
		List listValue = new ArrayList();
		for (RptCustomerAttachCount object : RptAttachCount) {
			listData.add(object.getCustomer_age_type());
			listValue.add(object.getCustomer_age_sum());
		}

		// String[] arr1 = (String[])listData.toArray(new
		// String[listData.size()]);
		// String[] arr2 = (String[])listValue.toArray(new
		// String[listData.size()]);

		try {
			String strarea = gnerateJsonFromObject(listareaName);
			String strareaValue = gnerateJsonFromObject(listareaValue);
			String RptCstName = gnerateJsonFromObject(listRptCstName);
			String RptCstValue = gnerateJsonFromObject(listRptCstValue);

			System.out.println(strarea);
			System.out.println(strareaValue);
			System.out.println(RptCstName);
			System.out.println(RptCstValue);
			System.out.println(listData);
			System.out.println(listValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<BProjModel> projList = sFollowRecotdService.getProjById(proj_guid);
		Integer buildCount = sFollowRecotdService
				.getBuildCountByProjGUID(proj_guid);
		Integer roomCount = sFollowRecotdService
				.getRoomCountByProjGUID(proj_guid);
		Integer selledNum = sFollowRecotdService.getSelledNum(proj_guid);
		Integer allNum = sFollowRecotdService.getNoSellNum(proj_guid);
		List<SFProjModel> sFollowRecordList = sFollowRecotdService
				.getSFollowRecordByProjId(proj_guid); // 查询项目轨迹

		Map map = new HashMap();
		map.put("indexbase", indexbase); // 关键指标
		map.put("indexFlow", indexFlow); // 关键流程分析
		map.put("formatConvert", formatConvert); // 转化率分析
		map.put("bProjSum", bProjSum); // 项目汇总
		map.put("rptCustomerCountMain", rptCustomerCountMain); // 已购客户
		map.put("rptCustomerBoughtCount", rptCustomerBoughtCount); // 意向客户
		map.put("listareaName", listareaName); // 籍贯区域名字
		map.put("listareaValue", listareaValue); // 籍贯区域值
		map.put("listRptCstName", listRptCstName); // 从事行业名字
		map.put("listRptCstValue", listRptCstValue); // 从事行业值
		map.put("listData", listData); // 年龄分布
		map.put("RptAttachCount", RptAttachCount);
		map.put("RptArea", RptArea);
		map.put("listData", listData);
		map.put("listValue", listValue);
		map.put("rptXsFormatIndex", rptXsFormatIndex);

		if (projList != null && projList.size() > 0) {
			BProjModel bProj = projList.get(0);
			bProj.setBuildNum(buildCount);
			bProj.setRoomNum(roomCount);
			bProj.setSelledNum(selledNum);
			bProj.setNoSellNum(allNum - selledNum);
			map.put("bProj", bProj);
		}

		if (sFollowRecordList != null && sFollowRecordList.size() > 0) {
			for (int i = 0; i < sFollowRecordList.size(); i++) {
				Long cstGUID = sFollowRecordList.get(i).getCstGUID();
				if (cstGUID != null) {
					String cstGUIDStr = cstGUID.toString();
					cstGUIDStr = cstGUIDStr.substring(cstGUIDStr.length() - 1);
					String tablename = "cst_customer_" + cstGUIDStr;
					String cstName = sFollowRecotdService.getCstNameById(
							tablename, cstGUID);
					sFollowRecordList.get(i).setCstName(cstName);
					Date date = sFollowRecordList.get(i).getCreatetime();
					int year = date.getYear();
					if (year == 1970) {
						sFollowRecordList.get(i).setCreatetime(null);
					}
				}
			}

			map.put("sFollowRecordList", sFollowRecordList);
		}

		if (indexbase == null) {
			message = "未找到相关项目指标值";
			return ResultMessage.errorMsg(message);
		}

		return ResultMessage.successMsg("获取成功", map);
	}

	// @AuthPassport(rightCode = "pmp.IndexBase")
	@RequestMapping(value = "/indexWeekDetail")
	public @ResponseBody
	ResultMessage detailweek(@RequestParam String proj_guid) {
		// String pguid = request.getParameter("proj_guid");
		String message = "";
		if (proj_guid == null) {
			message = "此项目id不存在！";
			return ResultMessage.errorMsg(message);
		}
		
		IndexBase indexWeek = indexBaseService.getIndexWeekBaseById(proj_guid);
		// 带写工具类 亿转万，金额，数字为空默认为零
		if (indexWeek != null) {
			indexWeek.setConfess_amount_sum((double) Math.round(indexWeek
					.getConfess_amount_sum() / 10000));
			indexWeek.setSubscribe_amount_sum((double) Math.round(indexWeek
					.getSubscribe_amount_sum() / 10000));
			indexWeek.setContract_amount_sum((double) Math.round(indexWeek
					.getContract_amount_sum() / 10000));
			indexWeek.setReturned_amount_sum((double) Math.round(indexWeek
					.getReturned_amount_sum() / 10000));
		}

		Map map = new HashMap();
		map.put("indexbase", indexWeek);

		if (indexWeek == null) {
			message = "未找到相关项目指标值";
			return ResultMessage.errorMsg(message);
		}

		return ResultMessage.successMsg("获取成功", map);
	}

	// @AuthPassport(rightCode = "pmp.IndexBase")
	@RequestMapping(value = "/indexMonthDetail")
	public @ResponseBody
	ResultMessage detailMonth(@RequestParam String proj_guid) {
		// String pguid = request.getParameter("proj_guid");
				String message = "";
				if (proj_guid == null) {
					message = "此项目id不存在！";
					return ResultMessage.errorMsg(message);
				}
				
				IndexBase indexMonth = indexBaseService.getIndexMonthBaseById(proj_guid);
				// 带写工具类 亿转万，金额，数字为空默认为零
				if (indexMonth != null) {
					indexMonth.setConfess_amount_sum((double) Math.round(indexMonth
							.getConfess_amount_sum() / 10000));
					indexMonth.setSubscribe_amount_sum((double) Math.round(indexMonth
							.getSubscribe_amount_sum() / 10000));
					indexMonth.setContract_amount_sum((double) Math.round(indexMonth
							.getContract_amount_sum() / 10000));
					indexMonth.setReturned_amount_sum((double) Math.round(indexMonth
							.getReturned_amount_sum() / 10000));
				}
				
				
				
				Map map = new HashMap();
				map.put("indexbase", indexMonth);

				if (indexMonth == null) {
					message = "未找到相关项目指标值";
					return ResultMessage.errorMsg(message);
				}

				return ResultMessage.successMsg("获取成功", map);
	}
	
	@RequestMapping(value = "/indexCSBaseDetail")
	public @ResponseBody
	ResultMessage CSdetail(@RequestParam String proj_guid) {
		String message = "";
		if (proj_guid == null) {
			message = "此项目id不存在！";
			return ResultMessage.errorMsg(message);
		}
		
		RptKfKeyIndex rptKfKeyIndex = rptKfKeyIndexService.getIndexBaseById(proj_guid);
		if (rptKfKeyIndex != null) {
			if(rptKfKeyIndex.getCurr_member_num()== null)
				rptKfKeyIndex.setCurr_member_num(0);
			if(rptKfKeyIndex.getAdd_member_num()==null)
				rptKfKeyIndex.setAdd_member_num(0);
			if(rptKfKeyIndex.getExit_member_num()==null)
				rptKfKeyIndex.setExit_member_num(0);
			if(rptKfKeyIndex.getReception_record_num()==null)
				rptKfKeyIndex.setReception_record_num(0);
			if(rptKfKeyIndex.getPay_reception_num()==null)
				rptKfKeyIndex.setPay_reception_num(0);
			if(rptKfKeyIndex.getVisit_reception_num()==null)
				rptKfKeyIndex.setVisit_reception_num(0);
			if(rptKfKeyIndex.getTele_reception_num()==null)
				rptKfKeyIndex.setTele_reception_num(0);
			if(rptKfKeyIndex.getRepair_apply_num()==null)
				rptKfKeyIndex.setRepair_apply_num(0);
			if(rptKfKeyIndex.getGuar_handle_num()==null)
				rptKfKeyIndex.setGuar_handle_num(0);
			if(rptKfKeyIndex.getGuar_finished_num()==null)
				rptKfKeyIndex.setGuar_finished_num(0);
			if(rptKfKeyIndex.getRepair_hs()==null)
				rptKfKeyIndex.setRepair_hs(0);
			if(rptKfKeyIndex.getComplaint_num()==null)
				rptKfKeyIndex.setComplaint_num(0);
			if(rptKfKeyIndex.getVisit_num()==null)
				rptKfKeyIndex.setVisit_num(0);
		}
		
		RptKfFormatIndex rptKfFormatIndex = rptKfFormatIndexService.getIndexBaseById(proj_guid);
		if(rptKfFormatIndex != null){
			if(rptKfFormatIndex.getDevel_member_num()==null)
			   rptKfFormatIndex.setDevel_member_num(0);
			if(rptKfFormatIndex.getCst_num()==null)
			   rptKfFormatIndex.setCst_num(0);
			if(rptKfFormatIndex.getRepair_satisfy_num()==null)
				rptKfFormatIndex.setRepair_satisfy_num(0);
			if(rptKfFormatIndex.getRepair_num()==null)
				rptKfFormatIndex.setRepair_num(0);
			if(rptKfFormatIndex.getComplain_satisfy_num()==null)
				rptKfFormatIndex.setComplain_satisfy_num(0);
			if(rptKfFormatIndex.getComplain_num()==null)
				rptKfFormatIndex.setComplain_num(0);
		}
		RptKfIndexFlow rptKfIndexFlow = rptKfIndexFlowService.getIndexBaseById(proj_guid);
		if(rptKfIndexFlow != null){
			
		}
		
		Map map = new HashMap();
		map.put("rptKfKeyIndex", rptKfKeyIndex);
		map.put("rptKfFormatIndex", rptKfFormatIndex);
		map.put("rptKfIndexFlow", rptKfIndexFlow);
		
		return ResultMessage.successMsg("获取成功", map);
	}
	
	@RequestMapping(value = "/indexZYBaseDetail")
	public @ResponseBody
	ResultMessage ZYdetail(@RequestParam String proj_guid) {
		String message = "";
		if (proj_guid == null) {
			message = "此项目id不存在！";
			return ResultMessage.errorMsg(message);
		}
		
		RptZyKeyIndex rptZyKeyIndex = rptZyKeyIndexService.getIndexBaseById(proj_guid);
		if(rptZyKeyIndex != null){
			
		}
		
		RptZyIndexFlow rptZyIndexFlow = rptZyIndexFlowService.getIndexBaseById(proj_guid);
        if(rptZyIndexFlow != null){
			
		}
		
		RptZyFormatIndex rptZyFormatIndex = rptZyFormatIndexService.getIndexBaseById(proj_guid);
        if(rptZyFormatIndex != null){
			
		}
		
		RptZyFormatConvert rptZyFormatConvert = rptZyFormatConvertService.getIndexBaseById(proj_guid);
        if(rptZyFormatConvert != null){
			
		}
		
		Map map = new HashMap();
		map.put("rptZyKeyIndex", rptZyKeyIndex);
		map.put("rptZyIndexFlow", rptZyIndexFlow);
		map.put("rptZyFormatIndex", rptZyFormatIndex);
		map.put("rptZyFormatConvert", rptZyFormatConvert);
		
		return ResultMessage.successMsg("获取成功", map);
	}

	public static String gnerateJsonFromObject(Object object) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}
}
