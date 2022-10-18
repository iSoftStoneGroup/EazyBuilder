app.controller('msgController', function($scope,$rootScope,$http,$window,$state,$filter,basicService,$modal) {
	$scope.condition={};
	$scope.entity={};
	basicService.getMembers().then(function(response){
		$scope.members=response.data;
	});
	basicService.getPipelineProfile().then(function(response){
		$scope.profiles=response.data;
	});
	$scope.tableControl = {
		options: {
			url: backend.url + "/api/deveops/getDeveopsPage",
			cache: false,
			idField: 'id',
			queryParams: function (params) {
				var queryParam = angular.extend({}, params, $scope.condition);
				return queryParam;
			},
			columns: [{
				field: 'state',
				checkbox: true //设置多选
			}, {
				field: 'teamName',
				title: '项目组',
				align: 'center',
				valign: 'bottom',
				sortable: true,
				formatter: function (value, row, index) {
					return "<a>" + value + "</a>";
				},
				events: {
					'click a': function (e, value, row, index) {
						$scope.viewDetail(row);
					}
				}
			}, {
				field: 'teamCode',
				title: '项目组编号',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
			}, {
				field: 'teamBeginDate',
				title: '项目组开始时间',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
			}, {
				field: 'teamEndDate',
				title: '项目组结束时间',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
			}],
			clickToSelect: true, //设置支持行多选
			search: true, //显示搜索框
			searchOnEnterKey: true,//enter时才search
			toolbar: '#toolbar', //关联工具栏
			showHeader: true,
			showColumns: true, //显示列
			showRefresh: true, //显示刷新按钮
			showToggle: true, //显示切换视图按钮
			showPaginationSwitch: true, //显示数据条数框
			pagination: true, //设置为 true 会在表格底部显示分页条
			paginationLoop: true, //设置为 true 启用分页条无限循环的功能。
			sidePagination: 'server', //设置在哪里进行分页，可选值为 'client' 或者 'server'。
			pageSize: 10,
			pageList: [10, 15, 20, 25, 50],
			paginationHAlign: 'right' //分页条位置
		}
	};

	$scope.add=function(){
		$scope.entity={ownerId:$rootScope.currentUserId};
		$scope.entity.teamThresholdMap = {};
		$state.go('team.add');
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
        $.ajax({
            url: backend.url+"/api/upms/getUpmsUsers?groupId="+row.groupId,
            async: false,
            method: "GET",
            success: function (data, status) {
                row.devopsUsers = JSON.parse(data);
            }
        });
		var modalInstance=$modal({
			title: '流水线执行详情',
			templateUrl: 'app/msg/form.html',
			show: true,
			animation:'am-fade-and-scale',
			controller:function($scope){
				$scope.entity=angular.copy(row);
				$scope.entity.msgType='mail';
				$scope.save=function(){
					$http.post(backend.url +"/api/deveops/init", $scope.entity).then(function (response) {
						alert("保存成功");
						jQuery("#table").bootstrapTable("refresh");
						modalInstance.destroy();
					});
				}
				$scope.closeModal=function(){
					modalInstance.destroy();
				}
			}
		});
	}

	$scope.back = function () {
		$state.go('msg.list');
	}
});

