package com.hongyang.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hongyang.beans.PageQuery;
import com.hongyang.dto.SearchPlanDto;
import com.hongyang.model.MesOrder;

public interface MesPlanCustomerMapper {

	int countBySearchDto(@Param("dto") SearchPlanDto dto);

	List<MesOrder> getPageListBySearchDto(@Param("dto") SearchPlanDto dto,@Param("page") PageQuery page);
    
}