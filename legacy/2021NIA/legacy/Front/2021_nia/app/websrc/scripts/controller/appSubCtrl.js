function appSubCtrl($scope, storeService, tools, $rootScope) {
    tools.$debug('appSubCtrl');

    $scope.isLoadingHide = true;
    $scope.views = storeService.store.views;
    storeService.loadData(() => {
        let page = null;

        if (tools.store.encryptParams != null) {
            page = tools.decryptView(tools.store.encryptParams);
        }

        if (page != null) {
            $scope.showPage(page);
        } else if (tools.isDebug() && !storeService.store.auth) {
            page = tools.view(tools.localStorage.page || storeService.store.start_page);
        } else
            if (storeService.store.auth) {
            page = tools.view(storeService.store.start_page);
        } else {
            page = storeService.store.login_page;
        }

        $scope.statego(page);
    });

    $(document).keydown(function (event) {

        if ($scope.keyDelay) {
            return;
        }

        $rootScope.$broadcast('BroadcastKeyDown', event.keyCode);

        function delay(s) {
            $scope.keyDelay = true;
            setTimeout(() => {
                $scope.keyDelay = false;
            }, s);
        };

        delay(100);

    });
}

export default ['$scope', 'storeService', 'tools', '$rootScope', appSubCtrl]
