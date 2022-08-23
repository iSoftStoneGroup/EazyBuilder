app.controller('pipelineController', function($scope,$http,$window,$state,$filter,$modal,$interval,$alert,basicService) {
	$scope.condition={};
	$scope.entity={deployConfig:{}};
	$scope.cracker = {}
	basicService.getDockerRegistrys().then(function(response){
		$scope.dockerRegistrys=response.data;
	});

	basicService.getPipelineProfile().then(function(response){
		$scope.profiles=response.data;
	});

	basicService.getTeams().then(function(response){
		$scope.teams=response.data;
	});
	$scope.statusTypes = statusTypes;

	$scope.tableControl={
		options:{
			url:backend.url+"/api/project/pipeline",
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
				formatter:function(value,row,index){
					if(!value){
						return '--';
					}
					return "<a class='detail' title='"+value+"'>"+value+"</a>";
				},
				events:{
					'click a':function(e, value, row, index){
						$scope.viewDetail(row);
					}
				}
			},{
				field:'description',
				title:'中文名称',
				formatter:function(value){
					if(!value){
						return '--';
					}
					return "<span class='detail' title='"+value+"'>"+value+"</span>";
				}
			},{
				field:'projectType',
				title:'项目类型',
				formatter: function (value) {
					if (!value) {
						return "--";
					}
					switch (value) {
						case 'java':
							return "java";
						case 'npm':
							return "npm";
						case 'gradle':
							return "gradle";
						case 'initDeploy':
							return "项目初始化部署";
						case 'dataBaseScript':
							return "数据库脚本";
						case 'config':
							return "配置文件";
						default:
							return value;
					}
				}
			},
			{
				field:'lastBuild',
				title:'最新构建时间',
				formatter:function(value,row,index){
					if(!value){
						return '--';
					}
					let dateTime = new Date(value).Format('yyyy-MM-dd hh:mm:ss');
					return "<span title='"+dateTime+"'>" + dateTime + "</span>";
				}
			},
			{
				field:'profileName',
				title:'最近执行过程'
			},
			{
				field: 'buildStatus',
				title: '最新状态',
				align: 'center',
				sortable: true,
				formatter:function(value,row,index){
				    let statusName = $scope.statusTypes[value];
					return "<img title='点击查看阈值对比结果，当前状态："+ statusName + "' class='buildStatus' onerror='this.style.display = \"none\"' originSrc='"+backend.url+"/public/buildStatus/svg?projectId="+row.id+"' src='"+backend.url+"/public/buildStatus/svg?projectId="+row.id+"'/>";
				},
				events:{
					'click .buildStatus':function(e, value, row, index){
						$scope.thresholdCompareResult(row);
					}
				}
			},
			{
				field:'lastLogId',
				title:'最近一次构建',
				formatter:function(value,row,index){
					return "<a href=''><i class='fa fa-search'>查看</i></a>";
				},
				events:{
					'click a':function(e, value, row, index){
						$http.get(backend.url+"/api/pipeline/buildStatus?projectId="+row.id).then(function(response){

							if(response.data&&response.data!='null'){
								if(response.data.buildStatus=='IN_PROGRESS'){
									var buildUrl=row.jenkinsUrl+"/job/"+row.jobName+"/";
									window.open(buildUrl,'_blank');
									// alert('正在执行，请稍候查看最新结果');
								}else{
									var buildUrl=backend.url+"/resources/"+response.data.lastLogId;
									if(response.data.lastPipelineId){
										$scope.onlineViewStage({id:response.data.lastPipelineId,logId:response.data.lastLogId,name:row.name});
									}else{
										window.open(buildUrl,'_blank');
									}
								}
							}else{
								alert('没有近期构建记录');
							}
						});

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
	$scope.viewMetric = function(metricMap,row){
		let parentScope = $scope;
		if(metricMap == null || Object.values(metricMap).length==0 ){
			alert("没有发现代码扫描结果信息,请在流水线构建定义中增加代码扫描！");
		}else{
			let modalInstance=$modal({
				title: '流水线执行详情',
				templateUrl: 'app/pipeline/checkMetrics.html',
				show: true,
				animation:'am-fade-and-scale',
				controller:function($scope){
					let thresholdTypes = actionScopeGuardTypes.PIPELINE_BUILD;
					let checkMetrics =  Object.keys(metricMap).filter(metricType=> thresholdTypes.includes(metricType))
						.map(metricType=> metricMap[metricType]);
					$scope.metrics = [];
					$scope.title = row.projectName
					$scope.metrics = checkMetrics;
					$scope.guardTypes = guardTypes;
					$scope.closeModal=function(){
						modalInstance.destroy();
					}
					$scope.showTestResult = function(){
						modalInstance.destroy();
						parentScope.viewDtpReport(row);
					}
				}
			});
		}
	}

	$scope.thresholdCompareResult = function(row){
		$http.get(backend.url+"/api/pipeline/buildStatus?projectId="+row.id).then(function(response){
			$scope.viewMetric(response.data.metrics);
		});

	}

	$scope.backToPipeline=function(){
		$state.go("pipeline.list");
	};


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

	$scope.historyTableControl={
		options:{
			url:backend.url+"/api/pipeline/projectPage",
			cache:false,
			idField:'id',
			queryParams:function(params){
				var queryParam=angular.extend({},params,{project:$scope.entity.id});
				queryParam=angular.extend({},queryParam,$scope.condition);
				return queryParam;
			},
			onCheck:function(row){
				var selection=jQuery("#historyTable").bootstrapTable("getAllSelections");
				if(selection&&selection.length>=2){
					return false;
				}
			},
			columns: [{
				field:'state',
				checkbox:true //设置多选
			},{
				field:'projectName',
				title:'项目名称',
				align:'center',
				formatter:function(value){
					if(!value){
						if($scope.entity.description){
							return $scope.entity.name+"/"+$scope.entity.description;
						}
						return $scope.entity.name;
					}
					return '<span title="'+value+'">'+value+'</span>' ;
				}
			},{
				field:'sourceBranch',
				title:'操作分支',
				formatter:function(value,row){
					let sourceBranch = "";
					let targetBranch = "";
					let branchStr = "";
					if(value){
						sourceBranch = value
					}
					if(row.targetBranch){
						targetBranch = row.targetBranch
					}
					if(sourceBranch===""&&targetBranch==""){
						return '--'
					}
					if(sourceBranch!=""&&targetBranch==""){
						return sourceBranch
					}
					if(sourceBranch==""&&targetBranch!=""){
						return targetBranch
					}
					if(sourceBranch!=""&&targetBranch!=""){
						branchStr = sourceBranch + ' <i class="fa fa-long-arrow-right" style="color:#00B16A;"></i> ' + targetBranch;
						return `<span title="${sourceBranch}——>${targetBranch}">${branchStr}</span>`;
					}
				}
			},{
				field: 'startTimeMillis',
				title: '开始时间',
				align: 'center',
				sortable: true,
				formatter:function(value){
					if(!value){
						return "--";
					}
					let dateTime = new Date(value).Format("yyyy-MM-dd hh:mm:ss")
					return '<span title="'+dateTime+'">'+dateTime+'</span>' ;
				}
			}, {
				field: 'endTimeMillis',
				title: '结束时间',
				align: 'center',
				formatter:function(value){
					if(!value){
						return "--";
					}
					let dateTime = new Date(value).Format("yyyy-MM-dd hh:mm:ss")
					return '<span title="'+dateTime+'">'+dateTime+'</span>' ;
				}
			},{
				field:'durationMillis',
				title:'总耗时',
				align: 'center',
				formatter:function(value){
					if(!value){
						return "--";
					}
					var minutes = parseInt((value % (1000 * 60 * 60)) / (1000 * 60));
					var seconds = parseInt((value % (1000 * 60)) / 1000);
					return (minutes>0?(minutes+"分"):"")+seconds+"秒";
				}
			}, {
					field:'pipelineType',
					title:'触发类型',
					formatter: function (value,row) {
						if (!value) {
							return "其它";
						}
						switch (value) {
							case 'release':
								return '<span title="提测-'+row.pipelineVersion+'">提测-'+row.pipelineVersion+'</span>';
							case 'online':
								return '<span title="上线-'+row.pipelineVersion+'">上线-'+row.pipelineVersion+'</span>';
							case 'push':
								return "代码提交";
							case 'merge':
								return "分支合并";	
							case 'manual':
								return "手动触发";	
							case 'job':
								return "定时任务";	
							case 'manualJob':
								return "定时任务-手动触发";	
							default:
								return '<span title="'+value+'">'+value+'</span>';
						}
					}
				},
				{
				field:'profileName',
				title:'构建过程',
				formatter:function(value){
					if (!value) {
						return "--";
					}
					return `<a title="${value}">${value}</a>` ;
                },
				events:{
					'click a':function(e, value, row, index){
                		$scope.viewProfile(row);
                	}
				}
			},
			{
				field:'status',
				title:'流水线状态',
				align: 'center',
                formatter: function (value,row,index) {
                        if (!value) {
                            return "--";
                        }
                        let detail = $scope.statusTypes[value];
                        detail = detail == null ? value : detail;
						const statusTypes = {SUCCESS:'成功',FAILED:'失败',IN_PROGRESS:'执行中',UNSTABLE:'成功(不稳定)',ABORTED:'已取消',ASSERT_WARNRULE_FAILED:'门禁判定失败',WAIT_AUTO_TEST_RESULT:'等待测试结果',NOT_EXECUTED:'未执行'};
						switch(value){
                            case 'SUCCESS':
								return `<span class="tdLabel label-success">${detail}</span>`;
							case 'FAILED':
								return `<span class="tdLabel label-danger">${detail}</span>`;
							case 'IN_PROGRESS':
								return `<span class="tdLabel label-progress">${detail}</span>`;
							case 'UNSTABLE':
								return `<span class="tdLabel label-unstable">${detail}</span>`;
							case 'ABORTED':
								return `<span class="tdLabel label-cancel">${detail}</span>`;
							case 'ASSERT_WARNRULE_FAILED':
								return `<span class="tdLabel label-failed">${detail}</span>`;
							case 'WAIT_AUTO_TEST_RESULT':
								return `<span class="tdLabel label-wait">${detail}</span>`;
							case 'NOT_EXECUTED':
								return `<span class="tdLabel label-cancel">${detail}</span>`;
							default:
								return 	`<span title="${detail}">${detail}</span>`;
						}
                        
                    }
			},
			{					
				field:'statusGuard',
				title:'门禁结果',
				formatter:function(value,row){
					if (!value) {
						return "--";
					}
					switch (value) {
						case 'NOT_EXECUTED':
							return '--';
						case 'SUCCESS':
							return '<span class="tdLabel label-success">通过</span>';
						case 'FAILED':
							return '<span class="tdLabel label-danger">未通过</span>';
						default:
							return '<span title="'+value+'">'+value+'</span>';
					}
                }
			},
			{
				field:'stages',
				title:'详情查看',
				align: 'center',
				formatter:function(value,row,index){
					return "<a class='detail'>查看</a>";
				},
				events:{
					'click a':function(e, value, row, index){
						$scope.viewStage(row);
					}
				}
			}],
            sortable: true,                     //是否启用排序
            sortOrder: "asc",
            sortName: "startTimeMillis",
            paginationHAlign:'right',
			clickToSelect:true, //设置支持行多选
			//search:true, //显示搜索框
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
	$scope.refreshTable = function(e){
		var keycode = window.event?e.keyCode:e.which;
		if(keycode==13){
			$('#historyTable').bootstrapTable('refresh')
		}
	}
	$scope.viewProfile= function(row){
		$http.get(backend.url+"/api/pipelineProfile/"+row.profileId).then(function(response){
			var modalInstance=$modal({
				title: '流水线执行详情',
				templateUrl: 'app/pipeline/profile-form.html',
				show: true,
				animation:'am-fade-and-scale',
				controller:function($scope,$sce){
					$scope.expression = true;
					$scope.cracker = response.data;
					basicService.getTeams().then(function(response){
						$scope.teams=response.data;
					});
					$scope.getNamespacesByTeam = function () {
						$http.get(backend.url+"/api/team/teamNameSpaces/" +$scope.cracker.teamId).then(function(response){
							$scope.teamNamespaces = response.data;
						});
					}
					if($scope.cracker.teamId){
						$scope.getNamespacesByTeam();
					}
                    $scope.closeModal=function(){
						modalInstance.destroy();
					}
				}
			});
		});
	}
	$scope.viewDtpReport = function (row) {
		var dtpReports = row.dtpReports;
		var modalInstance=$modal({
				title: '流水线执行详情',
				templateUrl: 'app/pipeline/dtpReport.html',
				show: true,
				animation:'am-fade-and-scale',
				controller:function($scope){
					$scope.dtpReports=dtpReports;
					$scope.closeModal=function(){
						modalInstance.destroy();
					}
					$scope.dtpReportHref = function (url) {
						// window.location.href = url;
						window.open(url);
					}
				}
			}
		);
	}
	//每10秒刷新一次构建状态
	$interval(function(){
		jQuery("#table").find("img.buildStatus").each(function(){
			var imgUrl=jQuery(this).attr("originSrc");
			//根据pipeline Id查询状态
			var img=jQuery(this).attr("src", imgUrl+"&r="+new Date());
		});

	},5000,-1);

	$scope.getStatusSvg=function(status){
		switch(status){
			case 'SUCCESS':
			case 'UNSTABLE':
				return "images/svg/passing.svg";
			case 'ASSERT_WARNRULE_FAILED':
			case 'FAILED':
				return "images/svg/failing.svg";
			case 'IN_PROGRESS':
			case 'WAIT_AUTO_TEST_RESULT':
				return "images/svg/running.svg";
			case 'ABORTED':
				return "images/svg/aborted.svg";
			default:
				return "images/svg/notRun.svg";
		}
	}



	$scope.build=function(){
		var selected=jQuery("#table").bootstrapTable("getAllSelections")
		if(!selected||!selected.length){
			$alert({content: '请选择项目!', container:'#alerts-container',type:'warning',show: true ,duration:3});
			return;
		}
		var ids=[];
		for(var i=0;i<selected.length;i++){
			ids.push(selected[i].id);
		}
		$.ajax({
			url: backend.url + "/api/dockerImage/getDockerImageByProjectId?projectId=" + selected[0].id,
			async: false,
			method: "GET",
			success: function (data, status) {
				$scope.dockerImage = data;
			}
		});
		var profiles=$scope.profiles;
		var dockerImage = $scope.dockerImage;
		var modalInstance=$modal({
				title: '选择构建设置',
				templateUrl: 'app/pipeline/profile.html',
				show: true,
				animation:'am-fade-and-scale',
				controller:function($scope){
					$scope.buildProfiles=profiles;
					$scope.dockerImage=dockerImage;
					$scope.closeModal=function(){
						modalInstance.destroy();
					}
                     $http.get(backend.url+"/api/deveops").then(function(response){
                          $scope.teams=response.data;
                     });

                    $scope.teamChanged=function(){
                      $scope.teamNamespaces=$scope.team.teamNamespaces;
                    }

                    $scope.nameSpaceChanged=function(){
                         $http.get(backend.url+"/api/pipelineProfile/getFromBelongsTeamsAndNameSpace?teamId="+$scope.team.id+"&nameSpace="+$scope.nameSpace.code).then(function(response){
                                     $scope.buildProfiles=response.data;
                        });
                    }
					//保存
					$scope.startBuild=function(){
						if(confirm("确认立即构建选中的项目？")){
							$http({
								url:backend.url+"/api/pipeline",
								method:"post",
								headers: {
									'Content-type': 'application/json;charset=utf-8'
								},
								data: {
									profile: $scope.profile,
									projects: ids,
									arriveTagName: $scope.arriveTagName,
									dbUrl: $scope.dbUrl,
									dbUserName: $scope.dbUserName,
									dbPassword: $scope.dbPassword,
                                    gitlabApiUrl: $scope.gitlabApiUrl,
									dockerImageTag:$scope.dockerImageTag,
									rolloutVersion: $scope.rolloutVersion,
									releaseDate: new Date($scope.releaseDate)
								}
							}).then(function(){
								$alert({content: '任务创建成功', container:'#alerts-container',type:'warning',show: true ,duration:3});
								jQuery("#table").bootstrapTable("refresh");
								modalInstance.destroy();
							});
						}
					}
				}
			}
		);
	}


	$scope.viewHistory = function (row) {
		$scope.entity=angular.copy(row);
		$scope.condition={};
		$state.go('pipeline.history');
	}

	$scope.viewDetail = function (row) {
		$http.get(backend.url+"/api/project/"+row.id).then(function(response){
			$scope.entity=response.data;
			if($scope.entity.developers&&$scope.devs){
				var devIds=[];
				for(var i=0;i<$scope.entity.developers.length;i++){
					for(var j=0;j<$scope.devs.length;j++){
						if($scope.entity.developers[i].id==$scope.devs[j].id){
							devIds.push($scope.devs[j].id);
						}
					}
				}
				$scope.entity.devIds=devIds;
			}
			if($scope.entity.team){
				$scope.entity.teamId=$scope.entity.team.id;
			}
			if($scope.entity.defaultProfile){
				$scope.entity.defaultProfileId=$scope.entity.defaultProfile.id;
			}
			$state.go('pipeline.edit');
		});
	}
    function bug_blocker(row,metrics,flag){
		var val=getMetric(flag,metrics);
		if(val!='N/A'){
			let branch = row.targetBranch? '&branch='+row.targetBranch: '&branch=master';
			var bugDetail=sonarServer+"/project/issues?id="+row.project.sonarKey+"&resolved=false&types=BUG"+ branch +"";
			return "<a href='"+bugDetail+"' target='_blank'>"+val+"</a>";
		}
		return null;
	}
	function vulner_blocker (row,metrics,flag){
		var val=getMetric(flag,metrics);
		if(val!='N/A'){
			let branch = row.targetBranch? '&branch='+row.targetBranch: '&branch=master';
			var vulnerDetail=sonarServer+"/project/issues?id="+row.project.sonarKey+"&resolved=false&types=VULNERABILITY"+ branch +"";
			return  "<a href='"+vulnerDetail+"' target='_blank'>"+val+"</a>";
		}
		return null;
	}
	function unit_test_coverage_rate(row,metrics,flag){
		var val=getMetric(flag,metrics);
		if(val!='N/A'){
			  let branch = row.targetBranch? '&branch='+row.targetBranch: '&branch=master';
			  var vulnerDetail=sonarServer+"/dashboard?id="+row.project.sonarKey+ branch +"";
			  return "<a href='"+vulnerDetail+"' target='_blank'>"+val+"%"+"</a>";
		}
		return null;
	}
	function code_smell_blocker(row,metrics,flag){
            var val=getMetric(flag,metrics);
            if(val!='N/A'){
    			var vulnerDetail=sonarServer+"/project/issues?id="+row.project.sonarKey+"&resolved=false&types=CODE_SMELL&severities=BLOCKER";
                  return "<a href='"+vulnerDetail+"' target='_blank'>"+val+"</a>";
            }
            return null;
        }
	$scope.onlineViewStage = function (row) {
		var logId=row.logId;
		let showStageLog = $scope.showStageLog;
		$http.get(backend.url+"/api/pipeline/stage?pipelineId="+row.id).then(function(response){
			var modalInstance=$modal({
					title: '流水线执行详情',
					templateUrl: 'app/pipeline/online-stage.html',
					show: true,
					animation:'am-fade-and-scale',
					controller:function($scope){
						$scope.logUrl=backend.logUrl+"/resources/"+logId;
						$scope.stages=response.data;
						$scope.title = row.name;
						$scope.status=row.status;
                        $scope.logId = logId;
                        $scope.showStageLog = showStageLog;
						$scope.closeModal=function(){
							modalInstance.destroy();
						}
					}
				}
			);
		});
	}

    $scope.showStageLog = function(stage,logId){
        let stageName = stage.name;
        $http.post(`${backend.url}/api/pipeline/getStageLog?logId=${logId}`,stage).then(function(response){
            let stageLogInstance=$modal({
                title: '流水线执行详情',
                templateUrl: 'app/pipeline/stageLog.html',
                show: true,
                animation:'am-fade-and-scale',
                controller:function($scope){
                    $scope.stageLog = response.data ? response.data[stageName] : "";
                    $scope.closeModal=function(){
                        stageLogInstance.destroy();
                    }
                }
            });
        });
    }

	$scope.viewStage = function (row) {
		var logId=row.logId;
		let showStageLog = $scope.showStageLog;
		var modalInstance=$modal({
				title: '流水线执行详情',
				templateUrl: 'app/pipeline/stage.html',
				show: true,
				animation:'am-fade-and-scale',
				controller:function($scope,$sce){
					$scope.logUrl=backend.logUrl+"/resources/"+logId;
					$scope.status=row.status;
					$scope.stages=row.stages;
					$scope.accessControls=[]
					$scope.dtpTask = row.dtpTask
					$scope.statusGuard= row.statusGuard;
					if($scope.dtpTask){
						$scope.dtpReports = row.dtpReports; 
						if($scope.dtpReports.length>0){
							let dtpReportJson = {}
							let succeedTotal = 0;
							let faildTotal = 0;
							$scope.dtpReports.forEach((dtpReport) =>{
								let stage = {}
								stage.name = dtpReport.name;
								stage.durationMillis = dtpReport.elapsedTime;
								stage.status = dtpReport.succeed?'SUCCESS':'FAILED';
								$scope.stages.push(stage)
                                if(dtpReport.succeed){
									succeedTotal++;
								}else{
									faildTotal++;
								}
							})
							dtpReportJson.succeedTotal= succeedTotal;
							dtpReportJson.faildTotal= faildTotal;
							dtpReportJson.reslut = faildTotal>0? false:true;
							dtpReportJson.name = row.projectName;
							$scope.accessControls.push(dtpReportJson)
						}
					}
					$scope.title = row.projectName +'——'+row.profileName;
					let obj = {}
					obj.bug_blocker = $sce.trustAsHtml(bug_blocker(row,row.metrics,'bug_blocker'))
					obj.bug_critical = $sce.trustAsHtml(bug_blocker(row,row.metrics,'bug_critical'))
					
					obj.vulner_blocker = $sce.trustAsHtml(vulner_blocker(row,row.metrics,'vulner_blocker'))
					obj.vulner_critical = $sce.trustAsHtml(vulner_blocker(row,row.metrics,'vulner_critical'))

					obj.unit_test_coverage_rate = $sce.trustAsHtml(unit_test_coverage_rate(row,row.metrics,'unit_test_coverage_rate'))
					obj.unit_test_success_rate = $sce.trustAsHtml(unit_test_coverage_rate(row,row.metrics,'unit_test_success_rate'))
					obj.new_unit_test_coverage_rate = $sce.trustAsHtml(unit_test_coverage_rate(row,row.metrics,'new_unit_test_coverage_rate'))
					obj.code_smell_blocker = $sce.trustAsHtml(code_smell_blocker(row,row.metrics,'code_smell_blocker'))


					if(obj.bug_blocker===null&&obj.bug_critical===null&&obj.vulner_blocker===null&&obj.vulner_critical===null&&obj.unit_test_coverage_rate===null&&obj.unit_test_success_rate===null){
						$scope.tasks = [];
					}else{
						let list = []
						list.push(obj)
						$scope.tasks = list;
					}
					let metrics = row.metrics;
					let metricMap = {};
					metrics.forEach(function(metric){
						metricMap[metric.type] = metric;
					});
					let thresholdTypes = actionScopeGuardTypes.PIPELINE_BUILD;
					let checkMetrics =  Object.keys(metricMap).filter(metricType=> thresholdTypes.includes(metricType))
						.map(metricType=> metricMap[metricType]);
					$scope.metrics = [];
					$scope.title = row.projectName
					if(row.npmInstal){
						checkMetrics.forEach((item)=>{
							if(item.type!="unit_test_coverage_rate"&&item.type!="unit_test_success_rate"&&item.type!="new_unit_test_coverage_rate"){
							$scope.metrics.push(item)
							}
						})
					}else{
						$scope.metrics = checkMetrics
					}
					$scope.guardTypes = guardTypes;

					//重新执行流水线
					$scope.retryPipeline = function () {
						$http.get(backend.url + "/api/pipeline/retryPipeline?pipelineId=" + row.id).then(function (response) {
							alert("已重新执行,稍后请查看流水线执行结果");
							modalInstance.destroy();
							jQuery("#table").bootstrapTable("refresh");
						});

					}


					$scope.showTestResult = function(){
						modalInstance.destroy();
						parentScope.viewDtpReport(row);
					}
					$scope.closeModal=function(){
						modalInstance.destroy();
					}
                    $scope.logId = logId;
                    $scope.showStageLog = showStageLog;

					$scope.dtpReportHref = function (url) {
						// window.location.href = url;
						window.open(url);
					}
				}
			}
		);
	}

	$scope.back = function () {
		$state.go('pipeline.list');
	}


});


app.controller('revisionLogExportController', function($scope,$http,$window,$state,$filter,$modal,$interval,$alert,basicService) {
	$scope.entity={};

	basicService.getProjects().then(function(response){
		$scope.projects=response.data;
	});

	$scope.back = function () {
		$state.go('pipeline.history');
	}

	$scope.queryChangeList=function(){
		$http.get(backend.url+"/api/pipeline/changeList?projectId="
			+$scope.entity.projectId
			+"&from="+$scope.entity.from
			+"&to="+$scope.entity.to).then(function(response){
			var modalInstance=$modal({
					title: '变更集',
					templateUrl: 'app/pipeline/changelog.html',
					show: true,
					animation:'am-fade-and-scale',
					controller:function($scope){
						$scope.tableMode=true;
						$scope.changelogs=response.data;
						var multiline="";
						for(var i=response.data.length-1;i>=0;i--){
							if(response.data[i].log&&response.data[i].log.length&&response.data[i].log.trim()!=""){
								multiline+=response.data[i].version+":\n\t"+response.data[i].log.replaceAll('\n','\n\t')+"\n";
							}
						}
						$scope.multiline=multiline;

						$scope.onCopySuccess=function(e){
							e.clearSelection();
							alert("已复制到剪贴板");
						};
						$scope.closeModal=function(){
							modalInstance.destroy();
						}
					}
				}
			);

		});
	}
});

