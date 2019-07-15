package com.hongyang.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongyang.beans.PageQuery;
import com.hongyang.beans.PageResult;
import com.hongyang.common.JsonData;
import com.hongyang.model.MesProduct;
import com.hongyang.param.MesProductVo;
import com.hongyang.param.SearchProductParam;
import com.hongyang.service.ProductService;
@Controller
@RequestMapping("/product")
public class ProductController {
	private static final String FPATH="product/";
	
	@Resource
	private ProductService productService;
	
	@RequestMapping("/productinsert.page")
	public String productinsertPage() {
		return FPATH+"productinsert";
	}
	@RequestMapping("/productinsert.json")
	@ResponseBody
	public JsonData	productinsertAjax(MesProductVo mesProductVo) {
//		System.out.println("--------------------------------------");
//		System.out.println(mesProductVo.toString());
//		System.out.println("66666666666666666666");
		productService.productinsert(mesProductVo);
		return JsonData.success();
	}
	
	//材料批量到库页面
	@RequestMapping("/product.page")
	public String productPage() {
		return FPATH+"product";
	}
	
	//批量到库分页
	@RequestMapping("/product.json")
	@ResponseBody
	public JsonData searchProductAjax(SearchProductParam param,PageQuery page) {
		System.out.println("------------------------");
		System.out.println(param);
		PageResult<MesProduct> pr=productService.searchProductAjax(param,page);
		return JsonData.success(pr);
	}
	//修改
	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData updateAjax(MesProductVo mesProductVo) {
//		System.out.println("-----------------------");
//		System.out.println(mesProductVo);
		productService.updateAjax(mesProductVo);
		return JsonData.success();
	}
}
