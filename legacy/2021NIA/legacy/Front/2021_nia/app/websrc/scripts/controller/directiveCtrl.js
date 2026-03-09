import BaseController from 'scripts/controller/baseController'

class directiveCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdSidenav, $mdPanel, uiCalendarConfig) {
        super($injector, $scope, tools, $http, $timeout);
        this.$mdSidenav = $mdSidenav;
        this.$mdPanel = $mdPanel;
        this.uiCalendarConfig = uiCalendarConfig;
    }
}

directiveCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdSidenav', '$mdPanel', 'uiCalendarConfig'];

export default directiveCtrl;
