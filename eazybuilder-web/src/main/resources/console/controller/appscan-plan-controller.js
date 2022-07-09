app.controller('appscanPlanController', function($scope,$http,$window,$state,$filter,$modal,basicService) {
	$scope.condition={};
	$scope.entity={};
	$scope.selectedScript=null;
	basicService.getTeams().then(function(response){
		$scope.teams=response.data;
	});
	basicService.getTestEnvSet().then(function(response){
		$scope.envs=response.data;
	});
	basicService.getProjects().then(function(response){
		$scope.projects=response.data;
	});
	$scope.tableControl={
		options:{
            url:backend.url+"/api/appscan/plan/page",
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
                title: '测试计划名称',
                align: 'center',
                sortable: true,
                formatter:function(value,row,index){
                	if(!value){
                		return '--'
					}
                	return "<a>"+value+"</a>";
                },
                events:{
                	'click a':function(e, value, row, index){
                		$scope.viewDetail(row);
                	}
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
            	field:'executeType',
            	title:'执行策略',
            	formatter:function(value,row){
            		if(!value){
            			return '--';
            		}
            		switch(value){
            		case 'manual':
            			return '人工执行';
            		case 'timePlan':
            			return '按时间计划执行 [下一次:'+new Date(row.nextTime).Format('yyyy-MM-dd hh:mm:ss')+']';
            		case 'afterDeploy':
            			return '在项目成功部署后执行';
            		default:
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
	
	$scope.add=function(){
		$scope.entity={scripts:[],executeType:'manual'};
		$state.go('appScanPlan.add');
	}
	
	$scope.showAddModel=function(env){
		var scripts=$scope.entity.scripts;
		var modalInstance=$modal({
			  title: '选择测试脚本', 
			  templateUrl: 'app/appScanPlan/scriptModal.html', 
		      show: true,
		      animation:'am-fade-and-scale',
		      controller:function($scope){
		    	  if(scripts){
		    		  $scope.selectedScripts=angular.copy(scripts);
		    	  }else{
		    	  	  $scope.selectedScripts=[];
		      	  }
		    	  var excludes=[];
		    	  for(var i=0;i<$scope.selectedScripts.length;i++){
		    		  excludes.push($scope.selectedScripts[i].id);
		    	  }
		    	  $scope.condition={excludes:angular.toJson(excludes)};
		    	  
		    	  $scope.scriptTableControl={
		    				options:{
		    		            url:backend.url+"/api/appscan/script/pageWithExcludes",
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
		    		                title: '测试脚本名称',
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
		    		  var selected=jQuery("#scriptTable").bootstrapTable("getAllSelections")
		    		  if(!selected||!selected.length){
		    				return;
		    		  }
		    		  for(var i=0;i<selected.length;i++){
		    			  scripts.push(selected[i]);
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
		$scope.selectedScript=envId;
	}
	
	$scope.isSelected=function(id){
		return $scope.selectedScript==id;
	}
	
	$scope.moveUp=function(){
		if($scope.entity.scripts&&$scope.selectedScript){
			for(var i=0;i<$scope.entity.scripts.length;i++){
				if($scope.entity.scripts[i].id==$scope.selectedScript){
					zIndexDown($scope.entity.scripts,i);
					return;
				}
			}
		}
	}
	
	$scope.moveDown=function(){
		if($scope.entity.scripts&&$scope.selectedScript){
			for(var i=0;i<$scope.entity.scripts.length;i++){
				if($scope.entity.scripts[i].id==$scope.selectedScript){
					zIndexUp($scope.entity.scripts,i);
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
		if($scope.entity.scripts){
			for(var i=0;i<$scope.entity.scripts.length;i++){
				if($scope.entity.scripts[i].id==$scope.selectedScript){
					$scope.entity.scripts.splice(i,1);
				}
			}
		}
		$scope.selectedScript=null;
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
		$http.post(backend.url+"/api/appscan/plan",$scope.entity).then(function(response){
			alert("保存成功");
			$state.go("appScanPlan.list");
		});
	}
	
	$scope.viewDetail = function (row) {
		$scope.entity=angular.copy(row);
		if($scope.entity.team){
			$scope.entity.teamId=$scope.entity.team.id;
		}
		if(!$scope.entity.scripts){
			$scope.entity.scripts=[];
		}
		$state.go('appScanPlan.edit');
	}
	
	$scope.back = function () {
		$state.go('appScanPlan.list');
	}
	$scope.$on("uploaded",function(event,eventData){
		$scope.entity.scm.url=eventData.data.id;
	});
	
	$scope.execute=function(){
		var selected=jQuery("#table").bootstrapTable("getAllSelections")
		if(!selected||!selected.length){
			alert('未选中测试计划');
			return;
		}
		if(selected.length>1){
			alert('只能执行一项测试计划');
			return;
		}
		if(confirm("确认立即执行测试计划["+selected[0].name+"]?")){
			$http.get(backend.url+"/appscan/run?planId="+selected[0].id+"&uid="+guid()).then(function(){
				alert("已提交执行，测试完成后将有邮件通知");
			},function(){
				alert("测试执行异常，请联系系统管理员");
			});
		}
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
				url:backend.url+"/api/appscan/plan",
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

