app.controller('appscanHistoryController', function($scope,$http,$window,$state,$filter,$modal,basicService) {
	$scope.condition={};
	$scope.entity={};
	$scope.tableControl={
		options:{
            url:backend.url+"/api/appScanHistory/page",
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
					return value;
				}
            },{
            	field:'teamName',
            	title:'项目组',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return value;
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
            	field:'totalHigh',
            	title:'结果',
            	align: 'center',
            	formatter:function(value,row){
            		if((value!=null||value!=undefined)&&'SUCCESS'==row.executeStatus){
            			return "<a href='#'>"+createBadgeSvg("高危",row.totalHigh,"#ff3333")+"&nbsp;"+createBadgeSvg("中等",row.totalMedium,"#ffcc33")+"</a>";
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
            singleSelect:true,
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
		$http.get(backend.url+"/api/appScanHistory/detail?hisId="+row.id).then(function(response){
			var details=response.data;
			var modalInstance=$modal({
				  title: '选择构建设置', 
				  templateUrl: 'app/appScanHistory/detailsModal.html', 
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
});

