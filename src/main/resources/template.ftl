<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta http-equiv="Content-Type" content="application/msword"/>
    <title>toWord</title>
    <style type="text/css">
        .bg {
            background-color: rgb(84, 127, 177);
        }
        table {
            table-layout: fixed;
        }
        tr {
            height: 20px;
            font-size: 12px;
        }
        td {
            overflow: hidden;
            word-break: break-all;
            word-wrap: break-word;
        }
        .specialHeight {
            height: 40px;
        }
    </style>
</head>

<body>
<div style="width:800px; margin: 0 auto">
    <#list divs as controller>
        <h3>${controller.divIndex + '.' + controller.title}</h3>
        <#list controller.tables as table>
            <h4>${controller.divIndex + '.' + table.tableIndex + '.' + table.tag}</h4>
            <table border="1" cellspacing="0" cellpadding="0" width="100%">
                <tr class="bg">
                    <td colspan="5" align="center">${table.tag}</td>
                </tr>
                <tr>
                    <td>URL</td>
                    <td colspan="4">${table.url}</td>
                </tr>
                <tr>
                    <td>请求方式</td>
                    <td colspan="4">${table.requestType}</td>
                </tr>
                <tr>
                    <td>返回类型</td>
                    <td colspan="4">${table.responseForm}</td>
                </tr>
                <tr class="bg">
                    <td colspan="5" align="center">请求参数</td>
                </tr>
                <tr class="bg" align="center">
                    <td>参数名</td>
                    <td>数据类型</td>
                    <td>参数类型</td>
                    <td>是否必填</td>
                    <td>说明</td>
                </tr>
                <#list table.requestList as request>
                <tr align="center">
                    <td>${request.name}</td>
                    <td>${request.type}</td>
                    <td>${request.paramType}</td>
                    <td>${request.require}</td>
                    <td>${request.description}</td>
                </tr>
                </#list>
                <tr class="bg">
                    <td colspan="5" align="center">返回参数</td>
                </tr>
                <tr class="bg" align="center">
                    <td>状态码</td>
                    <td colspan="4">描述</td>
                </tr>
                <#list table.responseList as response>
                <tr align="center">
                    <td>${response.name}</td>
                    <td colspan="4">${response.description}</td>
                </tr>
                </#list>
                <tr class="bg">
                    <td colspan="5" align="center">示例</td>
                </tr>
                <tr class="specialHeight">
                    <td class="bg">请求值</td>
                    <td colspan="4">${table.requestParam}</td>
                </tr>
                <tr class="specialHeight">
                    <td class="bg">返回值</td>
                    <td colspan="4">${table.responseParam}</td>
                </tr>
            </table>
        </#list>
    </#list>
</div>
</body>
</html>
