app.controller('projectSvnScanController', function($scope,$interval,$http,$window,$state,$filter,$modal,basicService) {
	$scope.condition={};
	$scope.entity={charset:"utf-8",compileLevel:"1.8"};
	basicService.getPipelineProfile().then(function(response){
		  $scope.profiles=response.data;
	});
	basicService.getTeams().then(function(response){
		$scope.teams=response.data;
	});
	
	$scope.doScan=function(){
		if(!$scope.condition.url){
			return;
		}
		$scope.scanning=true;
		$http.post(backend.url+"/api/projectBatchImport/scanSvn",$scope.condition).then(function(response){
			$scope.taskUid=response.data.uid;
			$scope.progressUpdate=$interval($scope.getProgress,5000,-1);
		});
		
	}
	
	$scope.getProgress=function(){
		if($scope.taskUid){
		$http.get(backend.url+"/api/asyncTask/"+$scope.taskUid+"?r="+Math.random()).then(function(response){
			console.log(response.data);
			if(response.data&&response.data.log){
				$scope.log=response.data.log;
			}
			if(response.data.done){
				$scope.entity.projects=response.data.projects;
				jQuery("#table").bootstrapTable("load",response.data.projects);
				$interval.cancel($scope.progressUpdate);
				$scope.scanning=false;
			}
		});
		}
	}
	
	
	$scope.tableControl={
		options:{
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
            	field:'legacyProject',
            	title:'类型',
            	formatter:function(value){
            		if(value){
            			return 'ANT';
            		}else{
            			return 'Maven';
            		}
            	}
            },{
            	field:'srcPath',
            	title:'源代码路径',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
            },{
            	field:'libPath',
            	title:'lib路径',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
            }],
            clickToSelect:true, //设置支持行多选
            search:true, //显示搜索框
            toolbar:'#projectToolbar', //关联工具栏
            pagination:false //设置为 true 会在表格底部显示分页条
		}	
	};
	
	$scope.removeSelected=function(){
		var selected=jQuery("#table").bootstrapTable("getAllSelections")
		if(!selected||!selected.length){
			return;
		}
		
		var names=[];
		for(var i=0;i<selected.length;i++){
			names.push(selected[i].name);
		}
		var projects=[];
		for(var i=0;i<$scope.entity.projects.length;i++){
			var prj=$scope.entity.projects[i];
			var match=false;
			for(var j=0;j<names.length;j++){
				if(prj.name==names[j]){
					match=true;
					break;
				}
			}
			if(!match){
				projects.push(prj);
			}
		}
		if(confirm("确认删除选中项？")){
			$scope.entity.projects=projects;
			jQuery("#table").bootstrapTable("load",projects);
		}
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
		for(var i=0;i<$scope.entity.projects.length;i++){
			var project=$scope.entity.projects[i];
			project.team=$scope.entity.team;
			project.defaultProfile=$scope.entity.defaultProfile;
			project.codeCharset=$scope.entity.charset;
			project.jdk=$scope.entity.compileLevel;
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
	}
	
	$scope.back = function () {
		$state.go('project.list');
	}
	
});

