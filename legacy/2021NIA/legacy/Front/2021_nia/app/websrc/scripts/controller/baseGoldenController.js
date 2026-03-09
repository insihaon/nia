import BaseController from 'scripts/controller/baseController'

class baseGoldenController extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout) {
        super($injector, $scope, tools, $http, $timeout);

        var event = $injector.get('event');
        var container = $injector.get('container');
        var state = $injector.get('state');

        Object.assign(this, { event,  container, state });
        Object.assign($scope, { event,  container, state });

        var $rootScope = $injector.get('$rootScope');
        $rootScope.$on('$routeChangeStart', function () {
            alert('refresh');
        });

        $scope.windowId = state.sequence;
        $scope.element_id = state.templateId + '_' + $scope.windowId;

        let content = $scope.content || container._config;
        if(container.layout)
        {
            container.layout($scope.content);
        }

        if(content)
        {
            let style = content.componentState.templateUrl.replace("html", "css");
            $scope.$css.add(style);
            $scope.$on('$destroy', function () {
                $scope.$css.remove(style);
            });
        }

        if (container.layoutManager) {

            //this.container.setTitle($scope.state.title + ' ' + id );
            container.on('resize', function () {
                this.onsize($scope);
            }.bind(this));

            container.layoutManager.eventHub.on('showOption', function (option) {
                if (this.container === option.container) {
                    this.onShowOption(option)
                }
            }.bind(this));

            $scope.$watch( () => $scope.chart, (n, o) => {
                if (n === o)
                    return;

                this.onsize($scope);
            });
        }

        /**
         * 종료 이벤트 Handler
         **/
        $scope.$on('$destroy', function () {

            // 이벤트 전파
            this.$scope.event.fireEvent({
                event_name: "broadcast.$destroy"
            });
        }.bind(this));
    }

    /**
     * container 사이즈 변경 이벤트 핸들러
     **/
    onsize($scope) {
        // for overriding
        //console.log('called resize');

        var elementId = '#' + $scope.element_id;
        var width = parseFloat($(elementId).closest('.lm_item_container').css('width'));
        var height = parseFloat($(elementId).closest('.lm_item_container').css('height'));

        if ($scope.chart && width && height) {

            $(elementId).css("width", (width - 10) + 'px');
            $(elementId).css("height", (height - 4) + 'px');

            //$scope.chart.resize({height:height, width:width});
            $scope.chart.resize();
        }
    }

    /**
     * 옵션 버튼 클릭 이벤트
     * 각 뷰에서 오버라이딩 해서 사용하기 바람
     * */
    onShowOption(option) {
        console.log('called onShowOption in baseGoldenController');
    }

    $onDestroy() {
        debugger;
    }
}

export default baseGoldenController;
