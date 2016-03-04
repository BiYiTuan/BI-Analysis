package com.sxit.mgt.customer.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sxit.common.action.BaseAction;
import com.sxit.common.dto.ResultMessage;
import com.sxit.common.pagehelper.Page;
import com.sxit.common.excel.ExcelExport;
import com.sxit.common.excel.ExcelUtil;
import com.sxit.common.utils.CstAttachValues;
import com.sxit.mgt.customer.service.CstAttachService;
import com.sxit.mgt.customer.service.CstMemberService;
import com.sxit.mgt.customer.service.CustomerService;
import com.sxit.mgt.customer.service.SFollowRecordService;
import com.sxit.mgt.customer.service.TContractService;
import com.sxit.mgt.customer.vo.BProjectModel;
import com.sxit.mgt.customer.vo.ClientListModel;
import com.sxit.mgt.customer.vo.CustomerAllData;
import com.sxit.mgt.customer.vo.CustomerLable;
import com.sxit.mgt.customer.vo.FromOPP;
import com.sxit.mgt.customer.vo.RptChart;
import com.sxit.mgt.customer.vo.RptCstDataVo;
import com.sxit.mgt.customer.vo.SFollowRecordModel;
import com.sxit.mgt.customer.vo.TContractModel;
import com.sxit.mgt.customer.vo.UnionTrade;
import com.sxit.mgt.reports.dto.BAreaCompany;
import com.sxit.mgt.system.service.BOrgService;
import com.sxit.model.customer.ClientList;
import com.sxit.model.customer.CstAttach;
import com.sxit.model.customer.Customer;
import com.sxit.model.customer.RptCityDistributed;
import com.sxit.model.customer.RptClassification;
import com.sxit.model.customer.RptCstLabel;
import com.sxit.model.customer.RptCstNumber;
import com.sxit.model.customer.RptMutipleFormats;
import com.sxit.model.member.CstMember;


/**
 * @公司:深讯信科
 * @功能:客户画像 Action
 * @作者:xiongj    
 * @日期:2015-09-22 10:35:59  
 * @版本:1.0
 * @修改:
 */

@Controller
@RequestMapping("/customer")
public class CustomerAction extends BaseAction {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CstMemberService cstMemberService;
	
	@Autowired
	private TContractService tContractService;
	
	@Autowired
	private SFollowRecordService sFollowRecordService;
	
	@Autowired
	private CstAttachService cstAttachService;
	
	@Autowired
	private BOrgService bOrgService;
	
	
	//客户概览入口
	@RequestMapping(value = "/clientShow")
	public @ResponseBody ResultMessage cleintShow() {
		
		RptCstDataVo rptCstData = new RptCstDataVo();
		
		RptCstNumber  allCustomer = customerService.getAllCustomerVo();
		
		List<RptCstNumber> customerSorts = customerService.getRptCstNumber();
		
		List<RptMutipleFormats> customerFormat = customerService.getRptMutipleFormats();
		
		List<RptClassification> customerTypeProportion = customerService.getRptClassification();
				
		List<RptCstLabel> labels = customerService.getRptCstLabel();
		
		//前十
		RptChart chart1 = new RptChart();
		List<String> chart1_list1 = new ArrayList<String>();
		List<Long> chart1_list2 = new ArrayList<Long>();
		List<RptCityDistributed> first = customerService.getRptCityDistributedFirst();
		for (int i = 0; i < first.size(); i++) {
			chart1_list1.add(first.get(i).getCityName());
			if(first.get(i).getAmount() != null ){
				chart1_list2.add(first.get(i).getAmount());
			}else{
				chart1_list2.add(0L);
			}
		}
		chart1.setCategories(chart1_list1);
		chart1.setData(chart1_list2);
		
		//后十
		RptChart chart2 = new RptChart();
		List<String> chart2_list1 = new ArrayList<String>();
		List<Long> chart2_list2 = new ArrayList<Long>();
		List<RptCityDistributed> last = customerService.getRptCityDistributedLast();
		for (int i = 0; i < last.size(); i++) {
			chart2_list1.add(last.get(i).getCityName());
			if(last.get(i).getAmount() != null ){
				chart2_list2.add(last.get(i).getAmount());
			}else{
				chart2_list2.add(0L);
			}
		}
		chart2.setCategories(chart2_list1);
		chart2.setData(chart2_list2);
		
		rptCstData.setAllCustomer(allCustomer);
		rptCstData.setCustomerSorts(customerSorts);
		rptCstData.setCustomerFormat(customerFormat);
		rptCstData.setCustomerTypeProportion(customerTypeProportion);
		rptCstData.setLabels(labels);
		rptCstData.setChart1(chart1);
		rptCstData.setChart2(chart2);
		
		return ResultMessage.successMsg("获取成功", rptCstData);
	}
	
//	@AuthPassport(rightCode = "cstCustomer_manage")
	@RequestMapping(value = "/clientSearchInit")
	public @ResponseBody ResultMessage init() {
		
		List<BAreaCompany> areas = bOrgService.getAreaOrCompany(2, null);
		BAreaCompany area = null;
		BAreaCompany company = null;
		BAreaCompany prject = null;
		int area_size = areas.size();
		int company_size = 0;
		int project_size = 0;
		for (int i = 0; i < area_size; i++) {
			area = areas.get(i);
			if(area != null){
				String areaId = area.getId();
				if((areaId == null || "".equals(areaId)) || (area.getName() == null || "".equals(area.getName()))){
					areas.remove(i);
					i -= 1;
					area_size -= 1;
				}else{
					List<BAreaCompany> companys = bOrgService.getAreaOrCompany(3, areaId);
					company_size = companys.size();
					for (int j = 0; j < company_size; j++) {
						company = companys.get(j);
						if(company != null){
							String citycode = company.getId();
							if((citycode == null || "".equals(citycode)) || (company.getName() == null || "".equals(company.getName()))){
								companys.remove(company);
								j -=1;
								company_size -= 1;
							}else{
								List<BAreaCompany> projects = customerService.getProjects(citycode);
								project_size = projects.size();
								for (int k = 0; k < project_size; k++) {
									prject = projects.get(k);
									if(prject != null){
										String projId = prject.getId();
										if((projId == null || "".equals(projId)) || (prject.getName() == null || "".equals(prject.getName()))){
											projects.remove(prject);
											k -=1;
											project_size -= 1;
										}
									}else{
										projects.remove(prject);
										k -=1;
										project_size -= 1;
									}
								}
								companys.get(j).setChild(projects);
							}
							areas.get(i).setChild(companys);
						}else{
							companys.remove(company);
							j -=1;
							company_size -= 1;
						}
					}
				}
			}
			else{
				areas.remove(i);
				i -= 1;
				area_size -= 1;
			}
			
		}
		return ResultMessage.successMsg("获取成功", areas);
	}
	
	
//	@AuthPassport(rightCode = "cstCustomer_manage")
	@RequestMapping(value = "/clientSearch")
	public @ResponseBody ResultMessage cleintSearch(@RequestParam String number) {
		if (number == null || "".equals(number)) {
			String message = "请输入客户手机号码或者证件号码";
			return ResultMessage.errorMsg(message);
		}
		
		//根据尾号取分表
		String lastNum = number.substring(number.length() - 1);
		String table_name = "";	//表名
		String shuxing_name = "";	//字段名
		
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(number); 
		
		//如果是电话号码
		if(m.matches()){
			table_name = "cst_mobilemapping_" + lastNum;
			shuxing_name = "mobile";
		}else{
			if("X".equals(lastNum)){
				lastNum = "x";
			}
			table_name = "cst_cardidmapping_" + lastNum;
			shuxing_name = "cardId";
		}
		
		Long cstGUID = customerService.getCstGUID(number, table_name, shuxing_name);
		if(cstGUID == null || cstGUID == 0L){
			return ResultMessage.errorMsg("你好，没有找到符合你输入关键词的客户", -1);
		}
		return ResultMessage.successMsg("获取成功", cstGUID);
	}
	
//	@AuthPassport(rightCode = "cstCustomer_manage")
	@RequestMapping(value = "/clientList")
	public @ResponseBody ResultMessage clientList(@RequestParam Integer currentPage, Integer PageSize, String areaSearch, String citySearch, String projectSearch, String formatCheckedSearch, String startDate, String endDate) {
		
		if(currentPage == null){
			currentPage = 1;
		}
		if(PageSize == null){
			PageSize = 10;
		}
		int totalRows = customerService.getClientListCount(areaSearch, citySearch, projectSearch, formatCheckedSearch, startDate, endDate);
		Page<ClientList> list = customerService.getClientList(currentPage, PageSize, areaSearch, citySearch, projectSearch, formatCheckedSearch, startDate, endDate);
		
		ClientListModel clientListModel = new ClientListModel();
		clientListModel.setClientList(list);
		clientListModel.setTotalRows(totalRows);
		clientListModel.setPageSize(PageSize);
		int totalPages = totalRows/PageSize + 1;
		clientListModel.setTotalPages(totalPages);
		
		return ResultMessage.successMsg("获取成功", clientListModel);
	}
	
	/**
	 * 明细
	 * 
	 * @param cstguid
	 * @return
	 */
//	@AuthPassport(rightCode = "cstCustomer_manage")
	@RequestMapping(value = "/cstCustomerDetail")
	public @ResponseBody ResultMessage detail(@RequestParam Long cstGUID) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat df = new DecimalFormat("######0");
		String lastChar = cstGUID.toString().substring(cstGUID.toString().length() - 1);
		
		String message = "";
		if (cstGUID == null || cstGUID == 0L) {
			message = "请求的链接错误";
			return ResultMessage.errorMsg(message);
		}
		
		//所有数据的封装
		CustomerAllData customerAllData = new CustomerAllData();
		
		//会员关系
		List<CstMember> cstMemberList = cstMemberService.getCstMember(cstGUID);
		
		//交易关系
		List<TContractModel> tContractList = new ArrayList<TContractModel>();
		
		List<TContractModel> sProductOrderList = tContractService.getSProductOrder(cstGUID);
		for (TContractModel tContractModel : sProductOrderList) {
			if(tContractModel != null){
				String productCode = tContractModel.getRoomGUID();
				List<TContractModel> bRoomList  = tContractService.getBRoom(productCode);
				if(bRoomList != null && bRoomList.size() > 0 && bRoomList.get(0) != null){
					TContractModel model = new TContractModel();
					model.setRoom(bRoomList.get(0).getRoom());
					model.setRoomGUID(bRoomList.get(0).getRoomGUID());
					model.setRoomId(bRoomList.get(0).getRoomId());
					
					String projGUID = bRoomList.get(0).getProjGUID();
					if(projGUID != null && !"".equals(projGUID)){
						List<TContractModel> bProjList = tContractService.getBProj(projGUID);
						if(bProjList != null && bProjList.size() > 0 && bProjList.get(0)!= null){
							model.setProjName(bProjList.get(0).getProjName());
						}
					}
					String bldGUID = bRoomList.get(0).getBldGUID();
					if(bldGUID != null && !"".equals(bldGUID)){
						List<TContractModel> bBuildingList = tContractService.getBBuilding(bldGUID);
						if(bBuildingList != null && bBuildingList.size() > 0){
							String roomNum = bBuildingList.get(0).getBldFullName();
							if(roomNum != null && !"".equals(roomNum)){
								int s = 0;
								for (int j = 0; j < 2; j++) {
									s = roomNum.indexOf("-");
									roomNum = roomNum.substring(s + 1);
								}
								model.setBldFullName(roomNum);
							}
						}
					}
					List<TContractModel> tContracts = tContractService.getTContract(productCode);
					if(tContracts != null && tContracts.size() > 0 && tContracts.get(0)!= null){
						Date qsDate = tContracts.get(0).getQsDate();
						if(qsDate != null){
							String qsDateStr = sdf.format(qsDate);
							String str = qsDateStr.substring(0, 4);
							if("1970".equals(str)){
								model.setQsDateStr(null);
							}else{
								model.setQsDateStr(qsDateStr);
							}
							model.setQsDate(tContracts.get(0).getQsDate());
						}
						
						Double money = tContracts.get(0).getHtTotal();
						if(money != null){
							money = money/10000;
							model.setHtTotal(Double.parseDouble(df.format(money)));
						}
					}
					tContractList.add(model);
				}
			}
		}
		
		//房产关系
		List<TContractModel> tContractRoomList = tContractList;
		
		//房产特征
		List<BProjectModel> tradeList = new ArrayList<BProjectModel>();
		
		List<TContractModel> tradeSPOList = tContractService.getSProductOrder(cstGUID);
		if(tradeSPOList != null && tradeSPOList.size() > 0){
			for (TContractModel tContractModel : tradeSPOList) {
				if(tContractModel != null){
					String productCode = tContractModel.getRoomGUID();
					List<BProjectModel> bTradeRoomList  = cstMemberService.getTradeRoomList(productCode);
					if(bTradeRoomList != null && bTradeRoomList.size() > 0 && bTradeRoomList.get(0) != null){
						BProjectModel model = new BProjectModel();
						model.setRoomStru(bTradeRoomList.get(0).getRoomStru());
						model.setBldArea(bTradeRoomList.get(0).getBldArea());
						
						String projGUID = bTradeRoomList.get(0).getProjGUID();
						if(projGUID != null && !"".equals(projGUID)){
							List<BProjectModel> bTradeProjList = cstMemberService.getTradeProjList(projGUID);
							if(bTradeProjList != null && bTradeProjList.size() > 0 && bTradeProjList.get(0)!= null){
								model.setProjGUID(bTradeProjList.get(0).getProjGUID());
								model.setProjName(bTradeProjList.get(0).getProjName());
								model.setProjArea(bTradeProjList.get(0).getProjArea());
							}
						}
						String bldGUID = bTradeRoomList.get(0).getBldGUID();
						if(bldGUID != null && !"".equals(bldGUID)){
							List<BProjectModel> bTradeBuildingList = cstMemberService.getTradeBBuilding(bldGUID);
							if(bTradeBuildingList != null && bTradeBuildingList.size() > 0 && bTradeBuildingList.get(0) != null){
								model.setBldProPerty(bTradeBuildingList.get(0).getBldProPerty());
							}
						}
						List<TContractModel> tContracts = tContractService.getTContract(productCode);
						if(tContracts != null && tContracts.size() > 0 && tContracts.get(0)!= null){
							
							Double money = tContracts.get(0).getHtTotal();
							if(money != null){
								money = money/10000;
								model.setPrice(Double.parseDouble(df.format(money)));
							}
						}
						tradeList.add(model);
					}
				}
			}
		}
		
		//意向特征
		List<BProjectModel> oppList = cstMemberService.getOppList(cstGUID);
		String attachTabNum = "cst_attach_" + lastChar;
		if(oppList != null && oppList.size() > 0){
			for (int i = 0; i < oppList.size(); i++) {
				if(oppList.get(i) != null){
					String projGUID = oppList.get(i).getProjGUID();
					//意向等级
					List<CstAttach> oppIntentLevelList = cstAttachService.getCstAttachValues(cstGUID, projGUID, CstAttachValues.CST_OPP_INTENTLEVEL, attachTabNum);
					if(oppIntentLevelList != null && oppIntentLevelList.size() > 0){
						oppList.get(i).setOppIntentLevel(oppIntentLevelList.get(0).getAttachValue());
					}
					//意向户型
					List<CstAttach> oppHuxingIntentList = cstAttachService.getCstAttachValues(cstGUID, projGUID, CstAttachValues.CST_OPP_HUXINGINTENT, attachTabNum);
					if(oppHuxingIntentList != null && oppHuxingIntentList.size() > 0){
						oppList.get(i).setOppHuxingIntent(oppHuxingIntentList.get(0).getAttachValue());
					}
					//意向面积
					List<CstAttach> oppRoomAreaIntentList = cstAttachService.getCstAttachValues(cstGUID, projGUID, CstAttachValues.CST_OPP_ROOMAREAINTENT, attachTabNum);
					if(oppRoomAreaIntentList != null && oppRoomAreaIntentList.size() > 0){
						oppList.get(i).setOppRoomAreaIntent(oppRoomAreaIntentList.get(0).getAttachValue());
					}
					//意向价格
					List<CstAttach> oppTotalIntentList = cstAttachService.getCstAttachValues(cstGUID, projGUID, CstAttachValues.CST_OPP_TOTALINTENT, attachTabNum);
					if(oppTotalIntentList != null && oppTotalIntentList.size() > 0){
						oppList.get(i).setOppTotalIntent(oppTotalIntentList.get(0).getAttachValue());
					}
					//意向区域
					List<CstAttach> oppAreaIntentList = cstAttachService.getCstAttachValues(cstGUID, projGUID, CstAttachValues.CST_OPP_AREAINTENT, attachTabNum);
					if(oppAreaIntentList != null && oppAreaIntentList.size() > 0){
						oppList.get(i).setOppAreaIntent(oppAreaIntentList.get(0).getAttachValue());
					}
					//意向项目
					List<CstAttach> oppProjectIntentList = cstAttachService.getCstAttachValues(cstGUID, projGUID, CstAttachValues.CST_OPP_PROJECTINTENT, attachTabNum);
					if(oppProjectIntentList != null && oppProjectIntentList.size() > 0){
						oppList.get(i).setOppProjectIntent(oppProjectIntentList.get(0).getAttachValue());
					}
					oppList.get(i).setOppTypeIntent("未知");
				}else{
					oppList = null;
					break;
				}
			}
		}
		
		//关联房产
		List<UnionTrade> unionTradeList = new ArrayList<UnionTrade>();
		
		List<TContractModel> utSPOtradeList = tContractService.getSProductOrder(cstGUID);
		if(utSPOtradeList != null && utSPOtradeList.size() > 0){
			for (TContractModel tContractModel : utSPOtradeList) {
				if(tContractModel != null){
					String productCode = tContractModel.getRoomGUID();
					List<TContractModel> utRoomList  = tContractService.getBRoom(productCode);
					if(utRoomList != null && utRoomList.size() > 0 && utRoomList.get(0) != null){
						UnionTrade model = new UnionTrade();
						model.setRoomGUID(utRoomList.get(0).getRoomGUID());
						model.setRoom(utRoomList.get(0).getRoom());
						model.setRoomId(utRoomList.get(0).getRoomId());
						
						String projGUID = utRoomList.get(0).getProjGUID();
						if(projGUID != null && !"".equals(projGUID)){
							List<TContractModel> utProjList = tContractService.getBProj(projGUID);
							if(utProjList != null && utProjList.size() > 0 && utProjList.get(0)!= null){
								model.setProjName(utProjList.get(0).getProjName());
							}
						}
						String bldGUID = utRoomList.get(0).getBldGUID();
						if(bldGUID != null && !"".equals(bldGUID)){
							List<UnionTrade> utBuildingList = cstMemberService.getUnionTradeBuilding(bldGUID);
							if(utBuildingList != null && utBuildingList.size() > 0 && utBuildingList.get(0) != null){
								model.setBldName(utBuildingList.get(0).getBldName());
							}
						}
						unionTradeList.add(model);
					}
				}
			}
		}
		
		//所有邮箱地址
		List<CstAttach> mailList = cstAttachService.getCstAttachValues(cstGUID, null, CstAttachValues.CST_EMAIL, attachTabNum);
		
		//房产区域
		List<CstAttach> lifeAreaList = cstAttachService.getCstAttachValues(cstGUID, null, CstAttachValues.CST_LIFEAREA, attachTabNum);
		if(lifeAreaList != null && lifeAreaList.size() > 0){
			if(lifeAreaList.get(0) != null){
				String  lifeArea = lifeAreaList.get(0).getAttachValue();
				customerAllData.setLifeArea(lifeArea);
			}
		}
		//籍贯
		List<CstAttach> hometownList = cstAttachService.getCstAttachValues(cstGUID, null, CstAttachValues.CST_HOMETOWN, attachTabNum);
		if(hometownList != null && hometownList.size() > 0){
			if(hometownList.get(0) != null){
				String  hometown = hometownList.get(0).getAttachValue();
				customerAllData.setHometown(hometown);
			}
		}
		
		//工作区域
		List<CstAttach> addressList = cstAttachService.getCstAttachValues(cstGUID, null, CstAttachValues.CST_WORKAREA, attachTabNum);
		if(addressList != null && addressList.size() > 0){
			if(addressList.get(0) != null){
				String  address = addressList.get(0).getAttachValue();
				customerAllData.setAddress(address);
			}
		}
		
		//文化程度
		List<CstAttach> degreeList = cstAttachService.getCstAttachValues(cstGUID, null, CstAttachValues.CST_DEGREE, attachTabNum);
		if(degreeList != null && degreeList.size() > 0){
			if(degreeList.get(0) != null){
				String  degree = degreeList.get(0).getAttachValue();
				customerAllData.setDegree(degree);
			}
		}
		
		//从事行业
		List<CstAttach> jobList = cstAttachService.getCstAttachValues(cstGUID, null, CstAttachValues.CST_JOB, attachTabNum);
		if(jobList != null && jobList.size() > 0){
			if(jobList.get(0) != null){
				String  job = jobList.get(0).getAttachValue();
				customerAllData.setJob(job);
			}
		}
		
		//工作职位
		List<CstAttach> positionList = cstAttachService.getCstAttachValues(cstGUID, null, CstAttachValues.CST_POSITION, attachTabNum);
		if(positionList != null && positionList.size() > 0){
			if(positionList.get(0) != null){
				String  position = positionList.get(0).getAttachValue();
				customerAllData.setPosition(position);
			}
		}
		
		//计算客户类型
		int count = cstMemberService.getSProductOrderCount(cstGUID);//判断是否为已购客户
		
		//判断是否为意向客户
		boolean flag = false;
		List<String> oppIds = cstMemberService.getOppGUIDsFromOPP(cstGUID);
		if(oppIds != null && oppIds.size() > 0){
			List<FromOPP> fromOPPList = cstMemberService.getFromOPP(oppIds);
			if(fromOPPList != null && fromOPPList.size() > 0){
				for (FromOPP fromOPP : fromOPPList) {
					if(fromOPP != null){
						List<FromOPP> fromTCstTradeList = cstMemberService.getFromTCstTrade(fromOPP.getOppGUID(), fromOPP.getRoomGUID());
						if(fromTCstTradeList == null || fromTCstTradeList.size() == 0){
							flag = true;
							break;
						}else{
							for (FromOPP fromOPP2 : fromTCstTradeList) {
								if(fromOPP2 != null){
									int count_TContract = cstMemberService.getFromTContract(fromOPP2.getOppGUID(), fromOPP2.getRoomGUID());
									if(count_TContract <= 0){
										flag = true;
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		
		String customerName = "";
		//客户基础信息
		String table_name = "cst_customer_" + lastChar;
		List<Customer> customerList = customerService.getCstCustomerById(table_name, cstGUID);
		if(customerList !=null && customerList.size() > 0){
			Customer customer = customerList.get(0);
			customerName = customer.getCstname();
			if(customerName == null || "".equals(customerName)){
				customerName = "***";
			}
			String mobile = customer.getMobiletel();
			int length = 0;
			if(mobile != null && !"".equals(mobile)){
				length = mobile.length();
				mobile = mobile.substring(0, 3) + "****" + mobile.substring(length - 4);
				customer.setMobiletel(mobile);
			}
			
			String cardId = customer.getCardid();
			if(cardId != null && !"".equals(cardId)){
				length = cardId.length();
				cardId = cardId.substring(0, length - 4) + "****";
				customer.setCardid(cardId);
			}
			
			//customer.customerType:0未知；1意向客户；2已购客户；3既是意向客户又是已购客户
			if(customer != null){
				customer.setCustomerType(0);
				if(count > 0){
					if(flag){
						customer.setCustomerType(3);
					}else{
						customer.setCustomerType(2);
					}
				}else{
					if(flag){
						customer.setCustomerType(1);
					}
				}
				
				Date birthday = customer.getBirthdate();
				if(birthday != null){
					String birthStr = sdf.format(birthday);
					Integer age = getAge(birthStr);
					birthStr = birthStr.substring(5);
					customer.setBirthStr(birthStr);
					
					customer.setAgeStr(age.toString());
				}
				customerAllData.setCustomer(customer);
			}
		}
		
		//客户轨迹
		List<SFollowRecordModel> sFollowRecordList= sFollowRecordService.getSFollowRecordById(cstGUID);
		
		//客户标签
		List<CustomerLable> customerLables = customerService.getCustomerLables(cstGUID);
		
		customerAllData.setCstMemberList(cstMemberList);
		customerAllData.settContractList(tContractList);
		customerAllData.settContractRoomList(tContractRoomList);
		customerAllData.setsFollowRecordList(sFollowRecordList);
		customerAllData.setTradeList(tradeList);
		customerAllData.setOppList(oppList);
		customerAllData.setUnionTradeList(unionTradeList);
		customerAllData.setMailList(mailList);
		customerAllData.setCustomerLables(customerLables);
		
		return ResultMessage.successMsg("获取成功", customerAllData);
		
	}
	
	public static int getAge(String brithday){
		int age = 0;
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
			String currentTime = formatDate.format(calendar.getTime());
			Date today = formatDate.parse(currentTime);
			Date brithDay = formatDate.parse(brithday);
			int todayM = today.getMonth();
			int todayD = today.getDay();
			int brithM = brithDay.getMonth();
			int brithD = brithDay.getDay();
			age = today.getYear() - brithDay.getYear();
			if(todayM > brithM){
				age += 1;
			}else if(todayM == brithM){
				if(todayD >= brithD){
					age += 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return age;
	}
	
	/*
	 * 导出execl
	 */
	@RequestMapping(value = "/customerExport")
	public @ResponseBody ResultMessage export(@RequestParam Integer currentPage, Integer PageSize, String areaSearch, String citySearch, String projectSearch, String formatCheckedSearch, String startDate, String endDate){
		if(currentPage == null){
			currentPage = 1;
		}
		if(PageSize == null){
			PageSize = 10;
		}
		int totalRows = customerService.getClientListCount(areaSearch, citySearch, projectSearch, formatCheckedSearch, startDate, endDate);
		Page<ClientList> list = customerService.getClientList(currentPage, PageSize, areaSearch, citySearch, projectSearch, formatCheckedSearch, startDate, endDate);
		
		ClientListModel clientListModel = new ClientListModel();
		clientListModel.setClientList(list);
		clientListModel.setTotalRows(totalRows);
		clientListModel.setPageSize(PageSize);
		int totalPages = totalRows/PageSize + 1;
		clientListModel.setTotalPages(totalPages);
		
		if (list != null && list.size() > 0) {
			Map map = new HashMap();

			Map<Integer, String> userTypeMap = new HashMap<Integer, String>();
			userTypeMap.put(1, "男");
			userTypeMap.put(2, "女");
			userTypeMap.put(0, "未知");

			map.put("userTypeMap", userTypeMap);

			try {
				ExcelExport export = ExcelUtil.exportList(list, "customerExport",
						"客户数据", map);
				if(export!=null)
				{
					this.dowloadExcel(export, "customerdata.xls");
					return ResultMessage.successMsg("下载成功!");
				}else{
					return ResultMessage.successMsg("下载失败!");
				}

			} catch (Exception e) {
				e.printStackTrace();

				return ResultMessage.errorMsg("下载出错!");
			}
		} else {
			return ResultMessage.errorMsg("您要下载的数据为空!");
		}

	}
}
