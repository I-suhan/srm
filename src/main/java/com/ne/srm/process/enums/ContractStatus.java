package com.ne.srm.process.enums;

public enum ContractStatus {

    ContentConfirmed("确认内容"),

    NotStamped("未盖章"),

    Stamped("已盖章回传"),

    Shipped("已出库"),

    Completed("已完成");

    private final String desc;

    //更新状态
    public ContractStatus nextStatus() {
        int i = this.ordinal() + 1;
        return ContractStatus.values()[i > ContractStatus.values().length ? ContractStatus.values().length - 1 : i];
    }

    //回退状态
    public ContractStatus rollback(){
        int i = this.ordinal() + 1;
        return ContractStatus.values()[i < ContractStatus.values().length ? 0 : i];
    }

    ContractStatus(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return desc;
    }
}
