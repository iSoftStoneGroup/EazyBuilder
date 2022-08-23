app.controller('testEnvSetController', function($scope,$http,$window,$state,$filter,$modal,basicService) {
	$scope.condition={};
	$scope.entity={};
	$scope.selectedEnv=[];
	$scope.tableControl={
		options:{
            url:backend.url+"/api/testEnvSet/page",
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
                title: '配置名称',
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
            	field:'siteUrl',
            	title:'测试环境地址',
				formatter:function(value){
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
	
	$scope.add=function(){
		$scope.entity={envs:[]};
		$state.go('testEnvSet.add');
	}
	
	$scope.$on("uploaded",function(event,eventData){
		$scope.$apply(function(){
			$scope.entity.envs=eventData.data;
		});
	});
	
	$scope.showEnvAddModel=function(env){
		var envs=$scope.entity.envs;
		var modalInstance=$modal({
			  title: '选择构建设置', 
			  templateUrl: 'app/testEnvSet/envModal.html', 
		      show: true,
		      animation:'am-fade-and-scale',
		      controller:function($scope){
		    	  if(env){
		    		  $scope.env=angular.copy(env);
		    		  $scope.edit=true;
		    	  }else{
		    	  	  $scope.env={id:guid()};
		      	  }
		    	  $scope.closeModal=function(){
		    		  modalInstance.destroy();
		    	  }
		    	  //保存
		    	  $scope.saveEnv=function(){
		    		  if($scope.edit){
		    			  for(var i=0;i<envs.length;i++){
		    				  if(envs[i].id==$scope.env.id){
		    					  envs[i]=$scope.env;
		    				  }
		    			  }
		    		  }else{
		    			  envs.push($scope.env);
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
		var idx=$scope.selectedEnv.indexOf(envId);
		if(idx>=0){
			$scope.selectedEnv.splice(idx,1);
		}else{
			$scope.selectedEnv.push(envId);
		}
		console.log($scope.selectedEnv);
	}
	
	$scope.isSelected=function(id){
		return $scope.selectedEnv.indexOf(id)>=0;
	}
	
	$scope.checkAll=function(){
		if(!$scope.entity.envs){
			return;
		}
		var selectedEnv=[];
		
		if($scope.selectedEnv.length!=$scope.entity.envs.length){
			for(var i=0;i<$scope.entity.envs.length;i++){
				selectedEnv.push($scope.entity.envs[i].id);
			}
		}
		$scope.selectedEnv=selectedEnv;
		console.log($scope.selectedEnv);
	}
	
	$scope.delSelectedEnv=function(){
		if($scope.entity.envs){
			for(var i=0;i<$scope.entity.envs.length;i++){
				for(var j=0;j<$scope.selectedEnv.length;j++){
					if($scope.entity.envs[i].id==$scope.selectedEnv[j]){
						$scope.entity.envs.splice(i,1);
					}
				}
			}
		}
		$scope.selectedEnv=[];
	}
	
	$scope.doImport=function(){
		
	}
	
	$scope.save=function(){
		if($scope.entity.envs){
			var reg=/^[0-9a-zA-Z_]{1,}$/;
			for(var i=0;i<$scope.entity.envs.length;i++){
				$scope.entity.envs[i].id=null;
				if(!reg.test($scope.entity.envs[i].key)){
					alert("变量名["+$scope.entity.envs[i].key+"]无效（只能由数字、字母和下划线组成）");
					return;
				}
			}
		}
		
		$http.post(backend.url+"/api/testEnvSet",$scope.entity).then(function(response){
			alert("保存成功");
			$state.go("testEnvSet.list");
		});
	}
	
	$scope.viewDetail = function (row) {
		$scope.entity=angular.copy(row);
		console.log($scope.entity);
		$state.go('testEnvSet.edit');
	}
	
	$scope.back = function () {
		$state.go('testEnvSet.list');
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
				url:backend.url+"/api/testEnvSet",
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

