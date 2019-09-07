package com.example.demo.service;

import com.example.demo.model.Div;

import java.util.List;

public interface ApiDocService {

    List<Div> tableList();

    void htmlToWord(String html);

}
