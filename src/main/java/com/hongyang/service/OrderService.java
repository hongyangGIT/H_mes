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
	//�Զ���һ��id������
	private IdGenerator ig=new IdGenerator();

	public void insertOrderBatch(MesOrderVo orderVo) {
		System.out.println(orderVo);
		//����У��
		BeanValidator.check(orderVo);
		//�ж��Ƿ��������
		Integer counts=orderVo.getCount();
		System.out.println("COUNT:"+counts);
		//�������ɵĸ���������ids������
		List<String > ids=createOrderIdsDefault(Long.valueOf(counts));
		
		for(String orderid:ids) {
			try {
				//��VOת����PO
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
				throw new SysMineException("�������̷�������");
			}
		}
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
}
