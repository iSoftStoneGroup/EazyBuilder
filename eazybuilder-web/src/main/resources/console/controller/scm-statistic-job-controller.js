app.controller('scmStatisticJobController', function($scope,$http,$window,$state,$filter,basicService) {
	$scope.condition={};
	$scope.entity={};
	$scope.tableControl={
		options:{
            url:backend.url+"/api/scmStatisticJob/page",
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
                title: '仓库名称',
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
                field: 'repoType',
                title: '仓库',
                align: 'center',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
            },{
            	field:'repoUrl',
            	title:'地址',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
            },{
            	field:'enable',
            	title:'状态',
            	formatter:function(value,row,index){
                	return value?'启用':'停用';
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
		$scope.entity={repoType:'git'};
		$state.go('scmStatisticJob.add');
	}
	
	$scope.save=function(){
		$http.post(backend.url+"/api/scmStatisticJob",$scope.entity).then(function(response){
			alert("保存成功");
			$state.go("scmStatisticJob.list");
		});
	}
	
	$scope.viewDetail = function (row) {
		$scope.entity=angular.copy(row);
		$state.go('scmStatisticJob.edit');
	}
	
	$scope.back = function () {
		$state.go('scmStatisticJob.list');
	}
	$scope.analysis=function(){
		var selected=jQuery("#table").bootstrapTable("getAllSelections")
		if(!selected||!selected.length){
			return;
		}
		var day=prompt('抓取多少天内的活动数据?',30);
		
		$http.get(backend.url+"/api/scmStatisticJob/collect?job="+selected[0].id+"&start="+new Date((new Date()-day*24*60*60*1000)).Format("yyyy-MM-dd")+"&end="+new Date().Format("yyyy-MM-dd")).then(function(response){
			alert('已向后台提交任务，稍后请查看统计图表');
		});
	}
	$scope.analysisByProject=function(){
		$http.get(backend.url+"/api/scmStatisticJob/collectByProject").then(function(response){
			alert('已向后台提交任务，稍后请查看统计图表');
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
				url:backend.url+"/api/scmStatisticJob",
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

