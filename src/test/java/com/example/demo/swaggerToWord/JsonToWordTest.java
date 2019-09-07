package com.example.demo.swaggerToWord;


import com.example.demo.swaggerToWord.model.DivEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
public class JsonToWordTest {


    @Test
    public void testMain() {
        String filePath = "F:/swaggerApi/";
        Map<String, Object> resultMap = new HashMap<>();

        //获取api-docs.json数据
        Map map = JsonToWordUtil.getApiJson(filePath);
        //获取规范的json数据
        List<DivEntity> divEntities = JsonToWordUtil.tableList(map);
        resultMap.put("divs", divEntities);

        //生成HTML或doc
        //String fileName = filePath + "apiWord.html";
        String fileName = filePath + "apiWord.doc";
        JsonToWordUtil.createDayReportFiles(resultMap, fileName, filePath,"template.ftl");

        //HTML转Doc
        //String content = JsonToWordUtil.getHtmlContent(fileName);
        //String wordFileName = filePath + "apiWord.doc";
        //JsonToWordUtil.htmlToWord(wordFileName, content);

    }




}
