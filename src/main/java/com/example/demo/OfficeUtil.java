package com.example.demo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;

/**
 * 适用于word 2007
 */
public class OfficeUtil {

    /**
     * 根据指定的参数值、模板，生成 word 文档
     * @param template 模板
     */
    public static XWPFDocument generateWord(String template) {
        XWPFDocument doc = null;
        try {
            OPCPackage pack = POIXMLDocument.openPackage(template);
            doc = new XWPFDocument(pack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }


}

