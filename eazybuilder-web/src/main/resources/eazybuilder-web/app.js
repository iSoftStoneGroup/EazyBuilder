var app = angular.module('consoleApp', ['ui.router','bsTable','mgcrea.ngStrap','chart.js','hc.marked','pickadate','smart-progressbar','ngclipboard','wui.date','ng-bootstrap-select','mgo-angular-wizard']);

app.filter('text_length_set', function () {
    return function (str, length, tail) {
        if (!str) {
            return '';
        }
        if (str.length <= length) {
            return str;
        }
        str = str.substr(0, length);
        return str + (tail || '...');
    }
});

app.controller("sideBarController",function($rootScope,$scope,$window,upmsLoginService){
	var currentUser=JSON.parse($window.sessionStorage.user);
	$rootScope.roles=currentUser.roles;
	$rootScope.currentUserId=currentUser.id;
	$rootScope.isCM=currentUser.isCM;
    $scope.portal=  $window.sessionStorage.portal ?  JSON.parse($window.sessionStorage.portal): null;
    $scope.getMenuButtons = function(menuList,buttons){
        menuList.forEach(function(menu){
            if(menu.type == 'BUTTON'){
                buttons.push(menu);
            }else if(menu.children){
                $scope.getMenuButtons(menu.children,buttons);
            }
        });
    }

    if($scope.portal && $scope.portal.used){
        upmsLoginService.getMenuFromUpms().then(function(res){
            if(res.data.message.code==200){
                $scope.menuList= res.data.data[0].children;
                $scope.buttonList = [];
                $scope.getMenuButtons($scope.menuList,$scope.buttonList);
                $window.sessionStorage.buttonsJson=JSON.stringify($scope.buttonList);
            }else {
                console.error("请求菜单信息失败："+res.data.message.description);
                alert("请求菜单信息失败："+res.data.message.description);
            }
         });
    }


	
	$rootScope.haveRoles=function (...roles) {
		let flag=false;
		for (let i = 0; i <roles.length ; i++) {
			angular.forEach($rootScope.roles, function(item,index,array){
				if(item.roleEnum==roles[i]){
					flag=true;
					return flag;
				}
			});
		}
		return flag;
	}

	
	$scope.active=function($event){
		var currentSubMenu=jQuery($event.target).parents('li');
		currentSubMenu.children('a').addClass('active');
		currentSubMenu.siblings("li").each(function(){
			$(this).children('a').removeClass('active');
		});
	}	 
	
});
app.directive('treeComponent',function($http){
	return{
		restrict:'EA',
		transclude: true,
		replace:true,
		scope:{
			options:'=',
		},
		template : '<div style="width:100%;height:100%;"  ng-class="{\'treemodul\':!ifheader}"><div class="left_header" ng-transclude>'+
			'</div>'+
			'<div class="left_body">'+
			'<ul id="{{options.el}}" class="ztree"></ul>'+
			'</div></div>',
		link:function($scope, element, attrs){
			$scope.ifheader =  attrs.header=="show"? true: false;
			var tree = element.find('ul.ztree')
			ztreeInitModule($scope.options,tree)
			function ztreeInitModule(options,tree){
				var settings = {
				}
				if(options.settings.async!=undefined){
					settings.async = objectDeepCopy(options.settings.async);
				}
				if(options.settings.data!=undefined){
					settings.data =objectDeepCopy(options.settings.data);
				} else{
					settings.data = { //设置数据
						simpleData: {
							enable: true,//是否设置数据满足父子节点关系（默认是false，如果设置true需设置其他参数）
							idKey: "id",
							pIdKey: "pId",
							rootPId: 0
						}
					}
				}
				if(options.settings.check!=undefined){
					settings.check = objectDeepCopy(options.settings.check);
				} else{
					settings.check = {
						enable: false,//节点是否选择checkbox/radio
						chkStyle: "checkbox",
						chkboxType: { "Y": "", "N": "" },
						radioType: "all",//分组依据 以全树和父节点下为组
					}
				}
				if(options.settings.callback!=undefined){
					settings.callback =  objectDeepCopy(options.settings.callback);
				}
				if(options.settings.view!=undefined)
				{
					settings.view = objectDeepCopy(options.settings.view);
				}else{
					settings.view =  {
						addHoverDom: function(treeId, treeNode){
							var length = $("button[id='"+treeNode.id+"']").length;
							if(length>0)return;
							var editStr =   "<button value='编辑' class='diyEditBtn' name='"+treeNode.name+"' tId='"+treeNode.tId+"' pId='"+treeNode.pId+"' id='" + treeNode.id
								+ "' title='修改' onfocus='this.blur();'></button>";
							var delStr =   "<button value='删除'   class='diyDelBtn' tId='"+treeNode.tId+"' id='" + treeNode.id
								+ "' title='删除' onfocus='this.blur();'></button>";
							var addStr =   "<button value='添加'   class='diyAddBtn ' tId='"+treeNode.tId+"' id='" + treeNode.id
								+ "' title='添加' onfocus='this.blur();'></button>";
							var aObj = $("#" + treeNode.tId + "_a");
							var btnStr = ''
							if(options.eventOper.btn){
								if(options.eventOper.btn.dele===true){
									btnStr +=delStr
								}
								if(options.eventOper.btn.add===true){
									btnStr +=addStr
								}
								if(options.eventOper.btn.mod===true){
									btnStr +=editStr
								}
							}
							aObj.append(btnStr);

						},//节点操作按钮的显示回调
						removeHoverDom: function(treeId, treeNode){
							$("button[id='"+treeNode.id+"']").unbind().remove();
						}//节点操作按钮的显示隐藏
					}
				}
				if(options.settings.key!=undefined)
				{
					settings.key = objectDeepCopy(options.settings.key);
				} else{
					settings.key = {
						isParent: "parent"
					}
				}
				if(options.settings.edit!=undefined)
				{
					settings.edit = objectDeepCopy(options.settings.edit);
				}
				$http({
					url:options.dataUrl,
					method:"post",
					headers: {
						'Content-type': 'application/json;charset=utf-8'
					},
				}).then(function(res){
					if(res.data) {
						$.fn.zTree.init(tree, settings, res.data);
						var treeObj =  $.fn.zTree.getZTreeObj(options.el);
						if(options.eventOper!=undefined){
							tree.on('click','.diyDelBtn',function () {
								if(options.eventOper.deleNode!=undefined){
									options.eventOper.deleNode(this,treeObj)
								}
							})
							tree.on('click','.diyEditBtn',function () {
								if(options.eventOper.modNode!=undefined){
									options.eventOper.modNode(this,treeObj)
								}
							})
							tree.on('click','.diyAddBtn',function () {
								if(options.eventOper.addNode!=undefined){
									options.eventOper.addNode(this,treeObj)
								}
							})
						}
					}
				});
			}
		}
	}
})

app.directive('treeSelect',function($http){
	return{
		restrict:'EA',
		transclude: false,
		replace:true,
		scope:{
			options:'=',
		},
		template : '<div class="toolbar-condition">'+
			'<span class="label-name" ng-show="options.input.label.enable">{{options.input.label.text}}：</span>'+
			'<input   ng-click="options.input.click()"   ng-keyup="options.input.keyup($event)" id="{{options.input.id}}" ng-model="options.input.bindmodel" placeholder="请选择..." class="form-control" >'+
			'<div id="{{options.tree.treemoduleId}}" class="tree-boxdiv"  ng-mouseleave="options.tree.mouseleave()" >'+
			'<div class="sousuobox"  ng-show="options.tree.sousuo.searchContent">'+
			'<ul ng-repeat="c in options.tree.sousuo.searchContent">'+
			'<li   ng-click="options.tree.sousuo.click($event)" ng-style="liCss" value="{{c.id}} "class="search-li">{{c.name}}</li>'+
			'</ul>'+
			'</div>'+
			'<div class="treebox">'+
			'<ul id="{{options.tree.module.id}}" class="ztree"></ul>'+
			'</div>'+
			'</div>'+
			'</div>',
		link:function($scope, element, attrs){
			var tree = element.find('ul.ztree')
			ztreeInitModule($scope.options.tree.module,tree)
			function ztreeInitModule(options,tree){
				var settings = {
				}
				if(options.settings.async!=undefined){
					settings.async = objectDeepCopy(options.settings.async);
				}
				if(options.settings.data!=undefined){
					settings.data =objectDeepCopy(options.settings.data);
				} else{
					settings.data = { //设置数据
						simpleData: {
							enable: true,//是否设置数据满足父子节点关系（默认是false，如果设置true需设置其他参数）
							idKey: "id",
							pIdKey: "pId",
							rootPId: 0
						}
					}
				}
				if(options.settings.check!=undefined){
					settings.check = objectDeepCopy(options.settings.check);
				} else{
					settings.check = {
						enable: false,//节点是否选择checkbox/radio
						chkStyle: "checkbox",
						chkboxType: { "Y": "", "N": "" },
						radioType: "all",//分组依据 以全树和父节点下为组
					}
				}
				if(options.settings.callback!=undefined){
					settings.callback =  objectDeepCopy(options.settings.callback);
				}
				if(options.settings.view!=undefined)
				{
					settings.view = objectDeepCopy(options.settings.view);
				}else{
					settings.view = {
						expandSpeed: "slow"
					}
				}
				if(options.settings.key!=undefined)
				{
					settings.key = objectDeepCopy(options.settings.key);
				} else{
					settings.key = {
						isParent: "parent"
					}
				}
				if(options.settings.edit!=undefined)
				{
					settings.edit = objectDeepCopy(options.settings.edit);
				}
				$http({
					url:options.dataUrl,
					method:"post",
					headers: {
						'Content-type': 'application/json;charset=utf-8'
					},
					data:options.data
				}).then(function(res){
					if(res.data) {
						$.fn.zTree.init(tree, settings, res.data);
					}
				});
			}
		}
	}
})
app.directive('uploader',function ($window) {
    return {
        restrict : 'E',
        template : "<button class='btn btn-default'>{{name}}</button>",
        link : function($scope, element, attrs) {
        	$scope.name=attrs.name;
        	var uploader = new ss.SimpleUpload({
  		      button: element,
  		      url: backend.url+attrs.url+"?token="+$window.sessionStorage.token, // URL of server-side upload handler
  		      name: 'uploadfile',
  		      allowedExtensions: ['xls', 'xlsx', 'xml','zip','rar','scan','jpg','png','sql','doc','docx'],
  		      data:{token:$window.sessionStorage.token},
  		      onExtError:function( filename, extension ){
  		    	  alert(filename+'该文件类型不允许上传');
  		      },
  		      onError:function(filename, errorType, status, statusText, response){
  		    	  if (!response) {
		              alert(filename + '上传失败');
		          }else{
		        	  var data=angular.fromJson(response);
		        	  alert(data.message);
		          }
  		      },
  		      onComplete: function(filename, response) {
  		          if (!response) {
  		              alert(filename + '上传失败');
  		              return false;            
  		          }else{
  		        	  var data=angular.fromJson(response);
  		        	  $scope.$emit("uploaded",{data:data});
  		        	  alert('上传成功');
  		          }
  		      }
  		    });
        }
    };
});

app.directive('autoHeight',function ($window) {
    return {
        restrict : 'A',
        scope : {},
        link : function($scope, element, attrs) {
            var windowHeight = $window.innerHeight; //获取窗口高度
            var headerHeight = 60;
            var footerHeight = 0;
            element.css('min-height',
                    (windowHeight - headerHeight - footerHeight) + 'px');
        }
    };
});
const getLoginUrl = function(){
    if(sessionStorage.portal && sessionStorage.usePortal){
        return JSON.parse(sessionStorage.portal).loginUrl;
    }else{
        return "login.html";
    }
}
var relogging=false;
app.factory('modelerHttpInterceptor', function ($q,$location, $window,$rootScope) {
	
	if(!$window.sessionStorage.token){
		$window.location.href=getLoginUrl();
		return;
	}
	
    return {
    	//拦截请求
    	request: function(config) {
    		$rootScope.isLoading=true;
    		//拦截ng $http request，补上user token
    		var apiPattern = /\/api\//;
    		//毫秒
			config.timeout = 340000;
            config.params = config.params || {};
			config.headers.Authorization='Bearer '+$window.sessionStorage.token;
            if ($window.sessionStorage.token && apiPattern.test(config.url)) {
                config.params.token = $window.sessionStorage.token;
            }
            return config || $q.when(config);
        },
        response: function(response){
        	$rootScope.isLoading=false;
        	return response;
        },
        responseError: function (response) {
        	$rootScope.isLoading=false;
			if(JSON.parse($window.sessionStorage.portal).used && response.status==401){
				alert("身份认证已失效，请重新登录!");
				window.parent.postMessage({status: 403}, "*");
                window.location.href=getLoginUrl();
			}
        	if(response.status==403){
        		if(!relogging){
        			alert("身份认证已失效，请重新登录");
        			window.location.href=getLoginUrl();
        			relogging=true;
        		}
        		return false;
        	}
        	if(response.status==500&&response.data.exception=="org.springframework.dao.DataIntegrityViolationException"){
        		alert("操作失败，重复数据或数据已被引用");
        		return $q.reject(response);
        	}
        	if(response.status==502){
        		alert("后端服务暂时不可用，请稍后再尝试");
        		return false;
        	}
            // do something on error
        	alert("操作失败："+(response.data.message?response.data.message:""));
            return $q.reject(response);
        }
    };
});

//全局拦截 处理jQuery ajax请求(补上token/userId)
(function() {
	$.ajaxSetup({
		beforeSend:function(XMLHttpRequest){
			XMLHttpRequest.setRequestHeader("Authorization", 'Bearer '+window.sessionStorage.token);
		}
	});
    var _ajax = $.ajax;
    $.ajax = function(opts) {
    	var apiPattern = /\/api\//;
        opts.data = opts.data || {};
        if (window.sessionStorage.token && apiPattern.test(opts.url)) {
        	opts.data.token = window.sessionStorage.token;
        }
        _ajax(opts).fail(function(data){
    		if(data.status==403){
        		alert("身份认证已失效，请重新登录");
        		window.location.href=getLoginUrl();
        		return;
        	}
    	});
    };
})();

app.config(function ($httpProvider,ChartJsProvider) {
    $httpProvider.interceptors.push('modelerHttpInterceptor');
    var global=Chart.defaults.global;
    ChartJsProvider.setOptions({
    	global:{
            colors : ['#ff0000','#0000ff'],
            defaultColor: '#ff0000',
            defaultFontColor: '#333',
            defaultFontSize: 13,
            elements: {
            	rectangle: {
            		borderWidth: 0,
            		backgroundColor: 'rgab(255,0,0,1)'
            	}
            },
            tooltips:{
            	displayColors:false
            }
        },
    	scales: {
            yAxes: [{
            	display: false ,
                ticks: {
                    beginAtZero:true
                }
            }],
            xAxes: [{
                gridLines: {
                    offsetGridLines: true
                }
            }]
        }
    });
});

Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

String.prototype.replaceAll = function(s1,s2){
	　　return this.replace(new RegExp(s1,"gm"),s2);
	　　}

function guid() {
	  function S4() {
	    return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	  }
	  return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
}

function createBadgeSvg(key,value,color){
	return '<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="110" height="20"><linearGradient id="b" x2="0" y2="100%"><stop offset="0" stop-color="#bbb" stop-opacity=".1"/><stop offset="1" stop-opacity=".1"/></linearGradient><clipPath id="a"><rect width="110" height="20" rx="3" fill="#fff"/></clipPath><g clip-path="url(#a)"><path fill="#555" d="M0 0h59v20H0z"/><path fill="'+color+'" d="M59 0h51v20H59z"/><path fill="url(#b)" d="M0 0h110v20H0z"/></g><g fill="#fff" text-anchor="middle" font-family="DejaVu Sans,Verdana,Geneva,sans-serif" font-size="110"> <text x="305" y="150" fill="#010101" fill-opacity=".3" transform="scale(.1)" textLength="300">'+key+'</text><text x="305" y="140" transform="scale(.1)" textLength="300">'+key+'</text><text x="835" y="150" fill="#010101" fill-opacity=".3" transform="scale(.1)" textLength="300">'+value+'</text><text x="835" y="140" transform="scale(.1)" textLength="300">'+value+'</text></g> </svg>';
}


