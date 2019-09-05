package com.example.demo;

public class Request {

    /**
     * 请求参数描述
     */
    private String description;

    /**
     * 参数名
     */
    private String name;

    /**
     * 数据类型
     */
    private String type;

    /**
     * 参数类型
     */
    private String paramType;

    /**
     * 是否必填
     */
    private String require;



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequire() {
        return require;
    }

    public void setRequire(String require) {
        this.require = require;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }
}

