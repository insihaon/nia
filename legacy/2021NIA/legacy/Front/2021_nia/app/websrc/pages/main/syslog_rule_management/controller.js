import BaseController from 'scripts/controller/baseController'

class SyslogRuleManagementCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $mdSelect, $element, param) {
        $scope.config = tools.store.viewType.syslog_rule_management;
        super($injector, $scope, tools, $http, $timeout);
        $scope.isReadOnly = true;

        $scope.$watch('isReadOnly', function () {
            $('table.user-setting-table input').prop('readonly', $scope.isReadOnly);
        });

        $scope.syslogRuleList = [];

        $scope.historyPagingTable = {
            selected: 1,
            limit: 50
        };

        $scope.searchSyslogRuleData = {
            ruleName:'',
            occurKeyword: '',
            occurExceptKeyword: ''
        }

        $scope.chkProcessingTemplate = [
            { value: 'recovery', display_name: '자가회복'},
            { value: 'construction', display_name: '공사'}
        ];

        $scope.searchProcessing= (data) =>{
            return $scope.chkProcessingTemplate.filter(v => v.display_name.includes(data)).map(val => val.value).join()

        }

        $scope.ticketType = [
            {id:'1', ticket:'장비장애(RT)', value : 'RT'},
            {id:'2', ticket:'유해트래픽(NTT)', value : 'NTT'},
            {id:'3', ticket:'이상트래픽(ATT2)', value : 'ATT2'},
        ]

        $scope.searchClear = () => {
            $scope.searchSyslogRuleData.ruleName = '';
            $scope.searchSyslogRuleData.occurKeyword = '';
            $scope.searchSyslogRuleData.occurExceptKeyword = '';
            $scope.handleSearch();
        };

        $scope.handleSearch = () => {
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_SYSLOG_RULE_LIST",
                SYSLOG_RULE_NM: $scope.searchSyslogRuleData.ruleName,
                OCCUR_KEYWORD: $scope.searchSyslogRuleData.occurKeyword,
                OCCUR_EXCEPT_KEYWORD: $scope.searchSyslogRuleData.occurExceptKeyword
            }, function (result) {
                if (!result) return true;
                $scope.syslogRuleList = result;
            })
        };

        $scope.openSysRuleAdd = (editType = 'C', row = {}) => {
            const data = Object.assign(row, { editType })
            $mdDialog.show({
                controller: tools.store.viewType.syslog_rule_detail.controller,
                templateUrl: tools.store.viewType.syslog_rule_detail.path,
                locals: {param: {data}},
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true,
                fullscreen: false, // Only for -xs, -sm breakpoints.
                multiple: true
            }).then(function (result) {
                if(result) {
                    $scope.handleSearch();
                }
            }, function () {
            });
        };

        $scope.onMoveDialog = () => {
            const element = document.querySelector('#sysRuleListDialog');
            const toolbar = element.querySelector('md-toolbar');
            tools.draggableDialog(element, toolbar);
        };
 

        $scope.$broadcast('parentSearch', {search:$scope.handleSearch()});
        angular.element(document).ready(() => {
            $(".sys-rule-list-dialog").on('click', function (e) {
                if (!$(e.target).is('md-select') && !$(e.target).is('md-select *')) {
                    $mdSelect.hide();
                }
            });

            setTimeout(() => {
                $scope.$watch('historyPagingTable.selected', function () {
                    $scope.handleSearch();
                });

                $scope.$watch('historyPagingTable.limit', function () {
                    $scope.handleSearch();
                });

            }, 5 * 1000);
        });

        $scope.Options=[
            {id: 1, name:'유'},
            {id: 2, name:'무'},
        ]

    }
}
SyslogRuleManagementCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$mdSelect','$element','param'];

export default SyslogRuleManagementCtrl;
