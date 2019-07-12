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
import com.hongyang.dao.MesOrderCustomerMapper;
import com.hongyang.dao.MesOrderMapper;
import com.hongyang.dto.SearchOrderDto;
import com.hongyang.exception.ParamException;
import com.hongyang.exception.SysMineException;
import com.hongyang.model.MesOrder;
import com.hongyang.param.MesOrderVo;
import com.hongyang.param.SearchOrderParam;
import com.hongyang.util.BeanValidator;
import com.hongyang.util.MyStringUtils;

@Service
public class OrderService {
	@Resource
	private MesOrderMapper mesOrderMapper;
	@Resource
	private MesOrderCustomerMapper mesOrderCustomerMapper;
	
	@Resource
	private PlanService planService;
	//自定义一个ID生成器
	private IdGenerator ig=new IdGenerator();

	//添加
	public void insertOrderBatch(MesOrderVo orderVo) {
		//校验
		BeanValidator.check(orderVo);
		//判断是否批量添加
		Integer counts=orderVo.getCount();
		System.out.println("COUNT:"+counts);
		//id集合
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
				mesOrder.setOrderOperator("hongyang");
				mesOrder.setOrderOperateIp("127.0.0.1");
				mesOrder.setOrderOperateTime(new Date());
				// 批量添加未启动订单
				if (mesOrder.getOrderStatus() ==1) {
					planService.prePlan(mesOrder);
				}
				mesOrderMapper.insertSelective(mesOrder);
			} catch (Exception e) {
				throw new SysMineException("创建过程出现问题");
			}
		}
	}
	//修改
	public void updateOrder(MesOrderVo orderVo) {
		
		BeanValidator.check(orderVo);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//判断该id有没有
		MesOrder before = mesOrderMapper.selectByPrimaryKey(orderVo.getId());
		//没有就出异常返回界面
		Preconditions.checkNotNull(before, "待更新的材料不存在");
		try {
			//将VO转换成PO
			MesOrder mesOrder = MesOrder.builder()
					.id(orderVo.getId())
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
			
			mesOrderMapper.updateByPrimaryKeySelective(mesOrder);
		} catch (Exception e) {
			throw new SysMineException("更改过程出现问题");
		}
		
	}
	
	//分页查询刷新
	public PageResult<MesOrder> searchPageList(SearchOrderParam param, PageQuery page) {
		///校验
		BeanValidator.check(page);
//		// searchDto 用于分页的where语句后面
		SearchOrderDto dto = new SearchOrderDto();
		if (StringUtils.isNotBlank(param.getKeyword())) {
			dto.setKeyword("%" + param.getKeyword() + "%");
		}
		if (StringUtils.isNotBlank(param.getSearch_status())) {
			dto.setSearch_status(Integer.parseInt(param.getSearch_status()));
		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if (StringUtils.isNotBlank(param.getFromTime())) {
				dto.setFromTime(dateFormat.parse(param.getFromTime()));
			}
			if (StringUtils.isNotBlank(param.getToTime())) {
				dto.setToTime(dateFormat.parse(param.getToTime()));
			}
		} catch (Exception e) {
			throw new ParamException("传入的日期格式有问题，正确格式为：yyyy-MM-dd");
		}
		int count = mesOrderCustomerMapper.countBySearchDto(dto);
		if (count > 0) {
			List<MesOrder> orderList = mesOrderCustomerMapper.getPageListBySearchDto(dto, page);
			return PageResult.<MesOrder>builder().total(count).data(orderList).build();
		}

		return PageResult.<MesOrder>builder().build();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//��ȡid����
	// ��ȡid����
	public List<String> createOrderIdsDefault(Long ocounts) {
		if (ig == null) {
			ig = new IdGenerator();
		}

		ig.setCurrentdbidscount(getOrderCount());
		List<String> list = ig.initIds(ocounts);
		ig.clear();
		return list;
	}
	
	//��ȡ���ݿ��е�����
	private Long getOrderCount() {
		return mesOrderCustomerMapper.getOrderCount();
	}
		// 1 Ĭ�����ɴ���
		// 2 �ֹ����ɴ���
		// id������
		class IdGenerator {
			// ������ʼλ��
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
				// ϵͳĬ������5λ ZX1700001
				int goallength = 5;
				// ��ȡ���ݿ�order��������+1+ѭ������(addcount)
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
		public void orderBatchStart(String ids) {
			if(ids!=null&&ids.length()>0) {
				// 批量处理的sqlSession代理
				String[] idArray = ids.split("&");
				mesOrderCustomerMapper.batchStart(idArray);
				//批量启动未启动计划
				planService.startPlansByOrderIds(idArray);
			}
		}
		
}
