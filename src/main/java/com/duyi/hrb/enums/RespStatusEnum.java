package com.duyi.hrb.enums;

public enum RespStatusEnum {

    SUCCESS("Success"), FAIL("Fail");

    private String value;

    RespStatusEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
