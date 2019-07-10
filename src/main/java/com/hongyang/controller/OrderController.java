package com.hongyang.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hongyang.common.JsonData;
import com.hongyang.param.MesOrderVo;
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
	
	@RequestMapping("/insert.json")
	public JsonData insertOrderAjax(MesOrderVo orderVo) {
		
		orderService.insertOrderBatch(orderVo);
		return JsonData.success();
	}
}
