package com.example.demo.controller;


import com.example.demo.service.ApiDocService;
import com.example.demo.model.Div;
import com.example.demo.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.*;


@Controller
public class WordController {

    @Autowired
    private ApiDocService tableService;


    @Deprecated
    @GetMapping("/getWord")
    public String getWord(Model model) {
        List<Div> divs = tableService.tableList();
        model.addAttribute("divs", divs);

        return "word";
    }

    @PostMapping("/htmlToWord")
    public void htmlToWord(String html) {

        tableService.htmlToWord(html);
    }



    public static void main(String[] args) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Div> divs = Util.tableList();
        resultMap.put("divs", divs);

        //生成HTML
        Util.createDayReportFiles(resultMap,"E:/test/1.html","E:/test/","template.ftl");

        //HTML转Doc
        String concent = Util.getHtmlContent("E:/test/1.html");
        Util.htmlToWord(concent);

    }




}
