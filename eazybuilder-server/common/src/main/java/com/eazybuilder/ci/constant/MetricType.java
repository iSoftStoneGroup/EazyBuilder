package com.eazybuilder.ci.constant;

public enum MetricType {
	//单元测试(成功率)
	unit_test_success_rate,
	//单元测试(覆盖率)
	unit_test_coverage_rate,
	//bug
	bug_blocker,
	bug_critical,
	bug_major,
	//安全漏洞
	vulner_blocker,
	vulner_critical,
	vulner_major,
	//编码规范
	code_smell_blocker,
	code_smell_critical,
	code_smell_major,
	//代码行
	code_line,
	//功能自动化测试
	function_test_blocker,
	function_test_critical,
	function_test_major,
	//dependency check
	dc_high,
	dc_medium,
	//sql scan
	sql_imcompatible,
	//新增代码行
	new_lines,
	//技术债
	technology_debt,
	new_bugs,
	new_vulnerabilities,
	new_code_smells,
	new_technical_debt,
	new_unit_test_coverage_rate,
	new_uncovered_lines;

	/**
	 *
	 * @param thresholdVal
	 * @param actualVal
	 * @return
	 * 如果是单测相关的内容，阈值小于实际值，true是通过。
	 * 如果是数值
	 */
	public boolean compare(Double thresholdVal, Double actualVal){
		switch (this){
			case unit_test_success_rate:
			case unit_test_coverage_rate:
			case new_unit_test_coverage_rate:
				return Double.compare(thresholdVal,actualVal) <= 0;
			default:
				return Double.compare(thresholdVal,actualVal) >= 0;
		}
	}
}
