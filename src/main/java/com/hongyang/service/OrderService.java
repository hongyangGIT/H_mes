package com.hongyang.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hongyang.dao.MesOrderCustomerMapper;
import com.hongyang.dao.MesOrderMapper;
import com.hongyang.exception.SysMineException;
import com.hongyang.model.MesOrder;
import com.hongyang.param.MesOrderVo;
import com.hongyang.util.BeanValidator;
import com.hongyang.util.MyStringUtils;

@Service
public class OrderService {
	@Resource
	private MesOrderMapper merOrderMapper;
	@Resource
	private MesOrderCustomerMapper mesOrderCustomerMapper;
	//自定义一个id生成器
	private IdGenerator ig=new IdGenerator();

	public void insertOrderBatch(MesOrderVo orderVo) {
		System.out.println(orderVo);
		//数据校验
		BeanValidator.check(orderVo);
		//判断是否批量添加
		Integer counts=orderVo.getCount();
		System.out.println("COUNT:"+counts);
		//根据生成的个数，生成ids到集合
		List<String > ids=createOrderIdsDefault(Long.valueOf(counts));
		
		for(String orderid:ids) {
			try {
				//将VO转换成PO
				MesOrder mesOrder = MesOrder.builder()
						.orderId(orderid)
						.orderClientname(orderVo.getOrderClientname())//
						.orderProductname(orderVo.getOrderProductname())
						.orderContractid(orderVo.getOrderContractid())//
						.orderImgid(orderVo.getOrderImgid())
						.orderMaterialname(orderVo.getOrderMaterialname())
						.orderCometime(MyStringUtils.string2Date(orderVo.getComeTime(), null))//
						.orderCommittime(MyStringUtils.string2Date(orderVo.getCommitTime(), null))
						.orderInventorystatus(orderVo.getOrderInventorystatus())
						.orderStatus(orderVo.getOrderStatus())//
						.orderMaterialsource(orderVo.getOrderMaterialsource())
						.orderHurrystatus(orderVo.getOrderHurrystatus())
						.orderStatus(orderVo.getOrderStatus())
						.orderRemark(orderVo.getOrderRemark()).build();
				
				merOrderMapper.insertSelective(mesOrder);
			} catch (Exception e) {
				throw new SysMineException("创建过程发生问题");
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//获取id集合
	// 获取id集合
	public List<String> createOrderIdsDefault(Long ocounts) {
		if (ig == null) {
			ig = new IdGenerator();
		}

		ig.setCurrentdbidscount(getOrderCount());
		List<String> list = ig.initIds(ocounts);
		ig.clear();
		return list;
	}
	
	//获取数据库中的数量
	private Long getOrderCount() {
		return mesOrderCustomerMapper.getOrderCount();
	}
		// 1 默认生成代码
		// 2 手工生成代码
		// id生成器
		class IdGenerator {
			// 数量起始位置
			private Long currentdbidscount;
			private List<String> ids = new ArrayList<String>();
			private String idpre;
			private String yearstr;
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

			public String getYearstr() {
				return yearstr;
			}

			public void setYearstr(String yearstr) {
				this.yearstr = yearstr;
			}

			public String getIdafter() {
				return idafter;
			}

			public void setIdafter(String idafter) {
				this.idafter = idafter;
			}

			public List<String> initIds(Long ocounts) {
				for (int i = 0; i < ocounts; i++) {
					this.ids.add(getIdPre() + yearStr() + getIdAfter(i));
				}
				return this.ids;
			}

			//
			private String getIdAfter(int addcount) {
				// 系统默认生成5位 ZX1700001
				int goallength = 5;
				// 获取数据库order的总数量+1+循环次数(addcount)
				int count = this.currentdbidscount.intValue() + 1 + addcount;
				StringBuilder sBuilder = new StringBuilder("");
				// 计算与5位数的差值
				int length = goallength - new String(count + "").length();
				for (int i = 0; i < length; i++) {
					sBuilder.append("0");
				}
				sBuilder.append(count + "");
				return sBuilder.toString();
			}

			private String getIdPre() {
				// idpre==null?this.idpre="ZX":this.idpre=idpre;
				this.idpre = "ZX";
				return this.idpre;
			}

			private String yearStr() {
				Date currentdate = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String yearstr = sdf.format(currentdate).substring(2, 4);
				return yearstr;
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
