package com.eazybuilder.ci.entity;

public enum ThresholdType {
	//门禁类型 bug_blocker(BUG)、、
	bug_blocker,
	//vulner_blocker(安全漏洞)
	vulner_blocker,
	//code_smell_blocker(编码规范)
	code_smell_blocker,
	//单元测试(成功率)
	unit_test_success_rate,
	//单元测试(覆盖率)
	unit_test_coverage_rate,
	//新增代码单元测试覆盖率
	new_unit_test_coverage_rate,
	//新增代码未覆盖行数
	new_uncovered_lines

}
