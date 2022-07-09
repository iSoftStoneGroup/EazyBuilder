app.controller('guardController', function($scope,$http,$window,$state,$filter,$modal,basicService) {
	$scope.condition={};
	$scope.entity={};
	$scope.guardTypes = guardTypes;




	basicService.getMembers().then(function(response){
		$scope.members=response.data;
	});
	basicService.getTeams().then(function(response){
		$scope.teams=response.data;
	});
	$scope.tableControl={
		options:{
            url:backend.url+"/api/guard/page",
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
                field: 'name',
                title: '名称',
                align: 'center',
                sortable: true,
                formatter:function(value,row,index){
                	return "<a>"+value+"</a>";
                },
                events:{
                	'click a':function(e, value, row, index){
                		$scope.viewDetail(row);
                	}
                }
            },{
            	field:'guardType',
            	title:'门禁类型',
				formatter:function(value){
					return $scope.guardTypes[value];
				}
            },{
				field:'thresholdMax',
				title:'阈值（上限）',
				align: 'center',
				valign: 'bottom',
				sortable: true,
				formatter:function(value, row){
					if(row.guardType){
						let suffix = row.guardType.includes("rate") ? "%": "";
						return `${value}${suffix}`;
					}
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
		$scope.entity={port:22};
		$state.go('guard.add');
	}
	
	
	$scope.save=function(){
		$http.post(backend.url+"/api/guard",$scope.entity).then(function(response){
			alert("保存成功");
			$state.go("guard.list");
		});
	}
	
	$scope.viewDetail = function (row) {
		$scope.entity=angular.copy(row);
		$state.go('guard.edit');
	}
	
	$scope.back = function () {
		$state.go('guard.list');
	}
	
	$scope.generateToken=function(){
		$http.post(backend.url+"/api/guard/gen-token",$scope.entity).then(function(response){
			$scope.apiToken=response.data;
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
				url:backend.url+"/api/guard",
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

