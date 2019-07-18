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
	
	//批量到库与到库查询分页
	@RequestMapping("/product.json")
	@ResponseBody
	public JsonData searchProductAjax(SearchProductParam param,PageQuery page) {
//		System.out.println("------------------------");
//		System.out.println(param);
		PageResult<MesProduct> pr=productService.searchProductAjax(param,page);
		return JsonData.success(pr);
	}
	//修改
	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData updateAjax(MesProductVo mesProductVo) {
		productService.updateAjax(mesProductVo);
		return JsonData.success();
	}
	
	//到库查询页面
	@RequestMapping("/productCome.page")
	public String productComePage() {
		return FPATH+"productCome";
	}
	
	//批量到库逻辑
	@RequestMapping("/productStart.json")
	@ResponseBody
	public JsonData productStart(String ids) {
		productService.productStart(ids);
		return JsonData.success();
	}
	
	//钢锭查询页面
	@RequestMapping("/productIron.page")
	public String productIronPage() {
		return FPATH+"productIron";
	}
	//钢锭查询页面分页
	@RequestMapping("/productIron.json")
	@ResponseBody
	public JsonData searchProductIronAjax(SearchProductParam param,PageQuery page) {
		System.out.println("-------------------------------------------");
		System.out.println(param);
		PageResult<MesProduct> pr=productService.searchProductIronAjax(param,page);
		return JsonData.success(pr);
	}
	
	//材料绑定页面
	@RequestMapping("/productBindList.page")
	public String productBindListPage() {
		return FPATH+"productBindList";
	}
//	//真正的绑定页面
//	@RequestMapping("/bind.page")
//	public String bindPage() {
//		return FPATH+"bindPage";
//	}
	//真正的绑定方法逻辑
	@RequestMapping("/realBind.json")
	@ResponseBody
	public JsonData realBindAjax(String ids,Float status) {
//		System.out.println("---------------------------ids------------------------");
//		System.out.println(ids);
//		System.out.println(status);
		productService.realBindAjax(ids,status);
		return JsonData.success();
	}
	
	//绑定页面查询一条父级数据
	@RequestMapping("/bindOneSearch.json")
	@ResponseBody
	public JsonData bindOneSearchAjax(Integer id) {
		System.out.println("-----------------------id-------------------------------");
		System.out.println(id);
		MesProduct mp=productService.bindOneSearchAjax(id);
		return JsonData.success(mp);
	}
	
	//解除绑定逻辑
	@RequestMapping("/unBindProduct.json")
	@ResponseBody
	public JsonData unBindProductAjax(Float bakweight,Float bakweight_F,String ids) {
//		System.out.println("---------------------解绑数据-----------------------");
//		System.out.println(bakweight);
//		System.out.println(bakweight_F);
//		System.out.println(ids);
		productService.unBindProductAjax(bakweight,bakweight_F,ids);
		return JsonData.success();
	}
}
