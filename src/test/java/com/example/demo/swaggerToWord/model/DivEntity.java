package com.example.demo.swaggerToWord.model;

import java.util.List;

/**
 * 代表一个controller
 */
public class DivEntity {

    /**
     * 一个Controller
     */
    private String title;

    /**
     * 一个Controller下的所有方法
     */
    private List<TableEntity> tables;

    /**
     * 大序号
     */
    private Integer divIndex;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TableEntity> getTables() {
        return tables;
    }

    public void setTables(List<TableEntity> tables) {
        this.tables = tables;
    }

    public Integer getDivIndex() {
        return divIndex;
    }

    public void setDivIndex(Integer divIndex) {
        this.divIndex = divIndex;
    }

}
