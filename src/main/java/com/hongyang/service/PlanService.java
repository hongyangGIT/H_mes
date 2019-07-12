package com.hongyang.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.hongyang.beans.PageQuery;
import com.hongyang.beans.PageResult;
import com.hongyang.dao.MesOrderMapper;
import com.hongyang.dao.MesPlanCustomerMapper;
import com.hongyang.dao.MesPlanMapper;
import com.hongyang.dto.SearchPlanDto;
import com.hongyang.exception.ParamException;
import com.hongyang.model.MesOrder;
import com.hongyang.model.MesPlan;
import com.hongyang.param.MesPlanVo;
import com.hongyang.param.SearchPlanParam;
import com.hongyang.util.BeanValidator;
import com.hongyang.util.MyStringUtils;

@Service
public class PlanService {
	@Resource
	private MesPlanMapper mesPlanMapper;
	@Resource
	private MesPlanCustomerMapper mesPlanCustomerMapper;
	@Resource
	private MesOrderMapper mesOrderMapper;
	@Resource
	private SqlSession sqlSession;
	public PageResult<MesOrder> searchPageList(SearchPlanParam param, PageQuery page) {
		///校验
				BeanValidator.check(page);
//				// searchDto 用于分页的where语句后面
				SearchPlanDto dto = new SearchPlanDto();
				if (StringUtils.isNotBlank(param.getKeyword())) {
					dto.setKeyword("%" + param.getKeyword() + "%");
				}
				if (StringUtils.isNotBlank(param.getSearch_status())) {
					dto.setSearch_status(Integer.parseInt(param.getSearch_status()));
				}
				if(StringUtils.isNotBlank(param.getSearch_msource())) {
					dto.setSearch_msource(param.getSearch_msource());
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
				int count = mesPlanCustomerMapper.countBySearchDto(dto);
				if (count > 0) {
					List<MesOrder> orderList = mesPlanCustomerMapper.getPageListBySearchDto(dto, page);
					return PageResult.<MesOrder>builder().total(count).data(orderList).build();
				}
		return PageResult.<MesOrder>builder().total(count).build();
	}
	//创建未启动计划
	public void prePlan(MesOrder mesOrder) {
		// 批量处理
				MesPlanMapper planMapper = sqlSession.getMapper(MesPlanMapper.class);
				MesPlan mesPlan =MesPlan.builder().planOrderid(mesOrder.getOrderId()).planProductname(mesOrder.getOrderProductname())//
						.planClientname(mesOrder.getOrderClientname()).planContractid(mesOrder.getOrderContractid()).planImgid(mesOrder.getOrderImgid())//
						.planMaterialname(mesOrder.getOrderMaterialname()).planCurrentstatus("计划").planCurrentremark("计划待执行").planSalestatus(mesOrder.getOrderSalestatus())//
						.planMaterialsource(mesOrder.getOrderMaterialsource()).planHurrystatus(mesOrder.getOrderHurrystatus()).planStatus(0).planCometime(mesOrder.getOrderCometime())//
						.planCommittime(mesOrder.getOrderCommittime()).planInventorystatus(mesOrder.getOrderInventorystatus()).build();
				mesPlan.setPlanOperator("hongyang");
				mesPlan.setPlanOperateIp("127.0.0.1");
				mesPlan.setPlanOperateTime(new Date());
				planMapper.insertSelective(mesPlan);
		
	}
	//根据已启动订单生成未启动计划
	public void startPlansByOrderIds(String[] idArray) {
		for(String tempId:idArray) {
			Integer id=Integer.parseInt(tempId);
			MesOrder order=mesOrderMapper.selectByPrimaryKey(id);
			//查询内容非空 ，使用google的guava工具类
			Preconditions.checkNotNull(order);
			prePlan(order);
		}
		
	}
	public void planBatchStartWithIds(String ids) {
		if(ids!=null&&ids.length()>0) {
			//批量处理
			MesPlanMapper mapper=sqlSession.getMapper(MesPlanMapper.class);
			String[] strs=ids.split(",");
			String[] idsTemp=strs[0].split("&");
			String[] times=strs[1].split("&");
			String startTime=times[0];
			String endTime=times[1];
//			for(int i=0;i<idsTemp.length;i++) {
//				
//				System.out.println("idsTemp:-----------"+idsTemp[i]);
//			}
//			System.out.println("times:-----------"+times);
			for(int i=0;i<idsTemp.length;i++) {
				MesPlan mesPlan=new MesPlan();
				mesPlan.setId(Integer.parseInt(idsTemp[i]));
				mesPlan.setPlanWorkstarttime(MyStringUtils.string2Date(startTime,null));
				mesPlan.setPlanWorkendtime(MyStringUtils.string2Date(endTime,null));
				mesPlan.setPlanStatus(1);
				mesPlan.setPlanCurrentremark("计划已启动");
				mapper.updateByPrimaryKeySelective(mesPlan);
			//TODO	
			
			
			
			}
			
		}
	}
	public void update(MesPlanVo mesPlanVo) {
		//校验
		BeanValidator.check(mesPlanVo);
		MesPlan mesPlan=new MesPlan();
		BeanUtils.copyProperties(mesPlanVo, mesPlan);
		mesPlan.setPlanCometime(MyStringUtils.string2Date(mesPlanVo.getPlanCometime(),null));
		mesPlan.setPlanCommittime(MyStringUtils.string2Date(mesPlanVo.getPlanCommittime(),null));
		mesPlan.setPlanWorkstarttime(MyStringUtils.string2Date(mesPlanVo.getPlanWorkstarttime(),null));
		mesPlan.setPlanWorkendtime(MyStringUtils.string2Date(mesPlanVo.getPlanWorkendtime(),null));
		mesPlan.setPlanOperator("hongyang");
		mesPlan.setPlanOperateIp("127.0.0.1");
		mesPlan.setPlanOperateTime(new Date());
		mesPlanMapper.updateByPrimaryKeySelective(mesPlan);
	}
}
