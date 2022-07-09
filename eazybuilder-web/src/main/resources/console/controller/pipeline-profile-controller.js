app.controller('pipelineProfileController', function($scope,$http,$window,$state,$modal,$filter,basicService) {
	$scope.condition={};
	$scope.cracker={};
	basicService.getTeams().then(function(response){
		$scope.teams=response.data;
	});
	
	basicService.getHosts().then(function(response){
		$scope.hosts=response.data;
	});
	$scope.tableControl={
		options:{
            url:backend.url+"/api/pipelineProfile/page",
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
                sortable: true,
                formatter:function(value){
                	if(!value){
                		return '--';
					}
                	return "<a>"+value+"</a>";
                },
                events:{
                	'click a':function(e, value, row, index){
                		$scope.viewDetail(row);
                	}
                }
            },{
				field:'teamName',
				title:'项目组',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
			},{
				field:'nameSpace',
				title:'命名空间',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
			},{
            	field:'publicProfile',
            	title:'类型',
            	formatter:function(value){
            		if(value){
            			return "公开";
            		}
            		return "私有";
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

	$scope.viewHistory=function(){
		var selected=jQuery("#table").bootstrapTable("getAllSelections")
		if(!selected || !selected.length){
			alert("请选择要查看的构建过程");
			return;
		}
		if(selected.length>1){
			alert("只能查看单个构建过程的历史数据");
			return;
		}
		var entity=angular.copy(selected[0]);
		var dataId=selected[0].id;
		var dockerRegistrys = $scope.dockerRegistrys;
		var profiles= $scope.profiles;
		var teams = $scope.teams;
		var modalInstance=$modal({
			title: '查看数据历史变更',
			templateUrl: 'app/pipelineProfile/history.html',
			width:'800',
			show: true,
			animation:'am-fade-and-scale',
			controller:function($scope,$rootScope){
				$scope.closeModal=function(){
					modalInstance.destroy();
				}
				$scope.compareToSelected = function(){
					var selectedCompare=jQuery("#historyTable").bootstrapTable("getAllSelections")
					if(!selectedCompare || !selectedCompare.length){
						alert("请选择两个历史数据进行比对");
						return;
					}
					if(selectedCompare.length!=2){
						alert("只能选择两个历史数据进行比对");
						return;
					}
					var sourceProject=angular.copy(selectedCompare[0]);
					var targetProject=angular.copy(selectedCompare[1]);

					var compareInstance=$modal({
						title: '历史数据对比',
						templateUrl: 'app/project/compare.html',
						height:'65vh',
						show: true,
						animation:'am-fade-and-scale',
						controller:function($scope,$rootScope){
							$scope.left=JSON.stringify(sourceProject);
							$scope.right=JSON.stringify(targetProject);
							let isCompareWithLocal = true;
							let mergeView;
							$scope.viewMarge = function(){
								mergeView= CodeMirror.k_init('viewMarge',JSON.stringify(targetProject).jsonFormat(),JSON.stringify(sourceProject).jsonFormat(),isCompareWithLocal);
							}

							$scope.closeModal=function(){
								compareInstance.destroy();
							}
						}
					})
				}
				$scope.switchToSelected=function(){
					var selectedHistory=jQuery("#historyTable").bootstrapTable("getAllSelections")
					if(!selectedHistory || !selectedHistory.length){
						alert("请选择要切换的的项目数据");
						return;
					}
					if(selectedHistory.length>1){
						alert("只能选择单个项目的历史数据");
						return;
					}
					$http.post(backend.url+"/api/pipelineProfile/recover",selectedHistory[0]).then(function(response){
						alert("切换成功");
						modalInstance.destroy();
						jQuery("#table").bootstrapTable("refresh");
					});
				}
				$scope.viewHistoryDetail=function(row){
					var modalInstance=$modal({
						title: '查看数据历史变更',
						templateUrl: 'app/pipeline/profile-form.html',
						width:'800',
						show: true,
						animation:'am-fade-and-scale',
						controller:function($scope,$rootScope){
							$scope.getNamespacesByTeam = function () {
								$http.get(backend.url+"/api/team/teamNameSpaces/" +$scope.cracker.teamId).then(function(response){
									$scope.teamNamespaces = response.data;
								});
							}
							$scope.expression = true;
							$scope.dockerRegistrys = dockerRegistrys;
							$scope.profiles = profiles;
							$scope.teams = teams;
							$scope.cracker=JSON.parse(row.jsonData);
							
							if($scope.cracker.teamId){
								$scope.getNamespacesByTeam();
							}
							$scope.closeModal=function(){
								modalInstance.destroy();
							}
						}
					});
				}
				$scope.tableControlHistory={
					options:{
						url:backend.url+"/api/pipelineProfile/history/?profileId="+dataId,
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
							field:'profileName',
							title:'配置名称',
							formatter:function(value,row,index){
								return "<a>"+value+"</a>";
							},
							events:{
								'click a':function(e, value, row, index){
									$scope.viewHistoryDetail(row);
								}
							},
						},{
							field:'teamName',
							title:'项目组'
						},{
							field:'nameSpace',
							title:'命名空间'
						},{
							field:'updateTime',
							title:'修改时间',
							sortable: true,
							formatter:function(value){
								return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
							}

						},{
							field:'updateUser',
							title:'修改人'

						},],
						clickToSelect:false, //设置支持行多选
						search:false, //显示搜索框
						searchOnEnterKey:false,//enter时才search
						toolbar:'#historyToolbar', //关联工具栏
						showHeader:true,
						showColumns:false, //显示列
						showRefresh:false, //显示刷新按钮
						showToggle:false, //显示切换视图按钮
						showPaginationSwitch:false, //显示数据条数框
						pagination:true, //设置为 true 会在表格底部显示分页条
						paginationLoop:false, //设置为 true 启用分页条无限循环的功能。
						sidePagination:'server', //设置在哪里进行分页，可选值为 'client' 或者 'server'。
						pageSize:10,
						pageList:[10,15,20,25,50],
						paginationHAlign:'right' //分页条位置
					}
				};
			}
		});
	}

	$scope.add = function () {
		let cracker = {
			publicProfile: false,
			namespace: 'dev',
			sendMailSwitch: 'TOTAL',
			teamId: '1'
		};
		$scope.viewDetail(cracker);
	}
	$scope.viewDetail = function (row) {
		var modalInstance=$modal({
			title: '流水线执行详情',
			templateUrl: 'app/pipeline/profile-form.html',
			show: true,
			animation:'am-fade-and-scale',
			controller:function($scope,$sce){
				$scope.expression = false;
				$scope.cracker=angular.copy(row);
				$scope.teamNamespaces = []
				$scope.getNamespacesByTeam = function () {
					$http.get(backend.url+"/api/team/teamNameSpaces/" +$scope.cracker.teamId).then(function(response){
						$scope.teamNamespaces = response.data;
					});
				}
				$scope.$on("uploaded",function(event,eventData){
					$scope.cracker.sqlTakeMinUrl=eventData.data.id;
				});
				basicService.getTeams().then(function(response){
					$scope.teams=response.data;
				});
				if($scope.cracker.teamId){
					$scope.getNamespacesByTeam();
				}
				
				$scope.save=function(){
					if($scope.cracker.publicProfile){
						$scope.cracker.teamId=null;
					}
					if($scope.cracker.teamId){
						for(var i=0;i<$scope.teams.length;i++){
							if($scope.cracker.teamId==$scope.teams[i].id){
								$scope.cracker.teamName=$scope.teams[i].name;
								break;
							}
						}
					}
					$http.post(backend.url+"/api/pipelineProfile",$scope.cracker).then(function(response){
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
		$state.go('pipelineProfile.list');
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
				url:backend.url+"/api/pipelineProfile",
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

