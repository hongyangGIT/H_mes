package com.hongyang.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hongyang.beans.PageQuery;
import com.hongyang.dto.SearchProductDto;
import com.hongyang.model.MesOrder;
import com.hongyang.model.MesProduct;

public interface MesProductCustomerMapper {
	
	Long getProductCount();

	List<String> getPlanProducts();

	int countBySearchDto(@Param("dto") SearchProductDto dto);

	List<MesProduct> getPageListBySearchDto(@Param("dto") SearchProductDto dto,@Param("page") PageQuery page);
}