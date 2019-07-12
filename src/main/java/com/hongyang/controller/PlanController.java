package com.hongyang.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongyang.beans.PageQuery;
import com.hongyang.beans.PageResult;
import com.hongyang.common.JsonData;
import com.hongyang.model.MesOrder;
import com.hongyang.param.MesPlanVo;
import com.hongyang.param.SearchPlanParam;
import com.hongyang.service.PlanService;
@Controller
@RequestMapping("/plan")
public class PlanController {
	private static String FPATH="plan/";
	
	
	@Resource
	private PlanService planService;
	//跳转页面
	@RequestMapping("/plan.page")
	public String planPage() {
		return FPATH+"plan";
	}
	
	//分页
	@RequestMapping("/plan.json")
	@ResponseBody
	public JsonData searchPlanAjax(SearchPlanParam param ,PageQuery page) {
		PageResult<MesOrder> pr=planService.searchPageList(param,page);
		return JsonData.success(pr);
	}
	//批量启动
	@RequestMapping("/planBatchStart.json")
	@ResponseBody
	public JsonData planBatchStartAjax(String ids) {
		planService.planBatchStartWithIds(ids);
		return JsonData.success();
	}
	//跳转计划已启动界面
	@RequestMapping("/planStarted.page")
	public String planStartedPage() {
		return FPATH+"planStarted";
	}
	//更新
	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData updateAjax(MesPlanVo mesPlanVo) {
		System.out.println("进来了----------------------");
		planService.update(mesPlanVo);
		return JsonData.success();
	}
}
