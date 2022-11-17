<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport" content="width=device-width" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>项目质量报告</title>
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
  vertical-align: top;
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
  max-width: 600px !important;
  margin: 0 auto !important;
  /* makes it centered */
  clear: both !important;
}

.content {
  max-width: 600px;
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
  margin: 40px 0 0;
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

<table class="body-wrap">
    <tr>
        <td></td>
        <td class="container" width="650">
            <div class="content">
                <table class="main" width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td class="content-wrap aligncenter">
                            <table width="100%" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td class="content-block">
                                        <h2 class="aligncenter">项目:${project.description}(${project.name}) </h2>
                                    </td>
                                </tr>
                                <#if event?? && event.ref??>
                                <tr>
                                    <td>
                                        <h5 class="aligncenter">[${event.push_event!}${event.event_name!}]${event.ref}  commiter:${event.user_name}</h5>
                                        <#if event.commits??>
                                        <ul class="aligncenter">
                                          <#list event.commits as commit>
                                          <li>${commit.message}</li>
                                          </#list>
                                        </ul>
                                        </#if>
                                    </td>
                                </tr>
                                </#if>
                                <tr>
                                    <td class="content-block">
                                        <h3 class="aligncenter">构建结果:${pipeline.status}&nbsp;&nbsp;&nbsp;&nbsp;总用时：${pipeline.durationMillis/1000}(秒)</h3>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="content-block aligncenter">
                                        <table class="invoice">
                                            <tr>
                                                <td>
                                                    <table class="invoice-items" cellpadding="0" cellspacing="0">
                                                        <#if reports?size gt 0 >
                                                            <tr class="total">
                                                                <td class="aligncenter" colspan="3">汇总报告</td>
                                                            </tr>
                                                        </#if>
                                                        <#if reports.project_analysis??>
                                                            <tr>
                                                                <td>新增代码行数</td>
                                                                <td>${reports.project_analysis.summary.rows[0][3]}</td>
                                                                <td class="alignright"><a href="${reports.project_analysis.link}">查看</a></td>
                                                            </tr>
                                                        </#if>
                                                        <#if reports.junit??>
                                                            <tr>
                                                            <td>单元测试通过率</td>
                                                            <td>${reports.project_analysis.summary.rows[0][2]}</td>
                                                            <td class="alignright"><a href="${reports.project_analysis.link}">查看</a></td>
                                                            </tr>
                                                        </#if>
                                                        <#if reports.jacoco??>
                                                            <tr>
                                                            <td>测试覆盖率</td>
                                                            <td>${reports.project_analysis.summary.rows[0][1]}</td>
                                                            <td class="alignright"><a href="${reports.project_analysis.link}">查看</a></td>
                                                            </tr>
                                                        </#if>
														<#if reports.dependency_check??>
															<tr>
                                                            <td>依赖包检查</td>
                                                            <td>问题包总数：${reports.dependency_check.summary.dataMap['Total'][0]},含高危漏洞的:${reports.dependency_check.summary.dataMap['High'][0]},含中等漏洞的:${reports.dependency_check.summary.dataMap['Medium'][0]}</td>
                                                            <td class="alignright"><a href="${reports.dependency_check.link}">查看</a></td>
                                                            </tr>
														</#if>
														<#if reports.sql_scan??>
														   <tr>
                                                            <td>SQL检查</td>
                                                            <td>SQL总数：${reports.sql_scan.summary.dataMap['total'][0]},不兼容的:${reports.sql_scan.summary.dataMap['imcompatible'][0]}</td>
                                                            <td class="alignright"><a href="${reports.sql_scan.link}">查看</a></td>
                                                            </tr>
														</#if>
                                                        <#if reports.sonar??>
                                                        <tr>
                                                            <td>BUG</td>
                                                            <td class="">新增数量: ${reports.project_analysis.summary.rows[0][5]!''} 阻断(总):${reports.sonar.summary.rows[0][1]},严重(总):${reports.sonar.summary.rows[0][2]},主要(总):${reports.sonar.summary.rows[0][3]}</td>
                                                            <td class="alignright"><a href="${reports.sonar.link}&types=BUG">查看</a></td>
                                                        </tr>
                                                        <tr>
                                                            <td>源码安全漏洞</td>
                                                            <td class="">新增数量: ${reports.project_analysis.summary.rows[0][6]!''} 阻断(总):${reports.sonar.summary.rows[1][1]},严重(总):${reports.sonar.summary.rows[1][2]},主要(总):${reports.sonar.summary.rows[1][3]}</td>
                                                            <td class="alignright"><a href="${reports.sonar.link}&types=VULNERABILITY">查看</a></td>
                                                        </tr>
                                                        <tr>
                                                            <td>编码规范</td>
                                                            <td class="">新增数量: ${reports.project_analysis.summary.rows[0][7]!''}  阻断(总):${reports.sonar.summary.rows[2][1]},严重(总):${reports.sonar.summary.rows[2][2]},主要(总):${reports.sonar.summary.rows[2][3]}</td>
                                                            <td class="alignright"><a href="${reports.sonar.link}&types=CODE_SMELL">查看</a></td>
                                                        </tr>
                                                        </#if>
                                                        <#if reports.project_analysis??>
                                                            <tr>
                                                                <td>技术债</td>
                                                                <td>新增数量：${reports.project_analysis.summary.rows[0][8]!''} 总数：${reports.project_analysis.summary.rows[0][4]} </td>
                                                                <td class="alignright"><a href="${reports.project_analysis.link}">查看</a></td>
                                                            </tr>
                                                        </#if>
                                                        <tr class="total">
                                                            <td class="aligncenter" colspan="3">访问地址：<#if project.deployInfo?? && project.deployInfo.url??> <a href="${project.deployInfo.url}">${project.deployInfo.url}</a><#else>无</#if></td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="content-block aligncenter">
                                        <a href="${buildUrl}">在浏览器中查看构建详情</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="content-block aligncenter">
                                        
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <div class="footer">
                    <table width="100%">
                        <tr>
                            <td class="aligncenter content-block">有任何疑问，请发送邮件至<a href="mailto:zpzhaoa@Eazybuilder.com">DevOps项目组</a></td>
                        </tr>
                    </table>
                </div></div>
        </td>
        <td></td>
    </tr>
</table>
</body>
</html>