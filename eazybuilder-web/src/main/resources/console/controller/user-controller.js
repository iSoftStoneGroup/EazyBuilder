app.controller('userController', function($scope,$http,$window,$state,$modal,$filter,basicService) {
	$scope.usedUpms=JSON.parse($window.sessionStorage.portal).used;
	$scope.condition={};
	$scope.entity={};
	$scope.currentUser=JSON.parse($window.sessionStorage.user);
	$scope.tableControl={
		options:{
            url:backend.url+"/api/user/page",
        	cache:false,
            idField:'id',
            queryParams:function(params){
            	var queryParam=angular.extend({},params,$scope.condition);
            	return queryParam;
            },
            columns: [{
                field:'state',
                checkbox:true, //设置多选
                formatter:function(value,row,index){
                	if(row.email=='admin'){
                		return {
                			disabled: true,
                			checked: false
                		}
                	}
                	return true;
                }
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
					return '<span title="'+value+'">'+value+'</span>' ;
				}
            },{
            	field:'email',
            	title:'邮箱',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
            },{
            	field:'phone',
            	title:'手机号',
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
	
	
	$scope.editProfile=function(){
		var user=JSON.parse($window.sessionStorage.user);
		
		var modalInstance=$modal({
			  title: '构建步骤设置', 
			  templateUrl: 'app/user/profile.html', 
		      show: true,
		      animation:'am-fade-and-scale',
		      controller:function($scope){
		    	  $scope.user=user;
		    	  $scope.editPasswd=false;
		    	  
		    	  $scope.closeModal=function(){
		    		  modalInstance.destroy();
		    	  }
		    	  //保存
		    	  $scope.saveProfile=function(){
		    		  var url=backend.url+"/api/user";
		    		  if($scope.user.confirmPass!=$scope.user.password){
		    			  alert("确认密码输入不一致，请检查");
		    			  return;
		    		  }else if($scope.user.password){
		    			  url=backend.url+"/api/user/all";
		    		  }
		    		  $http.post(url,$scope.user).then(function(response){
		    				alert("保存成功");
		    				$scope.currentUser=$scope.user;
		    				$window.sessionStorage.user=JSON.stringify(response.data);
		    				modalInstance.destroy();
		    			})
		    	  }
		    	  
	          }        
			}
		);
		
	}
	
	$scope.logout=function(){
		if(confirm("确认注销？")){
			$window.sessionStorage.token=null;
			$window.sessionStorage.user=null;
			$window.location.href="login.html";
		}
	}
	
	$scope.add=function(){
		$scope.entity={};
		$state.go('user.add');
	}
	
	$scope.save=function(){
		$http.post(backend.url+"/api/user",$scope.entity).then(function(response){
			alert("保存成功");
			$state.go("user.list");
		});
	}
	
	$scope.viewDetail = function (row) {
		$scope.entity=angular.copy(row);
		$state.go('user.edit');
	}
	
	$scope.back = function () {
		$state.go('user.list');
	}
	$scope.resetPwd=function(){
		var selected=jQuery("#table").bootstrapTable("getAllSelections")
		if(!selected||!selected.length){
			return;
		}
		
		var ids=[];
		for(var i=0;i<selected.length;i++){
			ids.push(selected[i].id);
		}
		if(confirm("确认重置选中用户的密码？")){
			$http({
				url:backend.url+"/api/user/reset",
				method:"post",
				headers: {
        	        'Content-type': 'application/json;charset=utf-8'
        	    },
				data:ids
			}).then(function(){
				alert("密码重置成功");
				jQuery("#table").bootstrapTable("refresh");
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
				url:backend.url+"/api/user",
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

