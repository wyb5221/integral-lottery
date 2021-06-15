package com.dachen.integral.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: wangyongbin
 * @Date: 2020/7/9 13:49
 * @Description:
 */
@Data
public class UserInfo {

    /**用户id*/
    private Integer userId;
    /**用户id*/
    private Integer id;
    /**用户名称*/
    private String name;
    /**用户类型*/
    private Integer userType;
    /**电话*/
    private String telephone;
    /**医院/企业*/
    private String host;
    /**部门/科室*/
    private String deptName;
    /**职称*/
    private String title;
    /**来源*/
    private String source;
    /**企业id*/
    private String orgId;
    /**企业名称*/
    private String orgName;
    @ApiModelProperty("省")
    private String province;
    @ApiModelProperty("省code")
    private String provinceCode;
    @ApiModelProperty("市")
    private String town;
    @ApiModelProperty("市code")
    private String townCode;
    @ApiModelProperty("区")
    private String area;
    @ApiModelProperty("区code")
    private String areaCode;
    @ApiModelProperty("是否备案")
    private boolean isExamine;
}
