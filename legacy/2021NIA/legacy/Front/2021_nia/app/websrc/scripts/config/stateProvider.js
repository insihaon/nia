import StoreService from '../factory/storeService';

var config = function ($stateProvider, $urlRouterProvider, $locationProvider, $provide, $uiRouterProvider) {

    $locationProvider.hashPrefix(''); //주소에 ! 붙는것이 싫을 경우 이렇게 사용
    //$locationProvider.hashPrefix('!');

    //$locationProvider.html5Mode({
    //    enabled: true,
    //    requireBase: false
    //});

    $provide.factory('storeService', StoreService);

    let storeService = StoreService[StoreService.length - 1]();

    storeService.store.views.forEach(function (item) {
        let type = item.type;
        let path = item.path;
        let controller = item.controller;
        let abstract = type.indexOf('.') === -1;
        let service = {
            templateUrl: path,
            controller: controller,
            type: type,
            viewName: item.viewName,
            image: item.image,
            style: item.style,
            path: path,
            content: item.content
        };

        if (storeService.store.debug) {
            service.url = '/'+ type + (abstract ? '' : '/');
        }

        $stateProvider.state(type, service);
    });

    // catch all route
    // send users to the form page
    //$urlRouterProvider.otherwise('/');
};

export default ['$stateProvider', '$urlRouterProvider', '$locationProvider', '$provide', '$uiRouterProvider', config]
