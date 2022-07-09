app.controller('eventController', function($scope,$http,$window,$state,$filter,$modal,basicService) {
	$scope.condition={};
	$scope.entity={};
	basicService.getMembers().then(function(response){
		$scope.members=response.data;
	});
	basicService.getTeams().then(function(response){
		$scope.teams=response.data;
		jQuery("#table").bootstrapTable("refresh");
	});
	basicService.getPipelineProfile().then(function(response){
		$scope.profiles=response.data;
		jQuery("#table").bootstrapTable("refresh");
	});
	$scope.tableControl={
		options:{
            url:backend.url+"/api/event/page",
        	cache:false,
            idField:'id',
            queryParams:function(params){
            	var queryParam=angular.extend({},params,$scope.condition);
            	return queryParam;
            },
            columns: [{
                field:'state',
                checkbox:true //设置多选
            },
			{
            	field:'projectType',
            	title:'工程类型',
				formatter:function(value,row){
					let type = ''
					if(value=='all'&&!row.legacyProject){
						type = '全部';
					}else if(value==='java'&&!row.legacyProject){
						type = 'Java(Maven)';
					}else if(value=='gradle'){
						type = 'Java(Gradle)';
					}else if(value=='java'&&row.legacyProject){
						type = 'Java(其他)'
					}else if(value=='net'){
						type = '.net'
					}else if(value=='npm'){
						type = '前端'
					}else if(value=='initDeploy'){
                        type = '项目初始化部署'
					}else if(value=='dataBaseScript'){
						type = '数据库脚本'
					}
					return '<span title="'+type+'">'+type+'</span>' ;
				}
            },
			{
            	field:'eventType',
            	title:'事件类型',
				formatter:function(value){
					switch(value){
						case 'push':
							return "<a>代码提交</a>";
						case 'mergeRequest':
							return "<a>申请分支合并</a>";
						case 'merge':
							return "<a>分支合并成功</a>";
						case 'pushTag':
							return "<a>打标签</a>";
						case 'applyOnlineAllowed':
							return "<a>申请上线通过</a>";
						case 'applyOntestAllowed':
							return "<a>申请提测通过</a>";
						case 'scheduling':
							return "<a>测试机调度</a>";
					}
				},
				events:{
					'click a':function(e, value, row, index){
						$scope.viewDetail(row);
					}
				}
            },{
				field:'userName',
				title:'创建用户',
				align: 'center',
				valign: 'bottom',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
			},{
				field:'createDate',
				title:'创建时间',
				formatter:function(value){
					if(!value){
						return '--';
					}
					let dateTime = new Date(value).Format('yyyy-MM-dd hh:mm:ss');
                    return "<span title='"+dateTime+"'>" + dateTime + "</span>";
				}
			},{
				field:'teamId',
				title:'项目组',
				formatter:function(value){
					if($scope.teams){
						for(var i=0;i<$scope.teams.length;i++){
							if($scope.teams[i].id==value){
								return $scope.teams[i].name;
							}
						}
					}
					return '--';
				}
			},{
				field:'profileId',
				title:'构建过程',
				formatter:function(value){
					if($scope.profiles){
						for(var i=0;i<$scope.profiles.length;i++){
							if($scope.profiles[i].id==value){
								let showValue = $scope.profiles[i].name+"-"+$scope.profiles[i].teamName+"-"+$scope.profiles[i].nameSpace;
								return '<a title="'+showValue+'">'+showValue+'</a>' ; 
							}
						}
					}
					return '--';
				},
				events:{
					'click a':function(e, value, row, index){
						$scope.viewProfile(row);
					}
				}
			}, /*{
				field:'environmentType',
				title:'部署环境',
				formatter:function(value){
					if(!value){
						return '--';
					}
					switch(value){
						case 'dev':
							return "开发";
						case 'test':
							return "测试";
						case 'prod':
							return "正式";
					}
				}
			},*/{
				field:'sourceBranch',
				title:'来源分支',
				align: 'center',
				valign: 'bottom',
                formatter: function(value){
					if(!value){
						return '--';
					}
                    return '<span title="'+value+'">'+value+'</span>' ;
                }
			},{
				field:'targetBranch',
				title:'目标分支',
				align: 'center',
				valign: 'bottom',
                formatter: function(value){
					if(!value){
						return '--';
					}
                    return '<span title="'+value+'">'+value+'</span>' ;
                }
			},{
				field:'detail',
				title:'描述',
				align: 'center',
				valign: 'bottom',
                formatter: function(value){
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
	$scope.viewProfile= function(row){
		$http.get(backend.url+"/api/pipelineProfile/"+row.profileId).then(function(response){
			var modalInstance=$modal({
				title: '自定义构建配置',
				templateUrl: 'app/pipeline/profile-form.html',
				show: true,
				animation:'am-fade-and-scale',
				controller:function($scope,$sce){
					$scope.expression = true;
					$scope.cracker = response.data;
					basicService.getTeams().then(function(response){
						$scope.teams=response.data;
					});
					$scope.getNamespacesByTeam = function () {
						$http.get(backend.url+"/api/team/teamNameSpaces/" +$scope.cracker.teamId).then(function(response){
							$scope.teamNamespaces = response.data;
						});
					}
					if($scope.cracker.teamId){
						$scope.getNamespacesByTeam();
					}
                    $scope.closeModal=function(){
						modalInstance.destroy();
					}
				}
			});
		});
	}
	$scope.add=function(){
		$scope.entity={port:22};
		$state.go('event.add');
	}
	
	
	$scope.save=function(){
		$http.post(backend.url+"/api/event",$scope.entity).then(function(response){
			alert("保存成功");
			$state.go("event.list");
		});
	}
	
	$scope.viewDetail = function (row) {
		$scope.entity=angular.copy(row);
		if($scope.entity.team){
			$scope.entity.teamId=$scope.entity.team.id;
		}
		if($scope.entity.pipelineProfile){
			$scope.profileId =$scope.entity.pipelineProfile.id;
		}
		$scope.getProfilestByTeam();
		$state.go('event.edit');
	}
	
	$scope.back = function () {
		$state.go('event.list');
	}

	$scope.getProfilestByTeam=function(){
		$scope.teamProfiles = [];
		if($scope.profiles&&$scope.entity.teamId){
			for(var i=0;i<$scope.profiles.length;i++){
					if($scope.profiles[i].teamId==$scope.entity.teamId){
						$scope.teamProfiles.push($scope.profiles[i]);
					}
			}
		}
	}
	$scope.generateToken=function(){
		$http.post(backend.url+"/api/event/gen-token",$scope.entity).then(function(response){
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
				url:backend.url+"/api/event",
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

