import BaseController from 'scripts/controller/baseController'

class BatchFixDialogCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog) {
        $scope.config = tools.store.viewType.mon_tt_template_batch_fix;
        super($injector, $scope, tools, $http, $timeout);

        $scope.actionDetail = '';
        Object.assign($scope.dlgFunc, {
            result: function(result)
            {
                if($scope.actionDetail.length <= 0){
                    document.getElementById("el3").focus();
                    document.getElementById("el3").blur();
                    return;
                }
                $mdDialog.hide(result);
            }
        });
    }
}
BatchFixDialogCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog'];

export default BatchFixDialogCtrl;
