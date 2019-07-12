package com.hongyang.model;

import java.util.Date;


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
public class MesPlan {
    private Integer id;

    private String planOrderid;

    private String planProductname;

    private String planClientname;

    private String planContractid;

    private String planImgid;

    private String planMaterialname;

    private String planMaterialdesc;

    private String planCurrentstatus;

    private String planCurrentremark;

    private String planSalestatus;

    private String planMaterialsource;

    private Integer planHurrystatus;

    private Integer planStatus;

    private Date planCometime;

    private Date planCommittime;

    private Date planWorkstarttime;

    private Date planWorkendtime;

    private Integer planInventorystatus;

    private String planRemark;

    private String planOperator;

    private Date planOperateTime;

    private String planOperateIp;

   
}