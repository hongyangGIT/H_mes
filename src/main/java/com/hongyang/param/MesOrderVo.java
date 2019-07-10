package com.hongyang.param;

import java.util.Date;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class MesOrderVo{
	//接收批量生成的order个数
	@Min(1)
	private Integer count=1;//这个数字就算是没有值，默认是1
	
    private Integer id;

    private String orderId;
    @NotBlank(message="客户名称不可以为空")
    private String orderClientname;
    @NotBlank(message="零件名称不可以为空")
    private String orderProductname;
    @NotBlank(message="合同编号不可以为空")
    private String orderContractid;

    private String orderImgid;

    private String orderMaterialname;

    private Date orderCometime;

    private Date orderCommittime;

    private Integer orderInventorystatus;

    private String orderSalestatus;

    private String orderMaterialsource;

    private Integer orderHurrystatus;

    private Integer orderStatus;

    private String orderRemark;
    
    @NotBlank(message="来料日期不可以为空")
    private String comeTime;
    
    @NotBlank(message="提交日期不可以为空")
    private String commitTime;
    
}
