app.service('upmsLoginService', function($http,$q,$rootScope, $state,$interval,$window) {

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


    this.uuid = function(){
        function uuid(len, radix) {
            var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
            var uuid = [], i;
            radix = radix || chars.length;

            if (len) {
                // Compact form
                for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
            } else {
                // rfc4122, version 4 form
                var r;

                // rfc4122 requires these characters
                uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
                uuid[14] = '4';

                // Fill in random data.  At i==19 set the high bits of clock sequence as
                // per rfc4122, sec. 4.1.5
                for (i = 0; i < 36; i++) {
                    if (!uuid[i]) {
                        r = 0 | Math.random()*16;
                        uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
                    }
                }
            }

            return uuid.join('');
        }
        return uuid(32,16);
    }


    this.getMenuFromUpms=function (){
        let portal =JSON.parse($window.sessionStorage.portal);
        return fetch(portal.getMenusForCurrentUser+"?id="+$window.sessionStorage.resource_id,{ 'Authorization' : "Bearer "+ $window.sessionStorage.token }	);
    }

});