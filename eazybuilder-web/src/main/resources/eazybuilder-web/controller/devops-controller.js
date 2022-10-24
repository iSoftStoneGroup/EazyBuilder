app.controller('deveopsController', function ($scope, $http, $window, $state, $modal, $filter, basicService) {
    $scope.condition = {};
    $scope.entity = {};
    $scope.page = {};
    $scope.currentUser = JSON.parse($window.sessionStorage.user);

    $scope.namespaceTypes = {
        dev:"开发环境",
        test: "测试环境",
        stage:"预生产环境",
        prod:"生产环境"
    };
    $scope.dingMsgTypes = {
        issues:"需求变动",
        push:"代码提交",
        merge:"分支合并",
        pipeline:"流水线构建状态",
        release:"提测相关流程变动",
        online:"上线相关流程变动"
    };
    $scope.mailMsgTypes = {
        issues:"需求变动",
        push:"代码提交",
        merge:"分支合并",
        pipeline:"流水线构建状态",
        release:"提测相关流程变动",
        online:"上线相关流程变动"
    };
    basicService.getPipelineProfile().then(function (response) {
        $scope.profiles = response.data;
    });
    basicService.getProjectManages().then(function(response){
        $scope.projectManages=response.data;
    });



    $scope.addSpace = function(){
        $scope.entity.teamNamespaces.push({});
    }

    $scope.delSpace = function(index){
        $scope.entity.teamNamespaces.splice(index, 1);
    }

    //先判断是否使用统一登录门户
    $.get(backend.url+"/getPortalInfo", function(response){
        $window.sessionStorage.portal=JSON.stringify(response);
        if(response.used){
            basicService.getUpmsAllUsers().then(function (response) {
                $scope.upmsUsers = response.data;
                $scope.upmsUsersAll = response.data;
            });
        }else{
            basicService.getAllUsers().then(function (response) {
                $scope.upmsUsers = response.data;
                $scope.upmsUsersAll = response.data;
            });
        }
    });

    // basicService.getUpmsAllUsers().then(function (response) {
    //     $scope.upmsUsers = response.data;
    //     $scope.upmsUsersAll = response.data;
    // });
    //
    // basicService.getAllUsers().then(function (response) {
    //     $scope.upmsUsers = response.data;
    //     $scope.upmsUsersAll = response.data;
    // });



    $scope.tableControl = {
        options: {
            url: backend.url + "/api/deveops/getDeveopsPage",
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
                field: 'teamName',
                title: '项目组',
                align: 'center',
                valign: 'bottom',
                sortable: true,
                formatter: function (value, row, index) {
                    return "<a>" + value + "</a>";
                },
                events: {
                    'click a': function (e, value, row, index) {
                        $scope.viewDetail(row);
                    }
                }
            }, {
                field: 'teamCode',
                title: '项目组编号',
                formatter:function(value){
                    if(!value){
                        return '--';
                    }
                    return '<span title="'+value+'">'+value+'</span>' ;
                }
            }, {
                field: 'teamBeginDate',
                title: '项目组开始时间',
                formatter:function(value){
                    if(!value){
                        return '--';
                    }
                    return '<span title="'+value+'">'+value+'</span>' ;
                }
            }, {
                field: 'teamEndDate',
                title: '项目组结束时间',
                formatter:function(value){
                    if(!value){
                        return '--';
                    }
                    return '<span title="'+value+'">'+value+'</span>' ;
                }
            }, {
                field: 'projectInitStatuses',
                title: '初始化情况',
                formatter:function(value){
                    if (!value || value.length == 0) {
                        return '';
                    }
                    var result = [];
                    for (var i = 0; i < value.length; i++) {
                        let data = value[i].status=="SUCCESS"?"成功":"失败";
                        result.push(value[i].projectCode+":"+data);
                    }
                    return result.join(',');
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

    $scope.tableControlLocal = {
        options: {
            url: backend.url + "/api/deveops/getDeveopsPage",
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
                field: 'teamName',
                title: '项目组',
                align: 'center',
                valign: 'bottom',
                sortable: true,
                formatter: function (value, row, index) {
                    return "<a>" + value + "</a>";
                },
                events: {
                    'click a': function (e, value, row, index) {
                        $scope.viewDetailLocal(row);
                    }
                }
            }, {
                field: 'teamCode',
                title: '项目组编号',
                formatter:function(value){
                    if(!value){
                        return '--';
                    }
                    return '<span title="'+value+'">'+value+'</span>' ;
                }
            }, {
                field: 'teamBeginDate',
                title: '项目组开始时间',
                formatter:function(value){
                    if(!value){
                        return '--';

                    }
                    return '<span title="'+value+'">'+value+'</span>' ;
                }
            }, {
                field: 'teamEndDate',
                title: '项目组结束时间',
                formatter:function(value){
                    if(!value){
                        return '--';
                    }
                    return '<span title="'+value+'">'+value+'</span>' ;
                }
            }, {
                field: 'projectInitStatuses',
                title: '初始化情况',
                formatter:function(value){
                    if (!value || value.length == 0) {
                        return '';
                    }
                    var result = [];
                    for (var i = 0; i < value.length; i++) {
                        let data = value[i].status=="SUCCESS"?"成功":"失败";
                        result.push(value[i].projectCode+":"+data);
                    }
                    return result.join(',');
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
    //重新执行流水线
    $scope.retryInit = function () {
        var selected = jQuery("#table").bootstrapTable("getAllSelections")
        if (!selected || !selected.length) {
            return;
        }
        var ids = [];
        if(selected.length>1){
            alert("只能查看单个项目的历史数据");
            return;
        }
        var entity=angular.copy(selected[0]);

        $.ajax({
            url: backend.url+"/api/upms/getUpmsUsers?groupId="+entity.groupId,
            async: false,
            method: "GET",
            success: function (data, status) {
                entity.devopsUsers = JSON.parse(data);
            }
        });
        $http.post(backend.url + "/api/deveops/init", entity).then(function (response) {
            alert("已重新执行,稍后请在列表中查看初始化结果");
            jQuery("#table").bootstrapTable("refresh");
        });

    }
    $scope.matchTeamCode = function(){
        var teamCode = $scope.entity.teamCode;
        var regTeamCode = /^[a-z]+$/;
        if (!regTeamCode.test(teamCode)) {
            alert("项目组名仅支持小写英文");
        }
    }
    $scope.sendInit = function () {
        var teamCode = $scope.entity.teamCode;
        var regTeamCode = /^[a-z]+$/;
        if (!regTeamCode.test(teamCode)) {
            alert("项目组名仅支持小写英文");
        }else {
            // $scope.entity.devopsUsers = $scope.upmsUsers.users;
            if($scope.entity.projectManageId){
                for(var i=0;i<$scope.projectManages.length;i++){
                    if($scope.entity.projectManageId==$scope.projectManages[i].id){
                        $scope.entity.projectManageName=$scope.projectManages[i].name;
                        break;
                    }
                }
            }
            $http.post(backend.url + "/api/deveops/init", $scope.entity).then(function (response) {
                alert("初始化信息已成功发送到各个平台");
                $state.go('deveops.list');
            })
        }
    }

    $scope.sendInitLocal = function () {
        var teamCode = $scope.entity.teamCode;
        var regTeamCode = /^[a-z]+$/;
        if (!regTeamCode.test(teamCode)) {
            alert("项目组名仅支持小写英文");
        }else {
            // $scope.entity.devopsUsers = $scope.upmsUsers.users;
            if($scope.entity.projectManageId){
                for(var i=0;i<$scope.projectManages.length;i++){
                    if($scope.entity.projectManageId==$scope.projectManages[i].id){
                        $scope.entity.projectManageName=$scope.projectManages[i].name;
                        break;
                    }
                }
            }
            $http.post(backend.url + "/api/deveops/init", $scope.entity).then(function (response) {
                alert("初始化信息已成功发送到各个平台");
                $state.go('deveopsLocal.list');
            })
        }
    }

    //删除
    $scope.removeUser = function () {
        var selected = jQuery("#tableControlUser").bootstrapTable("getAllSelections")
        if (!selected || !selected.length) {
            return;
        }
        var ids = [];
        for (var i = 0; i < selected.length; i++) {
            ids.push(selected[i].id);
            if ($scope.entity.devopsUsers) {
                for (var ii = 0; ii < $scope.entity.devopsUsers.length; ii++) {
                    if ($scope.entity.devopsUsers[ii].userId == selected[i].userId) {
                        $scope.entity.devopsUsers.splice(ii, 1);
                        ii--;
                    }
                }
            }
        }
        $('#tableControlUser').bootstrapTable('refreshOptions', {data: $scope.entity.devopsUsers})
    }
    $scope.showAddModel = function () {
        $scope.upmsUsers = $scope.upmsUsersAll;
        var entity = $scope.entity;
        var upmsUsers = $scope.upmsUsers;
        if ($scope.entity.devopsUsers) {
            for (var i = 0; i < upmsUsers.length; i++) {
                for (var ii = 0; ii < $scope.entity.devopsUsers.length; ii++) {
                    if ($scope.entity.devopsUsers[ii].userId == upmsUsers[i].userId) {
                        upmsUsers.splice(i, 1);
                        i--;
                        break;
                    }
                }
            }
        }
        var modalInstance = $modal({
                title: '选择项目组用户',
                templateUrl: 'app/devops/detail/userModal.html',
                show: true,
                animation: 'am-fade-and-scale',
                controller: function ($scope) {
                    $scope.entity = entity;
                    $scope.addUserTableControl = {
                        options: {
                            data: upmsUsers,
                            cache: false,
                            idField: 'email',
                            // data:ids,
                            queryParams: function (params) {
                                var queryParam = angular.extend({}, params, $scope.condition);
                                return queryParam;
                            },
                            columns: [{
                                field: 'state',
                                checkbox: true //设置多选
                            }, {
                                field: 'userName',
                                title: '用户名',
                                align: 'center',
                                sortable: true,
                                formatter: function (value, row, index) {
                                    return "<a>" + value + "</a>";
                                }
                            }, {
                                field: 'nickName',
                                title: '昵称'
                            }, {
                                field: 'email',
                                title: '邮箱'
                            }, {
                                field: 'employeeId',
                                title: '工号'
                            }, {
                                field: 'phoneNumber',
                                title: '手机号'
                            },{
                                field: 'deptName',
                                title: '机构名称'
                            }],
                            clickToSelect: true, //设置支持行多选
                            search: true, //显示搜索框
                            searchOnEnterKey: false,//enter时才search
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
                    $scope.closeModal = function () {
                        modalInstance.destroy();
                    }
                    //保存
                    $scope.confirmAdd = function () {
                        var selected = jQuery("#addUserTableControl").bootstrapTable("getAllSelections")
                        if (!selected || !selected.length) {
                            return;
                        }
                        if (!$scope.entity.devopsUsers) {
                            $scope.entity.devopsUsers = [];
                        }
                        for (var i = 0; i < selected.length; i++) {
                            $scope.entity.devopsUsers.push(selected[i]);
                        }
                        modalInstance.destroy();
                        jQuery("#tableControlUser").bootstrapTable("refresh");
                        $('#tableControlUser').bootstrapTable('refreshOptions', {data: $scope.entity.devopsUsers})
                        $scope.$applyAsync();
                    }

                }
            }
        );
    }

    $scope.showAddModelLocal = function () {
        $scope.upmsUsers = $scope.upmsUsersAll;
        var entity = $scope.entity;
        var upmsUsers = $scope.upmsUsers;
        if ($scope.entity.devopsUsers) {
            for (var i = 0; i < upmsUsers.length; i++) {
                for (var ii = 0; ii < $scope.entity.devopsUsers.length; ii++) {
                    if ($scope.entity.devopsUsers[ii].email == upmsUsers[i].email) {
                        upmsUsers.splice(i, 1);
                        i--;
                        break;
                    }
                }
            }
        }
        var modalInstance = $modal({
                title: '选择项目组用户',
                templateUrl: 'app/devopsLocal/detail/userModal.html',
                show: true,
                animation: 'am-fade-and-scale',
                controller: function ($scope) {
                    $scope.entity = entity;
                    $scope.addUserTableControl = {
                        options:{
                            // url:backend.url+"/api/user/page",
                            data: upmsUsers,
                            cache:false,
                            idField: 'email',
                            queryParams:function(params){
                                var queryParam=angular.extend({},params,$scope.condition);
                                return queryParam;
                            },
                            columns: [{
                                field:'state',
                                checkbox:true, //设置多选
                            }, {
                                field: 'name',
                                title: '用户名',
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
                            },{
                                field:'department',
                                title:'机构名称',
                                formatter:function(value){
                                    if(!value){
                                        return '--';
                                    }
                                    return '<span title="'+value+'">'+value+'</span>' ;
                                }
                            }],
                            clickToSelect: true, //设置支持行多选
                            search: true, //显示搜索框
                            searchOnEnterKey: false,//enter时才search
                            toolbar: '#toolbar', //关联工具栏
                            showHeader: true,
                            showColumns: false, //显示列
                            showRefresh: false, //显示刷新按钮
                            showToggle: false, //显示切换视图按钮
                            showPaginationSwitch: false, //显示数据条数框
                            pagination: true, //设置为 true 会在表格底部显示分页条
                            paginationLoop: true, //设置为 true 启用分页条无限循环的功能。
                            sidePagination: 'server', //设置在哪里进行分页，可选值为 'client' 或者 'server'。
                            pageSize: 10,
                            pageList: [10, 15, 20, 25, 50],
                            paginationHAlign: 'right' //分页条位置
                        }
                    };
                    $scope.closeModal = function () {
                        modalInstance.destroy();
                    }
                    //保存
                    $scope.confirmAdd = function () {
                        var selected = jQuery("#addUserTableControl").bootstrapTable("getAllSelections")
                        if (!selected || !selected.length) {
                            return;
                        }

                        if (!$scope.entity.devopsUsers) {
                            $scope.entity.devopsUsers = [];
                        }
                        // for (var i = 0; i < selected.length; i++) {
                        //     $scope.entity.devopsUsers.push(selected[i]);
                        // }
                        for (var i = 0; i < selected.length; i++) {
                            selected[i].userName=selected[i].name;
                            selected[i].phoneNumber=selected[i].phone;
                            selected[i].nickName=selected[i].name;
                            selected[i].inTeam=false;
                            selected[i].userId=selected[i].id;
                            selected[i].deptName=selected[i].department;
                            console.log(selected[i]);
                            $scope.entity.devopsUsers.push(selected[i]);
                        }
                        modalInstance.destroy();
                        jQuery("#tableControlUser").bootstrapTable("refresh");
                        $('#tableControlUser').bootstrapTable('refreshOptions', {data: $scope.entity.devopsUsers})
                        $scope.$applyAsync();
                    }

                }
            }
        );
    }

    $scope.addProject = function () {
        var entity = $scope.entity;
        var profiles = $scope.profiles;
        var modalInstance = $modal({
                templateUrl: 'app/devops/detail/formEdit2.html',
                show: true,
                animation: 'am-fade-and-scale',
                controller: function ($scope) {
                    $scope.entity = entity;
                    $scope.devopsProject = {deployConfigList: {}};
                    $scope.profiles = profiles;
                    $scope.closeModal = function () {
                        modalInstance.destroy();
                    }
                    $scope.createDeployConfig = function () {
                        var description = $scope.devopsProject.description;
                        var regTeamCode = /^[A-Za-z]+[A-Za-z-]*$/;
                        if (!(regTeamCode.test(description))){
                            alert("项目英文名称: 必须以字母开头，只能包含 字母 或 中划线- ");
                        }else{
                            $scope.devopsProject.deployConfigList = [{
                                name: $scope.devopsProject.description,
                                ingressHost: $scope.devopsProject.description + ".iss-devops.cn",
                                imageTag: $scope.devopsProject.description,
                                appType:"deployment",
                                limitsCpu: "100m",
                                limitsMemory: "1Gi",
                                replicas: "1"
                            }]
                            if ($scope.devopsProject.projectType == 'java') {
                                $scope.devopsProject.deployConfigList[0].containerPort = "8080"
                            } else if ($scope.devopsProject.projectType == 'npm') {
                                $scope.devopsProject.deployConfigList[0].containerPort = "80"
                            }
                        }
                    }

                    //保存
                    $scope.saveProject = function () {
                        var projectCode = $scope.entity.projectCode;
                        var regprojectCode = /^[a-z0-9_-]{1,}$/;

                        var scmUrl = $scope.devopsProject.scmUrl;
                        var regTeamCode1 = /^[-A-Za-z0-9+&@#/%?=~_|!:,.;]+(.git)$/;

                        if (!regprojectCode.test(projectCode)) {
                            alert("项目编号由数字、26个小写英文字母或者下划线、中划线组成的字符串");
                        }
                        else if(!regTeamCode1.test(scmUrl)) {
                            alert("源码仓库地址必须以 .git结尾");
                        }
                        else {
                            if (!$scope.entity.devopsProjects) {
                                $scope.entity.devopsProjects = [];
                            }
                            if (!$scope.entity.devopsUsers) {
                                $scope.entity.devopsUsers = [];
                            }
                            if (!$scope.devopsProject.scmUrl) {
                                $scope.devopsProject.scmUrl = backend.gitUrl + "/" + $scope.entity.teamCode + "/" + $scope.devopsProject.description + ".git"
                            }
                            $scope.entity.devopsProjects.push($scope.devopsProject);
                            alert("保存成功");
                            modalInstance.destroy();
                            jQuery("#tableControlProject").bootstrapTable("refresh");
                        }

                    }

                }
            }
        );

    }

    $scope.add = function () {
        $scope.entity = {
            devopsUsers: [],
            devopsProjects: [],
            deployConfigList: [],
            teamNamespaces : []
        };
        $scope.showTab = 1;
        $scope.tableControlProject = {
            options: {
                data: $scope.entity.devopsProjects,
                cache: false,
                idField: 'id',
                // data:ids,
                queryParams: function (params) {
                    var queryParam = angular.extend({}, params, $scope.condition);
                    return queryParam;
                },
                columns: [{
                    field: 'state',
                    checkbox: true //设置多选
                }, {
                    field: 'description',
                    title: '工程缩写/英文',
                    align: 'center',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return "<a>" + value + "</a>";
                    },
                    events: {
                        'click a': function (e, value, row, index) {
                            $scope.viewDetailProject(row);
                        }
                    }
                }, {
                    field: 'projectName',
                    title: '中文名称'
                }, {
                    field: 'projectType',
                    title: '项目类型'
                }, {
                    field: 'legacyProject',
                    title: '类型',
                    formatter: function (value) {
                        if (value) {
                            return 'ANT';
                        } else {
                            return 'Maven';
                        }
                    }
                }],
                clickToSelect: true, //设置支持行多选
                search: false, //显示搜索框
                searchOnEnterKey: false,//enter时才search
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
        $scope.tableControlUser = {
            options: {
                data: $scope.entity.devopsUsers,
                cache: false,
                idField: 'id',
                // data:ids,
                queryParams: function (params) {
                    var queryParam = angular.extend({}, params, $scope.condition);
                    return queryParam;
                },
                columns: [{
                    field: 'state',
                    checkbox: true //设置多选
                },{
                    field: 'userName',
                    title: '用户名'
                }, {
                    field: 'nickName',
                    title: '昵称'
                }, {
                    field: 'email',
                    title: '邮箱'
                },{
                    field: 'employeeId',
                    title: '工号'
                }, {
                    field: 'phoneNumber',
                    title: '手机号'
                },{
                    field: 'deptName',
                    title: '机构名称'
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
        $state.go('deveops.add');
    }


    $scope.addLocal = function () {
        $scope.entity = {
            devopsUsers: [],
            devopsProjects: [],
            deployConfigList: [],
            teamNamespaces : []
        };
        $scope.showTab = 1;
        $scope.tableControlProject = {
            options: {
                data: $scope.entity.devopsProjects,
                cache: false,
                idField: 'id',
                // data:ids,
                queryParams: function (params) {
                    var queryParam = angular.extend({}, params, $scope.condition);
                    return queryParam;
                },
                columns: [{
                    field: 'state',
                    checkbox: true //设置多选
                }, {
                    field: 'description',
                    title: '工程缩写/英文',
                    align: 'center',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return "<a>" + value + "</a>";
                    },
                    events: {
                        'click a': function (e, value, row, index) {
                            $scope.viewDetailProject(row);
                        }
                    }
                }, {
                    field: 'projectName',
                    title: '中文名称'
                }, {
                    field: 'projectType',
                    title: '项目类型'
                }, {
                    field: 'legacyProject',
                    title: '类型',
                    formatter: function (value) {
                        if (value) {
                            return 'ANT';
                        } else {
                            return 'Maven';
                        }
                    }
                }],
                clickToSelect: true, //设置支持行多选
                search: false, //显示搜索框
                searchOnEnterKey: false,//enter时才search
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
        $scope.tableControlUser = {
            options: {
                data: $scope.entity.devopsUsers,
                cache: false,
                idField: 'id',
                // data:ids,
                queryParams: function (params) {
                    var queryParam = angular.extend({}, params, $scope.condition);
                    return queryParam;
                },
                columns: [{
                    field: 'state',
                    checkbox: true //设置多选
                },{
                    field: 'name',
                    title: '用户名'
                }, {
                    field: 'email',
                    title: '邮箱'
                },{
                    field: 'phone',
                    title: '手机号'
                },{
                    field: 'department',
                    title: '机构名称'
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
        $state.go('deveopsLocal.add');
    }

    $scope.save = function () {
        $http.post(backend.url + "/api/deveops", $scope.entity).then(function (response) {
            alert("保存成功");
            $state.go("deveops.list");
        });
    }

    $scope.saveLocal = function () {
        $http.post(backend.url + "/api/deveops", $scope.entity).then(function (response) {
            alert("保存成功");
            $state.go("deveopsLocal.list");
        });
    }

    $scope.viewDetailProject = function (row) {
        if (!row.deployConfigList || !row.deployConfigList.length > 0) {
            row.deployConfigList=[{}];
        }
        var devopsProject = row;
        var entity = $scope.entity;
        var modalInstance = $modal({
                templateUrl: 'app/devops/detail/formEdit2.html',
                show: true,
                animation: 'am-fade-and-scale',
                controller: function ($scope) {
                    $scope.entity = entity;
                    $scope.devopsProject = devopsProject;

                    $scope.addQueryParam = function () {
                        if (!$scope.entity.deployConfigList) {
                            $scope.entity.deployConfigList = [{}];
                        }
                        $scope.entity.deployConfigList.push({});
                    }
                    $scope.delQueryParam = function (index) {
                        $scope.entity.deployConfigList.splice(index, 1);
                    }
                    $scope.closeModal = function () {
                        modalInstance.destroy();
                    }
                    //保存
                    $scope.saveProject = function () {
                        var projectCode = $scope.entity.projectCode;
                        var regprojectCode = /^[a-z0-9_-]{1,}$/;
                        if (!regprojectCode.test(projectCode)) {
                            alert("项目编号由数字、26个小写英文字母或者下划线、中划线组成的字符串");
                        } else {
                            if (!$scope.entity.devopsProjects) {
                                $scope.entity.devopsProjects = [];
                            }
                            if (!$scope.entity.devopsUsers) {
                                $scope.entity.devopsUsers = [];
                            }
                            for (j = 0, len = $scope.entity.devopsProjects.length; j < len; j++) {
                                if ($scope.entity.devopsProjects[j].id == $scope.devopsProject.id) {
                                    console.log($scope.entity.devopsProjects[j]);
                                    $scope.entity.devopsProjects.splice(j, 1);
                                }
                            }
                            $scope.entity.devopsProjects.push($scope.devopsProject);
                            alert("保存成功");
                            modalInstance.destroy();
                            jQuery("#tableControlProject").bootstrapTable("refresh");
                        }

                    }

                }
            }
        );
    }

    $scope.changeName = function(namespace){
        namespace.name = $scope.namespaceTypes[namespace.namespaceType];
    }


    $scope.viewDetail = function (row) {
        $scope.entity = angular.copy(row);
        $scope.entity.teamNamespaces = $scope.entity.teamNamespaces? $scope.entity.teamNamespaces : [];
        $.ajax({
            url: backend.url+"/api/upms/getUpmsUsers?groupId="+row.groupId,
            async: false,
            method: "GET",
            success: function (data, status) {
                $scope.entity.devopsUsers = JSON.parse(data);
                console.log(JSON.parse(data));
            }
        });
        $scope.tableControlUser = {
            options: {
                data:$scope.entity.devopsUsers,
                cache: false,
                idField: 'id',
                // data:ids,
                queryParams: function (params) {
                    var queryParam = angular.extend({}, params, $scope.condition);
                    return queryParam;
                },
                columns: [{
                    field: 'state',
                    checkbox: true //设置多选
                }, {
                    field: 'userName',
                    title: '用户名'
                }, {
                    field: 'nickName',
                    title: '昵称'
                }, {
                    field: 'email',
                    title: '邮箱'
                }, {
                    field: 'phoneNumber',
                    title: '手机号'
                },{
                    field: 'employeeId',
                    title: '工号'
                },{
                    field: 'deptName',
                    title: '机构名称'
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
        $scope.tableControlProject = {
            options: {
                // url:backend.url+"/api/devopsProject/getProjectPageById?teamId="+$scope.entity.deveopsTeamId,
                data: $scope.entity.devopsProjects,
                cache: false,
                idField: 'id',
                // data:ids,
                queryParams: function (params) {
                    var queryParam = angular.extend({}, params, $scope.condition);
                    return queryParam;
                },
                columns: [{
                    field: 'state',
                    checkbox: true //设置多选
                }, {
                    field: 'description',
                    title: '工程缩写/英文',
                    align: 'center',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return "<a>" + value + "</a>";
                    },
                    events: {
                        'click a': function (e, value, row, index) {
                            $scope.viewDetailProject(row);
                        }
                    }
                }, {
                    field: 'projectName',
                    title: '中文名称'
                }, {
                    field: 'projectType',
                    title: '项目类型'
                }, {
                    field: 'legacyProject',
                    title: '类型',
                    formatter: function (value) {
                        if (value) {
                            return 'ANT';
                        } else {
                            return 'Maven';
                        }
                    }
                }],
                clickToSelect: true, //设置支持行多选
                search: false, //显示搜索框
                searchOnEnterKey: false,//enter时才search
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
        $state.go('deveops.edit');
    }

    $scope.viewDetailLocal = function (row) {
        $scope.entity = angular.copy(row);
        $scope.entity.teamNamespaces = $scope.entity.teamNamespaces? $scope.entity.teamNamespaces : [];
        $.ajax({
            url: backend.url+"/api/local/getLocalUsers?groupId="+row.groupId,
            async: false,
            method: "GET",
            success: function (data, status) {
                var json = JSON.parse(data);
                console.log(json);
                for(var i = 0; i < json.length; i++) {
                    json[i]['phone'] = json[i]['phoneNumber'];
                    json[i]['name'] = json[i]['userName'];
                    // delete json[i]['phoneNumber'];
                    // delete json[i]['userName'];
                }
                $scope.entity.devopsUsers = json;
                console.log($scope.entity.devopsUsers);

            }
        });
        $scope.tableControlUser = {
            options: {
                data:$scope.entity.devopsUsers,
                cache: false,
                idField: 'id',
                // data:ids,
                queryParams:function(params){
                    var queryParam=angular.extend({},params,$scope.condition);
                    return queryParam;
                },
                columns: [{
                    field:'state',
                    checkbox:true, //设置多选
                }, {
                    field: 'name',
                    title: '用户名',
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
                },{
                    field:'department',
                    title:'机构名称',
                    formatter:function(value){
                        if(!value){
                            return '--';
                        }
                        return '<span title="'+value+'">'+value+'</span>' ;
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
        $scope.tableControlProject = {
            options: {
                // url:backend.url+"/api/devopsProject/getProjectPageById?teamId="+$scope.entity.deveopsTeamId,
                data: $scope.entity.devopsProjects,
                cache: false,
                idField: 'id',
                // data:ids,
                queryParams: function (params) {
                    var queryParam = angular.extend({}, params, $scope.condition);
                    return queryParam;
                },
                columns: [{
                    field: 'state',
                    checkbox: true //设置多选
                }, {
                    field: 'description',
                    title: '工程缩写/英文',
                    align: 'center',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return "<a>" + value + "</a>";
                    },
                    events: {
                        'click a': function (e, value, row, index) {
                            $scope.viewDetailProject(row);
                        }
                    }
                }, {
                    field: 'projectName',
                    title: '中文名称'
                }, {
                    field: 'projectType',
                    title: '项目类型'
                }, {
                    field: 'legacyProject',
                    title: '类型',
                    formatter: function (value) {
                        if (value) {
                            return 'ANT';
                        } else {
                            return 'Maven';
                        }
                    }
                }],
                clickToSelect: true, //设置支持行多选
                search: false, //显示搜索框
                searchOnEnterKey: false,//enter时才search
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
        $state.go('deveopsLocal.edit');
    }

    $scope.back = function () {
        $state.go('deveops.list');
    }
    $scope.backLocal = function () {
        $state.go('deveopsLocal.list');
    }

    //删除
    $scope.removeProject = function () {
        var selected = jQuery("#tableControlProject").bootstrapTable("getAllSelections")
        if (!selected || !selected.length) {
            return;
        }

        var ids = [];
        for (var i = 0; i < selected.length; i++) {
            ids.push(selected[i].id);
        }
        var data = {ids, id: $scope.entity.id, type: "project"};
        if (confirm("确认删除选中项？")) {
            $http({
                url: backend.url + "/api/deveops/delete",
                method: "post",
                headers: {
                    'Content-type': 'application/json;charset=utf-8'
                },
                data: data
            }).then(function () {
                alert("删除成功");
                jQuery("#tableControlProject").bootstrapTable("refresh");
            });
        }
    }
});

