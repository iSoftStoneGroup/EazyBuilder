app.service("auth", ["$http","$window", function($http, $window){
    let buttons =JSON.parse($window.sessionStorage.buttonsJson);
    let isUpmsLogin = $window.sessionStorage.user;
    let codes = buttons.map(button=>button.code);
    this.isAccessUrl=function(title,state){
      if(!isUpmsLogin){
    		return true;
      }
      if(!state && ! buttons ){
		  return false;
	  }
	  return codes.includes(state);
  }
}]);




app.directive("userPromission",function(auth){
	 return {
	        restrict: 'A',
	        compile: function(element, attrs) {
	            let title = attrs.title;
                let state = attrs.uiSref? attrs.uiSref :attrs.userPromission ;
				let dom = attrs.element ? attrs.element : element.context.parentElement;
	            if(!auth.isAccessUrl(title,state)){
	            	angular.element(dom).hide();
	            }else{
	            	angular.element(dom).show();
	            }
	        }
	};
});
