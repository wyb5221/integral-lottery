package com.dachen.integral.data.base;

import lombok.Data;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;

@Data
public abstract class BasePO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    private String id;

    private Long createTime;

    private Long updateTime;

    private String createUserId;

    private String createUserName;

    private String updateUserId;

    private String updateUserName;

}
