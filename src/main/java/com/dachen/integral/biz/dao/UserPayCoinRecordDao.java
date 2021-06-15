package com.dachen.integral.biz.dao;

import com.dachen.integral.data.po.UserPpayCoinRecordPO;
import com.dachen.support.annotation.Model;
import com.dachen.support.mongo.dao.BaseDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.Key;
import org.springframework.stereotype.Service;

/**
 * 充值学币记录表
 * @Author: wangyongbin
 * @Date: 2021/5/8 18:53
 * @Description:
 */
@Service
@Slf4j
@Model(UserPpayCoinRecordPO.class)
public class UserPayCoinRecordDao extends BaseDao {

    /**
     * 新增用户学币转账记录
     * @param businessCode
     * @param creator
     * @param fromReason
     * @param remark
     * @param sourceId
     * @param sourceType
     * @param targetId
     * @param targetType
     * @param toReason
     * @param value
     * @return
     */
    public String insertUserSign(String businessCode, String creator,String fromReason,String remark,String sourceId,Integer sourceType,String targetId, Integer targetType,String toReason, Long value) {
        UserPpayCoinRecordPO payCoinRecordPO = new UserPpayCoinRecordPO();
        payCoinRecordPO.setBusinessCode(businessCode);
        payCoinRecordPO.setCreator(creator);
        payCoinRecordPO.setFromReason(fromReason);
        payCoinRecordPO.setRemark(StringUtils.isEmpty(remark) ? toReason : remark);
        payCoinRecordPO.setSourceId(sourceId);
        payCoinRecordPO.setSourceType(sourceType);
        payCoinRecordPO.setTargetId(targetId);
        payCoinRecordPO.setTargetType(targetType);
        payCoinRecordPO.setToReason(toReason);
        payCoinRecordPO.setValue(value);

        payCoinRecordPO.setCreateTime(System.currentTimeMillis());
        payCoinRecordPO.setUpdateTime(System.currentTimeMillis());
        Key<UserPpayCoinRecordPO> insert = dsForRW.insert(payCoinRecordPO);
        return insert.getId().toString();
    }

}
