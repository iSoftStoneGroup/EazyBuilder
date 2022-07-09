app.controller('testExecHistoryController', function($scope,$http,$window,$state,$filter,$modal,basicService) {
	$scope.condition={};
	$scope.entity={};
	$scope.tableControl={
		options:{
            url:backend.url+"/api/testExecHistory/page",
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
                field: 'planName',
                title: '测试计划名称',
                align: 'center',
                sortable: true,
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
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
            	field:'startTime',
            	title:'开始执行时间',
            	formatter:function(value){
            		if(!value){
            			return '--';
					}
            		return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
            	}
            },{
            	field:'endTime',
            	title:'结束执行时间',
            	formatter:function(value){
            		if(!value){
            			return '--';
            		}
            		return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
            	}
            },{
            	field:'executeStatus',
            	title:'执行状态',
            	formatter:function(value){
            		switch(value){
            		case 'NOT_EXECUTED':
            			return "等待执行";
            		case 'IN_PROGRESS':
            			return "执行中";
            		case 'FAILED':
            			return "执行失败";
            		case 'SUCCESS':
            			return "完成";
            		case 'ABORTED':
            			return "异常/执行中断";
            		
            		}
            	}
            },{
            	field:'remindMsg',
            	title:'提示信息',
            	width:'100px',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
            },{
            	field:'totalPass',
            	title:'结果',
            	align: 'center',
            	formatter:function(value,row){
            		if(value==0||value){
            			return "<a href='#'>"+createBadgeSvg("PASS",row.totalPass,"#4c1")+"&nbsp;"+createBadgeSvg("FAIL",row.totalFailed,"#ff3333")+"&nbsp;"+createBadgeSvg("WARN",row.totalWarning,"#ffcc33")+"</a>";
            		}else{
            			return "--";
            		}
            	},
            	events:{
                	'click a':function(e, value, row, index){
                		$scope.viewDetail(row);
                	}
                }
            },{
            	field:'reportFileId',
            	title:'操作',
            	formatter:function(value){
            		if(!value){
            			return '--';
            		}
            		return '<a href="/ci/download?fileId='+value+'">下载报告</a>'
            	}
            }],
            clickToSelect:true, //设置支持行多选
            singleSelect:false,
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
	
	
	$scope.viewDetail=function(row){
		$http.get(backend.url+"/api/testDetailResult?historyId="+row.id).then(function(response){
			var details=response.data;
			var modalInstance=$modal({
				  title: '选择构建设置', 
				  templateUrl: 'app/testExecHistory/detailsModal.html', 
			      show: true,
			      animation:'am-fade-and-scale',
			      controller:function($scope){
			    	  $scope.details=details;
			    	  $scope.closeModal=function(){
			    		  modalInstance.destroy();
			    	  }
		          }        
				}
			);
		});
	}
	
	$scope.deleteTestHistory=function(){
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
				url:backend.url+"/api/testExecHistory",
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

