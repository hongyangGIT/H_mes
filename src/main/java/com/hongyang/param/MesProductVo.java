package com.hongyang.param;

import java.util.Date;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

import com.hongyang.model.MesOrder.MesOrderBuilder;

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
public class MesProductVo {
	@Min(1)
	private Integer count=1;//这个数字就算是没有值，默认是1
	
    private Integer id;

    private Integer pId;

    private String productId;

    private Integer productOrderid;

    private Integer productPlanid;
    @NotBlank(message="工艺重量不可以为空")
    private String productTargetweight;//工艺重量
//    @NotBlank(message="投料重量不可以为空")
    private String productRealweight;//投料重量
//    @NotBlank(message="剩余重量不可以为空")
    private String productLeftweight;//剩余重量

    private String productBakweight;
    @NotBlank(message="锭型类别不可以为空")
    private String productIrontype;//锭型类别
    @NotBlank(message="锭型不可以为空")
    private String productIrontypeweight;//锭型
    @NotBlank(message="材料名称不可以为空")
    private String productMaterialname;//材料名称
    @NotBlank(message="图号不可以为空")
    private String productImgid;//图号
    @NotBlank(message="炉号不可以为空")
    private String productHeatid;//炉号
    
    private String productMaterialsource;//材料来源
    
    private String productStatus;//是否启用--状态
   
    private String productRemark;//备注

    private String productOperator;

    private Date productOperateTime;

    private String productOperateIp;
   
}