package com.ne.srm.process.enums;

/****
 * 付款类型（财务）
 * */
public enum PaymentType {

    Advance("预付款"),   //预付款

    Balance("尾款"),   //尾款

    Full("全款");       //全款

    private final String value;

    PaymentType(String value) {this.value = value;}

    @Override
    public String toString() {
        return value;
    }
}
