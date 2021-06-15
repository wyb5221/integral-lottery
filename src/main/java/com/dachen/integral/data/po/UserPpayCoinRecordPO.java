package com.dachen.integral.data.po;

import com.dachen.integral.data.base.BasePO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;

/**
 * 学币充值记录
 * @Author: wangyongbin
 * @Date: 2021/5/8 18:49
 * @Description:
 */
@Data
@Entity(value = "user_pay_coin_record", noClassnameStored = true)
public class UserPpayCoinRecordPO extends BasePO {

    @ApiModelProperty(dataType="String",name="businessCode",notes="业务码",required=true)
    private String businessCode;

    @ApiModelProperty(dataType="String",name="creator",notes="创建人ID",required=true)
    private String creator;

    @ApiModelProperty(dataType="String",name="fromReason",notes="转出原因",required=true)
    private String fromReason;

    @ApiModelProperty(dataType="String",name="remark",notes="备注",required=true)
    private String remark;

    @ApiModelProperty(dataType="String",name="sourceId", notes="源账户ID 平台 10000, 个人 userId, 企业 companyId, 圈子/科室 deptId, 虚拟账户 虚拟账户ID, 业务收入账户 业务收入账户ID",required=true)
    private String sourceId;

    @ApiModelProperty(dataType="Integer",name="sourceType",notes="源账户类型 0 平台, 1 个人, 2 企业, 3 圈子/科室, 4 虚拟账户,5 业务收入账户",required=true)
    private int sourceType;

    @ApiModelProperty(dataType="String",name="targetId",notes="目标账户ID 平台 10000, 个人 userId, 企业 companyId, 圈子/科室 deptId, 虚拟账户 虚拟账户ID, 业务收入账户 业务收入账户ID",required=true)
    private String targetId;

    @ApiModelProperty(dataType="Integer",name="targetType",notes="目标账户类型 0 平台, 1 个人, 2 企业, 3 圈子/科室, 4 虚拟账户, 5 业务收入账户",required=true)
    private int targetType;

    @ApiModelProperty(dataType="String",name="toReason",notes="转入原因",required=true)
    private String toReason;

    @ApiModelProperty(dataType="Long",name="value",notes="转账学币数 正整数",required=true)
    private Long value;

}
