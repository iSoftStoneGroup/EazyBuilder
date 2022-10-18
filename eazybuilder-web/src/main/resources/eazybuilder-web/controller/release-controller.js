app.controller('releaseController', function ($scope, $http, $window, $state, $filter, $modal, basicService) {
    $scope.condition = {};
    $scope.entity = {};
    $scope.redmine = {};
    $scope.dockerDigests = [];
    $scope.pipelineList= [];
    $scope.checkReleasePipeline=true;

    $scope.statusTypes = statusTypes;

    $scope.checkFlag = false;

    basicService.getRedmineTeam().then(function (response) {
        $scope.redmine.teams = response.data;
    });

    $scope.getRedmineSprintAndUsersByTeam = function () {
       if($scope.redmine.teams && $scope.entity.teamId){
          for(var i=0;i<$scope.redmine.teams.length;i++){
              if($scope.redmine.teams[i].id==$scope.entity.teamId){
                  $scope.entity.teamName=$scope.redmine.teams[i].name;
              }
          }
       }
        $http.get(backend.url + '/api/redmine/getRedmineSprintAndUsersByTeam?teamId=' + $scope.entity.teamId+"&teamName="+$scope.entity.teamName).then(function(response){
            $scope.redmine.sprints = response.data.redmineSprints;
            $scope.members = response.data.members;
        });
        $scope.getRedmineSprintByTeam();
    }

    $scope.getRedmineIssuesBySprint = function () {

        var teamName="";
        if($scope.redmine.teams&&$scope.entity.teamId){
            for(var i=0;i<$scope.redmine.teams.length;i++){
                if($scope.redmine.teams[i].id==$scope.entity.teamId){
                    teamName=$scope.redmine.teams[i].name;
                }
            }
        }
        $http.get(backend.url + '/api/release/checkReleaseSprints?sprintId=' + $scope.entity.sprintId + "&teamName=" + teamName).then(function(response){
            if(response.data){
                $http.get('/ci/api/redmine/getIssuesBySprintId?sprintId=' + $scope.entity.sprintId + "&teamName=" + teamName).then(function(response){
                    $.fn.zTree.init($("#issuesTree"), $scope.treeOption, response.data);
                });
            }else{
                alert("限制一个迭代版本只能有一次提测");
            }
        });

    }
    $scope.getRedmineSprintByTeam = function () {
        $http.get(backend.url + '/api/redmine/getRedmineSprintByTeam?teamId=' + $scope.entity.teamId).then(function(response){
            $scope.redmine.sprints = response.data;
            var teamName="";
            if($scope.redmine.teams&&$scope.entity.teamId){
                for(var i=0;i<$scope.redmine.teams.length;i++){
                    if($scope.redmine.teams[i].id==$scope.entity.teamId){
                        teamName=$scope.redmine.teams[i].name;
                    }
                }
            }
            $http.get(backend.url + '/api/team/getUserByTeamName?teamName=' + teamName).then(function(response){
                $scope.checkReleasePipeline = response.data.checkReleasePipeline;
            });
        });

    }

    $scope.refreshIssuesBySprintId = function () {


        if(!$scope.entity.sprintId || !$scope.entity.teamId){
            return;
        }
        $scope.entity.refreshButtonDisabled= true;
        var teamName="";
        for(var i=0;i<$scope.redmine.teams.length;i++){
            if($scope.redmine.teams[i].id==$scope.entity.teamId){
                teamName=$scope.redmine.teams[i].name;
            }
        }
        $http.get(backend.url + '/api/release/checkReleaseSprints?sprintId=' + $scope.entity.sprintId + "&teamName=" + teamName).then(function(response){
            if(response.data){
                $http.get('/ci/api/redmine/refreshIssuesBySprintId?sprintId=' + $scope.entity.sprintId + "&teamName=" + teamName).then(function(response){
                    $.fn.zTree.init($("#issuesTree"), $scope.treeOption, response.data);
                    $scope.entity.refreshButtonDisabled= false;
                },function(error){
                    $scope.entity.refreshButtonDisabled= false;
                });
            }else{
                alert("限制一个迭代版本只能有一次提测");
                $scope.entity.refreshButtonDisabled= false;
            }
        });
    }


    $scope.addQueryParam = function () {
        if (!$scope.entity.deployConfigList) {
            $scope.entity.deployConfigList = [];
        }
        $scope.entity.deployConfigList.push({});
    }
    $scope.delQueryParam = function (index) {
        $scope.entity.deployConfigList.splice(index, 1);
    }
    $scope.tableControl = {
        options: {
            url: backend.url + "/api/release/page",
            cache: false,
            idField: 'id',
            queryParams: function (params) {
                var queryParam = angular.extend({}, params, $scope.condition);
                return queryParam;
            },
            columns: [{
                field: 'state',
                checkbox: true //设置多选
            }, {
                field: 'title',
                title: '提测标题',
                align: 'center',
                sortable: true,
                formatter: function (value) {
                    if(!value){
                        return '--';
                    }
                    return "<a title='"+value+"'>" + value + "</a>";
                },
                events: {
                    'click a': function (e, value, row, index) {
                        $scope.viewDetail(row);
                    }
                }
            }, {
                field: 'releaseCode',
                title: '提测申请号',
                formatter: function (value) {
                    if(!value){
                        return '--';
                    }
                    return "<span title='"+value+"'>" + value + "</span>";
                }
            }, {
                field: 'imageTag',
                title: '提测版本',
                formatter: function (value) {
                    if(!value){
                        return '--';
                    }
                    return "<span title='"+value+"'>" + value + "</span>";
                }
            }, {
                field: 'releaseDate',
                title: '预计提测时间',
                formatter: function (value, row, index) {
                    if (!value) {
                        return '--';
                    }
                    let dateTime = new Date(value).Format('yyyy-MM-dd hh:mm:ss');
                    return "<span title='"+dateTime+"'>" + dateTime + "</span>";
                }
            }, {
                field: 'createDate',
                title: '申请日期',
                formatter: function (value, row, index) {
                    if (!value) {
                        return '--';
                    }
                    let dateTime = new Date(value).Format('yyyy-MM-dd hh:mm:ss');
                    return "<span title='"+dateTime+"'>" + dateTime + "</span>";
                }
            }, {
                field: 'batchStatus',
                title: '审批状态',
                align: 'center',
                formatter: function (value) {
                    if (!value) {
                        return "已提交申请";
                    }
                    switch (value) {
                        case 'NOT_EXECUTED':
                            return "已提交申请";
                        case 'SUCCESS':
                            return "审批通过";
                        case 'FAILED':
                            return "审批拒绝";
                        case 'IN_PROGRESS':
                            return "审批中";
                        case 'ON_LINE_IN':
                            return "上线中";
                        default:
                            return value;
                    }
                }
            },
            {
                field: 'releaseUserName',
                title: '提测申请人',
                formatter:function(value){
                    if(!value){
                        return '--';
                    }
                    return '<span title="'+value+'">'+value+'</span>' ;
                }
            }, {
                field: 'batchUserName',
                title: '审批负责人',
                formatter:function(value){
                    if(!value){
                        return '--';
                    }
                    return '<span title="'+value+'">'+value+'</span>' ;
                }
            }],
            clickToSelect: true, //设置支持行多选
            search: true, //显示搜索框
            searchOnEnterKey: true,//enter时才search
            toolbar: '#toolbar', //关联工具栏
            showHeader: true,
            showColumns: true, //显示列
            showRefresh: true, //显示刷新按钮
            showToggle: true, //显示切换视图按钮
            showPaginationSwitch: true, //显示数据条数框
            pagination: true, //设置为 true 会在表格底部显示分页条
            paginationLoop: true, //设置为 true 启用分页条无限循环的功能。
            sidePagination: 'server', //设置在哪里进行分页，可选值为 'client' 或者 'server'。
            pageSize: 10,
            pageList: [10, 15, 20, 25, 50],
            paginationHAlign: 'right' //分页条位置
        }
    };

    $scope.zTreeChangeCheck=function(){
        $scope.checkFlag=false;
    }

    $scope.checkTableControl={
    	options:{
    		data:$scope.pipelineList,
    		cache:false,
    		idField:'id',
    		columns: [{
    			field:'projectName',
    			title:'项目',
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
    			field: 'startTimeMillis',
    			title: '开始',
    			align: 'center',
    			sortable: true,
    			formatter:function(value){
    				if(!value){
    					return "--";
    				}
    				let dateTime = new Date(value).Format("yyyy-MM-dd hh:mm:ss")
    				return '<span title="'+dateTime+'">'+dateTime+'</span>' ;
    			}
    		},{
    			field: 'endTimeMillis',
    			title: '结束',
    			align: 'center',
    			formatter:function(value){
    				if(!value){
    					return "--";
    				}
    				let dateTime = new Date(value).Format("yyyy-MM-dd hh:mm:ss")
    				return '<span title="'+dateTime+'">'+dateTime+'</span>' ;
    			}
    		},{
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
    					default:
    						return '<span title="'+value+'">'+value+'</span>';
    				}
    			}
    		},{
    			field:'profileName',
    			title:'执行过程',
    			formatter:function(value){
    				return '<span title="'+value+'">'+value+'</span>' ;
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
			},{
                field:'stages',
                title:'流水线详细过程',
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
    		//clickToSelect:true, //设置支持行多选
    		//search:true, //显示搜索框
    		searchOnEnterKey:true,//enter时才search
    		toolbar:'#toolbar', //关联工具栏
    		showHeader:true,
    		showColumns:true, //显示列
    		showRefresh:true, //显示刷新按钮
    		showToggle:true, //显示切换视图按钮
    		showPaginationSwitch:true, //显示数据条数框
    		//pagination:true, //设置为 true 会在表格底部显示分页条
    		//paginationLoop:true, //设置为 true 启用分页条无限循环的功能。
    		//sidePagination:'server', //设置在哪里进行分页，可选值为 'client' 或者 'server'。
    		//pageSize:10,
    		//pageList:[10,15,20,25,50],
    		//paginationHAlign:'right' //分页条位置
    	}
    };

    $scope.viewDtpReport = function (row) {
        let result = row;
        if(row.releaseStatus == 'SUCCESS' || row.releaseStatus == 'IN_PROGRESS'){
            $http.get(backend.url + '/api/release/getReleaseById?releaseId=' + row.id).then(function(response){
                result = response.data;
                $scope.synRelease(result);
            });
        }else {
            $scope.synRelease(result);
        }
    }

    $scope.synRelease = function(row){
        let pipelineList = row.pipelineList;
        let dtpReports = [];
        for (let i = 0; i < pipelineList.length; i++) {
            dtpReports.push.apply(dtpReports,pipelineList[i].dtpReports);
        }
        let modalInstance = $modal({
                title: '流水线执行详情',
                templateUrl: 'app/pipeline/dtpReport.html',
                show: true,
                animation: 'am-fade-and-scale',
                controller: function ($scope) {
                    $scope.dtpReports = dtpReports;
                    $scope.closeModal = function () {
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

    $scope.treeOption = {//树形基本配置
        view: {
            selectedMulti: true,
            addHoverDom: function (treeId, treeNode) {
                // treeId 对应的是当前 tree dom 元素的 id
                // treeNode 是当前节点的数据
                var aObj = $("#" + treeNode.tId + "_a"); // 获取节点 dom
                if ($("#diyBtnGroup").length > 0) return;
                // 查看是否存在自定义的按钮组，因为 addHoverDom 会触发多次

                var editStr = `<span id='diyBtnGroup'>
                            <span id='diyBtn_space_${treeNode.id}'> </span>
                            <button type='button' class='btn btn-sm btn-success' id='diyBtn_${treeNode.id}_add' οnfοcus='this.blur();'> 查看 </button>
                        </span>`;
                //一级菜单是git路径，git路径是ci在后端拼装的，和redmine上的需求对应不上 本身并没有id。所以需要去掉查看按钮。
                if (treeNode.id) {
                    aObj.append(editStr);
                }
                var btnAdd = $('#diyBtn_' + treeNode.id + '_add');
                if (btnAdd) btnAdd.bind("click", function () {
                    // $scope.entity=treeNode;
                    $http.get(backend.url + "/api/redmine/getIssueDetailById?issueId=" + treeNode.id).then(function (response) {
                        var alterModel = $modal({
                                title: '需求明细查看',
                                templateUrl: 'app/release/alertIssueModel.html',
                                show: true,
                                animation: 'am-fade-and-scale',
                                controller: function ($scope) {
                                    $scope.issueDetail = response.data;
                                    $scope.getLocalTime = function (date) {
                                        var date = new Date(date);
                                        var YY = date.getFullYear() + '-';
                                        var MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
                                        var DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate());
                                        var hh = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
                                        var mm = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
                                        var ss = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
                                        return YY + MM + DD + " " + hh + mm + ss;

                                    }
                                    $scope.issueDetail.createOn = $scope.getLocalTime($scope.issueDetail.createOn);
                                    $scope.issueDetail.dueDate = $scope.getLocalTime($scope.issueDetail.dueDate);
                                    $scope.closeModal = function () {
                                        alterModel.destroy();
                                    }
                                }
                            }
                        );
                    });

                });
            },
            removeHoverDom: (treeId, treeNode) => {
                // 为了方便删除整个 button 组，上面我用 #diyBtnGroup 这个包了起来，这里直接删除外层即可，不用挨个找了。
                $("#diyBtnGroup").unbind().remove();
            },
        },
        callback: {
        	onCheck: $scope.zTreeChangeCheck
        },
        check: {
            enable: true,
            checkboxType: {"Y": "ps", "N": "ps"}
        },
        data: {
            simpleData: {
                enable: true,//是否采用简单数据模式
                idKey: "releaseId",//树节点ID名称
                pIdKey: "releaseParentId",//父节点ID名称
                rootPId: 0,//根节点ID
            },
            key: {
                name: "subject"
            }
        }
    };
    function zTreeBeforeCheck(treeId, treeNode) {
        return false;
    };
    //审批时展示的树，不能修改勾选状态
    $scope.treeOptionBatch = {//树形基本配置
        view: {
            selectedMulti: false,
            addHoverDom: function (treeId, treeNode) {
                // treeId 对应的是当前 tree dom 元素的 id
                // treeNode 是当前节点的数据
                var aObj = $("#" + treeNode.tId + "_a"); // 获取节点 dom
                if ($("#diyBtnGroup").length > 0) return;
                // 查看是否存在自定义的按钮组，因为 addHoverDom 会触发多次

                var editStr = `<span id='diyBtnGroup'>
                            <span id='diyBtn_space_${treeNode.id}'> </span>
                            <button type='button' class='btn btn-sm btn-success'  id='diyBtn_${treeNode.id}_add' οnfοcus='this.blur();'> 查看 </button>
                        </span>`;
                //一级菜单是git路径，git路径是ci在后端拼装的，和redmine上的需求对应不上 本身并没有id。所以需要去掉查看按钮。
                if (treeNode.id) {
                    aObj.append(editStr);
                }
                var btnAdd = $('#diyBtn_' + treeNode.id + '_add');
                if (btnAdd) btnAdd.bind("click", function () {
                    // $scope.entity=treeNode;
                    $http.get(backend.url + "/api/redmine/getIssueDetailById?issueId=" + treeNode.id).then(function (response) {
                        var alterModel = $modal({
                                title: '需求明细查看',
                                templateUrl: 'app/release/alertIssueModel.html',
                                show: true,
                                animation: 'am-fade-and-scale',
                                controller: function ($scope) {
                                    $scope.issueDetail = response.data;
                                    $scope.getLocalTime = function (date) {
                                        var date = new Date(date);
                                        var YY = date.getFullYear() + '-';
                                        var MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
                                        var DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate());
                                        var hh = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
                                        var mm = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
                                        var ss = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
                                        return YY + MM + DD + " " + hh + mm + ss;

                                    }
                                    $scope.issueDetail.createOn = $scope.getLocalTime($scope.issueDetail.createOn);
                                    $scope.issueDetail.dueDate = $scope.getLocalTime($scope.issueDetail.dueDate);
                                    $scope.closeModal = function () {
                                        alterModel.destroy();
                                    }
                                }
                            }
                        );
                    });

                });
            },
            removeHoverDom: (treeId, treeNode) => {
                // 为了方便删除整个 button 组，上面我用 #diyBtnGroup 这个包了起来，这里直接删除外层即可，不用挨个找了。
                $("#diyBtnGroup").unbind().remove();
            },
        },
        callback: {
            beforeCheck: zTreeBeforeCheck
        },
        check: {
            enable: true
        },
        data: {
            simpleData: {
                enable: true,//是否采用简单数据模式
                idKey: "releaseId",//树节点ID名称
                pIdKey: "releaseParentId",//父节点ID名称
                rootPId: 0,//根节点ID
            },
            key: {
                name: "subject"
            }
        }
    };
    $scope.checkAll = function () {
        var treeObj = $.fn.zTree.getZTreeObj("quickTree");
        treeObj.checkAllNodes(true);
    }
    $scope.unCheckAll = function () {
        var treeObj = $.fn.zTree.getZTreeObj("quickTree");
        treeObj.checkAllNodes(false);
    }
    $scope.add = function () {
        $scope.entity = {};
        $scope.$broadcast('ifDialogShow', true);
    }

    $scope.check = function () {
        var treeObj = $.fn.zTree.getZTreeObj("issuesTree");
        var issues = treeObj.getCheckedNodes(true);
        let gitPath = [];
        issues.forEach((item) => {
            if (item.gitlabPath) {
                gitPath.push(item.gitlabPath);
            }
        });
        $http({
        	url:backend.url+"/api/release/getPipelineByGitPath",
        	method:"post",
        	headers: {
                'Content-type': 'application/json;charset=utf-8'
            },
        	data:gitPath
        }).then(function(response){
            //alert(JSON.stringify(response.data));
            $scope.pipelineList=response.data.pipelineList;
            $scope.checkFlag = response.data.checkFlag;
        	jQuery("#checkTableControl").bootstrapTable("refresh");
        	$('#checkTableControl').bootstrapTable('refreshOptions', {data: $scope.pipelineList})
        });
    }

    $scope.save = function () {
        if($scope.checkFlag||!$scope.checkReleasePipeline){
            $scope.entity.releaseDate = new Date($scope.entity.releaseDate);
            $scope.entity = $scope.entity ? $scope.entity : {};
            var treeObj = $.fn.zTree.getZTreeObj("issuesTree");
            var issues = treeObj.getCheckedNodes(true);
            // var nodes = treeObj.transformToArray(issues);
            var nodes = treeObj.getNodes();
            let issue = [];
            issues.forEach((item) => {
                if (item.id) {
                    issue.push(item.id);
                }
            });
            let gitPath = [];
            issues.forEach((item) => {
                if (item.gitlabPath) {
                    gitPath.push(item.gitlabPath);
                }
            });
            $scope.entity.issuesTreeJson=JSON.stringify(nodes);
            $scope.entity.gitPath = gitPath.toString();
            $scope.entity.issuesId = issue.toString();
            $scope.entity.releaseCode = $scope.getUuid();
            $scope.entity.teamName=
            $http.post(backend.url + "/api/release", JSON.parse(JSON.stringify($scope.entity))).then(function (response) {
                alert("保存成功");
                $state.go('release.list');
                jQuery("#table").bootstrapTable("refresh");
            });
        }else{
            alert("未通过门禁判断");
        }

    }
    $scope.updateRelease = function (status) {
        $scope.entity.batchStatus = status;
        $scope.entity.releaseDate = new Date($scope.entity.releaseDate);
        var entity = $scope.entity ? $scope.entity : {};
        var modalInstance = $modal({
                templateUrl: 'app/release/alertBatchModel.html',
                show: true,
                animation: 'am-fade-and-scale',
                controller: function ($scope) {
                    $scope.entity=entity;
                    $scope.closeModal = function () {
                        $scope.entity.batchStatus = 'ABORTED';
                        modalInstance.destroy();

                    }
                    //保存
                    $scope.confirmAdd=function(){
                        if($scope.entity.gitPath.split(",").length>3){
                            $http.post(backend.url + "/api/release/updateRelease", ($scope.entity)).then(function (response) {
                            });
                            alert("审批完成");
                            jQuery("#table").bootstrapTable("refresh");
                            $state.go('release.list');
                        }else{
                            $http.post(backend.url + "/api/release/updateRelease", ($scope.entity)).then(function (response) {
                                alert("审批完成");
                                jQuery("#table").bootstrapTable("refresh");
                                $state.go('release.list');
                            });
                        }
                        modalInstance.destroy();
                    }
                }
            }
        );
    }
    function CurentTime()
    { 
        var now = new Date();
        
        var year = now.getFullYear();       //年
        var month = now.getMonth() + 1;     //月
        var day = now.getDate();            //日
        
        var hh = now.getHours();            //时
        var mm = now.getMinutes();          //分
        
        var clock = year + "-";
        
        if(month < 10)
            clock += "0";
        
        clock += month + "-";
        
        if(day < 10)
            clock += "0";
            
        clock += day + " ";
        
        if(hh < 10)
            clock += "0";
            
        clock += hh + ":";
        if (mm < 10) clock += '0'; 
        clock += mm; 
        return(clock); 
    }
    $scope.add = function () {
        $scope.entity = {
            releaseDate:CurentTime()
        };
        $state.go('release.add');
    }

    $scope.batchImport = function () {
        $state.go('projectBatchImport');
    }

    $scope.scanSvn = function () {
        $state.go('projectSvnScan');
    }
    $scope.getLocalTime = function (date) {
        var date = new Date(date);
        var YY = date.getFullYear() + '-';
        var MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
        var DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate());
        var hh = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
        var mm = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
        var ss = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
        return YY + MM + DD + " " + hh + mm + ss;

    }
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
    $scope.viewDetail = function(row){
        let result = row;
        if(row.releaseStatus == 'SUCCESS'  || row.releaseStatus == 'IN_PROGRESS'  ){
            $http.get(backend.url + '/api/release/getReleaseById?releaseId=' + row.id).then(function(response){
                result = response.data;
                $scope.viewReleaseDetail(result);
            });
        }else {
            $scope.viewReleaseDetail(result);
        }
    }


    //这里相当于是进行审批操作。单独弄一个审批页面
    $scope.viewReleaseDetail = function (row) {
        $scope.entity = angular.copy(row);
        $scope.entity.releaseDate = $scope.getLocalTime($scope.entity.releaseDate);
        $scope.historyTableControl = {
            options: {
                data: $scope.entity.pipelineList?$scope.entity.pipelineList:[],
                cache: false,
                idField: 'id',
                // data:ids,
                queryParams: function (params) {
                    var queryParam = angular.extend({}, params, $scope.condition);
                    return queryParam;
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
                    title:'流水线详细过程',
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
                clickToSelect: true, //设置支持行多选
                search: false, //显示搜索框
                searchOnEnterKey: true,//enter时才search
                toolbar: '#toolbar', //关联工具栏
                showHeader: true,
                showColumns: false, //显示列
                showRefresh: false, //显示刷新按钮
                showToggle: false, //显示切换视图按钮
                showPaginationSwitch: false, //显示数据条数框
                pagination: true, //设置为 true 会在表格底部显示分页条
                paginationLoop: true, //设置为 true 启用分页条无限循环的功能。
                sidePagination: 'client', //设置在哪里进行分页，可选值为 'client' 或者 'server'。
                pageSize: 10,
                pageList: [10, 15, 20, 25, 50],
                paginationHAlign: 'right' //分页条位置
            }
        };
        $scope.getRedmineSprintByTeam();
        $scope.findDockerDigest();
        $state.go('release.batch');

    }
    $scope.viewProfile= function(row){
		$http.get(backend.url+"/api/pipelineProfile/"+row.profileId).then(function(response){
			var modalInstance=$modal({
				title: '自定义构建配置',
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
    $scope.viewStage = function (row) {
		var logId=row.logId;
		var modalInstance=$modal({
				title: '流水线执行详情',
				templateUrl: 'app/pipeline/stage.html',
				show: true,
				animation:'am-fade-and-scale',
				controller:function($scope,$sce){
					if(logId.indexOf("console.txt")!=-1){
							$scope.logUrl=backend.logUrl+"/resources/"+logId;
						}else {
							$scope.logUrl=backend.url+"/resources/"+logId;
						}
					console.log(typeof row.stages)
					$scope.stages=row.stages;
					$scope.accessControls=[]
					$scope.dtpTask = row.dtpTask
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
							if(item.type!="unit_test_coverage_rate"&&item.type!="unit_test_success_rate"){
							$scope.metrics.push(item)
							}
						})
					}else{
						$scope.metrics = checkMetrics
					}
					$scope.guardTypes = guardTypes;
					$scope.showTestResult = function(){
						modalInstance.destroy();
						parentScope.viewDtpReport(row);
					}
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
    $scope.findDockerDigest = function() {
        var teamName="";
        if($scope.redmine.teams&&$scope.entity.teamId){
            for(var i=0;i<$scope.redmine.teams.length;i++){
                if($scope.redmine.teams[i].id==$scope.entity.teamId){
                    teamName=$scope.redmine.teams[i].name;
                }
            }
        }
        $.ajax({
            url: '/ci/api/onLine/findDockerDigest?releaseId=' + $scope.entity.id + "&teamName=" + teamName,
            async: false,
            method: "GET",
            success: function (data, status) {
                $scope.dockerDigests = data;
            }
        });
    }
    $scope.initBatchTree = function() {
        $.fn.zTree.init($("#issuesTreeBatch"), $scope.treeOptionBatch,JSON.parse($scope.entity.issuesTreeJson));
    }
    $scope.back = function () {
        $state.go('release.list');
    }

    //重新执行流水线
    $scope.heavey = function(){
        var selected = jQuery("#table").bootstrapTable("getAllSelections")
        if (!selected || !selected.length) {
            return;
        }
        if(selected.length>1){
            alert("该功能暂不支持多选，请选择单条数据");
            return;
        }
        var release = [];
        for (var i = 0; i < selected.length; i++) {
            release.push(selected[i].batchStatus);
            if(selected[i].batchStatus !='SUCCESS'){
                alert(selected[i].title+" 尚未审批通过");
                return;
            }
        }
        //重试的时候只传id就可以，不需要整条数据。
        if (confirm("确认重新构建流水线？该功能只会执行失败的流水线")) {
            $http.post(backend.url + "/api/release/updateRelease", JSON.parse(JSON.stringify(selected[0]))).then(function (response) {});
            alert("审批完成、稍后请查看流水线执行结果");
            jQuery("#table").bootstrapTable("refresh");
        }
    }

    //删除
    $scope.remove = function () {
        var selected = jQuery("#table").bootstrapTable("getAllSelections")
        if (!selected || !selected.length) {
            return;
        }
        var ids = [];
        for (var i = 0; i < selected.length; i++) {
            ids.push(selected[i].id);
        }
        if (confirm("确认删除选中项？")) {
            $http({
                url: backend.url + "/api/release",
                method: "delete",
                headers: {
                    'Content-type': 'application/json;charset=utf-8'
                },
                data: ids
            }).then(function () {
                alert("删除成功");
                jQuery("#table").bootstrapTable("refresh");
            });
        }
    }

    $scope.showLegacyNotice = function () {
        var modalInstance = $modal({
                title: '非Maven项目注意事项',
                templateUrl: 'app/project/legacy-release.html',
                show: true,
                animation: 'am-fade-and-scale',
                controller: function ($scope) {
                    $scope.closeModal = function () {
                        modalInstance.destroy();
                    }
                }
            }
        );
    }
    $scope.getUuid = function () {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }

    function bug_blocker(row,metrics,flag){
		var val=getMetric(flag,metrics);
		if(val!='N/A'){
			var bugDetail=sonarServer+"/project/issues?id="+row.project.sonarKey+"&resolved=false&types=BUG";
			return "<a href='"+bugDetail+"' target='_blank'>"+val+"</a>";
		}
		return null;
	}
	function vulner_blocker (row,metrics,flag){
		var val=getMetric(flag,metrics);
		if(val!='N/A'){
			var vulnerDetail=sonarServer+"/project/issues?id="+row.project.sonarKey+"&resolved=false&types=VULNERABILITY";
			return  "<a href='"+vulnerDetail+"' target='_blank'>"+val+"</a>";
		}
		return null;
	}
	function unit_test_coverage_rate(row,metrics,flag){
		var val=getMetric(flag,metrics);
		if(val!='N/A'){
			  var vulnerDetail=sonarServer+"/dashboard?id="+row.project.sonarKey;
			  return "<a href='"+vulnerDetail+"' target='_blank'>"+val+"%"+"</a>";
		}
		return null;
	}
	$scope.viewStage = function (row) {
		var logId=row.logId;
		var modalInstance=$modal({
				title: '流水线执行详情',
				templateUrl: 'app/pipeline/stage.html',
				show: true,
				animation:'am-fade-and-scale',
				controller:function($scope,$sce){
					if(logId.indexOf("console.txt")!=-1){
							$scope.logUrl=backend.logUrl+"/resources/"+logId;
						}else {
							$scope.logUrl=backend.url+"/resources/"+logId;
						}
					console.log(typeof row.stages)
					$scope.stages=row.stages;
					$scope.dtpTask = row.dtpTask
					if($scope.dtpTask){
						$scope.dtpReports = row.dtpReports; 
					}
					$scope.title = row.projectName +'——'+row.profileName;
					let obj = {}
					obj.bug_blocker = $sce.trustAsHtml(bug_blocker(row,row.metrics,'bug_blocker'))
					obj.bug_critical = $sce.trustAsHtml(bug_blocker(row,row.metrics,'bug_critical'))
					
					obj.vulner_blocker = $sce.trustAsHtml(vulner_blocker(row,row.metrics,'vulner_blocker'))
					obj.vulner_critical = $sce.trustAsHtml(vulner_blocker(row,row.metrics,'vulner_critical'))

					obj.unit_test_coverage_rate = $sce.trustAsHtml(unit_test_coverage_rate(row,row.metrics,'unit_test_coverage_rate'))
					obj.unit_test_success_rate = $sce.trustAsHtml(unit_test_coverage_rate(row,row.metrics,'unit_test_success_rate'))
					if(obj.bug_blocker===null&&obj.bug_critical===null&&obj.vulner_blocker===null&&obj.vulner_critical===null&&obj.unit_test_coverage_rate===null&&obj.unit_test_success_rate===null){
						$scope.tasks = [];
					}else{
						let list = []
						list.push(obj)
						$scope.tasks = list;
					}
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
    $scope.tableControlPipeline={
        options:{
            url:backend.url+"/api/project/pipelineByGitpaths",
            cache:false,
            idField:'id',
            columns: [{
                field: 'name',
                title: '工程缩写/英文',
                align: 'center',
                sortable: true
            },{
                field:'description',
                title:'中文名称'
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
            },{
    			field:'profileName',
    			title:'执行过程',
    			formatter:function(value){
    				return '<span title="'+value+'">'+value+'</span>' ;
                }
    		},{
                    field:'status',
                    title:'流水线执行状态',
                    align: 'center',
                    formatter:function(value){
                        if(!value){
                            return "--";
                        }
                        return $scope.statusTypes[value];
                    }
                },{
                    field:'stages',
                    title:'流水线详细过程',
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

});

