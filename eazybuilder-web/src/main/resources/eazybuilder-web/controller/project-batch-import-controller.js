app.controller('projectBatchImportController', function($scope,$http,$window,$state,$filter,$modal,basicService) {
	$scope.condition={};
	$scope.entity={projects:[],uploaded:false};
	$scope.saveResult=[];
	
	
	basicService.getPipelineProfile().then(function(response){
		  $scope.profiles=response.data;
	});
	basicService.getTeams().then(function(response){
		$scope.teams=response.data;
	});
	
	
	$scope.$on("uploaded",function(event,eventData){
		$scope.$apply(function () {
			$scope.entity.projects=eventData.data;
			$scope.entity.uploaded=true;
		});
		jQuery("#table").bootstrapTable("load",eventData);
	});
	
	$scope.tableControl={
		options:{
        	cache:false,
            idField:'id',
            queryParams:function(params){
            	var queryParam=angular.extend({},params,$scope.condition);
            	return queryParam;
            },
            columns: [{
                field: 'name',
                title: '工程缩写/英文',
                align: 'center',
                sortable: true,
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
            },{
            	field:'description',
            	title:'中文名称',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
            },{
            	field:'scm.url',
            	title:'源码仓库地址',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
            }],
            clickToSelect:true, //设置支持行多选
            search:false, //显示搜索框
            toolbar:'#toolbar', //关联工具栏
            pagination:false //设置为 true 会在表格底部显示分页条
		}	
	};
	
	
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
		for(var i=0;i<$scope.entity.projects.length;i++){
			var project=$scope.entity.projects[i];
			project.team=$scope.entity.team;
			project.defaultProfile=$scope.entity.defaultProfile;
		}
		$http.post(backend.url+"/api/projectBatchImport",$scope.entity.projects)
				.then(function(response){
			var modalInstance=$modal({
				  title: '批量导入结果', 
				  templateUrl: 'app/project/import-result.html', 
			      show: true,
			      animation:'am-fade-and-scale',
			      controller:function($scope){
			    	  $scope.saveResult=response.data;
			    	  $scope.closeModal=function(){
			    		  modalInstance.destroy();
			    	  }
		          }        
				}
			);
		});
		
		/*$http.post(backend.url+"/api/project",$scope.entity).then(function(response){
			alert("保存成功");
			$state.go("project.list");
		});*/
	}
	
	$scope.back = function () {
		$state.go('project.list');
	}
	
});

