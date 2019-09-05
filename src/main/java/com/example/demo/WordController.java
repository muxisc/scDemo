package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

        System.out.println("---->>>>-----------------哈哈哈哈");
    }



    public static void main(String[] args) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Div> divs = Util.tableList();
        resultMap.put("divs", divs);

        //生成HTML
        Util.createDayReportFiles(resultMap,"E:/test/111.html","E:/test/","template.ftl");

        //HTML转Doc
        String concent = Util.getHtmlContent("E:/test/111.html");
        Util.htmlToWord(concent);

    }




}
