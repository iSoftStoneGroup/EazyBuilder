app.controller('warnController', function($scope,$http,$window,$state,$filter,basicService) {
	$scope.condition={};
	$scope.entity={};
	basicService.getMembers().then(function(response){
		$scope.receivingUsers=response.data;
	});
	basicService.getTeams().then(function(response){
		$scope.scanTeams=response.data;
	});
	basicService.getProjectGroups().then(function(response){
		$scope.scanGroups=response.data;
	});
	basicService.getWarnRules().then(function(response){
		$scope.warnRules=response.data;
	});
	$scope.tableControl={
			options:{
	            url:backend.url+"/api/warn/page",
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
	                field: 'isEnable',
	                title: '是否启用',
	                align: 'center',
	                valign: 'bottom',
	                sortable: true,
	                formatter:function(value,row,index){
	                	if(!value){
	                		return "<a>未启用</a>";
	                	}
	                	return "<a>已启用</a>";
	                },
	                events:{
	                	'click a':function(e, value, row, index){
	                		$scope.viewDetail(row);
	                	}
	                }
	            },{
	            	field:'receivingUsers',
	            	title:'报告接收人',
	            	formatter:function(devs){
	            		if(!devs||devs.length==0){
	            			return '';
	            		}
	            		var names=[];
	            		for(var i=0;i<devs.length;i++){
	            			names.push(devs[i].name);
	            		}
	            		return names.join(',');
	            	}
	            },{
	            	field:'scanGroups',
	            	title:'扫描团队',
	            	formatter:function(devs){
	            		if(!devs||devs.length==0){
	            			return '';
	            		}
	            		var names=[];
	            		for(var i=0;i<devs.length;i++){
	            			names.push(devs[i].name);
	            		}
	            		return names.join(',');
	            	}
	            }, {
	                field: 'warnType',
	                title: '报告类型',
	                align: 'center',
	                valign: 'bottom',
	                sortable: true,
					formatter: function (value) {
						if (!value) {
							return "--";
						}
						switch (value) {
							case 'gao_liang_all_code':
								return "高亮预警项+全部代码质量汇总";
							case 'all_code':
								return "全部代码质量汇总";
							case 'du_liang_report':
								return "度量报告";
							default:
								return "--";
						}
					}
	            }, {
	                field: 'cron',
	                title: '时间计划',
	                align: 'center',
	                valign: 'bottom',
	                sortable: false,
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
			$state.go('warn.add');
		}
		
		$scope.save=function(){
			var receivingUsers=[];
			if($scope.entity.receivingUserIds){
				for(var i=0;i<$scope.entity.receivingUserIds.length;i++){
					for(var j=0;j<$scope.receivingUsers.length;j++){
						if($scope.entity.receivingUserIds[i]==$scope.receivingUsers[j].id){
							receivingUsers.push($scope.receivingUsers[j]);
						}
					}
				}
			}
			$scope.entity.receivingUsers=receivingUsers;
			
			var scanGroups=[];
			if($scope.entity.scanGroupIds){
				for(var i=0;i<$scope.entity.scanGroupIds.length;i++){
					for(var j=0;j<$scope.scanGroups.length;j++){
						if($scope.entity.scanGroupIds[i]==$scope.scanGroups[j].id){
							scanGroups.push($scope.scanGroups[j]);
						}
					}
				}
			}
			$scope.entity.scanGroups=scanGroups;
			
			var warnRules=[];
			if($scope.entity.warnRuleIds){
				for(var i=0;i<$scope.entity.warnRuleIds.length;i++){
					for(var j=0;j<$scope.warnRules.length;j++){
						if($scope.entity.warnRuleIds[i]==$scope.warnRules[j].id){
							warnRules.push($scope.warnRules[j]);
						}
					}
				}
			}
			$scope.entity.warnRules=warnRules;
			$http.post(backend.url+"/api/warn",$scope.entity).then(function(response){
				alert("保存成功");
				$state.go("warn.list");
			});
		}
		
		$scope.viewDetail = function (row) {
			$scope.entity=angular.copy(row);
			
			if($scope.entity.receivingUsers&&$scope.receivingUsers){
				var receivingUserIds=[];
				for(var i=0;i<$scope.entity.receivingUsers.length;i++){
					for(var j=0;j<$scope.receivingUsers.length;j++){
						if($scope.entity.receivingUsers[i].id==$scope.receivingUsers[j].id){
							receivingUserIds.push($scope.receivingUsers[j].id);
						}
					}
				}
				$scope.entity.receivingUserIds=receivingUserIds;
			}
			
			if($scope.entity.scanGroups&&$scope.scanGroups){
				var scanGroupIds=[];
				for(var i=0;i<$scope.entity.scanGroups.length;i++){
					for(var j=0;j<$scope.scanGroups.length;j++){
						if($scope.entity.scanGroups[i].id==$scope.scanGroups[j].id){
							scanGroupIds.push($scope.scanGroups[j].id);
						}
					}
				}
				$scope.entity.scanGroupIds=scanGroupIds;
			}
			
			if($scope.entity.warnRules&&$scope.warnRules){
				var warnRuleIds=[];
				for(var i=0;i<$scope.entity.warnRules.length;i++){
					for(var j=0;j<$scope.warnRules.length;j++){
						if($scope.entity.warnRules[i].id==$scope.warnRules[j].id){
							warnRuleIds.push($scope.warnRules[j].id);
						}
					}
				}
				$scope.entity.warnRuleIds=warnRuleIds;
			}
			$state.go('warn.edit');
		}
		
		$scope.back = function () {
			$state.go('warn.list');
		}
		
		$scope.restartJobs=function(){
			$http.post(backend.url+"/api/warn/restartJobs",$scope.entity).then(function(response){
				alert("重启成功");
			});
		}
		
		$scope.triggerJob=function(){
			var selected=jQuery("#table").bootstrapTable("getAllSelections")
			if(!selected||!selected.length){
				return;
			}
			$http.post(backend.url+"/api/warn/"+selected[0].id,{}).then(function(response){
				alert("已提交后台执行");
			});
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
					url:backend.url+"/api/warn",
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