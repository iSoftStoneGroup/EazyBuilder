app.controller('userStatisticController', function($scope,$http,$window,$state,$filter,basicService) {
	
});

app.controller('userProjectActivitiesController',function($scope,$http,$window,$state,$filter,basicService){
	
});

app.controller('userActivitiesController',function($scope,$http,$window,$state,$filter,basicService){
	$scope.day=7;
	$scope.byUser=true;
	$scope.getUserActivityData=function(start,end){
		$http.get(backend.url+'/api/userActivityStatistic/groupByUser?start='+start+'&end='+end).then(function(response){
			$scope.statistic=response.data;
			var dataArray=response.data;
			var labels=[];
			var series=['commit次数'];
			var serieData=[];
			for(var i=0;i<dataArray.length&&i<15;i++){
				var dataPoint=dataArray[i];
				labels.push(dataPoint.userName.substring(0,dataPoint.userName.indexOf('/')));
				serieData.push(dataPoint.push);
			}
			$scope.labels=labels;
			$scope.series=series;
			$scope.data=[serieData];
			$scope.clickBar=function(points,event){
				console.log(points,event);
				if(points&&points.length){
					console.log('label:',points[0]._model.label);
				}
			}
			$scope.options={
					title: {
						display: true,
						text: '开发者提交TOP15统计 ('+start+' ~ '+end+')'
					},
					scales:{yAxes: [{
		            	display: true
		            }]}
			};
			
		});
	}
	
	$scope.activeByUser=function(byUser){
		$scope.byUser=byUser;
		if(byUser){
			$scope.getDataIn($scope.day);
		}else{
			$scope.getProjectDataIn($scope.day);
		}
	}
	
	$scope.getProjectActivityData=function(start,end){
		$http.get(backend.url+'/api/userActivityStatistic/groupByProject?start='+start+'&end='+end).then(function(response){
			$scope.projectActivities=response.data;
			var dataArray=response.data;
			var labels=[];
			var series=['commit次数'];
			var serieData=[];
			for(var i=0;i<dataArray.length&&i<15;i++){
				var dataPoint=dataArray[i];
				labels.push(dataPoint.pname.substring(dataPoint.pname.indexOf('/')+1));
				serieData.push(dataPoint.push);
			}
			$scope.labels=labels;
			$scope.series=series;
			$scope.data=[serieData];
			$scope.options={
					title: {
						display: true,
						text: '项目提交统计 ('+start+' ~ '+end+')'
					},
					scales:{yAxes: [{
		            	display: true
		            }]}
			};
			
		});
	}
	
	$scope.getProjectDataIn=function(day){
		$scope.getProjectActivityData(new Date(new Date()-day*24*60*60*1000).Format('yyyy-MM-dd'),new Date().Format('yyyy-MM-dd'));
	}
	$scope.getUserDataIn=function(day){
		$scope.getUserActivityData(new Date(new Date()-day*24*60*60*1000).Format('yyyy-MM-dd'),new Date().Format('yyyy-MM-dd'));
	}
	
	$scope.getDataIn=function(day){
		if(!day){
			day=1
		}
		$scope.day=day;
		if($scope.byUser){
			$scope.getUserDataIn(day);
		}else{
			$scope.getProjectDataIn($scope.day);
		}
	}
	$scope.getDataIn($scope.day);//last day
});