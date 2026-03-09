import BaseController from 'scripts/controller/baseController'

class MainPageCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdSidenav) {
    	super($injector, $scope, tools, $http, $timeout);

    	// 토폴로지 편집창 편집 기능
    	$scope.broadcast = function (message) {
            // 하위 $scope 로 이벤트 전송
            $scope.$broadcast(message, {});
        };

        // $scope.$on('EmitOnChangedTicket', function listenStatus(event, param) {
        //     $scope.$broadcast('BroadcastOnChangedTicket', param);
        // });

         $scope.data = {
             selectedIndex: 0,
             secondLocked:  true,
             secondLabel:   "Item Two",
             bottom:        false
         };
         $scope.next = function() {
             $scope.data.selectedIndex = Math.min($scope.data.selectedIndex + 1, 2) ;
         };
         $scope.previous = function() {
             $scope.data.selectedIndex = Math.max($scope.data.selectedIndex - 1, 0);
         };

        /**
         Copyright 2018 Google Inc. All Rights Reserved.
         Use of this source code is governed by an MIT-style license that can be foundin the LICENSE file at http://material.angularjs.org/HEAD/license.
         **/


    }
}

export default ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdSidenav', MainPageCtrl];
