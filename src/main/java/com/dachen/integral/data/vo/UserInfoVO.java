package com.dachen.integral.data.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserInfoVO {

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("用户类型")
    private Integer userType;

    @ApiModelProperty("昵称")
    private String name;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(value = "name的拼音格式", hidden = true)
    private String namePinyin;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(value = "name的首字母", hidden = true)
    private String nameInitial;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("性别:1=男，2=女，0=保密")
    private Integer sex;

    @ApiModelProperty("出生日期")
    private Long birthday;

    @ApiModelProperty("状态。0未激活, 1正常, 2待审核, 3审核未通过")
    private Integer status;

    @ApiModelProperty("手机号")
    private String telephone;

    @ApiModelProperty("邮箱地址")
    private String emailAddress;

    @ApiModelProperty("头像")
    private String headPicFileName;

    private Long createTime;

    // =============== 以下来源于 user.doctor ========================

    @ApiModelProperty("医生号, 如 94790084. from user.doctor")
    private String doctorNum;

    @ApiModelProperty("医院ID, 如 201706300017. from user.doctor")
    private String hospitalId;

    @ApiModelProperty("所属医院名称, 如 康哲安徽医院. from user.doctor.hospital")
    private String hospitalName;

    @ApiModelProperty("科室, 如 骨/关节外科. from user.doctor")
    private String departments;

    @ApiModelProperty("科室Id, 如 WKAR. from user.doctor")
    private String deptId;

    @ApiModelProperty("职称, 如 主治医师. from user.doctor")
    private String title;

    @ApiModelProperty("职称等级, 如 3. from user.doctor")
    private String titleRank;

    @ApiModelProperty("简介. from user.doctor")
    private String introduction;

    @ApiModelProperty("手工输入的擅长. from user.doctor")
    private String skill;

    @ApiModelProperty("个人擅长. from user.doctor")
    private List<String> expertise;

}
