package com.hongyang.dto;


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
    private String bindPage_status;
    private String bindIcon_status;
    private String isbindIcon_status;
    private String productId_F;
}
