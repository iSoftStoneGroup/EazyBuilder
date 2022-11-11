app.controller('qaController', function($scope,$http,$window,$state,$filter,$modal,basicService) {
	$scope.condition={};
	$scope.entity={};
	basicService.getTeams().then(function(response){
		$scope.teams=response.data;
	});
	
	basicService.getProjectGroups().then(function(response){
		$scope.groups=response.data;
	});
	let baseServer = 'http://sonarqube.eazybuilder-devops.cn/'
	
	var getMetric=function(metric,list){
		if(!list){
			return 'N/A';
		}
		for(var i=0;i<list.length;i++){
			if(list[i].type==metric){
				return list[i].val;
			}
		}
		return 'N/A';
	};
	
	var getMetricLink=function(metric,list){
		if(!list){
			return 'N/A';
		}
		for(var i=0;i<list.length;i++){
			if(list[i].type==metric){
				if(list[i].link&&list[i].link!='null'){
					return "<a href='"+list[i].link+"' target='_blank'>"+list[i].val+"</a>";
				}else{
					return list[i].val;
				}
			}
		}
		return 'N/A';
	};
	
	var getBuildStatus=function(value){
		switch(value){
		case 'SUCCESS':
			return "成功";
		case 'FAILED':
			return "失败";
		case 'IN_PROGRESS':
			return "执行中";
		case 'UNSTABLE':
			return "不稳定";
		case 'ABORTED':
			return "已取消";
		default:
			return value;
		}
	};
	
	$scope.tableControl={
		options:{
            url:backend.url+"/api/project/qaReport",
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
            	field:'project.description',
            	title:'工程名称',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
            },{
            	field:'project.team',
            	title:'项目组',
            	formatter:function(team,row){
            		if(row.groupName&&team){
            			return row.groupName+"/"+team.name;
            		}else if(row.groupName){
            			return row.groupName
            		}else if(team){
            		    return team.name;
            		}
            		return '--';
            	}
            },{
            	field:'latestPipeline.status',
            	title:'扫描状态',
            	formatter:function(value,row){
            		if(!value){
            			return "--";
            		}
					var buildUrl=row.project.team.teamResource.jenkinsUrl+"/job/"+row.project.name+"/";
            		return "<a href='"+buildUrl+"' target='_blank'>"+getBuildStatus(value)+"</a>"
            	}
            },{
            	field:'latestPipeline.startTimeMillis',
            	title:'扫描时间',
            	formatter:function(value){
            		if(!value){
                		return "--";
                	}
                	return new Date(value).Format("yyyy-MM-dd hh:mm");
            	}
            	
            },{
            	field:'metrics',
            	title:'BUG(阻断)',
            	formatter:function(value,row){
            		if(!value)
            			return 'N/A';
            		var val=getMetric('bug_blocker',row.metrics);
            		if(val!='N/A'){
            			var bugDetail=row.project.team.teamResource.sonarUrl+"/project/issues?id="+row.project.sonarKey+"&resolved=false&types=BUG";
            			return "<a href='"+bugDetail+"' target='_blank'>"+val+"</a>";
            		}else{
            			return val;
            		}
            	}
            },{
            	field:'metrics',
            	title:'BUG(严重)',
            	formatter:function(value,row){
            		if(!value)
            			return 'N/A';
            		var val=getMetric('bug_critical',row.metrics);
            		if(val!='N/A'){
            			var bugDetail=row.project.team.teamResource.sonarUrl+"/project/issues?id="+row.project.sonarKey+"&resolved=false&types=BUG";
            			return "<a href='"+bugDetail+"' target='_blank'>"+val+"</a>";
            		}else{
            			return val;
            		}
            	}
            },{
            	field:'metrics',
            	title:'安全漏洞(阻断)',
            	formatter:function(value,row){
            		if(!value)
            			return 'N/A';
            		var val=getMetric('vulner_blocker',row.metrics);
            		if(val!='N/A'){
            			var vulnerDetail=row.project.team.teamResource.sonarUrl+"/project/issues?id="+row.project.sonarKey+"&resolved=false&types=VULNERABILITY";
                		return "<a href='"+vulnerDetail+"' target='_blank'>"+val+"</a>";
            		}else{
            			return val;
            		}
            		
            	}
            },{
            	field:'metrics',
            	title:'安全漏洞(严重)',
            	formatter:function(value,row){
            		if(!value)
            			return 'N/A';
            		var val=getMetric('vulner_critical',row.metrics);
            		if(val!='N/A'){
            			var vulnerDetail=row.project.team.teamResource.sonarUrl+"/project/issues?id="+row.project.sonarKey+"&resolved=false&types=VULNERABILITY";
                		return "<a href='"+vulnerDetail+"' target='_blank'>"+val+"</a>";
            		}else{
            			return val;
            		}
            	}
            },{
            	field:'metrics',
            	title:'依赖包高危漏洞',
            	formatter:function(value,row){
            		if(!value)
            			return 'N/A';
            		return getMetricLink('dc_high',row.metrics);
            	}
            },{
            	field:'metrics',
            	title:'依赖包中等漏洞',
            	formatter:function(value,row){
            		if(!value)
            			return 'N/A';
            		return getMetricLink('dc_medium',row.metrics);
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

