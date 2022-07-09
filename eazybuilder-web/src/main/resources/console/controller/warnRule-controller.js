app.controller('warnRuleController', function($scope,$http,$window,$state,$filter,basicService) {
	$scope.condition={};
	$scope.entity={};
	basicService.getMembers().then(function(response){
		$scope.receivingUsers=response.data;
	});
	basicService.getTeams().then(function(response){
		$scope.scanTeams=response.data;
	});
	$scope.tableControl={
			options:{
	            url:backend.url+"/api/warnRule/page",
	        	cache:false,
	            idField:'id',
	            queryParams:function(params){
	            	var queryParam=angular.extend({},params,$scope.condition);
	            	return queryParam;
	            },
	            columns: [{
	                field:'state',
	                checkbox:true //设置多选
	            }, {
	                field: 'metricType',
	                title: '校验列',
	                align: 'center',
	                valign: 'bottom',
	                formatter:function(value){
	                	switch(value){
	                	case 'unit_test_success_rate':
	                		return '<a>单元测试(成功率)</a>';
	                	case 'unit_test_coverage_rate':
	                		return '<a>单元测试(覆盖率)</a>';
	                	case 'bug_blocker':
	                		return '<a>bug(阻断)</a>';
	                	case 'bug_critical':
	                		return '<a>bug(严重)</a>';
	                	case 'bug_major':
	                		return '<a>bug(主要)</a>';
	                	case 'vulner_blocker':
	                		return '<a>安全漏洞(阻断)</a>';
	                	case 'vulner_critical':
	                		return '<a>安全漏洞(严重)</a>';
	                	case 'vulner_major':
	                		return '<a>安全漏洞(主要)</a>';
	                	case 'code_smell_blocker':
	                		return '<a>编码规范(阻断)</a>';
	                	case 'code_smell_critical':
	                		return '<a>编码规范(严重)</a>';
	                	case 'code_smell_major':
	                		return '<a>编码规范(主要)</a>';
	                	case 'code_line':
	                		return '<a>代码行数</a>';
	                	case 'function_test_blocker':
	                		return '<a>功能自动化测试(阻断)</a>';
	                	case 'function_test_critical':
	                		return '<a>功能自动化测试(严重)</a>';
	                	case 'function_test_major':
	                		return '<a>功能自动化测试(主要)</a>';
	                	case 'dc_high':
	                		return '<a>依赖检查(高危)</a>';
	                	case 'dc_medium':
	                		return '<a>依赖检查(中等)</a>';
	                	default:
	                		return '';
	                	}
	                },
	                events:{
	                	'click a':function(e, value, row, index){
	                		$scope.viewDetail(row);
	                	}
	                }
	            },{
	            	field:'thresholdMin',
	            	title:'阈值（下限）',
	                align: 'center',
	                valign: 'bottom',
	                sortable: true,
					formatter:function(value){
						if(!value){
							return '--';
						}
						return '<span title="'+value+'">'+value+'</span>' ;
					}
	            },{
	            	field:'thresholdMax',
	            	title:'阈值（上限）',
	                align: 'center',
	                valign: 'bottom',
	                sortable: true,
					formatter:function(value){
						if(!value){
							return '--';
						}
						return '<span title="'+value+'">'+value+'</span>' ;
					}
	            }],
	            clickToSelect:true, //设置支持行多选
	            search:true, //显示搜索框
	            searchOnEnterKey:true,//enter时才search
	            toolbar:'#toolbar', //关联工具栏
	            showHeader:true,
	            showColumns:true, //显示列
	            showRefresh:true, //显示刷新按钮
	            showToggle:true, //显示切换视图按钮
	            showPaginationSwitch:true, //显示数据条数框
	            pagination:true, //设置为 true 会在表格底部显示分页条
	            paginationLoop:true, //设置为 true 启用分页条无限循环的功能。
	            sidePagination:'server', //设置在哪里进行分页，可选值为 'client' 或者 'server'。
	            pageSize:10,
	            pageList:[10,15,20,25,50],
	            paginationHAlign:'right' //分页条位置
			}	
		};
		
		$scope.add=function(){
			$scope.entity={};
			$state.go('warnRule.add');
		}
		
		$scope.save=function(){
			$http.post(backend.url+"/api/warnRule",$scope.entity).then(function(response){
				alert("保存成功");
				$state.go("warnRule.list");
			});
		}
		
		$scope.viewDetail = function (row) {
			$scope.entity=angular.copy(row);
			$state.go('warnRule.edit');
		}
		
		$scope.back = function () {
			$state.go('warnRule.list');
		}
		
		//删除
		$scope.remove= function(){
			var selected=jQuery("#table").bootstrapTable("getAllSelections")
			if(!selected||!selected.length){
				return;
			}
			
			var ids=[];
			for(var i=0;i<selected.length;i++){
				ids.push(selected[i].id);
			}
			if(confirm("确认删除选中项？")){
				$http({
					url:backend.url+"/api/warnRule",
					method:"delete",
					headers: {
	        	        'Content-type': 'application/json;charset=utf-8'
	        	    },
					data:ids
				}).then(function(){
					alert("删除成功");
					jQuery("#table").bootstrapTable("refresh");
				});
			}
		}
});