app.directive('ciMenu',function ($window) {
    return {
        templateUrl : 'js/ci-menu/menu-template.html',
        link : function($scope, element, attrs) {
            $scope.menu = JSON.parse(attrs.data);
            $scope.active=function($event){
                var currentSubMenu=jQuery($event.target).parents('ci-menu');
                currentSubMenu.children('a').addClass('active');
                currentSubMenu.siblings("ci-menu").each(function(){
                    $(this).children('a').removeClass('active');
                });
            }
        }
    };
});