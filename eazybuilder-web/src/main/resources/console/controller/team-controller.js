app.controller('teamController', function($scope,$rootScope,$http,$window,$state,$filter,basicService) {
	$scope.condition={};
	$scope.entity={};
	basicService.getMembers().then(function(response){
		$scope.members=response.data;
	});
	basicService.getPipelineProfile().then(function(response){
		$scope.profiles=response.data;
	});
	$http.get(backend.url+"/api/guard/getGuardMap").then(function(response){
		$scope.guardMap = response.data;
	});

	$scope.guardTypes = guardTypes;
	$scope.actionScopes = actionScopes;
	$scope.actionScopeGuardTypes = actionScopeGuardTypes;

	$scope.tableControl={
		options:{
            url:backend.url+"/api/team/page",
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
                valign: 'bottom',
                sortable: true,
                formatter:function(value){
                	if(!value){
                		return '--';
					}
                	return "<a title='"+value+"'>"+value+"</a>";
                },
                events:{
                	'click a':function(e, value, row, index){
                		$scope.viewDetail(row);
                	}
                }
            },{
            	field:'members',
            	title:'成员',
            	formatter:function(devs){
            		if(!devs||devs.length==0){
            			return '';
            		}
            		var names=[];
            		if(devs.length>15){
            			for(var i=0;i<15;i++){
                			names.push(devs[i].name);
                		}
            			return names.join(',')+'等'+devs.length+"位成员";
            		}
            		for(var i=0;i<devs.length;i++){
            			names.push(devs[i].name);
            		}
            		return names.join(',');
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
		$scope.entity={ownerId:$rootScope.currentUserId};
		$scope.entity.teamThresholdMap = {};
		$scope.initData();
		$state.go('team.add');
	}

	$scope.initData = function(){
		Object.keys($scope.actionScopeGuardTypes).forEach(function(actionScope){
			let guardMapList = $scope.actionScopeGuardTypes[actionScope];
			let thresholdScopeList = $scope.entity.teamThresholdMap[actionScope];
			$scope.entity.teamThresholdMap[actionScope] = thresholdScopeList == null ? {} : thresholdScopeList;
			guardMapList.forEach(function(thresholdType,index){
				let threshold = $scope.entity.teamThresholdMap[actionScope][thresholdType];
				$scope.entity.teamThresholdMap[actionScope][thresholdType] =threshold == null ? {
					threSholdType :	thresholdType,
					actionScope: actionScope
				}: threshold;
			})
		});
	}
	
	$scope.save=function(){
		$scope.entity.teamThresholds  = Object.values($scope.entity.teamThresholdMap)
			.map(guardMap=> Object.values(guardMap)).flat().filter(teamThreshold=> teamThreshold.blockerId !=null);
		$http.post(backend.url+"/api/team/updateTeamThresholds",$scope.entity).then(function(response){
			alert("保存成功");
			$state.go("team.list");
		});
	}
	
	$scope.viewDetail = function (row) {
		$scope.entity=angular.copy(row);
		$scope.entity.partView = 'CODE_PUSH';
		let teamThresholdMap = {};
		$scope.entity.teamThresholds.forEach(function (threshold) {
			if(!teamThresholdMap[threshold.actionScope]){
				teamThresholdMap[threshold.actionScope] = {};
			}
			teamThresholdMap[threshold.actionScope][threshold.threSholdType] = threshold;
		});
		$scope.entity.teamThresholdMap = teamThresholdMap;
		$scope.initData();
		$state.go('team.edit');
	}
	
	$scope.back = function () {
		$state.go('team.list');
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
				url:backend.url+"/api/team",
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

