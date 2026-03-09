import BaseController from 'scripts/controller/baseController'

class BatchProcessingDialogCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $element, param) {
        $scope.config = tools.store.viewType.mon_tt_template_batch_processing;
        super($injector, $scope, tools, $http, $timeout);

        $scope.topasTT = [];
        $scope.selected = [];

        $scope.myQuery = '';

        function DialogController($scope, $mdDialog) {
            $scope.hide = function() {
                $mdDialog.hide();
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };

            $scope.answer = function(answer) {
                $mdDialog.hide(answer);
            };
        };
        $scope.ack = (selected, filteredTopasTT) => {
            $scope.tools.createConfirmDlg(
                '선택한 항목을 인지 하시겠습니까?',
                '확인을 선택하면 데이터가 저장됩니다.', null)
            .then(function() {
                var arr = [];
                var firstno = '';
                selected.forEach((v, i) => {
                    if(i==0){
                        firstno = v.ticketno;
                    }
                    arr.push(v.ticketno);
                });
                $scope.topasTT.forEach((v, i) => {
                    if(tools.store.checkAvailability(arr, v.ticketno)){
                        v.causesite = '공군작전지역국통사통';
                        v.brokencause = '회선시험: 전송로시험';
                        v.responsibility = '고객사유: 회선시험';
                    }
                });
                $scope.tools.showToast('인지 되었습니다.','batchArea', null, null, 'bottom left');

                var topHeight = 0;
                var find = $scope.filteredTopasTT.find(function(itm) {return itm.ticketno === firstno});
                var idx = $scope.filteredTopasTT.indexOf(find);
                if (idx > -1) {
                    topHeight = (idx - 4) * 24;
                    $('.topas-table').animate({scrollLeft : $("#ackFocus").offset().left}, 0);
                    $('#topas-tbody').animate({scrollTop : topHeight}, 0);
                }
            }, function() {
            });
        };

        $scope.fix = (selected) => {
            $mdDialog.show({
                 controller: tools.store.viewType.mon_tt_template_batch_fix.controller,
                 templateUrl: tools.store.viewType.mon_tt_template_batch_fix.path,
                 parent: angular.element(document.querySelector('#popupContainer')),
                 targetEvent: event,
                 clickOutsideToClose:true,
                 bindToController: true,
                 autoWrap: false,
                 multiple: true,
                 fullscreen: false // Only for -xs, -sm breakpoints.
             })
             .then(function(result) {
                 $scope.tools.createConfirmDlg(
                     '선택한 항목을 조치 하시겠습니까?',
                     '확인을 선택하면 데이터가 저장됩니다.', null)
                 .then(function() {
                     var arr = [];
                     var firstno = '';
                     selected.forEach((v, i) => {
                         if(i==0){
                             firstno = v.ticketno;
                         }
                         arr.push(v.ticketno);
                     });
                     $scope.topasTT.forEach((v, i) => {
                         if(tools.store.checkAvailability(arr, v.ticketno)){
                             v.fixed = result;
                         }
                     });
                     $scope.tools.showToast('조치 되었습니다.','batchArea', null, null, 'bottom left');
                     var topHeight = 0;
                     var find = $scope.filteredTopasTT.find(function(itm) {return itm.ticketno === firstno});
                     var idx = $scope.filteredTopasTT.indexOf(find);
                     if (idx > -1) {
                         topHeight = (idx - 4) * 24;
                         $('.topas-table').animate({scrollLeft : $("#ackFocus").offset().left}, 0);
                         $('#topas-tbody').animate({scrollTop : topHeight}, 0);
                     }
                 }, function() {
                 });
             }, function() {
             });
        };
        $scope.fin = (selected) => {
            $scope.tools.createConfirmDlg(
                '선택한 항목을 마감 하시겠습니까?',
                '확인을 선택하면 데이터가 저장됩니다.', null)
            .then(function() {
                var arr = [];
                var firstno = '';
                selected.forEach((v, i) => {
                    if(i==0){
                        firstno = v.ticketno;
                    }
                    arr.push(v.ticketno);
                });
                $scope.topasTT.forEach((v, i) => {
                    if(tools.store.checkAvailability(arr, v.ticketno)){
                        v.receivetime = new Date().getTime();
                    }
                });
                $scope.tools.showToast('마감 되었습니다.','batchArea', null, null, 'bottom left');
                var topHeight = 0;
                var find = $scope.filteredTopasTT.find(function(itm) {return itm.ticketno === firstno});
                var idx = $scope.filteredTopasTT.indexOf(find);
                if (idx > -1) {
                    topHeight = (idx - 4) * 24;
                    $('.topas-table').animate({scrollLeft : 0}, 0);
                    $('#topas-tbody').animate({scrollTop : topHeight}, 0);
                }
            }, function() {
            });
        };

        $scope.isAllSelected = false;
        $scope.toggleAll = (filteredTopasTT)=>{
            var toggleStatus = !$scope.isAllSelected;

            filteredTopasTT.forEach((v, i) => {
                v.selected = toggleStatus;
                $scope.checkSelected(v);
            });
        };
        $scope.toggled = function(){
            $scope.isAllSelected = $scope.topasTT.every(function(itm){ return itm.selected; })
        };
        $scope.checkSelected = function(item){
            if(item.selected){
                $scope.selected.push(item);
            }else{
                var find = $scope.selected.find(function(itm) {return itm.ticketno === item.ticketno});
                var idx = $scope.selected.indexOf(find);
                if (idx > -1) $scope.selected.splice(idx, 1);
            }
        };

        $scope.$watch('myQuery',() => {
            if($scope.topasTT.length > 0){
                $scope.toggled();
            }
        });

        $scope.options = {
            showOption : false,
            rowSelection: false,
            multiSelect: true,
            autoSelect: false,
            decapitate: false,
            largeEditDialog: false,
            boundaryLinks: false,
            limitSelect: true,
            pageSelect: true
        };
        $scope.query = {
            order: 'ticketno', // or ['-alarmtime','-receivetime'],
            limit: 500,
            page: 1
        };

         tools.http({
             service: tools.constants.Service.rca,
             action: "SELECT_TICKET_ALARM_BATCH_LIST",
             TICKET_ID: param
             // CLUSTER_NO: param
         }, function (result) {
             if(result){
                 $scope.topasTT = result;

                 $scope.topasTT.forEach((v, i) => {
                     v.selected = false;
                 });
             }
         });

        angular.element(document).ready(() => {
            $(window).resize( function() {
                $("table.topas-table > *").width($("table.topas-table").width() + $("table.topas-table").scrollLeft());
            });
            $("table.topas-table").on('scroll', function () {
                $("table.topas-table > *").width($("table.topas-table").width() + $("table.topas-table").scrollLeft());
            });
        });
    }
}
BatchProcessingDialogCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog','$element','param'];

export default BatchProcessingDialogCtrl;
