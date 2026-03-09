import Base from '../../scripts/controller/base'

class baseController extends Base {
    constructor($injector, $scope, tools, $http, $timeout) {
        super($scope, tools);

        Object.assign(this, {
            $injector, $scope, tools, $http, $timeout,
            $rootScope : $injector.get('$rootScope'),
            $state : $injector.get('$state'),
            $log : $injector.get('$log'),
            $css : $injector.get('$css'),
            $mdDialog : $injector.get('$mdDialog'),
            focus : $injector.get('focus'),
            $mdToast : $injector.get('$mdToast'),
            $transitions : $injector.get('$transitions'),
            $q : $injector.get('$q'),
            $mdSidenav : $injector.get('$mdSidenav'),
            breakpoint: () => {if (tools.store.breakpoint) debugger;},
            debugger: () => {if (tools.debug) debugger;}
        });

        var viewName = ($scope.config || {}).viewName;
        if (viewName) {
            let el = angular.element($scope.config.viewName);
            if (el) {
                $scope.$on('onViewEvent', (event, param) => {
                    if ($scope.onViewEvent) {
                        $scope.onViewEvent(event, param);
                        $scope.safeApply();
                    }
                });
            }

            var controllerName = ($scope.config || {}).controller;
        }

        $scope.$rootBroadcast = (name, param) => {
            let {control} = $scope;
            control.$rootScope.$broadcast(name, param);
        }

        //this.$window = $injector.get('$window');
        //window.addEventListener("beforeunload", function (event) {
        //    event.preventDefault();
        //    event.returnValue = "여기 메시지가 안 먹히네...";
        //    alert(1);
        //});

        this.ticketStorage = tools.store.ticketStorage;
        this.inspectorTicket = tools.store.inspectorTicket;

        /*for (let variable in Store) {
            this[variable] = Store[variable];
        }*/

        for (let variable in this) {
            $scope[variable] = this[variable];
        }

        if($scope != null) {
            this.init();
        }
    }

    $onInit() {
        //super.$onInit();
        //debugger;

        if(this.$scope.onInit) {
            this.$scope.onInit.call(this);
        }
    }

    $onDestroy() {
        // DashboardController 등에서는 호출안된다. 왜 그런지 모르겠다.
        //super.$onDestroy();
        //debugger;
    }

    init() {

        let { $scope, $css, tools, $mdDialog, $mdSidenav } = this;

        if($scope.config == null && $scope.$resolve == null) {

            // 디렉티브 css 초기화
            this.initStyle($scope.style);
            return;
        }

        let config = $scope.config || $scope.$resolve.$state$;
        let style = config.style ? config.path.replace("html", "css") : null;

        $scope.type = config.type;
        $scope.name = config.viewName;
        $scope.image = config.image;
        $scope.content = config.content;

        $scope.dlgFunc = { //모달 다일로그 함수
            hide: function() { $mdDialog.hide() },
            cancel: function() { $mdDialog.cancel() },
            result: function(result) { $mdDialog.hide(result) }
        };

        $scope.windowClose = () => {
            if($scope.singlePage) {
                window.close();
            }
        };

        $scope.close = function (id) {
            if ($mdSidenav(id)) {
                $mdSidenav(id).close()
                    .then(function () {
                        $scope.$log.debug("close " + id + " is done");
                    });
            }
        };

        tools.$debug($scope.type + ' controller loaded $scope.$id=' + $scope.$id);
        this.initStyle(style);
    }

    initStyle(style) {

        let {$scope, tools} = this;

        if (style != null) {
            tools.applyCss($scope, style);
        }
    }

    buildToggler(navID, type, thenFn) {

        let { $scope, $mdSidenav } = this;
        return function () {

            if (type)
                $scope.statego(type);

            $mdSidenav(navID)
                .toggle()
                .then(function () {
                    $scope.$log.debug("toggle " + navID + " is done");
                })
                .then(thenFn);
        };
    }
}

export default baseController;
