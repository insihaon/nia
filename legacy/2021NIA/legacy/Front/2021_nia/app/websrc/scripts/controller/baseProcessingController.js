import BaseController from 'scripts/controller/baseController'

class BaseProcessingController extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, param) {
        super($injector, $scope, tools, $http, $timeout);

        let tempParam = angular.copy(param);
        $scope.ticketInfo = tempParam.ticket || tempParam || {};
        $scope.processing = tempParam.processing;
        $scope.isModified = $scope.processing.code.includes('MODIFIED') || false;
        $scope.isBatch = $scope.processing.code.includes('BATCH') || false;
        $scope.selectedTicketIndex = 0;
        $scope.successResult = [];

        $scope.userInfo = {
            handling_user : tools.store.userName,
            handling_dept : tools.store.deptName,
            handling_agency : tools.store.agency_name
        };
    }

    deleteNewLabel(ticketId) {
        let el = document.getElementById('section-' + ticketId);
        if(el){ el.classList.add('not-new'); }
    }
}

export default BaseProcessingController;
