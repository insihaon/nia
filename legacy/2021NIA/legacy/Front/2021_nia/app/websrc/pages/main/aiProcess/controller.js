import BaseController from 'scripts/controller/baseController'

class AIProcessCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog) {
        $scope.config = tools.store.viewType.ai_process;
        super($injector, $scope, tools, $http, $timeout, $mdDialog);

        $scope.aiText = '장애 상세내역 및 조치 요청서를 발송합니다.';

        $scope.testToggle = () => {
            let el = $('div.ai-process-container');
            if(el.hasClass('show-debug-bg')) {
                el.removeClass('show-debug-bg');
            } else {
                el.addClass('show-debug-bg');
            }
        }

    }
}

export default ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', AIProcessCtrl];
