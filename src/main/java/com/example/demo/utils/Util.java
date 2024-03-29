package com.example.demo.utils;

import com.example.demo.model.Div;
import com.example.demo.model.Request;
import com.example.demo.model.Response;
import com.example.demo.model.Table;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.util.ClassUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Util {


    public static List<Div> tableList() {
        List<Table> list = new LinkedList<>();
        try {
            URL resource = ClassUtils.getDefaultClassLoader().getResource("api-docs.json");
            Map map = new ObjectMapper().readValue(resource, Map.class);
            //得到host，用于模拟http请求
            //String host = String.valueOf(map.get("host"));
            //解析paths
            LinkedHashMap<String, LinkedHashMap> paths = (LinkedHashMap) map.get("paths");
            //解析tags
            Map<String, String> tagsMap = parseTags(map);

            Map<String, List<Table>> divMap = new HashMap<>();
            if (paths != null) {
                Iterator<Map.Entry<String, LinkedHashMap>> iterator = paths.entrySet().iterator();
                while (iterator.hasNext()) {
                    Table table = new Table();
                    List<Request> requestList = new LinkedList<>();
                    String requestType = "";
                    Map.Entry<String, LinkedHashMap> next = iterator.next();
                    //得到url
                    String url = next.getKey();
                    LinkedHashMap<String, LinkedHashMap> value = next.getValue();
                    //得到请求方式，输出结果类似为 get/post/delete/put 这样
                    Set<String> requestTypes = value.keySet();
                    for (String str : requestTypes) {
                        requestType += str + "/";
                    }
                    Iterator<Map.Entry<String, LinkedHashMap>> it2 = value.entrySet().iterator();
                    //解析请求 得到get
                    Map.Entry<String, LinkedHashMap> get = it2.next();
                    LinkedHashMap getValue = get.getValue();
                    //得到大标题 和Controller描述（在tags里）
                    String title = (String) ((List) getValue.get("tags")).get(0);
                    String tag = String.valueOf(getValue.get("summary"));
                    String titleDesc = tagsMap.get(title);

                    //请求体
                    ArrayList parameters = (ArrayList) getValue.get("parameters");
                    if (parameters != null && parameters.size() > 0) {
                        for (int i = 0; i < parameters.size(); i++) {
                            Request request = new Request();
                            LinkedHashMap<String, Object> param = (LinkedHashMap) parameters.get(i);
                            request.setDescription(String.valueOf(param.get("description")));
                            request.setName(String.valueOf(param.get("name")));
                            request.setType(String.valueOf(param.get("type")));
                            request.setParamType(String.valueOf(param.get("in")));
                            String required = "否";
                            if((Boolean)param.get("required")){
                                required = "是";
                            }
                            request.setRequire(required);
                            requestList.add(request);
                        }
                    }

                    //返回体，比较固定
                    List<Response> responseList = listResponse();

                    //接口示例  模拟一次HTTP请求，封装请求体和返回体
                    //get：查询     post：保存、编辑   delete：删除
                    if (requestType.contains("post")) {
                        //{id=0,name=string}  设置默认值，故请求查询不出来任何数据
                        Map<String, String> stringStringMap = toPostBody(requestList);
                        table.setRequestParam(stringStringMap.toString());

                        //NetUtil 网络请求工具类  post肯定 = ""
                        //String post = NetUtil.post(host + url, stringStringMap);

                        Map<String, String> postMap = new HashMap<>();
                        postMap.put("code", "0000");
                        postMap.put("msg", "新增或修改成功");
                        postMap.put("orderNo", null);

                        //返回 封装的data的实体的字段  保存或编辑：返回null或该实体
                        postMap.put("data", null);

                        table.setResponseParam(postMap.toString());

                    } else if (requestType.contains("get")) {
                        //查询  分页pageNum = 0，pageSize = 10
                        String s = toGetHeader(requestList);
                        table.setRequestParam(url + s);

                        Map<String, String> postMap = new HashMap<>(16);
                        postMap.put("code", "0000");
                        postMap.put("msg", "查询成功");
                        postMap.put("orderNo", null);

                        //返回 封装的data的实体的字段  查询：返回列表数据 ？？？
                        postMap.put("data", "{}");

                        //String getStr = NetUtil.get(host + url + s);

                        table.setResponseParam(postMap.toString());

                    }else if(requestType.contains("delete")){
                        //删除



                    }

                    //封装Table
                    listTable(list, table, title, titleDesc, url, tag, requestType, requestList, responseList);

                    //map：一个table代表一个方法，需要封装成 一个controller + 多个方法
                    List<Table> divList = new ArrayList<>();
                    String newTitle = titleDesc + "（" + title + "）";
                    if(divMap.containsKey(newTitle)) {
                        divList = divMap.get(newTitle);
                    }
                    divList.add(table);
                    divMap.put(newTitle, divList);
                }
            }

            //集合：一个table代表一个方法，需要封装成 一个controller + 多个方法
            List<Div> divs = classifyTable(divMap);

            return divs;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //归类table方法
    private static List<Div> classifyTable(Map<String, List<Table>> divMap){
        List<Div> divs = new ArrayList<>();
        Set<String> keySet = divMap.keySet();
        Integer index = 1;
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()){
            Div div = new Div();
            String str = iterator.next();
            div.setTitle(str);
            List<Table> tables = new ArrayList<>();
            tables.addAll(divMap.get(str));
            div.setTables(tables);
            div.setDivIndex(index);
            index++;

            divs.add(div);
            //设置方法的序号
            Integer tableIndex = 1;
            for(Table table : tables){
                table.setTableIndex(tableIndex);
                tableIndex++;
            }
        }

        return divs;
    }

    //解析tags
    private static Map<String, String> parseTags(Map map){
        ArrayList<LinkedHashMap<String, String>> tags = (ArrayList) map.get("tags");
        Map<String, String> tagsMap = new HashMap<>();
        tags.forEach(item->{
            String name = item.get("name");
            String description = item.get("description");
            if(!tagsMap.containsKey(name)){
                tagsMap.put(name, description);
            }
        });

        return tagsMap;
    }

    //封装Table
    private static void listTable(List<Table> list, Table table, String title, String titleDesc, String url, String tag, String requestType,
                           List<Request> requestList, List<Response> responseList){
        table.setTitle(title);
        table.setTitleDesc(titleDesc);
        table.setUrl(url);
        table.setTag(tag);
        table.setResponseForm("application/json");
        table.setRequestType(StringUtils.removeEnd(requestType, "/"));
        table.setRequestList(requestList);
        table.setResponseList(responseList);

        list.add(table);
    }

    //封装返回信息，可能需求不一样，可以自定义
    private static List<Response> listResponse() {
        List<Response> responseList = new LinkedList<Response>();
        responseList.add(new Response("结果说明信息", "msg", null));
        responseList.add(new Response("返回对象", "data", null));
        responseList.add(new Response("结果码", "code", null));
        responseList.add(new Response("唯一编号", "orderNo", null));

        return responseList;
    }

    //封装post请求体
    private static Map<String, String> toPostBody(List<Request> list) {
        Map<String, String> map = new HashMap<>(16);
        if (list != null && list.size() > 0) {
            for (Request request : list) {
                String name = request.getName();
                String type = request.getType();
                switch (type) {
                    case "string":
                        map.put(name, "string");
                        break;
                    case "integer":
                        map.put(name, "0");
                        break;
                    case "double":
                        map.put(name, "0.0");
                        break;
                    default:
                        map.put(name, "null");
                        break;
                }
            }
        }

        return map;
    }

    //封装get请求头
    private static String toGetHeader(List<Request> list) {
        StringBuffer stringBuffer = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (Request request : list) {
                String name = request.getName();
                String type = request.getType();

                if("pageNum".equals(name)){

                    stringBuffer.append(name + "=0&");
                }else if("pageSize".equals(name)){

                    stringBuffer.append(name + "=10&");
                }else {
                    switch (type) {
                        case "string":
                            stringBuffer.append(name+"=string&");
                            break;
                        case "integer":
                            stringBuffer.append(name+"=0&");
                            break;
                        case "double":
                            stringBuffer.append(name+"=0.0&");
                            break;
                        default:
                            stringBuffer.append(name+"=null&");
                            break;
                    }
                }
            }
        }
        String s = stringBuffer.toString();
        if ("".equalsIgnoreCase(s)){
            return "";
        }

        return "?" + StringUtils.removeEnd(s, "&");
    }



    /**
     * 通过JSON生成Html，Excel，Word
     * @param data 数据源
     * @param fileName 输出的文件名
     * @param templateFilePath 模板的文件路径
     * @param templateFile 模板的文件名
     */
    public static void createDayReportFiles(Map<String, Object> data, String fileName, String templateFilePath, String templateFile) {
        BufferedInputStream in = null;
        Writer out = null;
        Template template = null;
        try {
            //构造Configuration
            Configuration configuration = new Configuration();
            configuration.setDefaultEncoding("utf-8");
            configuration.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
            configuration.setDirectoryForTemplateLoading(new File(templateFilePath));

            try {
                //template.ftl为要装载的模板
                template = configuration.getTemplate(templateFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //输出文档路径及名称
            File outFile = new File(fileName);

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outFile);
                OutputStreamWriter oWriter = new OutputStreamWriter(fos,"UTF-8");
                //这个地方对流的编码不可或缺，使用main（）单独调用时，应该可以，但是如果是web请求导出时导出后word文档就会打不开，并且包XML文件错误。主要是编码格式不正确，无法解析。
                out = new BufferedWriter(oWriter);

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }

            //生成HTML
            template.process(data, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void htmlToWord(String content) {
        String path = "E:/test";
        if (!"".equals(path)) {
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            content = HtmlUtils.htmlUnescape(content);
            try {
                byte by[] = content.getBytes("UTF-8");
                ByteArrayInputStream bais = new ByteArrayInputStream(by);
                POIFSFileSystem poifs = new POIFSFileSystem();
                DirectoryEntry directory = poifs.getRoot();
                directory.createDocument("WordDocument", bais);

                FileOutputStream ostream = new FileOutputStream("E:\\test\\1.doc");
                poifs.writeFilesystem(ostream);

                bais.close();
                ostream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static String getHtmlContent(String htmlPath) {
        StringBuffer sb = new StringBuffer();
        BufferedInputStream bis = null;
        try {
            File f = new File(htmlPath);
            FileInputStream fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);
            int len = 0;
            byte[] temp = new byte[1024];
            while ((len = bis.read(temp)) != -1) {
                sb.append(new String(temp, 0, len));
            }
        } catch (Exception e) {
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }



}
