package com.dachen.integral.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: wangyongbin
 * @Date: 2021/5/10 15:38
 * @Description:
 */
@Data
public class ExportInfo {

    private String createTime;
    private Integer userId;
    private String telephone;
    private String name;
    private String operateType;
    private Integer integral;
    private String bizName;
    @ApiModelProperty("所属医院名称, 如 康哲安徽医院")
    private String hospitalName;
    @ApiModelProperty("科室, 如 骨/关节外科")
    private String departments;
    @ApiModelProperty("职称, 如 主治医师")
    private String title;

}
