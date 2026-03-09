import Store from '../class/SingletonStore';

let interceptorHttp = function ($q, cipher, $state, $injector) {
    return {
        request: function (request) {

            var r = request;
            if (Store.debug && request.data && typeof request.data === 'string') {

                let data = request.data;
                data = JSON.parse(data);

                if(data.action) {
                    console.log(`<<< url=${request.url}, action=${data.action}, data=${request.data}`);
                    request.headers["is_intercept_sql"] = "true"
                }
            }

            if (cipher.keys != null) {
                request.headers["x-encrypt"] = "true";
//				console.log(cipher.keys);
//				console.log(cipher.enc(request.data));
            }
            return request;
        },
        response: function (response) {

            var result = response.data.result || {};

            if (Store.debug && response.config.method == 'POST') {
                try {
                    console.log(">>> ", response.config, response.data);
                } catch (e) {
                }
            }

            if (response.config.url === "service") {

                /*인증 만료 세션일 경우 로그인 페이지로...*/
                if (response.headers("auth") !== "true" && Store.authUse) {
                    $state.go(Store.login_page);
                    return response;
                }

                var sqllog = (response.data || {}).sqllog;
                var input = (response.data || {}).input
                if (sqllog) {

                    /*try {
                        console.log(response.config);
                        console.log(response.config.data);
                        console.log(response.config.data.action);
                    } catch (e) {
                    }*/

                    sqllog = sqllog.split(sqllog.substr(0, 1)).join('\n')

                    var arrName = sqllog.match(/\n\s*\[\s(.*)\s\]\s*\n/g);
                    var pattern = new RegExp('\s*\\[.+?seconds', 'g');
                    var arrElapsed = sqllog.match(pattern);
                    var arrSql = sqllog.slice(0, -1).split(pattern);
                    var result = response.data;

                    // var info = {elapsed: arrElapsed, sql: arrSql, result, time };
                    // var SqlStorage = $injector.get('SqlStorage');
                    // SqlStorage.save(info);

                    try {
                        for (let i = 0; i < arrElapsed.length; i++) {
                            var elapsed = arrElapsed[i];
                            var sql = arrSql[i].replace(/\n\s{0,1}\[\s(.*)\s\](\s{0,1}\n|.+second)/g, '');
                            var timestamp = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
                            var service = arrName[i].replace(/\s{0,1}\[\s(.*)\s\]\s{0,1}\n/g, "$1")
                            var output = result.result || result.list;
                            var info = {elapsed: elapsed, timestamp, service, input, output: output, sql: sql};
                            window.sql = window.sql || [];
                            window.sql.unshift(info);
                            // console.info('sql=', info);
                            // Live Expression :  JSON.stringify(sql.map(s=>s.name))

                            if(!window.sqlNames) {
                                window.sqlNames = () => JSON.stringify(window.sql.map(s=>s.name));
                            }
                        }
                    } catch (e) {
                        window.sql.unshift({name: 'SQL 로그 실패', error: e.toString()});
                    }

                    window.sql.splice(20);
                }
            }
            return response;
        },
        responseError: function (response) {
            if (response.status === 0) {
                response.statusText = "네트워크 연결 오류";
                alert(response.statusText);
                //alert(response.statusText + "\n\n" + response.config.url);
                return $q.resolve(response);
            }
            return $q.reject(response);
        }
    };
};

export default ['$q', 'cipher', '$state', '$injector', interceptorHttp]
