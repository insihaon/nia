import BaseController from 'scripts/controller/baseController'

class SelfHealingCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog) {
        $scope.config = tools.store.viewType.self_healing;
        super($injector, $scope, tools, $http, $timeout, $mdDialog);



    }
}

export default ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', SelfHealingCtrl];
