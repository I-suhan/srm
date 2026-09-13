package com.ne.srm.process.enums;

public enum ContainerLoadType {

    Full("整柜"),
    Less("拼柜");

    private final String value;

    ContainerLoadType(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return this.value;
    }
}
