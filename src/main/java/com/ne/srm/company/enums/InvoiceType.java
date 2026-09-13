package com.ne.srm.company.enums;

public enum InvoiceType {

    Special("增值税专用发票"),
    General("普通发票");

    private final String name;

    InvoiceType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
