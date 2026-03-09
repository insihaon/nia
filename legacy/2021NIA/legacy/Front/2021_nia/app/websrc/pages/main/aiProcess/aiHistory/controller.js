import BaseController from 'scripts/controller/baseController'

class AiHistoryCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog) {
        $scope.config = tools.store.viewType.ai_history;
        super($injector, $scope, tools, $http, $timeout, $mdDialog);



    }
}

export default ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', AiHistoryCtrl];
