let getBaseServer = function(){
    let portal = sessionStorage.portal?  JSON.parse(sessionStorage.portal): null;
    if(portal &&  portal.used){
        let the_url=JSON.parse(sessionStorage.portal).getMenusForCurrentUser;
        return getProtocol(the_url)+getDomain(the_url);
    }
    return '';
}

var sonarServer='http://sonarqubexxxxx';

var backend={
	url: ''+'/ci',
	gitUrl:"http://gitlabxxxxx/",
};


//判断如果是upms登录处理
function  getDomain(the_url){
    let lead_slashes = the_url.indexOf("//");
    let domain_start = lead_slashes + 2;
    let without_resource = the_url.substring(domain_start, the_url.length);
    let next_slash = without_resource.indexOf("/");
    return  without_resource.substring(0, next_slash);
}


function  getProtocol(the_url){
    let lead_slashes = the_url.indexOf("//");
    return the_url.substring(0, lead_slashes+2);
}