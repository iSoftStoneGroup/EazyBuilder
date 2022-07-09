if (typeof String.prototype.startWith != 'function') {
	 String.prototype.startWith = function (prefix){
	  return this.slice(0, prefix.length) === prefix;
	 };
}
let portal =JSON.parse(window.sessionStorage.portal);


const guardTypes = {new_unit_test_coverage_rate:'新增代码单元测试覆盖率',unit_test_success_rate:'单元测试成功率',unit_test_coverage_rate:'单元测试覆盖率',bug_blocker:'BUG(阻断)',
	vulner_blocker:'安全漏洞(阻断)',
	code_smell_blocker:'编码规范(阻断)'
};


const actionScopes = {CODE_PUSH:'代码PUSH'};

const actionScopeGuardTypes = {CODE_PUSH: ['unit_test_success_rate','unit_test_coverage_rate','new_unit_test_coverage_rate','bug_blocker','vulner_blocker','code_smell_blocker'],
	PIPELINE_BUILD: ['unit_test_success_rate','unit_test_coverage_rate','new_unit_test_coverage_rate','bug_blocker','vulner_blocker','code_smell_blocker']};

const statusTypes = {SUCCESS:'成功',FAILED:'失败',IN_PROGRESS:'执行中',UNSTABLE:'成功(不稳定)',ABORTED:'已取消',ASSERT_WARNRULE_FAILED:'门禁判定失败',WAIT_AUTO_TEST_RESULT:'等待测试结果',NOT_EXECUTED:'未执行'};

app.service('basicService', function($http,$q) {
	   var fetch=function(url,header){
		   var d = $q.defer();
           $http.get(url,header)
  	         .then(function(response){
  	    	 d.resolve(response);
  	       },function(){
  	    	 d.reject("query error");
  	       });
           return d.promise;
	   }

	   this.getOnlineTags=function(){
	   	  return fetch(backend.url+"/api/onLine/getOnlineTags");
	   }

	   this.getRedmineTeam = function(){
		  return fetch(backend.url+"/api/redmine/getRedmineTeams");
	   }
	  //  this.getDockerImages = function(){
		//  return fetch(backend.url+"/api/dockerImage");
	  // }
	   this.getUpmsAllUsers = function(){
		 return fetch(backend.url+"/api/upms/getUpmsAllUsers");
	   }


	   this.getProjects=function(){
		   return fetch(backend.url+"/api/project");
	   }
       this.getMembers=function(){
    	   return fetch(backend.url+"/api/user");
       }
       
       this.getHosts=function(){
    	   return fetch(backend.url+"/api/hostInfo");
       }
       
       this.getTeams=function(){
    	   return fetch(backend.url+"/api/team");
       }
       this.getMyTeams=function(){
    	   return fetch(backend.url+"/api/team/my");
	   }
       
       this.getProjectGroups=function(){
    	   return fetch(backend.url+"/api/project-group")
       }
       
       this.getWarnRules=function(){
    	   return fetch(backend.url+"/api/warnRule");
       }
       
       
       this.getDockerRegistrys = function(){
    	   return fetch(backend.url+"/api/dockerRegistry");
       };
       
       this.getTotalPorject=function(){
    	   return fetch(backend.url+"/api/chart/totalProject");
       }
       
       this.getTotalCodeLine=function(){
    	   return fetch(backend.url+"/api/chart/totalCodeLine");
       }
       
       this.getTotalBuild=function(){
    	   return fetch(backend.url+"/api/chart/totalBuild");
       }
       this.getTotalDevelopers=function(){
    	   return fetch(backend.url+"/api/chart/totalDevelopers");
       }
       
       this.getPipelineProfile=function(){
    	   return fetch(backend.url+"/api/pipelineProfile")
       }
       this.getGuards=function(){
		return fetch(backend.url+"/api/guard")
	   }
       
       this.getTestEnvSet=function(){
    	   return fetch(backend.url+"/api/testEnvSet")
       }


	/**
	 upms返回菜单提取适配。
	 总体思路:遍历每个对象，判断如果有children并且有子元素
	 **/
	this.getMenuInfo=function (newObj,obj,pid,leaveNumb,orderNumb){
		let menu=newObj;
		for(let key  in obj){
			if(key =="id" || key=='type' || key=='hidden' ){
				menu[key]=obj[key];
			}else if(key =="name"){
				menu.menuSref=obj[key];
			}else if(key =="meta"){
				menu.name=obj[key].title;
				menu.menuIcon=obj[key].icon;
			}else if(key =="path"){
				menu.state=obj[key];
			}else if(key =="children"){
				menu.children=[];
				if(obj[key] && obj[key].length>0){
					for( let i=0;i<obj[key].length;i++ ){
						let temp={};
						let objReturn=this.getMenuInfo(temp,obj[key][i],obj.id,leaveNumb+1,i+1);
						menu.children.push(objReturn);
					}
				}
			}

		}
		menu.parent=pid;
		menu.leaveNumb=leaveNumb;
		menu.orderNumb=orderNumb;
		return menu;
	}


	this.getMenuFromUpms=function (){
		return fetch(portal.getMenusForCurrentUser+"?id="+window.sessionStorage.resource_id,{ 'Authorization' : "Bearer "+ window.sessionStorage.token }	);
	}


	/**
	 * 将树形菜单结构 转为list一层结构，不含children字段
	 * @param arr 装菜单的数组
	 * @param obj 当前要处理的对象
	 */
	this.haveChildre=function (arr,obj){
		if(obj.children && obj.children.length>0){
			for (let i = 0; i < obj.children.length; i++) {
				this.haveChildre(arr,obj.children[i]);
			}
			let target={};
			Object.assign(target,obj);
			delete target.children;
			arr.push(target);
		}else {
			arr.push(obj);
		}
	}
});