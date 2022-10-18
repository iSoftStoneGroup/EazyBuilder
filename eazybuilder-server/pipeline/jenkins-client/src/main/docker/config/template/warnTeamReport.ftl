<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport" content="width=device-width" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>质量检查报告</title>
<style>
* {
  margin: 0;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
  box-sizing: border-box;
  font-size: 14px;
}

img {
  max-width: 100%;
}

body {
  -webkit-font-smoothing: antialiased;
  -webkit-text-size-adjust: none;
  width: 100% !important;
  height: 100%;
  line-height: 1.6em;
  /* 1.6em * 14px = 22.4px, use px to get airier line-height also in Thunderbird, and Yahoo!, Outlook.com, AOL webmail clients */
  /*line-height: 22px;*/
}

/* Let's make sure all tables have defaults */
table td {
  vertical-align: center;
}

/* -------------------------------------
    BODY & CONTAINER
------------------------------------- */
body {
  background-color: #f6f6f6;
}

.body-wrap {
  background-color: #f6f6f6;
  width: 100%;
}

.container {
  display: block !important;
  max-width: 800px !important;
  margin: 0 auto !important;
  /* makes it centered */
  clear: both !important;
}

.content {
  max-width: 800px;
  margin: 0 auto;
  display: block;
  padding: 20px;
}

/* -------------------------------------
    HEADER, FOOTER, MAIN
------------------------------------- */
.main {
  background-color: #fff;
  border: 1px solid #e9e9e9;
  border-radius: 3px;
}

.content-wrap {
  padding: 20px;
}

.content-block {
  padding: 0 0 20px;
}

.header {
  width: 100%;
  margin-bottom: 20px;
}

.footer {
  width: 100%;
  clear: both;
  color: #999;
  padding: 20px;
}
.footer p, .footer a, .footer td {
  color: #999;
  font-size: 12px;
}

/* -------------------------------------
    TYPOGRAPHY
------------------------------------- */
h1, h2, h3 {
  font-family: "Helvetica Neue", Helvetica, Arial, "Lucida Grande", sans-serif;
  color: #000;
  margin: 20px 0 0;
  line-height: 1.2em;
  font-weight: 400;
}

h1 {
  font-size: 32px;
  font-weight: 500;
  /* 1.2em * 32px = 38.4px, use px to get airier line-height also in Thunderbird, and Yahoo!, Outlook.com, AOL webmail clients */
  /*line-height: 38px;*/
}

h2 {
  font-size: 24px;
  /* 1.2em * 24px = 28.8px, use px to get airier line-height also in Thunderbird, and Yahoo!, Outlook.com, AOL webmail clients */
  /*line-height: 29px;*/
}

h3 {
  font-size: 18px;
  /* 1.2em * 18px = 21.6px, use px to get airier line-height also in Thunderbird, and Yahoo!, Outlook.com, AOL webmail clients */
  /*line-height: 22px;*/
}

h4 {
  font-size: 14px;
  font-weight: 600;
}

p, ul, ol {
  margin-bottom: 10px;
  font-weight: normal;
}
p li, ul li, ol li {
  margin-left: 5px;
  list-style-position: inside;
}

/* -------------------------------------
    LINKS & BUTTONS
------------------------------------- */
a {
  color: #348eda;
  text-decoration: underline;
}

.btn-primary {
  text-decoration: none;
  color: #FFF;
  background-color: #348eda;
  border: solid #348eda;
  border-width: 10px 20px;
  line-height: 2em;
  /* 2em * 14px = 28px, use px to get airier line-height also in Thunderbird, and Yahoo!, Outlook.com, AOL webmail clients */
  /*line-height: 28px;*/
  font-weight: bold;
  text-align: center;
  cursor: pointer;
  display: inline-block;
  border-radius: 5px;
  text-transform: capitalize;
}

/* -------------------------------------
    OTHER STYLES THAT MIGHT BE USEFUL
------------------------------------- */
.last {
  margin-bottom: 0;
}

.first {
  margin-top: 0;
}

.aligncenter {
  text-align: center;
}

.alignright {
  text-align: right;
}

.alignleft {
  text-align: left;
}

.clear {
  clear: both;
}

/* -------------------------------------
    ALERTS
    Change the class depending on warning email, good email or bad email
------------------------------------- */
.alert {
  font-size: 16px;
  color: #fff;
  font-weight: 500;
  padding: 20px;
  text-align: center;
  border-radius: 3px 3px 0 0;
}
.alert a {
  color: #fff;
  text-decoration: none;
  font-weight: 500;
  font-size: 16px;
}
.alert.alert-warning {
  background-color: #FF9F00;
}
.alert.alert-bad {
  background-color: #D0021B;
}
.alert.alert-good {
  background-color: #68B90F;
}

/* -------------------------------------
    INVOICE
    Styles for the billing table
------------------------------------- */
.invoice {
  margin: 40px auto;
  text-align: left;
  width: 80%;
}
.invoice td {
  padding: 5px 0;
}
.invoice .invoice-items {
  width: 100%;
}
.invoice .invoice-items td {
  border-top: #eee 1px solid;
}
.invoice .invoice-items .total td {
  border-top: 2px solid #333;
  border-bottom: 2px solid #333;
  font-weight: 700;
}

.report-table {
  border:1px solid #ccc;
  width:100%;
  border-collapse: collapse;
}

table.report-table th {
   border:1px solid #ccc;
   cellspacing:0;
   cellpadding:0;
}

table.report-table td {
   border:1px solid #ccc;
   cellspacing:0;
   cellpadding:0;
}


/* -------------------------------------
    RESPONSIVE AND MOBILE FRIENDLY STYLES
------------------------------------- */
@media only screen and (max-width: 640px) {
  body {
    padding: 0 !important;
  }

  h1, h2, h3, h4 {
    font-weight: 800 !important;
    margin: 20px 0 5px !important;
  }

  h1 {
    font-size: 22px !important;
  }

  h2 {
    font-size: 18px !important;
  }

  h3 {
    font-size: 16px !important;
  }

  .container {
    padding: 0 !important;
    width: 100% !important;
  }

  .content {
    padding: 0 !important;
  }

  .content-wrap {
    padding: 10px !important;
  }

  .invoice {
    width: 100% !important;
  }
}
</style>

</head>

<body itemscope itemtype="http://schema.org/EmailMessage">
<#setting datetime_format="yyyy-MM-dd hh:mm:ss"/>
<table class="body-wrap">
    <tr>
        <td></td>
        <td class="container" width="800">
            <div class="content">
                <table class="main" width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td class="content-wrap aligncenter">
                            <table width="100%">
                                <tr>
                                    <td>
                                        <h2 class="aligncenter">质量检查报告</h2>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <h4 class="aligncenter">生成时间：${reportDate?string('yyyy-MM-dd hh:mm:ss')}</h4>
                                    </td>
                                </tr>
                                <tr>
                                	
                                	
                                		<td class="content-block aligncenter">
                                		<#list projectReport as p>
                                		  <#if p.warnProjectReports?? && p.warnProjectReports?size gt 0>
                                    		<h3 class="aligncenter">预警项目-${p.teamName}</h3>
                                    		<table class="report-table" style="border:1px solid #ccc;width:100%;">
                                    		<tr style="border-bottom:1px solid #ccc;">
                                    			<td nowrap="nowrap"><b>工程名称</b></td>
                								<td nowrap="nowrap"><b>扫描状态</b></td>
                
                								<td nowrap="nowrap"><b>BUG(阻断)</b></td>
                								<td nowrap="nowrap"><b>安全漏洞(阻断)</b></td>
                								<td nowrap="nowrap"><b>代码规范(阻断)</b></td>
                								<td nowrap="nowrap"><b>依赖包高危漏洞</b></td>
                								<td nowrap="nowrap"><b>单元测试覆盖率%</b></td>
                								<td nowrap="nowrap"><b>代码行数</b></td>
                                    		</tr>
                                    		<#list p.warnProjectReports as qa>
                                    		<tr style="border-bottom:1px solid #ccc;">
            										<td>${qa.project.description}</td>
            	
            										<#if qa.latestPipeline.status??>
            										<td><a href='${baseUrl}/jenkins/job/${qa.project.name}' target='_blank'>${qa.latestPipeline.status}</a></td>
            										<#else>
            										<td>--</td>
            										</#if>
            										
            										<#assign flag = "false">
            										<#list qa.metrics as metric>
            										<#if metric.type=="bug_blocker">
    												<td>
    												<#if metric.isRed??&& metric.isRed==true>
    												<a href='${baseUrl}/sonarqube/project/issues?id=${qa.project.sonarKey!}&resolved=false&types=BUG' style="color:red" target='_blank'>${metric.val}</a>
    												<#else>
    												<a href='${baseUrl}/sonarqube/project/issues?id=${qa.project.sonarKey!}&resolved=false&types=BUG' target='_blank'>${metric.val}</a>
    												</#if>
    												</td>
            										<#assign flag = "true">
            										</#if>
            										</#list>
            										<#if flag=="false">
            										<td>N/A</td>
            										</#if>
            										
            										<#assign flag = "false">
            										<#list qa.metrics as metric>
            										<#if metric.type=="vulner_blocker">
    												<td>
    												<#if metric.isRed??&& metric.isRed==true>
    												<a href='${baseUrl}/sonarqube/project/issues?id=${qa.project.sonarKey!}&resolved=false&types=VULNERABILITY' style="color:red" target='_blank'>${metric.val}</a>
    												<#else>
    												<a href='${baseUrl}/sonarqube/project/issues?id=${qa.project.sonarKey!}&resolved=false&types=VULNERABILITY' target='_blank'>${metric.val}</a>
    												</#if>
    												</td>
            										<#assign flag = "true">
            										</#if>
            										</#list>
            										<#if flag=="false">
            										<td>N/A</td>
            										</#if>
            										
            										<#assign flag = "false">
            										<#list qa.metrics as metric>
            										<#if metric.type=="code_smell_blocker">
    												<td>
    												<#if metric.isRed??&& metric.isRed==true>
    												<a href='${baseUrl}/sonarqube/project/issues?id=${qa.project.sonarKey!}&resolved=false&types=CODE_SMELL' style="color:red" target='_blank'>${metric.val}</a>
    												<#else>
    												<a href='${baseUrl}/sonarqube/project/issues?id=${qa.project.sonarKey!}&resolved=false&types=CODE_SMELL' target='_blank'>${metric.val}</a>
    												</#if>
    												</td>
            										<#assign flag = "true">
            										</#if>
            										</#list>
            										<#if flag=="false">
            										<td>N/A</td>
            										</#if>
            										
            										<#assign flag = "false">
            										<#list qa.metrics as metric>
            										<#if metric.type=="dc_high">
            										<#if metric.link??>
            										<td>
            										<#if metric.isRed??&& metric.isRed==true>
            										<a href='${metric.link}' style="color:red" target='_blank'>${metric.val}</a>
            										<#else>
            										<a href='${metric.link}' target='_blank'>${metric.val}</a>
            										</#if>
            										</td>
            										<#else>
            										<td>${metric.val}</td>
            										</#if>
            										<#assign flag = "true">
            										</#if>
            										</#list>
            										<#if flag=="false">
            										<td>N/A</td>
            										</#if>
            										
            										<#assign flag = "false">
            										<#list qa.metrics as metric>
            										<#if metric.type=="unit_test_coverage_rate">
            										<#if metric.link??>
            										<td>
            										<#if metric.isRed??&& metric.isRed==true>
            										<a href='${metric.link}' style="color:red" target='_blank'>${metric.val}</a>
            										<#else>
            										<a href='${metric.link}' target='_blank'>${metric.val}</a>
            										</#if>
            										</td>
            										<#else>
            										<td>${metric.val}</td>
            										</#if>
            										<#assign flag = "true">
            										</#if>
            										</#list>
            										<#if flag=="false">
            										<td>N/A</td>
            										</#if>
            										
            										<#assign flag = "false">
            										<#list qa.metrics as metric>
            										<#if metric.type=="code_line">
    												<td>
    												<#if metric.isRed??&& metric.isRed==true><b style="color:red"></#if>${metric.val}</td>
    												<#assign flag = "true">
            										</#if>
            										</#list>
            										<#if flag=="false">
            										<td>N/A</td>
            										</#if>
            										
            										<#assign flag = "false">
            								</tr>
                                    		</#list>
                                		</table>
                                		<br/>
    							        <br/>
    							        </#if>
                                		</#list>
                                	</td>
                                	
                                </tr>
                                <tr>
                                	<td class="content-block aligncenter">
                                	<h3 class="aligncenter">团队汇总</h3>
                                	
                                	<table class="report-table" style="border:1px solid #ccc;width:100%;">
                                		<tr style="border-bottom:1px solid #ccc;">
            								<td nowrap="nowrap"><b>团队</b></td>
            								<td nowrap="nowrap"><b>工程数</b></td>
            
            								<td nowrap="nowrap"><b>bug（阻断）</b></td>
            								<td nowrap="nowrap"><b>安全漏洞(阻断)</b></td>
            								<td nowrap="nowrap"><b>代码规范(阻断)</b></td>
            								<td nowrap="nowrap"><b>依赖包高危漏洞</b></td>
            								<td nowrap="nowrap"><b>单元测试覆盖率%</b></td>
            
        								</tr>
        								<#list projectReport as p>
        								<tr style="border-bottom:1px solid #ccc;">
        									<td>${p.teamName}</td>
        									<td>${p.projectNum}</td>
        									
        									<td>${p.bugBlockerSum}</td>
        									<td>${p.vulnerBlockerSum}</td>
        									<td>${p.codeSmellBlocker}</td>
        									<td>${p.dcHighSum}</td>
        									<td>${p.unitTestCoverageRate}</td>
        								</tr>
                                		</#list>
                                	</table>
                                	
                                	</td>
                                </tr>
                                <tr>
                                    <td class="content-block aligncenter">
									<#list projectReport as p>
										<h3 class="aligncenter">${p.teamName}</h3>
										<table class="report-table" style="border:1px solid #ccc;width:100%;">
        									<tr style="border-bottom:1px solid #ccc;">
            									<td nowrap="nowrap"><b>工程名称</b></td>
            									<td nowrap="nowrap"><b>扫描状态</b></td>
            
            									<td nowrap="nowrap"><b>BUG(阻断)</b></td>
            									<td nowrap="nowrap"><b>安全漏洞(阻断)</b></td>
            									<td nowrap="nowrap"><b>代码规范(阻断)</b></td>
            									<td nowrap="nowrap"><b>依赖包高危漏洞</b></td>
            									<td nowrap="nowrap"><b>单元测试覆盖率%</b></td>
            									<td nowrap="nowrap"><b>代码行数</b></td>
            
        									</tr>
        									<#list p.projectQAReports as qa>
        									<tr style="border-bottom:1px solid #ccc;">
        										<td>${qa.project.description}</td>
        	
        										<#if qa.latestPipeline?? && qa.latestPipeline.status??>
        										<td><a href='${baseUrl}/jenkins/job/${qa.project.name}' target='_blank'>${qa.latestPipeline.status}</a></td>
        										<#else>
        										<td>--</td>
        										</#if>
        										
        										<#assign flag = "false">
												<#if qa.metrics??>
        										<#list qa.metrics as metric>
        										<#if metric.type=="bug_blocker">
												<td><a href='${baseUrl}/sonarqube/project/issues?id=${qa.project.sonarKey!}&resolved=false&types=BUG' target='_blank'>${metric.val}</a></td>
        										<#assign flag = "true">
        										</#if>
        										</#list>
												</#if>
        										<#if flag=="false">
        										<td>N/A</td>
        										</#if>
        										
        										<#assign flag = "false">
												<#if qa.metrics??>
        										<#list qa.metrics as metric>
        										<#if metric.type=="vulner_blocker">
												<td><a href='${baseUrl}/sonarqube/project/issues?id=${qa.project.sonarKey!}&resolved=false&types=VULNERABILITY' target='_blank'>${metric.val}</a></td>
        										<#assign flag = "true">
        										</#if>
        										</#list>
												</#if>
        										<#if flag=="false">
        										<td>N/A</td>
        										</#if>
        										
        										<#assign flag = "false">
												<#if qa.metrics??>
        										<#list qa.metrics as metric>
        										<#if metric.type=="code_smell_blocker">
												<td><a href='${baseUrl}/sonarqube/project/issues?id=${qa.project.sonarKey!}&resolved=false&types=CODE_SMELL' target='_blank'>${metric.val}</a></td>
        										<#assign flag = "true">
        										</#if>
        										</#list>
												</#if>
        										<#if flag=="false">
        										<td>N/A</td>
        										</#if>
        										
        										<#assign flag = "false">
												<#if qa.metrics??>
        										<#list qa.metrics as metric>
        										<#if metric.type=="dc_high">
        										<#if metric.link??>
        										<td><a href='${metric.link}' target='_blank'>${metric.val}</a></td>
        										<#else>
        										<td>${metric.val}</td>
        										</#if>
        										<#assign flag = "true">
        										</#if>
        										</#list>
												</#if>
        										<#if flag=="false">
        										<td>N/A</td>
        										</#if>
        										
        										<#assign flag = "false">
												<#if qa.metrics??>
        										<#list qa.metrics as metric>
        										<#if metric.type=="unit_test_coverage_rate">
        										<#if metric.link??>
        										<td><a href='${metric.link}' target='_blank'>${metric.val}</a></td>
        										<#else>
        										<td>${metric.val}</td>
        										</#if>
        										<#assign flag = "true">
        										</#if>
        										</#list>
												</#if>
        										<#if flag=="false">
        										<td>N/A</td>
        										</#if>
        										
        										<#assign flag = "false">
												<#if qa.metrics??>
        										<#list qa.metrics as metric>
        										<#if metric.type=="code_line">
												<td>${metric.val}</td>
												<#assign flag = "true">
        										</#if>
        										</#list>
												</#if>
        										<#if flag=="false">
        										<td>N/A</td>
        										</#if>
        										
        										<#assign flag = "false">
        								</tr>
        								</#list>
    							</table>
    							<br/>
    							<br/>
							</#list>
							</td>
                        </tr>
                  </table>
                        </td>
                    </tr>
                </table>
                <div class="footer">
                    <table width="100%">
                        <tr>
                            <td class="aligncenter content-block">有任何疑问，请发送邮件至<a href="mailto:zzzz@eazybuilder.com">Eazybuilder持续集成组</a></td>
                        </tr>
                    </table>
                </div></div>
        </td>
        <td></td>
    </tr>
</table>
</body>
</html>