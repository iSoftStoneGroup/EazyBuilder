app.controller('jobController', function($scope,$http,$window,$state,$interval,$filter,$modal,$location,basicService) {
	$scope.condition={};
	$scope.entity={projects:[]};
	$scope.baseUrl=$location.protocol()+"://"+$location.host()+":"+$location.port();

	basicService.getOnlineTags().then(function(response){
		$scope.onLineTags=response.data;
	});

	basicService.getProjects().then(function(response){
		$scope.projects=response.data;
	});
	basicService.getPipelineProfile().then(function(response){
		  $scope.profiles=response.data;
	});
	basicService.getTeams().then(function(response){
		$scope.teams=response.data;
	});

	$scope.searchTeamJobs = function(){
		if($scope.entity.teamId &&  $scope.entity.triggerType=='watch_job_executed'){
			$http.get(backend.url + "/api/job/findByQueryDlsList?teamId=" + $scope.entity.teamId).then(function(response){
				$scope.teamJobs = response.data;
			});
		}
	}
	
	$scope.tableControl={
		options:{
            url:backend.url+"/api/job/page",
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
            	field:'name',
            	title:'任务说明',
            	formatter:function(value,row,index){
            		if(!value){
                       return '--';
					}
                	return "<a title='"+ value+"'>"+value+"</a>";
                },
                events:{
                	'click a':function(e, value, row, index){
                		$scope.viewDetail(row);
                	}
                }
            		
            },{
                field: 'triggerType',
                title: '执行方式',
                align: 'center',
                formatter:function(value,row){
                	switch(value){
                	case 'cron':
                	return "时间计划["+row.cron+"]";
                	case 'git_web_hook':
                	return "Git钩子触发";
                	case 'manual':
                		return "人工执行";
                	case 'watch_job_executed':
                        return '监听其他任务';
                	}
                }
            },{
            	field:'projects',
            	title:'工程/项目',
            	formatter:function(value){
            		if(value&&value.length){
            			var names=[];
            			if(value.length>5){
            				for(var i=0;i<5;i++){
            					names.push(value[i].name);
            				}
            				return names.join(",")+"等"+value.length+"个工程";
            			}
            			for(var i=0;i<value.length;i++){
            				names.push(value[i].name);
            			}
						return '<span title="'+names.join(",")+'">'+names.join(",")+'</span>' ;
            		}
            		return '--';
            	}
            },{
            	field: 'name',
                title: '当前构建状态',
                align: 'center',
                sortable: true,
                formatter:function(value,row,index){
                	return "<span class='buildStatus' url='"+backend.url+'/api/job/status?id='+row.id+"'>未执行</span>";
                }
            },{
            	field: 'lastTrigger',
                title: '上次执行',
                align: 'center',
                formatter:function(value){
                	if(!value){
                		return '--';
                	}
					let dateTime = new Date(value).Format('yyyy-MM-dd hh:mm:ss');
                    return "<span title='"+dateTime+"'>" + dateTime + "</span>";
                }
            }
            ,{
            	field: 'nextTime',
                title: '下次执行时间',
                align: 'center',
                formatter:function(value){
                	if(value){
						let dateTime = new Date(value).Format('yyyy-MM-dd hh:mm:ss');
						return "<span title='"+dateTime+"'>" + dateTime + "</span>";
                	}else{
                		return '--';
					}
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
	
	//每30秒刷新一次构建状态
	$interval(function(){
		jQuery("#table").find("span.buildStatus").each(function(){
			var element=jQuery(this);
			var url=element.attr("url");
			$http.get(url+"&t="+new Date().getTime()).then(function(response){
				var statusText='未执行'
				if(response.data=='IN_PROGRESS'){
					statusText='执行中'
				}
				element.text(statusText);
			});
		});
	},30000,-1);

	$scope.getProjectByOnLineTag=function() {
		$http.get(backend.url + "/api/onLine/getProjectByOnLineTag?onLineId=" + $scope.entity.onLineId).then(function(response){
			$scope.entity.projects = response.data;
		});
	}

	$scope.add=function(){
		$scope.entity={projects:[],dingtalkWebHook:{}};
		$state.go('job.add');
	}
	
	$scope.showAddModel=function(env){
		var projects=$scope.entity.projects;
		var modalInstance=$modal({
			  title: '选择测试脚本', 
			  templateUrl: 'app/job/projectModal.html', 
		      show: true,
		      animation:'am-fade-and-scale',
		      controller:function($scope){
		    	  if(projects){
		    		  $scope.selectedProjects=angular.copy(projects);
		    	  }else{
		    	  	  $scope.selectedProjects=[];
		      	  }
		    	  var excludes=[];
		    	  for(var i=0;i<$scope.selectedProjects.length;i++){
		    		  excludes.push($scope.selectedProjects[i].id);
		    	  }
		    	  $scope.condition={excludes:angular.toJson(excludes)};
		    	  
		    	  $scope.projectTableControl={
		    				options:{
		    		            url:backend.url+"/api/project/pageWithExcludes",
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
		    		                title: '工程',
		    		                align: 'center'
		    		            },{
		    		            	field:'description',
		    		            	title:'备注'
		    		            }],
		    		            clickToSelect:true, //设置支持行多选
		    		            search:true, //显示搜索框
		    		            searchOnEnterKey:true,//enter时才search
		    		            toolbar:'#toolbar', //关联工具栏
		    		            showHeader:true,
		    		            showColumns:false, //显示列
		    		            showRefresh:false, //显示刷新按钮
		    		            showToggle:false, //显示切换视图按钮
		    		            showPaginationSwitch:false, //显示数据条数框
		    		            pagination:true, //设置为 true 会在表格底部显示分页条
		    		            paginationLoop:true, //设置为 true 启用分页条无限循环的功能。
		    		            sidePagination:'server', //设置在哪里进行分页，可选值为 'client' 或者 'server'。
		    		            pageSize:10,
		    		            pageList:[10,15,20,25,50],
		    		            paginationHAlign:'right' //分页条位置
		    				}	
		    			};
		    	  
		    	  
		    	  $scope.closeModal=function(){
		    		  modalInstance.destroy();
		    	  }
		    	  //保存
		    	  $scope.confirmAdd=function(){
		    		  var selected=jQuery("#projectTable").bootstrapTable("getAllSelections")
		    		  if(!selected||!selected.length){
		    				return;
		    		  }
		    		  for(var i=0;i<selected.length;i++){
		    			  projects.push(selected[i]);
		    		  }
		    		  modalInstance.destroy();
		    	  }
	          }        
			}
		);
	}
	$scope.updateSelection=function(event,envId){
		if(!envId){
			return;
		}
		$scope.selectedProject=envId;
	}
	
	$scope.isSelected=function(id){
		return $scope.selectedProject==id;
	}
	
	$scope.moveUp=function(){
		if($scope.entity.projects&&$scope.selectedProject){
			for(var i=0;i<$scope.entity.projects.length;i++){
				if($scope.entity.projects[i].id==$scope.selectedProject){
					zIndexDown($scope.entity.projects,i);
					return;
				}
			}
		}
	}
	
	$scope.moveDown=function(){
		if($scope.entity.projects&&$scope.selectedProject){
			for(var i=0;i<$scope.entity.projects.length;i++){
				if($scope.entity.projects[i].id==$scope.selectedProject){
					zIndexUp($scope.entity.projects,i);
					return;
				}
			}
		}
	}
	
	function swapArray(arr, index1, index2) {
		var temp=arr[index1];
		arr[index1]=arr[index2];
		arr[index2]=temp;
		return arr;
	}

	function zIndexUp(arr,index){
		if(index+1 != arr.length){
			swapArray(arr, index, index+1);
		}
	}

	function zIndexDown(arr,index){
		if(index!= 0){
			swapArray(arr, index, index-1);
		}
	}
	
	$scope.delSelected=function(){
		if($scope.entity.projects){
			for(var i=0;i<$scope.entity.projects.length;i++){
				if($scope.entity.projects[i].id==$scope.selectedProject){
					$scope.entity.projects.splice(i,1);
				}
			}
		}
		$scope.selectedProject=null;
	}
	
	$scope.save=function(){
		$http.post(backend.url+"/api/job",$scope.entity).then(function(response){
			alert("保存成功");
			$state.go("job.list");
		});
	}
	
	$scope.viewDetail = function (row) {
		$scope.entity=angular.copy(row)
		$scope.searchTeamJobs(row);
		$state.go('job.edit');
	}
	
	$scope.back = function () {
		$state.go('job.list');
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
				url:backend.url+"/api/job",
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
	
	$scope.triggerJob=function(){
		var selected=jQuery("#table").bootstrapTable("getAllSelections")
		if(!selected||!selected.length){
			return;
		}
		
		var ids=[];
		for(var i=0;i<selected.length;i++){
			ids.push(selected[i].id);
		}
		if(confirm("确认立即触发选中任务？")){
			$http({
				url:backend.url+"/api/job/trigger",
				method:"post",
				headers: {
        	        'Content-type': 'application/json;charset=utf-8'
        	    },
				data:ids
			}).then(function(){
				alert("任务已触发，正在后台执行");
				jQuery("#table").bootstrapTable("refresh");
			});
		}
	
	}
	
});

