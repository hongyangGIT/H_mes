package com.hongyang.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchProductDto {

    private String keyword;

    private Integer search_status=0;
    private String search_materialsource;
}
