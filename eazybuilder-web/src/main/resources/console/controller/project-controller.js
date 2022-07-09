app.controller('projectController', function($scope,$http,$window,$state,$filter,$modal,basicService) {
	$scope.condition={};
	$scope.entity={};
	basicService.getDockerRegistrys().then(function(response){
		$scope.dockerRegistrys=response.data;
	});
	basicService.getPipelineProfile().then(function(response){
		  $scope.profiles=response.data;
	});
	basicService.getTeams().then(function(response){
		$scope.teams=response.data;
	});
	$scope.addQueryParam=function(data){
		if(!data){
			data=[];
		}
		data.push({deployConfigDetailEnvs:[]});
	}
	$scope.delQueryParam=function(index,data){
		data.splice(index, 1);
	}
	$scope.viewDeployConfigDetail = function (index,deployConfig) {
		var entity = $scope.entity;
		var deployConfig = deployConfig;
		var modalInstance = $modal({
				templateUrl: 'app/alertModel/alertDeployConfigDetail.html',
				show: true,
				animation: 'am-fade-and-scale',
				controller: function ($scope) {
					$scope.deployConfig = deployConfig;
					$scope.entity = entity;
					$scope.addModelQueryParam=function(data){
						if(!data){
							data=[];
						}
						data.push({});
					}
					$scope.delModelQueryParam=function(index,data){
						data.splice(index, 1);
					}
					$scope.closeModal = function () {
						modalInstance.destroy();
					}
					//保存
					$scope.confirmAdd=function(){
						$scope.entity.deployConfigList[index]=$scope.deployConfig
						alert("保存成功");
						modalInstance.destroy();
					}
				}
			}
		);
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
	$scope.viewHistory=function(){
		var selected=jQuery("#table").bootstrapTable("getAllSelections")
		if(!selected || !selected.length){
			alert("请选择要查看的项目");
			return;
		}
		if(selected.length>1){
			alert("只能查看单个项目的历史数据");
			return;
		}
		var entity=angular.copy(selected[0]);
		var dataId=selected[0].id;
			var dockerRegistrys = $scope.dockerRegistrys;
			var profiles= $scope.profiles;
			var teams = $scope.teams;
		var modalInstance=$modal({
			title: '查看数据历史变更',
			templateUrl: 'app/project/history.html',
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
					$http.post(backend.url+"/api/project/recover",selectedHistory[0]).then(function(response){
						alert("切换成功");
						modalInstance.destroy();
						jQuery("#table").bootstrapTable("refresh");
					});
				}
				$scope.viewHistoryDetail=function(row){
					var modalInstance=$modal({
						title: '查看数据历史变更',
						templateUrl: 'app/project/historyDetail.html',
						width:'800',
						show: true,
						animation:'am-fade-and-scale',
						controller:function($scope,$rootScope){
							$scope.dockerRegistrys = dockerRegistrys;
							$scope.profiles = profiles;
							$scope.teams = teams;
							$scope.entity=JSON.parse(row.jsonData);
							$scope.closeModal=function(){
								modalInstance.destroy();
							}
						}
					});
				}
				$scope.tableControlHistory={
					options:{
						url:backend.url+"/api/project/history/?projectId="+dataId,
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
							field:'projectName',
							title:'中文名称',
							formatter:function(value){
								if(!value){
									return '--';
								}
								return "<a>"+value+"</a>";
							},
							events:{
								'click a':function(e, value, row, index){
									$scope.viewHistoryDetail(row);
								}
							},
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
							field:'updateTime',
							title:'修改时间',
							sortable: true,
							formatter:function(value){
								if(!value){
									return '--';
								}
								return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
							}

						},{
							field:'updateUser',
							title:'修改人',
							formatter:function(value){
								if(!value){
									return '--';
								}
								return '<span title="'+value+'">'+value+'</span>' ;
							}
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

    $scope.tableControl={
		options:{
            url:backend.url+"/api/project/page",
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
                title: '工程缩写/英文',
                align: 'center',
                sortable: true,
                formatter:function(value,row,index){
                	return "<a title='"+value+"'>"+value+"</a>";
                },
                events:{
                	'click a':function(e, value, row, index){
                		$scope.viewDetail(row);
                	}
                }
            },{
            	field:'description',
            	title:'中文名称',
				formatter:function(value){
					return "<span title='"+value+"'>"+value+"</span>"
				}
            },{
            	field:'team',
            	title:'项目组',
            	formatter:function(team){
            		if(team){
            		    return team.name;
            		}else{
            			return '--';
            		}
            	}
            },{
            	field:'deployInfo',
            	title:'访问地址',
            	formatter:function(value){
            		if(!value){
            			return '--';
            		}
            		return value.url;
            	}
            },{
            	field:'scm',
            	title:'源码仓库',
            	formatter:function(value){
            		if(!value){
            			return '--';
            		}
            		return value.type;
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
		$scope.entity={legacyProject:false,eazybuilderEjbProject:true,deployConfigList:[]};
		$state.go('project.add');
	}
	
	$scope.batchImport=function(){
		$state.go('projectBatchImport');
	}
	
	$scope.scanSvn=function(){
		$state.go('projectSvnScan');
	}
	
	$scope.save=function(){
		if($scope.entity.teamId){
			for(var i=0;i<$scope.teams.length;i++){
			    if($scope.entity.teamId==$scope.teams[i].id){
			    	$scope.entity.team=$scope.teams[i];
			    	break;
			    }
			}
		}
		if($scope.entity.defaultProfileId){
			for(var i=0;i<$scope.profiles.length;i++){
			    if($scope.entity.defaultProfileId==$scope.profiles[i].id){
			    	$scope.entity.defaultProfile=$scope.profiles[i];
			    	break;
			    }
			}
		}else{
			$scope.entity.defaultProfile=null;
		}
		var regEn = /[`~!@#$%^&*()+<>?:"{},.\/;'[\]]/im,
	    regCn = /[·！#￥（——）：；“”‘、，|《。》？、【】[\]]/im;

		if(regEn.test($scope.entity.name) || regCn.test($scope.entity.name)) {
			alert("名称不能包含特殊字符.");
		}else{
			$http.post(backend.url+"/api/project",$scope.entity).then(function(response){
				alert("保存成功");
				$state.go("project.list");
			});
		}
		
	}
	
	$scope.viewDetail = function (row) {
		$scope.entity=angular.copy(row);
		if($scope.entity.team){
			$scope.entity.teamId=$scope.entity.team.id;
		}
		if($scope.entity.defaultProfile){
			$scope.entity.defaultProfileId=$scope.entity.defaultProfile.id;
		}
		$scope.getProfilestByTeam();
		$state.go('project.edit');
	}
	
	$scope.back = function () {
		$state.go('project.list');
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
				url:backend.url+"/api/project",
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
	
	$scope.showLegacyNotice=function(){
		var modalInstance=$modal({
			  title: '非Maven项目注意事项', 
			  templateUrl: 'app/project/legacy-project.html', 
		      show: true,
		      animation:'am-fade-and-scale',
		      controller:function($scope){
		    	  $scope.closeModal=function(){
		    		  modalInstance.destroy();
		    	  }
	          }        
			}
		);
	}
	
});

