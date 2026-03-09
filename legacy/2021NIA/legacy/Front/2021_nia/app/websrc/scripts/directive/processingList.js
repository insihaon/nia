let processingList = function ($compile, tools) {
    let type1 = {
        service: 'SELECT_TICKET_ROOT_ALARM_LIST',
        TICKET_TYPE: tools.constants.RcaTicketType.TICKET.value,
        field: [
            { size: 190, head: 'A측 시스템', content: '{{item.root_cause_sysnamea}}' },
            { size: 130, head: 'A측 포트', content: '{{item.root_cause_porta}}' },
            { size: 190, head: 'Z측 시스템', content: '{{item.root_cause_sysnamez}}' },
            { size: 130, head: 'Z측 포트', content: '{{item.root_cause_sysnamez}}' },
        ]
    };
    let type2 = {
        fieldName: 'tc_list',
        field: [
            { size: 205, head: '광케이블명', content: '{{item.ocaname}}' },
            { size: 165, head: 'A측 설치장소', content: '{{item.locdescriptiona}}' },
            { size: 165, head: 'Z측 설치장소', content: '{{item.locdescriptionz}}' },
            { size: 105, head: '장애확률', content: '{{item.cnt1 || \'0\'}} / {{item.rca_max_cnt || \'0\'}} / {{item.max_cnt || \'0\'}}' },
        ]
    };
    let type3 = {
        service: 'SELECT_TICKET_ROOT_ALARM_LIST',
        TICKET_TYPE: tools.constants.RcaTicketType.INSPECTOR.value,
        field: [
            { size: 190, head: 'A측 시스템', content: '{{item.root_cause_sysnamea}}' },
            { size: 130, head: 'A측 포트', content: '{{item.root_cause_porta}}' },
            { size: 190, head: 'Z측 시스템', content: '{{item.root_cause_sysnamez}}' },
            { size: 130, head: 'Z측 포트', content: '{{item.root_cause_sysnamez}}' },
        ]
    };

    let getType = function(contentType){
        let type = {};

        switch(contentType){
            case 'Alarm':
                type = type1;
                break;
            case 'Loc':
                type = type2;
                break;
            case 'Inspector':
                type = type3;
                break;
        }

        return type;
    };

    let getTemplate = function (field) {
        return '<md-content layout="column" class="processing-table-container">' +
            '          <table md-table class="processing-table rca-md-table">' +
            '              <thead md-head>' +
            '              <tr md-row>' +
            field.map(val => ` <th md-column md-column-size="${val.size}">${val.head}</th> `).join('') +
            '              </tr>' +
            '              </thead>' +
            '              <tbody md-body>' +
            '              <tr md-row ng-repeat="item in list">' +
            field.map(val => ` <td md-cell md-column-size="${val.size}" over-cell>${val.content}</td> `).join('') +
            '              </tr>' +
            '              </tbody>' +
            '          </table>' +
            '          <rca-circular-progress size="40" ng-model="isShowProgress"></rca-circular-progress>' +
            '      </md-content>';
    };

    return  {
        restrict: 'E',
        scope: {
            content: '@',
            ticketId: '='
        },
        link: function($scope, element, attrs){
            let type = getType(attrs.content);
            let template = getTemplate(type.field);

            element.html(template);
            $compile(element.contents())($scope);
            load();

            $scope.$watch(() => $scope.ticketId, (n, o) => {
                if (n === o) { return; }
                load();
            });

            function load() {
                let tools = $scope.$parent.tools || $tools;

                if(attrs.content == 'Loc') {
                    let ticket = tools.store.tickets.find(val => val.ticket_id == $scope.ticketId);
                    if (ticket && ticket.tc_list) {
                        $scope.list = ticket.tc_list;
                    }
                    return;
                }

                $scope.isShowProgress = true;

                tools.http({
                    service: tools.constants.Service.rca,
                    action: type.service,
                    TICKET_ID: $scope.ticketId,
                    TICKET_TYPE: type.TICKET_TYPE,
                    MAX_DAYS: 31
                }, function (result) {
                    $scope.isShowProgress = false;
                    if (!result) return true;
                    $scope.list = result;
                });

            }
        }
    };
}

export default ['$compile', 'tools',  processingList]
