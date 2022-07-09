<#compress>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="UTF-8"></meta>
<title>SQL扫描报告</title>
</head>
<style>
.title {
    text-align: center;
}
body {
    font-size: 14px;
    background-color: white;
}
.table-title {
    background-color: #0066CC;
    color: white;
}
.table-title td th{
    width: 150px;
    text-align: center;
}
table {
    border: 2px solid black;
    border-collapse: collapse;
    text-align: center;
    width: 60%;
    margin: auto;
}
table td {
    border: 1px solid black;
}
th {
    text-align:center;
	color:red;
}
.info {
    background-color: green;
    color: white;
}
.warning {
    background-color: yellow;
    color: black;
}
.danger {
    background-color: red;
    color: white;
}
td.warning a {
    color: black;
}
td.info a {
    color: white;
}
td.danger a {
    color: white;
}
li {
    font-size: 16px;
    margin-top: 5px;
}
pre { 
white-space: pre-wrap; 
white-space: -moz-pre-wrap; 
white-space: -pre-wrap; 
white-space: -o-pre-wrap;
word-wrap: break-word; 
word-break: keep-all; 
} 
</style>
<body>
     <div style="height:25px;padding:5px 10px;">
        <img src="data:image/jpg;base64,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" style="height:25px;"></img>
    </div>
    <div>
        <h1 class="title">SQL兼容性扫描报告</h1>
        <div style="margin-bottom: 5px;margin-right:30px;">
            <div style="text-align:right;">
            <span>扫描时间：${.now?string("yyyy-MM-dd HH:mm:ss")}</span>
            </div>
        </div>
        <div  style="padding:0px 30px 15px 30px;border:none;">
            <table style="border: none;width: 60%;">
                <tr style="height: 30px">
                    <th>项目名称：${report.projectName} </th><th>SQL总数：${report.totalSql}</th><th>SQL不兼容数：${report.totalFound}</th>
                </tr>
            </table>
        </div>
		<#if report.totalFound == 0>
		<div style="border: 1px solid #000;margin:0px 30px 0px 30px;padding-bottom:15px;">
			<h3 class="title" style="color:green;">没有发现不兼容的SQL</h3>
		</div>
		</#if>
        <#if report.totalFound gt 0>
        <div style="border: 1px solid #000;margin:0px 30px 0px 30px;padding-bottom:15px;">            
            <h4 style="margin-left: 50px;margin-top:15px; display: inline-block;">扫描概要：</h4>
            <div style="padding: 0px 60px;">
                <table style="table-layout: fixed;width:100%;">
                            <tr>
                                <td  class="table-title">编号</td>
                                <td  class="table-title" width="60%">源文件</td>
                                <td  class="table-title">SQL总数</td>
                                <td  class="table-title">问题总数</td>
                            </tr>
							<#list report.summary as sourceFile> 
							
                            <tr>
                               <td>
                                   <span>${sourceFile_index+1}</span>
                               </td>
                               <td>
                                   <span><a href="#${sourceFile.source?url('utf-8')}">${sourceFile.source}</a></span>
                               </td>
                               <td>
                                   <span>${sourceFile.total}</span>
                               </td>
                               <td>
                                   <span>${sourceFile.imcompatible}</span>
                               </td>
                            </tr>
							</#list>				 
                        </table>
            </div>
        </div>
		
        <div style="border: 1px solid #000; margin:15px 30px 0px 30px;padding-bottom:15px;">            
            <h4 style="margin-left: 50px;margin-top:15px; display: inline-block;">问题详情：</h4>
            <div style="padding:0px 10px;">
                <table style="width:100%; table-layout: fixed;">
				    <#list report.detail?keys as sourceFile>
				            <tr>
							<td style="width:50px;"><b>源文件:</b></td>
							<td colspan="3" style="text-align:left;" id="${sourceFile?url('utf-8')}"><b>${sourceFile}</b></tr>
                            <tr>
                                <td  class="table-title">编号</td>
                                <td  class="table-title" style="width:50px;">SQL-ID</td>
                                <td  class="table-title" style="width:40%; white-space: pre-wrap;">SQL预览</td>
								<td  class="table-title" style="width:40%;white-space: pre-wrap;">问题描述</td>
                            </tr>
							<#list report.detail[sourceFile] as checkResult>
                            <tr>
                               <td>
                                   <span>${checkResult_index+1}</span>
                               </td>
                               <td>
                                   <span>${checkResult.sqlId}</span>
                               </td>
                               <td style="text-align:left;white-space: pre-wrap">
                                   ${checkResult.sql}
                               </td>
                               <td style="text-align:left;white-space: pre-wrap">
                                  ${checkResult.msg}
                               </td>
                            </tr>
							</#list>
				   </#list>	
							
                        </table>
            </div>

        </div>
</#if>
        
    </div>
    
</body>

</html>
</#compress>   