app.controller('dashboardController', function($scope,$http,$window,$state,$filter,basicService) {
	basicService.getTotalPorject().then(function(response){
		$scope.totalProject=response.data;
	});
	basicService.getTotalCodeLine().then(function(response){
		var total=response.data;
		if(total>1000){
			$scope.totalCodeLine=Math.round(total/1000)+"K";
		}else{
			$scope.totalCodeLine=response.data;
		}
	});
	basicService.getTotalBuild().then(function(response){
		$scope.totalBuild=response.data;
	});
	basicService.getTotalDevelopers().then(function(response){
		$scope.totalDevelopers=response.data;
	});
	
	$scope.drillTo=function(routeName){
		$state.go(routeName);
	}

});

app.controller('bugChartController',function($scope,$http,basicService){
	$http.get(backend.url+'/api/dashboard/top10BugOrderTeam').then(function(response){
        $scope.responseData = response.data;
        $scope.refreshData();
	});

	$scope.refreshData = function () {
        var labels=[];
        var series=['(阻断+严重)'];
        var serieData=[];
        $scope.teams =  Array.from(new Set($scope.responseData.map(team => team.teamName)));
        let arr =  $scope.team ?  $scope.responseData.filter(team=>team.teamName == $scope.team) : $scope.responseData;
        for(var i=0;i<arr.length;i++){
            var dataPoint=arr[i];
            labels.push(dataPoint.projectName);
            serieData.push(dataPoint.total);
        }
        $scope.labels=labels;
        $scope.series=series;
        $scope.data=[serieData];
        $scope.colours = ['#72C02C', '#3498DB', '#717984', '#F1C40F'];
    }
});

app.controller('vulnerChartController',function($scope,$http,basicService){
	$http.get(backend.url+'/api/dashboard/top10Vulner').then(function(response){
        $scope.responseData = response.data;
        $scope.refreshData();
	});
    $scope.refreshData = function () {
        var labels=[];
        var series=['安全漏洞(阻断)'];
        var serieData=[];
        $scope.teams =  Array.from(new Set($scope.responseData.map(team => team.teamName)));
        let arr =  $scope.team ?  $scope.responseData.filter(team=>team.teamName == $scope.team) : $scope.responseData;
        for(var i=0;i<arr.length;i++){
            var dataPoint=arr[i];
            labels.push(dataPoint.projectName);
            serieData.push(dataPoint.total);
        }
        $scope.labels=labels;
        $scope.series=series;
        $scope.data=[serieData];
    }
});

app.controller('codeSmellChartController',function($scope,$http,basicService){
	$http.get(backend.url+'/api/dashboard/top10CodeSmell').then(function(response){
        $scope.responseData = response.data;
        $scope.refreshData();
	});
    $scope.refreshData = function () {
        var labels=[];
        var series=['编码规范(阻断)'];
        var serieData=[];
        $scope.teams =  Array.from(new Set($scope.responseData.map(team => team.teamName)));
        let arr =  $scope.team ?  $scope.responseData.filter(team=>team.teamName == $scope.team) : $scope.responseData;
        for(var i=0;i<arr.length;i++){
            var dataPoint=arr[i];
            labels.push(dataPoint.projectName);
            serieData.push(dataPoint.total);
        }
        $scope.labels=labels;
        $scope.series=series;
        $scope.data=[serieData];
    }
});

app.controller('funcTestChartController',function($scope,$http,basicService){
	$http.get(backend.url+'/api/dashboard/top10DC').then(function(response){
        $scope.responseData = response.data;
        $scope.refreshData();
	});
    $scope.refreshData = function () {
        var labels=[];
        var series=['(依赖包高危漏洞)'];
        var serieData=[];
        $scope.teams =  Array.from(new Set($scope.responseData.map(team => team.teamName)));
        let arr =  $scope.team ?  $scope.responseData.filter(team=>team.teamName == $scope.team) : $scope.responseData;
        for(var i=0;i<arr.length;i++){
            var dataPoint=arr[i];
            labels.push(dataPoint.projectName);
            serieData.push(dataPoint.total);
        }
        $scope.labels=labels;
        $scope.series=series;
        $scope.data=[serieData];
    }
});