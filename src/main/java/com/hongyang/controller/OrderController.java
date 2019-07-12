package com.hongyang.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongyang.beans.PageQuery;
import com.hongyang.beans.PageResult;
import com.hongyang.common.JsonData;
import com.hongyang.model.MesOrder;
import com.hongyang.param.MesOrderVo;
import com.hongyang.param.SearchOrderParam;
import com.hongyang.service.OrderService;
@Controller
@RequestMapping("/order")
public class OrderController {
	private static String FPATH="order/";
	@Resource
	private OrderService orderService;
	
	@RequestMapping("/orderBatch.page")
	public String orderBatchPage() {
		return FPATH+"orderBatch";
	}
	//查看订单
	@RequestMapping("/order.page")
	public String orderPage() {
		return FPATH+"order";
	}
	//添加
	@ResponseBody
	@RequestMapping("/insert.json")
	public JsonData insertOrderAjax(MesOrderVo orderVo) {
		MesOrderVo orderVo1=new MesOrderVo();
		System.out.println("orderVo1:------------"+orderVo1.getCount());
		System.out.println("orderVo:-----------"+orderVo.getCount());
		orderService.insertOrderBatch(orderVo);
		return JsonData.success();
	}
	//修改
	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData updateOrderAjax(MesOrderVo orderVo) {
		orderService.updateOrder(orderVo);
		return JsonData.success();
	}
	//批量启动
	@RequestMapping("/orderBatchStart.json")
	@ResponseBody
	public JsonData orderBatchStartAjax(String ids) {
//		System.out.println(ids);
		orderService.orderBatchStart(ids);
		return JsonData.success();
	}
	
	//分页
	@ResponseBody
	@RequestMapping("/order.json")
	public JsonData searchPageAjax(SearchOrderParam param, PageQuery page) {
		PageResult<MesOrder> pr=orderService.searchPageList(param,page);
		return JsonData.success(pr);
	}
}
