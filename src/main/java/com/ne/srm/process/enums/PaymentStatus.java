package com.ne.srm.process.enums;

/****
 * 付款状态（采购）
 * */
public enum PaymentStatus {

    ApplyAdvancePayment("申请预付款"),

    AdvancePaid("已付预付款"),

    ApplyBalancePayment("申请尾款"),

    ApplyFullPayment("申请全款"),

    PaidOff("已结清");

    private final String desc;

    PaymentStatus(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return desc;
    }
}