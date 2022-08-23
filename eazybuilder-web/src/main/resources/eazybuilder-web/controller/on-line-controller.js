app.controller('onLineController', function ($scope, $http, $window, $state, $filter, $modal, basicService) {
    $scope.condition = {};
    $scope.entity = {};
    $scope.redmine = {};
    $scope.release = {};
    $scope.dockerDigests = [];

    basicService.getRedmineTeam().then(function (response) {
        $scope.redmine.teams = response.data;
    });

    $scope.getIssuesByReleaseId = function () {
        var teamName="";
        if($scope.redmine.teams&&$scope.entity.teamId){
            for(var i=0;i<$scope.redmine.teams.length;i++){
                if($scope.redmine.teams[i].id==$scope.entity.teamId){
                    teamName=$scope.redmine.teams[i].name;
                }
            }
        }
        $http.get('/ci/api/onLine/getIssuesAndReportByReleaseId?releaseId=' + $scope.entity.releaseId).then(function(response){
            $scope.dtpReports = response.data.dtpReports;
            $.fn.zTree.init($("#issuesTreeBatch"), $scope.treeOptionBatch,JSON.parse(response.data.issuesTreeJson));
        });

        $http.get('/ci/api/onLine/findDockerDigest?releaseId=' + $scope.entity.releaseId + "&teamName=" + teamName).then(function(response){
            $scope.dockerDigests = response.data;
        });
    }

    //提测是时查询看板下的需求，上线时是查血看板下的tag
    $scope.getApplyOnlineTags = function (batch) {
        $http.get('/ci/api/release/getApplyOnlineTags?sprintId=' + $scope.entity.sprintId).then(function(response){
            $scope.release.tags = response.data;
        });
    }
    $scope.getRedmineSprintAndUsersByTeam = function () {
            let teamName=null;
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
            url: backend.url + "/api/onLine/page",
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
                title: '上线标题',
                align: 'center',
                sortable: true,
                formatter: function (value, row, index) {
                    if(!value){
                        return '--';
                    }
                    return "<a title'"+value+"'>" + value + "</a>";
                },
                events: {
                    'click a': function (e, value, row, index) {
                        $scope.viewDetail(row);
                    }
                }
            }, {
                field: 'releaseCode',
                title: '上线申请号',
                formatter: function(value){
                    if(!value){
                        return '--';
                    }
                    return '<span title="'+value+'">'+value+'</span>' ;
                }
            }, {
                field: 'imageTag',
                title: '提测版本',
                formatter: function(value){
                    if(!value){
                        return '--';
                    }
                    return '<span title="'+value+'">'+value+'</span>' ;
                }
            }, {
                field: 'onLineImageTag',
                title: '上线版本',
                formatter: function(value){
                    if(!value){
                        return '--';
                    }
                    return '<span title="'+value+'">'+value+'</span>' ;
                }
            }, {
                field: 'releaseDate',
                title: '预计上线时间',
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
                        default:
                            return value;
                    }
                }
            }, {
                field: 'releaseUserName',
                title: '上线申请人',
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
            }, {
                field: 'memberName',
                title: '上线执行人',
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
    $scope.$on("uploaded",function(event,eventData){
        $scope.entity.dtpReportUrl=eventData.data.id;
    });
    $scope.viewDtpReport = function (row) {
        $.ajax({
            url: '/ci/api/release/getReleaseById?releaseId=' + $scope.entity.releaseId,
            async: false,
            method: "GET",
            success: function (data, status) {
                var pipelineList = data.pipelineList;
            }
        });
        var dtpReport = [];
        var pipelineList = row.pipelineList;
        for (var i = 0; i < pipelineList.length; i++) {
            var dtpReports = pipelineList[i].dtpReports;
            // dtpReport.push(dtpReports);
            dtpReport.push.apply(dtpReport,dtpReports);
        }
        var modalInstance = $modal({
                title: '流水线执行详情',
                templateUrl: 'app/pipeline/dtpReport.html',
                show: true,
                animation: 'am-fade-and-scale',
                controller: function ($scope) {
                    $scope.dtpReports = dtpReport;
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
    function zTreeBeforeCheck(treeId, treeNode) {
        return false;
    };
//审批时展示的树，不能修改复选框
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
                if(treeNode.id){
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
    $scope.add = function () {
        $scope.entity = {};

        $scope.$broadcast('ifDialogShow', true);
    }
    $scope.save = function () {
        $scope.entity.releaseDate = new Date($scope.entity.releaseDate);
        $scope.entity = $scope.entity ? $scope.entity : {};
        $scope.entity.releaseCode = $scope.getUuid();

        if($scope.entity.releaseId){
            for(var i=0;i<$scope.release.tags.length;i++){
                    if($scope.entity.releaseId==$scope.release.tags[i].id){
                        $scope.entity.imageTag = $scope.release.tags[i].imageTag;
                }
            }
        }

        if($scope.entity.batchUserId){
            for(let i=0;i<$scope.members.length;i++){
                if($scope.members[i].id==$scope.entity.batchUserId){
                    $scope.batchUserId=$scope.members[i].id;
                    break;
                }
            }
        }

        $http.post(backend.url + "/api/onLine", JSON.parse(JSON.stringify($scope.entity))).then(function (response) {
            alert("保存成功");
            $state.go('onLine.list');
            jQuery("#table").bootstrapTable("refresh");
        });
    }

    $scope.getUserByTeamName = function () {

    }
    $scope.updateRelease = function (status) {
        if(status=='SUCCESS'){
            $scope.getUserByTeamName();
        }
        $scope.entity.batchStatus = status;
        $scope.entity.releaseDate = new Date($scope.entity.releaseDate);
        var entity = $scope.entity ? $scope.entity : {};
            var members = $scope.members;
            var modalInstance = $modal({
                    templateUrl: 'app/onLine/alertBatchModel.html',
                    show: true,
                    animation: 'am-fade-and-scale',
                    controller: function ($scope) {
                        $scope.members = members;
                        $scope.entity = entity;
                        $scope.closeModal = function () {
                            modalInstance.destroy();
                        }
                        //保存
                        $scope.confirmAdd=function(){
                            if(status=='SUCCESS') {
                                for (var i = 0; i < members.length; i++) {
                                    if (members[i].id == $scope.entity.memberId) {
                                        $scope.entity.memberName = members[i].name;
                                    }
                                }
                            }
                            $http.post(backend.url + "/api/onLine/updateRelease", JSON.parse(JSON.stringify($scope.entity))).then(function (response) {
                                alert("审批成功");
                                $state.go('onLine.list');
                                jQuery("#table").bootstrapTable("refresh");
                            });
                            modalInstance.destroy();
                        }
                    }
                }
            );
    }
    function CurentTime(){ 
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
        $scope.dockerDigests = [];
        $state.go('onLine.add');
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
    //这里相当于是进行审批操作。单独弄一个审批页面
    $scope.viewDetail = function (row) {
        $scope.entity = angular.copy(row);
        $scope.entity.releaseDate = $scope.getLocalTime($scope.entity.releaseDate);
        $state.go('onLine.batch');
        $scope.getRedmineSprintAndUsersByTeam();
        $scope.getApplyOnlineTags();

    }

    $scope.back = function () {
        $state.go('onLine.list');
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
                url: backend.url + "/api/onLine",
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
                templateUrl: 'app/project/legacy-onLine.html',
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

});

