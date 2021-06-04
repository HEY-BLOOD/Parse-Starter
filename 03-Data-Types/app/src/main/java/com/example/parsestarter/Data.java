package com.example.parsestarter;

public class Data {
    private String type;
    private String value;

    public Data(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public Data setType(String type) {
        this.type = type;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Data setValue(String value) {
        this.value = value;
        return this;
    }
}
