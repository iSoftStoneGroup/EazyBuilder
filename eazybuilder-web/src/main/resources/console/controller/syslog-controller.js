app.controller('syslogController', function($scope,$http,$window,$state,$filter,$modal,basicService) {
	$scope.condition={};
	$scope.entity={};
	basicService.getMyTeams().then(function(response){
		$scope.teams=response.data;
	});
	$scope.tableControl={
		options:{
            url:backend.url+"/api/syslog/page",
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
                field: 'accessTime',
                title: '操作时间',
                align: 'center',
                	formatter:function(value){
                		if(!value){
                			return '--';
                		}
                		return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
                	}
            },{
            	field:'remoteAddr',
            	title:'IP',
                formatter:function(value){
                    if(!value){
                        return '--';
                    }
                    return '<span title="'+value+'">'+value+'</span>' ;
                }
            },{
            	field:'userName',
            	title:'用户名',
                formatter:function(value){
                    if(!value){
                        return '--';
                    }
                    return '<span title="'+value+'">'+value+'</span>' ;
                }
            },{
            	field:'opDesc',
            	title:'操作',
                formatter:function(value){
                    if(!value){
                        return '--';
                    }
                    return '<span title="'+value+'">'+value+'</span>' ;
                }
            },{
            	field:'accessUrl',
            	title:'访问路径',
                formatter:function(value){
                    if(!value){
                        return '--';
                    }
                    return '<span title="'+value+'">'+value+'</span>' ;
                }
            },{
            	field:'opResult',
            	title:'操作结果',
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
	
});

