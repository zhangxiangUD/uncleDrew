package com.dakun.jianzhong.model;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "account_system_message")
public class AccountSystemMessage {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_EXPERT = 2;
    public static final int TYPE_DEALER = 3;
    public static final int TYPE_ASSIGNMENT = 4;

    public static final int STATE_DELETED = -10;
    public static final int STATE_FAIL = -1;
    public static final int STATE_SAVED = 1;
    public static final int STATE_TO_PUSH = 2;
    public static final int STATE_PUSHED = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "发送人id")
    @Column(name = "sender_id")
    private Integer senderId;

    @ApiModelProperty(value = "发送人")
    @Transient
    private String senderName;

    @NotNull(message = "接收人类型不能为空")
    @Range(min = 0, max = 5, message = "接收人类型只能为1-5")
    @ApiModelProperty(value = "接收人类型，0=全体用户，1=普通用户，2=专家，3=经销商，4=指定用户")
    @Column(name = "receiver_type")
    private Integer receiverType;

    @NotNull(message = "接收人不能为空")
    @ApiModelProperty(value = "接收人id")
    @Column(name = "receiver_id")
    private String receiverId;

    @ApiModelProperty(value = "接收人名称")
    @Column(name = "receiver_name")
    private String receiverName;

    @ApiModelProperty(value = "标题")
    private String title;

    @NotBlank(message = "消息内容不能为空")
    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "发送时间")
    @Column(name = "push_time")
    private Date pushTime;

    @ApiModelProperty(value = "状态，-10=删除，1=已保存，2=待发布，3=已发布", allowableValues = "-10,1,2,3")
    private Integer state;

    @ApiModelProperty(value = "待发布")
    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取发送人
     *
     * @return sender_id - 发送人
     */
    public Integer getSenderId() {
        return senderId;
    }

    /**
     * 设置发送人
     *
     * @param senderId 发送人
     */
    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    /**
     * 获取接收人
     *
     * @return receiver_id - 接收人
     */
    public String getReceiverId() {
        return receiverId;
    }

    /**
     * 设置接收人
     *
     * @param receiverId 接收人
     */
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取消息内容
     *
     * @return content - 消息内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置消息内容
     *
     * @param content 消息内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取发送时间
     *
     * @return push_time - 发送时间
     */
    public Date getPushTime() {
        return pushTime;
    }

    /**
     * 设置发送时间
     *
     * @param pushTime 发送时间
     */
    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    /**
     * 获取-10=删除，1=已保存，2=待发布，3=已发布
     *
     * @return state - -10=删除，1=已保存，2=待发布，3=已发布
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置-10=删除，1=已保存，2=待发布，3=已发布
     *
     * @param state -10=删除，1=已保存，2=待发布，3=已发布
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Integer getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(Integer receiverType) {
        this.receiverType = receiverType;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}