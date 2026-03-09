import BaseController from 'scripts/controller/baseController'

class AiMonitorCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdPanel) {
        $scope.config = tools.store.viewType.aiMonitor;
        super($injector, $scope, tools, $http, $timeout, $mdPanel);
        {
        }
    }
}

export default ['$injector', '$scope', 'tools', '$http', '$timeout', AiMonitorCtrl];
