${job.name}
<#if event?? && event.ref??>
> [${event.object_kind}]${event.ref}  commiter:${event.user_name}

<#list event.commits as commit>
- ${commit.message}
</#list>
</#if>

<#list results as result>
> ${result.project.name}(${result.project.description})

<#if result.success?? && result.success?string=="true">
构建：成功
<#elseif result.success?? && result.success?string=="false">
构建：失败：[查看详情](${buildUrl}/${result.project.name}/)
<#else>
[查看详情](${buildUrl}/${result.project.name}/)
</#if>
<#if result.typedReport?? && result.typedReport?size gt 0>
<#assign reports=result.typedReport>
<#if reports.junit??>
- [单元测试通过率](${reports.junit.link}): ${reports.junit.summary.dataMap['Success Rate'][0]}
</#if>
<#if reports.jacoco??>
- [测试覆盖率](${reports.jacoco.link}): ${reports.jacoco.summary.dataMap['Cov.'][0]}
</#if>
<#if reports.dependency_check??>
- [依赖包检查](${reports.dependency_check.link}): 高危:${reports.dependency_check.summary.dataMap['High'][0]},中等:${reports.dependency_check.summary.dataMap['Medium'][0]}
</#if>
<#if reports.sql_scan??>
- [SQL兼容检查](${reports.sql_scan.link}): SQL总数：${reports.sql_scan.summary.dataMap['total'][0]},不兼容的:${reports.sql_scan.summary.dataMap['imcompatible'][0]}
</#if>
<#if reports.sonar?? && reports.sonar.summary??>
- [BUG](${reports.sonar.link}&types=BUG): 阻断:${reports.sonar.summary.rows[0][1]} 严重:${reports.sonar.summary.rows[0][2]} 主要:${reports.sonar.summary.rows[0][3]}
- [源码安全问题](${reports.sonar.link}&types=VULNERABILITY): 阻断:${reports.sonar.summary.rows[1][1]} 严重:${reports.sonar.summary.rows[1][2]} 主要:${reports.sonar.summary.rows[1][3]}
- [编码规范问题](${reports.sonar.link}&types=CODE_SMELL): 阻断:${reports.sonar.summary.rows[2][1]} 严重:${reports.sonar.summary.rows[2][2]} 主要:${reports.sonar.summary.rows[2][3]}
</#if>
</#if>
</#list>
