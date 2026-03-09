import Store from '../class/SingletonStore';

var storeService = function ($http) {

    console.log('This is storeService ', this);

    let service = {};

    service.store = Store;
    service.loadData = function (callback) {

        let login_pass = false;
        if(login_pass){
            service.store.auth = true;
            console.log("Login Skip...");
            callback();
        } else {
            $http.post("data", {action: 'SERVICE_DATA'})
                .then(function (response) {

                    // if (service.store.isLocal) {
                    //     service.store.auth = true;
                    //     service.store.service_data = response.data.result;
                    // } else
                    {
                        service.store.auth = false;
                        if (response.data != null) {
                            if (response.data.error != null) {
                                console.log(response.data.error)
                            } else if (response.data.result != null /*&& response.data.result.length > 0*/) {
                                service.store.auth = response.headers("auth") == "true";
                                service.store.service_data = response.data.result;
                                service.store.user = angular.copy(response.data.result.user);
                            }
                        }
                    }
                    callback();
                }, function (response) {
                    console.log(response);
                    callback();
                });
        }
    };

    return service;
};

export default ['$http', storeService]
