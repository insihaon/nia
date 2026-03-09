import BaseController from 'scripts/controller/baseController'

class SelfConfigurationCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog) {
        $scope.config = tools.store.viewType.self_configuration;
        super($injector, $scope, tools, $http, $timeout, $mdDialog);



    }
}

export default ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', SelfConfigurationCtrl];
