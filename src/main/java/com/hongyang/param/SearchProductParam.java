package com.hongyang.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SearchProductParam {

    private String keyword;

    private String search_materialsource;
    private String search_status;
    private String bindPage_status;
    private String bindIcon_status;
    private String isbindIcon_status;
    //父id
    private String productId_F;
}
