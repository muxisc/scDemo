package com.example.demo;

import java.util.List;

/**
 * 代表一个controller
 */
public class Div {

    /**
     * 一个Controller
     */
    private String title;

    /**
     * 一个Controller下的所有方法
     */
    private List<Table> tables;

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

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public Integer getDivIndex() {
        return divIndex;
    }

    public void setDivIndex(Integer divIndex) {
        this.divIndex = divIndex;
    }

}
