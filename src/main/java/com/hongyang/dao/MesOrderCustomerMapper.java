package com.hongyang.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hongyang.beans.PageQuery;
import com.hongyang.dto.SearchOrderDto;
import com.hongyang.model.MesOrder;

public interface MesOrderCustomerMapper {

	Long getOrderCount();

	int countBySearchDto(@Param("dto") SearchOrderDto dto);

	List<MesOrder> getPageListBySearchDto(@Param("dto") SearchOrderDto dto, @Param("page") PageQuery page);

	void batchStart(@Param("list") String[] idArray);
	
	
}