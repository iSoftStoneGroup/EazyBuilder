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

.invoice .invoice-items .project td {
  border-top: 2px solid #333;
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
        <td class="container" width="800">
            <div class="content">
                <table class="main" width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td class="content-wrap aligncenter">
                            <table width="100%" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td class="content-block">
                                        <h2 class="aligncenter">${job.name}——报告</h2>
                                    </td>
                                </tr>
                                <#if event?? && event.ref??>
                                <tr>
                                    <td>
                                        <h5 class="aligncenter">[${event.object_kind}]${event.ref}  commiter:${event.user_name}</h5>
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
                                    <td class="content-block aligncenter">
                                        <table class="invoice">
                                            <tr>
                                                <td>
                                                    <table class="invoice-items" cellpadding="0" cellspacing="0">
                                                            
                                                            <tr class="project">
                                                                <td>项目</td><td>构建状态</td><td class="alignleft">检查项</td><td colspan="2">情况</td>
                                                            </tr>
                                                            <#list results as result>
                                                                <#if result.success?? && result.success?string=="true">
                                                                        <#assign firstRowPended=false>
                                                                        <#assign totalRow=0>
                                                                        <tr class="project">
                                                                        <#if result.typedReport?? && result.typedReport?size gt 0>
                                                                        <#list result.typedReport?keys as type>
                                                                           <#if type=='sonar' && result.typedReport.sonar?? && result.typedReport.sonar.summary??>
                                                                              <#assign totalRow=totalRow+3>
                                                                           <#elseif type=='junit' || type=='dependency_check' || type=='jacoco'>
                                                                              <#assign totalRow=totalRow+1>
                                                                           <#else>
                                                                           </#if>
                                                                        </#list>
                                                                        </#if>
                                                                        
                                                                        <td rowspan="${totalRow}">${result.project.name}<br/>(${result.project.description})<br/></td>
                                                                        <td rowspan="${totalRow}">成功</td>
                                                                        <#if result.typedReport?? && result.typedReport?size gt 0>
                                                                            <#assign reports=result.typedReport>
                                                                            <#if reports.junit??>
                                                                                <#if firstRowPended>
                                                                                    <tr>
                                                                                <#else>
                                                                                    <#assign firstRowPended=true>
                                                                                </#if>
                                                                                <td>单元测试通过率</td>
                                                                                <td>${reports.junit.summary.dataMap['Success Rate'][0]}</td>
                                                                                <td><a href="${reports.junit.link}">查看</a></td>
                                                                                </tr>
                                                                            </#if>
                                                                            <#if reports.jacoco??>
                                                                                <#if firstRowPended>
                                                                                    <tr>
                                                                                <#else>
                                                                                    <#assign firstRowPended=true>
                                                                                </#if>
                                                                                <td>测试覆盖率</td>
                                                                                <td>${reports.jacoco.summary.dataMap['Cov.'][0]}</td>
                                                                                <td class="alignright"><a href="${reports.jacoco.link}">查看</a></td>
                                                                                </tr>
                                                                            </#if>
                                                                        
                                                                            <#if reports.dependency_check??>
                                                                                <#if firstRowPended>
                                                                                    <tr>
                                                                                <#else>
                                                                                    <#assign firstRowPended=true>
                                                                                </#if>
                                                                                <td>依赖包检查</td>
                                                                                <td>高危:${reports.dependency_check.summary.dataMap['High'][0]},中等:${reports.dependency_check.summary.dataMap['Medium'][0]}</td>
                                                                                <td><a href="${reports.dependency_check.link}">查看</a></td>
                                                                                </tr>
                                                                            </#if>
                                                                            
                                                                            <#if reports.sql_scan??>
                                                                                <#if firstRowPended>
                                                                                    <tr>
                                                                                <#else>
                                                                                    <#assign firstRowPended=true>
                                                                                </#if>
                                                                                <td>SQL检查</td>
                                                                                <td>SQL总数：${reports.sql_scan.summary.dataMap['total'][0]},不兼容的:${reports.sql_scan.summary.dataMap['imcompatible'][0]}</td>
                                                                                <td class="alignright"><a href="${reports.sql_scan.link}">查看</a></td>
                                                                                </tr>
                                                                            </#if>
                                                                        
                                                                            <#if reports.sonar?? && reports.sonar.summary??>
                                                                                <#if firstRowPended>
                                                                                    <tr>
                                                                                <#else>
                                                                                    <#assign firstRowPended=true>
                                                                                </#if>
                                                                                    <td>BUG</td>
                                                                                    <td>阻断:${reports.sonar.summary.rows[0][1]} 严重:${reports.sonar.summary.rows[0][2]} 主要:${reports.sonar.summary.rows[0][3]}</td>
                                                                                    <td><a href="${reports.sonar.link}&types=BUG">查看</a></td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td>源码安全问题</td>
                                                                                    <td>阻断:${reports.sonar.summary.rows[1][1]} 严重:${reports.sonar.summary.rows[1][2]} 主要:${reports.sonar.summary.rows[1][3]}</td>
                                                                                    <td><a href="${reports.sonar.link}&types=VULNERABILITY">查看</a></td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td>编码规范问题</td>
                                                                                    <td>阻断:${reports.sonar.summary.rows[2][1]} 严重:${reports.sonar.summary.rows[2][2]} 主要:${reports.sonar.summary.rows[2][3]}</td>
                                                                                    <td><a href="${reports.sonar.link}&types=CODE_SMELL">查看</a></td>
                                                                                </tr>
                                                                            </#if>
                                                                        <#else>
                                                                            <#--report not exist-->
                                                                            <#if firstRowPended>
                                                                               <tr>
                                                                            <#else>
                                                                               <#assign firstRowPended=true>
                                                                            </#if>
                                                                                 <td colspan="3">无</td>
                                                                               </tr>
                                                                     </#if>
                                                            <#else>
                                                                <#--failed-->
                                                                <tr class="project">
                                                                <td>${result.project.name}</td>
                                                                <td><#if result.success?? && result.success?string=="false">失败<#else>无法识别具体结果，请通过链接查看</#if></td>
                                                                <td colspan="2" class="aligncenter">--</td>
                                                                <td><a href="${buildUrl}/${result.project.name}/">查看</a></td>
                                                                </tr>
                                                            </#if>
                                                            </#list>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                               
                            </table>
                        </td>
                    </tr>
                </table>
                <div class="footer">
                    <table width="100%">
                        <tr>
                            <td class="content-block">
                                <li>1.邮件中的链接需要访问公司内网服务器(0.0.0.0)打开，如果您使用外网请通过VPN/开通访问权限；</li>
                                <li>2.对于构建失败的工程，请确保提供的SVN地址对应的工程项目能正常编译(没有缺失依赖)，<br/>点击【查看】链接jenkins上有更详细的信息；</li>
                                <li>3.阻断类问题及高危漏洞会严重威胁系统运行安全，如果存在上述问题，请优先进行整改处理。</li>
                            </td>
                        </tr>
                        <tr>
                            <td class="aligncenter content-block">有任何疑问，请发送邮件至<a href="mailto:xxx@eazybuilder.com">DevOps项目组</a></td>
                        </tr>
                    </table>
                </div></div>
        </td>
        <td></td>
    </tr>
</table>
</body>
</html>