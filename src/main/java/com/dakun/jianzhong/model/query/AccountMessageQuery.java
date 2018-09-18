package com.dakun.jianzhong.model.query;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * <p>User: wangjie
 * <p>Date: 12/15/2017
 * @author wangjie
 */
public class AccountMessageQuery extends PageQuery {

    @ApiModelProperty(value = "系统消息id")
    @NotNull(message = "系统消息id不能为空")
    private Integer pushId;

    @ApiModelProperty(value = "接收人名称")
    private String receiverName;

    @ApiModelProperty(value = "接收人手机号")
    private String receiverMobile;

    @ApiModelProperty(value = "状态")
    private Integer state;

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPushId() {
        return pushId;
    }

    public void setPushId(Integer pushId) {
        this.pushId = pushId;
    }
}
