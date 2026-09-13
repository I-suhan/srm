package com.ne.srm.process.enums;

public enum OrderStatus {

    InProgress("进行中"),

    Close("关闭"),

    ReInquiry("重新询价"),

    Completed("已完成");

    private final String desc;

    OrderStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
