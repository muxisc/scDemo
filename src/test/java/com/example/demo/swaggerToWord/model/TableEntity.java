package com.example.demo.swaggerToWord.model;

import java.util.List;

/**
 * 代表一个method
 */
public class TableEntity {

    /**
     * 大标题
     */
    private String title;

    /**
     * 大标题中文描述
     */
    private String titleDesc;

    /**
     * 小序号
     */
    private Integer tableIndex;

    /**
     * 小标题
     */
    private String tag;

    /**
     * url
     */
    private String url;

    /**
     * 响应参数格式
     */
    private String responseForm;

    /**
     * 请求方式
     */
    private String requestType;

    /**
     * 请求体
     */
    private List<RequestEntity> requestList;

    /**
     * 返回体
     */
    private List<ResponseEntity> responseList;

    /**
     * 示例：请求参数
     */
    private String requestParam;

    /**
     * 示例：返回值
     */
    private String responseParam;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResponseForm() {
        return responseForm;
    }

    public void setResponseForm(String responseForm) {
        this.responseForm = responseForm;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public List<RequestEntity> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<RequestEntity> requestList) {
        this.requestList = requestList;
    }

    public List<ResponseEntity> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<ResponseEntity> responseList) {
        this.responseList = responseList;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getResponseParam() {
        return responseParam;
    }

    public void setResponseParam(String responseParam) {
        this.responseParam = responseParam;
    }

    public String getTitleDesc() {
        return titleDesc;
    }

    public void setTitleDesc(String titleDesc) {
        this.titleDesc = titleDesc;
    }

    public Integer getTableIndex() {
        return tableIndex;
    }

    public void setTableIndex(Integer tableIndex) {
        this.tableIndex = tableIndex;
    }
}
