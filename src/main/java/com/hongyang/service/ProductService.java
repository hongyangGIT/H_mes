package com.hongyang.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.hongyang.beans.PageQuery;
import com.hongyang.beans.PageResult;
import com.hongyang.dao.MesProductCustomerMapper;
import com.hongyang.dao.MesProductMapper;
import com.hongyang.dto.SearchProductDto;
import com.hongyang.exception.SysMineException;
import com.hongyang.model.MesOrder;
import com.hongyang.model.MesProduct;
import com.hongyang.param.MesProductVo;
import com.hongyang.param.SearchProductParam;
import com.hongyang.util.BeanValidator;

@Service
public class ProductService {
	private IdGenerator ig;
	
	@Resource
	private MesProductMapper mesProductMapper;
	@Resource
	private MesProductCustomerMapper mesProductCustomerMapper;
	//添加材料
	public void productinsert(MesProductVo mesProductVo) {
//		List<String> lists=getPlanProducts();
//		for(String s:lists) {
//			System.out.println(s);
//		}
		String excption=null;
		//校验
		BeanValidator.check(mesProductVo);
		//判断是否批量添加
		Integer counts=mesProductVo.getCount();
//		System.out.println("--------------"+counts);
		//productId 集合
		//id集合
		List<String > productIds=createProductIdsDefault(Long.valueOf(counts));
		
//		System.out.println("----------------------");
//		System.out.println("投料重量："+mesProductVo.getProductRealweight());
//		if(mesProductVo.getProductRealweight().equals("0")) {
//			System.out.println("等于0");
//		}else {
//			System.out.println("不等于0");
//		}
//		System.out.println(mesProductVo.getProductMaterialsource());
		for(String productId:productIds) {
			try {
				//分为两种情况，材料来源：一种是钢锭，一种是其他材料
				if(mesProductVo.getProductMaterialsource().equals("钢锭")) {
					//如果是钢锭的话，判断投料重量与剩余重量是否为0，为0在添加进入数据库
					//从界面传过来的数据都为String类型，直接用equals("0")就可以
					if(mesProductVo.getProductRealweight().equals("0")&&mesProductVo.getProductLeftweight().equals("0")) {
						insertTemp(mesProductVo,productId);
					}else {
						excption="钢锭的投料重量与剩余重量应该为0";
						throw new SysMineException(excption);
					}
				}else {
					insertTemp(mesProductVo,productId);
//			
				}
			} catch (Exception e) {
				if(excption==null) {
					excption="添加材料过程出现问题";
				}
				throw new SysMineException(excption);
			}
		}
		
	}
	//添加数据库抽离出来的方法
	public void insertTemp(MesProductVo mesProductVo,String productId) {
		//将VO转换成PO
		MesProduct mesProduct=MesProduct.builder()
								.id(mesProductVo.getId())
								.pId(mesProductVo.getPId())
								.productId(productId)
								.productOrderid(mesProductVo.getProductOrderid())
								.productPlanid(mesProductVo.getProductPlanid())
								.productTargetweight(Float.parseFloat(mesProductVo.getProductTargetweight()))
								.productRealweight(Float.parseFloat(mesProductVo.getProductRealweight()))
								.productLeftweight(Float.parseFloat(mesProductVo.getProductLeftweight()))
								.productBakweight(Float.parseFloat(mesProductVo.getProductLeftweight()))
								.productIrontype(mesProductVo.getProductIrontype())
								.productIrontypeweight(Float.parseFloat(mesProductVo.getProductIrontypeweight()))
								.productMaterialname(mesProductVo.getProductMaterialname())
								.productImgid(mesProductVo.getProductImgid())
								.productHeatid(mesProductVo.getProductHeatid())
								.productMaterialsource(mesProductVo.getProductMaterialsource())
								.productStatus(Integer.parseInt(mesProductVo.getProductStatus()))
								.productRemark(mesProductVo.getProductRemark())
								.build();
				//模拟用户信息
		mesProduct.setProductOperator("hongyang");
		mesProduct.setProductOperateIp("127.0.0.1");
		mesProduct.setProductOperateTime(new Date());
//		System.out.println("-----------------------------");
//		System.out.println(mesProduct);
//		System.out.println(productId);
		mesProductMapper.insertSelective(mesProduct);
	}
	//修改
	public void updateAjax(MesProductVo mesProductVo) {
		String excption=null;
		BeanValidator.check(mesProductVo);
		//判断该id有没有
		MesProduct before = mesProductMapper.selectByPrimaryKey(mesProductVo.getId());
		//没有就出异常返回界面
		Preconditions.checkNotNull(before, "待更新的材料不存在");
		try {
			//分为两种情况，材料来源：一种是钢锭，一种是其他材料
			if(mesProductVo.getProductMaterialsource().equals("钢锭")) {
				//如果是钢锭的话，判断投料重量与剩余重量是否为0，为0在添加进入数据库
				//从界面传过来的数据都为String类型，直接用equals("0")就可以
				if(mesProductVo.getProductRealweight().equals("0")&&mesProductVo.getProductLeftweight().equals("0")) {
					updateTemp(mesProductVo);
				}else {
					excption="钢锭的投料重量与剩余重量应该为0";
					throw new SysMineException(excption);
				}
			}else {
				updateTemp(mesProductVo);
			}
			
			
		} catch (Exception e) {
			if(excption==null) {
				excption="更新过程出现问题";
			}
			throw new SysMineException(excption);
		}
			
	}
	
	//修改方法
	public void updateTemp(MesProductVo mesProductVo) {
		//将VO转换成PO
		MesProduct mesProduct=MesProduct.builder()
				.id(mesProductVo.getId())
				.pId(mesProductVo.getPId())
				.productId(mesProductVo.getProductId())
				.productOrderid(mesProductVo.getProductOrderid())
				.productPlanid(mesProductVo.getProductPlanid())
				.productTargetweight(Float.parseFloat(mesProductVo.getProductTargetweight()))
				.productRealweight(Float.parseFloat(mesProductVo.getProductRealweight()))
				.productLeftweight(Float.parseFloat(mesProductVo.getProductLeftweight()))
				.productBakweight(Float.parseFloat(mesProductVo.getProductLeftweight()))
				.productIrontype(mesProductVo.getProductIrontype())
				.productIrontypeweight(Float.parseFloat(mesProductVo.getProductIrontypeweight()))
				.productMaterialname(mesProductVo.getProductMaterialname())
				.productImgid(mesProductVo.getProductImgid())
				.productHeatid(mesProductVo.getProductHeatid())
				.productMaterialsource(mesProductVo.getProductMaterialsource())
				.productStatus(Integer.parseInt(mesProductVo.getProductStatus()))
				.productRemark(mesProductVo.getProductRemark())
				.build();
			//模拟用户信息
			mesProduct.setProductOperator("hongyang");
			mesProduct.setProductOperateIp("127.0.0.1");
			mesProduct.setProductOperateTime(new Date());
//			System.out.println("-------------------------------");
//			System.out.println(mesProduct);
			mesProductMapper.updateByPrimaryKeySelective(mesProduct);
	}
	
	
	
	//批量到库分页
	public PageResult<MesProduct> searchProductAjax(SearchProductParam param, PageQuery page) {
		//校验
		BeanValidator.check(page);
		// searchDto 用于分页的where语句后面
		SearchProductDto dto=new SearchProductDto();	
		if (StringUtils.isNotBlank(param.getKeyword())) {
			dto.setKeyword("%" + param.getKeyword() + "%");
		}
		if (StringUtils.isNotBlank(param.getSearch_materialsource())) {
			dto.setSearch_materialsource(param.getSearch_materialsource());
		}
		if (StringUtils.isNotBlank(param.getSearch_status())) {
			dto.setSearch_status(Integer.parseInt(param.getSearch_status()));
		}
		int count = mesProductCustomerMapper.countBySearchDto(dto);
		if (count > 0) {
			List<MesProduct> productList = mesProductCustomerMapper.getPageListBySearchDto(dto, page);
			return PageResult.<MesProduct>builder().total(count).data(productList).build();
		}
		return PageResult.<MesProduct>builder().build();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	//自定义id生成器
	//��ȡid����
		// ��ȡid����
		public List<String> createProductIdsDefault(Long ocounts) {
			if (ig == null) {
				ig = new IdGenerator();
			}

			ig.setCurrentdbidscount(getPlanCount());
			List<String> list = ig.initIds(ocounts);
			ig.clear();
			return list;
		}
		
		//��ȡ���ݿ��е�����
		private Long getPlanCount() {
			return mesProductCustomerMapper.getProductCount();
		}
		private List<String> getPlanProducts() {
			return mesProductCustomerMapper.getPlanProducts();
		}
			// 1 Ĭ�����ɴ���
			// 2 �ֹ����ɴ���
			// id������
			class IdGenerator {
				// ������ʼλ��
				private Long currentdbidscount;
				private List<String> ids = new ArrayList<String>();
				private String idpre;
				private String idafter;

				public IdGenerator() {
				}

				public Long getCurrentdbidscount() {
					return currentdbidscount;
				}

				public void setCurrentdbidscount(Long currentdbidscount) {
					this.currentdbidscount = currentdbidscount;
					if (null == this.ids) {
						this.ids = new ArrayList<String>();
					}
				}

				
				public List<String> getIds() {
					return ids;
				}

				public void setIds(List<String> ids) {
					this.ids = ids;
				}

				public String getIdpre() {
					return idpre;
				}

				public void setIdpre(String idpre) {
					this.idpre = idpre;
				}

				

				public String getIdafter() {
					return idafter;
				}

				public void setIdafter(String idafter) {
					this.idafter = idafter;
				}

				public List<String> initIds(Long ocounts) {
					for (int i = 0; i < ocounts; i++) {
						this.ids.add(getIdPre() + getIdAfter(i));
					}
					return this.ids;
				}

				//
				private String getIdAfter(int addcount) {
					// ϵͳĬ������5λ ZX1700001
					int goallength = 6;
					// ��ȡ���ݿ�order��������+1+ѭ������(addcount)
					//记录有多少符合格式的ID
					int count = this.currentdbidscount.intValue() + 1 + addcount;
					StringBuilder sBuilder = new StringBuilder("");
					// ������5λ���Ĳ�ֵ
					int length = goallength - new String(count + "").length();
					for (int i = 0; i < length; i++) {
						sBuilder.append("0");
					}
					sBuilder.append(count + "");
					return sBuilder.toString();
				}

				private String getIdPre() {
					// idpre==null?this.idpre="ZX":this.idpre=idpre;
					this.idpre = "ZX_P_";
					return this.idpre;
				}

				

				public void clear() {
					this.ids = null;
				}

				@Override
				public String toString() {
					
					return "IdGenerator [ids=" + ids + "]";
				}
			}
			
}
