package com.sxit.mgt.pmp.dto;

import java.util.Date;

public class RptCustomerBoughtCount{	private Long customer_bought_id;//   序列号	private String proj_id;//   项目编号	private String index_format_type;//   业态类型	private String customer_type;//   客户类型	private Integer customer_invest_sum;//   总投资客户数	private Integer customer_self_sum;//   总自住客户数	private Integer customer_forward_sum;//   总期房客户数	private Integer customer_change_sum;//   总更改业主客户数	private Integer customer_follow_sum;//   总跟进客户数	private Integer customer_expect_sum;//   总已预期客户数	private Integer customer_away_sum;//   总已流失客户数	private Date create_time;//   统计时间	public Long getCustomer_bought_id() {	    return this.customer_bought_id;	}	public void setCustomer_bought_id(Long customer_bought_id) {	    this.customer_bought_id=customer_bought_id;	}	public String getProj_id() {	    return this.proj_id;	}	public void setProj_id(String proj_id) {	    this.proj_id=proj_id;	}	public String getIndex_format_type() {	    return this.index_format_type;	}	public void setIndex_format_type(String index_format_type) {	    this.index_format_type=index_format_type;	}	public String getCustomer_type() {	    return this.customer_type;	}	public void setCustomer_type(String customer_type) {	    this.customer_type=customer_type;	}	public Integer getCustomer_invest_sum() {	    return this.customer_invest_sum;	}	public void setCustomer_invest_sum(Integer customer_invest_sum) {	    this.customer_invest_sum=customer_invest_sum;	}	public Integer getCustomer_self_sum() {	    return this.customer_self_sum;	}	public void setCustomer_self_sum(Integer customer_self_sum) {	    this.customer_self_sum=customer_self_sum;	}	public Integer getCustomer_forward_sum() {	    return this.customer_forward_sum;	}	public void setCustomer_forward_sum(Integer customer_forward_sum) {	    this.customer_forward_sum=customer_forward_sum;	}	public Integer getCustomer_change_sum() {	    return this.customer_change_sum;	}	public void setCustomer_change_sum(Integer customer_change_sum) {	    this.customer_change_sum=customer_change_sum;	}	public Integer getCustomer_follow_sum() {	    return this.customer_follow_sum;	}	public void setCustomer_follow_sum(Integer customer_follow_sum) {	    this.customer_follow_sum=customer_follow_sum;	}	public Integer getCustomer_expect_sum() {	    return this.customer_expect_sum;	}	public void setCustomer_expect_sum(Integer customer_expect_sum) {	    this.customer_expect_sum=customer_expect_sum;	}	public Integer getCustomer_away_sum() {	    return this.customer_away_sum;	}	public void setCustomer_away_sum(Integer customer_away_sum) {	    this.customer_away_sum=customer_away_sum;	}	public Date getCreate_time() {	    return this.create_time;	}	public void setCreate_time(Date create_time) {	    this.create_time=create_time;	}
	
}
