package com.dachen.integral.data.po;

import com.dachen.integral.data.base.BasePO;
import com.dachen.integral.data.vo.LotteryDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;

import java.util.List;

/**
 * 用户抽奖记录
 * @Author: wangyongbin
 * @Date: 2021/4/23 16:08
 * @Description:
 */
@Data
@Entity(value = "user_lottery_record", noClassnameStored = true)
public class UserLotteryRecordPO extends BasePO {

    @ApiModelProperty(value = "用户id")
    private Integer userId;
    @ApiModelProperty(value = "用户姓名")
    private String name;
    @ApiModelProperty(value = "用户手机")
    private String telephone;
    @ApiModelProperty("所属医院名称, 如 康哲安徽医院")
    private String hospitalName;
    @ApiModelProperty("科室, 如 骨/关节外科")
    private String departments;
    @ApiModelProperty("职称, 如 主治医师")
    private String title;

    @ApiModelProperty(value = "抽奖类型 1:积分抽奖")
    private Integer type;
    @ApiModelProperty(value = "抽奖方式 1:单次抽奖 2:十连抽")
    private Integer mode;
    @ApiModelProperty(value = "任务id")
    private String taskId;
    @ApiModelProperty(value = "任务名称")
    private String taskName;
    @ApiModelProperty(value = "任务积分")
    private Integer integral;
    @ApiModelProperty(value = "抽奖结果 1:已中奖 0:未中奖")
    private Integer status;
    @ApiModelProperty(value = "抽奖结果明细")
    private List<LotteryDetail> lotteryDetails;
    @ApiModelProperty(value = "抽奖结果总计")
    private List<AwardTotalInfo> awardTotal;

}
