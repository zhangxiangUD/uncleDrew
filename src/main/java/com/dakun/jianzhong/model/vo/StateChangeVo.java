package com.dakun.jianzhong.model.vo;

import javax.validation.constraints.NotNull;

/**
 * @author wangjie
 * @date 1/10/2018
 */
public class StateChangeVo {

    @NotNull(message = "id不能为空")
    private Integer id;

    @NotNull(message = "旧状态不能为空")
    private Integer oldState;

    @NotNull(message = "新状态不能为空")
    private Integer newState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOldState() {
        return oldState;
    }

    public void setOldState(Integer oldState) {
        this.oldState = oldState;
    }

    public Integer getNewState() {
        return newState;
    }

    public void setNewState(Integer newState) {
        this.newState = newState;
    }
}
