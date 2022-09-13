app.controller('projectManageController', function($scope,$http,$window,$state,$filter,$modal,$interval,$alert,basicService) {
	$scope.condition={};
	$scope.entity={};
	$scope.tableControl={
		options:{
			url:backend.url+"/api/projectManage/page",
			cache:false,
			idField:'id',
			queryParams:function(params){
				var queryParam=angular.extend({},params,$scope.condition);
				return queryParam;
			},
			columns: [{
				field:'state',
				checkbox:true //设置多选
			},{
				field: 'name',
				title: '配置名称',
				align: 'center',
				valign: 'bottom',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<a title="'+value+'">'+value+'</a>' ;
				},
				events:{
					'click a':function(e, value, row, index){
						$scope.viewDetail(row);
					}
				}
			},{
				field:'type',
				title:'配置类型',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
			},{
				field:'url',
				title:'需求管理平台配置',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
			},{
				field:'dbUrl',
				title:'数据库连接地址',
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
		$state.go('projectManage.add');
	}

	$scope.test=function(){
		$http.post(backend.url+"/api/projectManage/testConnection",$scope.entity).then(function(response){
			console.log(response);
			if(response.data){
				alert("连接数据库成功");
			}else{
				alert("连接数据库失败！！！");
			}
		});
	}


	$scope.save=function(){
		$http.post(backend.url+"/api/projectManage",$scope.entity).then(function(response){
			alert("保存成功");
			$state.go("projectManage.list");
		});
	}

	$scope.viewDetail = function (row) {
		$scope.entity=angular.copy(row);
		$state.go('projectManage.edit');
	}

	$scope.back = function () {
		$state.go('projectManage.list');
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
				url:backend.url+"/api/projectManage",
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

