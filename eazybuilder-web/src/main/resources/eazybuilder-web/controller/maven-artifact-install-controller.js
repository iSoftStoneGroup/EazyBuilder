app.controller('mavenRepoController',function($scope,basicService,$window,$http){
	
	if($window.sessionStorage.usePortal){
		$http.post(backend.url+"/getTeamFromUpms"+"?token="+$window.sessionStorage.token).then(function(response){
			$scope.teams=response.data;
			$scope.baseRepoUrl='/eazybuilder-web/filemanager/main.html';
			$scope.currentTeamId=$scope.teams[0].id;
			$scope.$watch('currentTeamId',function(){
				$scope.repoUrl=$scope.baseRepoUrl+'?teamId='+$scope.currentTeamId;
			});
		});
	}else {		
		basicService.getTeams().then(function(response){
			$scope.teams=response.data;
			$scope.baseRepoUrl='filemanager/main.html';
			$scope.currentTeamId=$scope.teams[0].id;
			$scope.$watch('currentTeamId',function(){
				$scope.repoUrl=$scope.baseRepoUrl+'?teamId='+$scope.currentTeamId;
			});
		});
	}
	
});


app.controller('mavenArtifactInstallController', function($scope,$http,$location,$window,$state,$modal,$filter,basicService) {
	var modalInstance=$modal({
		  title: '选择构建设置', 
		  templateUrl: 'app/mavenRepo/upload.html', 
	      show: true,
	      animation:'am-fade-and-scale',
	      controller:function($scope){
	    	  $scope.teamId = $location.search().teamId;
	    	  $scope.groupId='com.eazybuilder';
	    	  $scope.packaging='jar';
	    	  
	    	  $scope.closeModal=function(){
	    		  modalInstance.destroy();
	    		  $state.go("mavenRepo");
	    	  }
	    	  //保存
	    	  $scope.save=function(){
	    	        var fd = new FormData();
	    	        var file = document.querySelector('input[type=file]').files[0];
	    	        fd.append('uploadfile', file); 
	    	        fd.append('groupId',$scope.groupId);
	    	        fd.append('artifactId',$scope.artifactId);
	    	        fd.append('version',$scope.version);
	    	        fd.append('packaging',$scope.packaging);
	    	        if($scope.classfier){
	    	        	fd.append('classifier',$scope.classfier);
	    	        }
	    	        
	    	        $http({
	    	              method:'POST',
	    	              url:backend.url+"/repo/install/local?teamId="+$scope.teamId,
	    	              data: fd,
	    	              headers: {'Content-Type':undefined},
	    	              transformRequest: angular.identity 
	    	               })   
	    	              .then(function(response){
	    	            	  alert("上传成功");
	    	            	  modalInstance.destroy();
	    	            	  $state.go("mavenRepo");
	    	              },function(response){
	    	            	  alert("上传失败");
	    	              }); 
	    	    
	    	  }
        }        
		}
	);
});

