import BaseController from 'scripts/controller/baseController'

class SelfProcessCtrl extends BaseController {
    constructor($injector, $scope, $window, tools, $http, $timeout, $mdDialog, $mdToast, $q, $mdSelect) {
        $scope.config = tools.store.viewType.selfProcess;
        super($injector, $scope, tools, $http, $timeout, $mdDialog, $mdToast, $q);
        {
            $scope.dataTypeAll = [
                {text:'월별', value:'YEAR'},
                {text:'일별', value:'MONTH'},
                {text:'시간별', value:'DAY'}
            ]
            const emailInfo = {
                ticket_id : tools.store.ticket_id,
                alarmno : tools.store.alarmno,
                self_process_type : tools.store.self_process_type
            }

            const currentDate = new Date();
            const year = currentDate.getFullYear().toString();
            
            let month = (currentDate.getMonth() + 1).toString(); // getMonth()는 0부터 시작하므로 1을 더함
            month = month.length === 1 ? '0' + month : month; // 한 자릿수일 경우 앞에 0을 추가
            
            let day = currentDate.getDate().toString();
            day = day.length === 1 ? '0' + day : day; // 한 자릿수일 경우 앞에 0을 추가
            
            const formattedMonth = `${year}-${month}`;
            const formattedDay = `${year}-${month}-${day}`;
            
            angular.element(document).ready(() => {
                // $scope.drawChart();
                $scope.getSelfprocessYearList();
                $scope.loadSelfOptimizationList();
                $scope.loadSelfRecoveryList();
                if (tools.store.isEmail === true) {
                    $scope.handleSearch();
                }
            });

            $scope.yearList=[];

            $scope.getSelfprocessYearList=()=>{
                tools.http({
                    service: tools.constants.Service.rca,
                    action: 'SELECT_SELF_PROCESS_YEAR_LIST',
                }, function (result) {
                    if (!result) return;
                    $scope.yearList = result;
                });
            }

            $scope.handleSearch = () => {
                tools.http({
                    service: tools.constants.Service.rca,
                    action: "SELECT_SELF_PROCESS_DETAIL_PAGE2_LIST",
                    TICKET_ID: emailInfo.ticket_id != '' ? emailInfo.ticket_id : null,
                    ALARMNO: emailInfo.alarmno != '' ? emailInfo.alarmno : null,
                    SELF_PROCESS_TYPE: emailInfo.self_process_type != '' ? emailInfo.self_process_type : null
                }, function (result) {
                    if (!result) return true;
                    const item = result.list[0]
                    $scope.openToEmailDialog(item);
                })
            };
            
            $scope.openToEmailDialog = (item) => {
                $mdDialog.show({
                    controller: tools.store.viewType.selfProcess_email_dialog.controller,
                    templateUrl: tools.store.viewType.selfProcess_email_dialog.path,
                    locals: {param: {data: item}},
                    parent: angular.element(document.body),
                    targetEvent: event,
                    clickOutsideToClose: true,
                    fullscreen: false, // Only for -xs, -sm breakpoints.
                    multiple: true
                }).then(function (result) {
                    if(result) {
                        // $scope.handleSearch();
                    }
                }, function () {
                });
            };

            $scope.drawChart = () => {
                setTimeout(function () {
                    $scope.drawChart1();
                    $scope.drawChart2();
                }, 500);
            }

            $scope.openDetail = (item,type,dateType) => {
                $mdDialog.show({
                    controller: tools.store.viewType.selfProcess_dialog.controller,
                    templateUrl: tools.store.viewType.selfProcess_dialog.path,
                    locals: {param: {data: item, type: type, dateType : dateType}},
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

        const getAction = (type) => {
            if(type === 'O'){
                if($scope.optimizationOptions.dateType === 'YEAR'){
                    return "SELECT_SELF_PROCESS_MONTH_LIST"
                }else if($scope.optimizationOptions.dateType === 'MONTH'){
                    return "SELECT_SELF_PROCESS_DAY_LIST"
                }else{
                    return "SELECT_SELF_PROCESS_HOUR_LIST"
                }
            }else{
                if($scope.recoveryOptions.dateType === 'YEAR'){
                    return "SELECT_SELF_PROCESS_MONTH_LIST"
                }else if($scope.recoveryOptions.dateType === 'MONTH'){
                    return "SELECT_SELF_PROCESS_DAY_LIST"
                }else{
                    return "SELECT_SELF_PROCESS_HOUR_LIST"
                }
            }
        }

        // 자가 최적화
        $scope.optimizationData = null;
        $scope.optimizationEnabled = true;

        $scope.optimizationOptions = {
            dateType : 'DAY',
            dateTime: ''
        };
        $scope.optimizationSearchClear = () => {
            $scope.optimizationOptions = {
                dateType : 'DAY',
                dateTime: ''
            };

            $scope.loadSelfOptimizationList();
        };
        $scope.loadSelfOptimizationList= (type) => {
            $scope.optimizationOptions.dateTime !== '' ? $scope.optimizationEnabled = false : $scope.optimizationEnabled = true
            tools.http({
                service: tools.constants.Service.rca,
                action: getAction(type),
                YEAR : $scope.optimizationOptions.dateTime != '' ? moment($scope.optimizationOptions.dateTime).format('YYYY') : year,
                MONTH : $scope.optimizationOptions.dateTime != '' ? moment($scope.optimizationOptions.dateTime).format('YYYY-MM') : formattedMonth ,
                DAY : $scope.optimizationOptions.dateTime != '' ?moment($scope.optimizationOptions.dateTime).format('YYYY-MM-DD') : formattedDay,
                SELF_PROCESS_TYPE : 'SO',
                PROCESS : '최적화'
            }, function (result) {
                if (!result) return;
                $scope.optimizationData = result;
                $scope.optimizationDataLen = result.length;
                $scope.drawChart1($scope.optimizationDataLen);
            });
        };

        $scope.optimizationChart = document.querySelector('#optimizationChart');
        $scope.optimizationChart.addEventListener('scroll', function () {
            var scrollX = $scope.optimizationChart.scrollLeft;
            scrollX !== 0 ? document.querySelector('#optimizationChart > svg > g:nth-child(4)').style.transform = 'translateX(' + (scrollX + 20) + 'px)' : document.querySelector('#optimizationChart > svg > g:nth-child(4)').style.transform = 'translateX(50px)'
        });
        
        $scope.drawChart1 = (maxLabelCount) => {
            var chartWidth = maxLabelCount * 50;
            return c3.generate({
                size: {
                    width: chartWidth * 2
                },
                bindto: '#optimizationChart',
                legend: {
                    position: 'inset',
                    inset: {
                        anchor: 'top-left',
                        x: 0,
                        y: 0,
                        step: 2
                    }
                },
                data: {
                    x: 'x',
                    columns: $scope.getChart1Data(),
                    type: 'bar'
                },
                axis: {
                    x: {
                        show: true,
                        type: 'category',
                        categories: $scope.optimizationData.map(item => item.hourly_time),
                        tick: {
                            multiline: true,
                        }
                    },
                    y: {
                        show: true,
                        tick: {
                            format: function (d) {
                                if (d === Math.floor(d)) {
                                    return d;
                                }
                            }
                        }
                    }
                },
                tooltip: {
                    show: true
                },
                bar: {
                    width: {
                        ratio: 0.5
                    }
                },
                onrendered: function () {
                    d3.select('#optimizationChart').style('max-height', 'none');
                }
            });
        };

        $scope.getChart1Data = ()  => {
            if(!$scope.optimizationData) return [];
            const result = $scope.optimizationData;

            return  [
                    ['x',...result.map(item => item.hourly_time)],
                    ['총 발생 건수', ...result.map(item => item.totalcount)],
                    ['자가 최적화 처리 건수', ...result.map(item => item.count)]
                ]
        };

        // 자가 최적화 토글 함수
        // $scope.toggleOptimization = () => {
        //     $scope.optimizationEnabled = !$scope.optimizationEnabled;
        //     $scope.optimizationEnabled === true ? tools.showToast('실시간 조회가 시작되었습니다.') : tools.showToast('실시간 조회가 종료되었습니다.')
        //     // 토글 상태가 변경되었을 때 수행할 작업 추가
        // };

        //자가 회복
        $scope.recoveryData = null;
        $scope.recoveryEnabled = true;

        $scope.recoveryOptions = {
            dateType : 'DAY',
            dateTime: ''
        };
        
        $scope.recoverySearchClear = () => {
            $scope.recoveryOptions = {
                dateType : 'DAY',
                dateTime: ''
            };

            $scope.loadSelfRecoveryList();
        };
        $scope.loadSelfRecoveryList= (type) => {
            $scope.recoveryOptions.dateTime !== '' ? $scope.recoveryEnabled = false : $scope.recoveryEnabled = true
            tools.http({
                service: tools.constants.Service.rca,
                action: getAction(type),
                YEAR : $scope.recoveryOptions.dateTime != '' ? moment($scope.recoveryOptions.dateTime).format('YYYY') : year,
                MONTH : $scope.recoveryOptions.dateTime != '' ? moment($scope.recoveryOptions.dateTime).format('YYYY-MM') : formattedMonth ,
                DAY : $scope.recoveryOptions.dateTime != '' ?moment($scope.recoveryOptions.dateTime).format('YYYY-MM-DD') : formattedDay,
                SELF_PROCESS_TYPE : 'ST',
                PROCESS : '회복'
            }, function (result) {
                if (!result) return;
                $scope.recoveryData = result;
                $scope.recoveryDataLen = result.length;
                $scope.drawChart2($scope.recoveryDataLen);
            });
        };

        $scope.recoveryChart = document.querySelector('#recoveryChart');
        
        $scope.recoveryChart.addEventListener('scroll', function () {
            var scrollX = $scope.recoveryChart.scrollLeft;
            scrollX !== 0 ? document.querySelector('#recoveryChart > svg > g:nth-child(4)').style.transform = 'translateX(' + (scrollX + 20) + 'px)' : document.querySelector('#recoveryChart > svg > g:nth-child(4)').style.transform = 'translateX(50px)'
        });

        $scope.drawChart2= (maxLabelCount) => {
            var chartWidth = maxLabelCount * 50;
            return c3.generate({
                bindto: '#recoveryChart',
                legend: {
                    position: 'inset',
                    inset: {
                        anchor: 'top-left',
                        x: 0,
                        y: 0,
                        step: 2
                    }
                },
                size: {
                    width: chartWidth * 2 // 원하는 넓이로 설정
                },
                data: {
                    x: 'x',
                    columns:  $scope.getChart2Data(),
                    type: 'bar'
                },
                axis: {
                    x: {
                        type: 'category', // 카테고리 형식 사용
                        categories: $scope.recoveryData.map(item => item.hourly_time), // hourly_time의 값을 사용
                        tick:{
                            multiline: true,
                        }
                    },
                    y: {
                        show: true,
                        tick: {
                          format: function (d) {
                            if (d === Math.floor(d)) {
                                return d;
                            }
                          }
                        }
                    }
                },
                tooltip: {
                    show: true
                },
                onrendered: function () {
                    d3.select('#recoveryChart').style('max-height', 'none');
                }
            });
        };
        $scope.getChart2Data = ()  => {
            if(!$scope.recoveryData) return [];
            const result = $scope.recoveryData;

            return  [
                    ['x',...result.map(item => item.hourly_time)],
                    ['총 발생 건수', ...result.map(item => item.count)],
                    ['자가 회복 처리 건수', ...result.map(item => item.count)]
                ]
        };
        
        // 자가 회복 토글 함수
        // $scope.toggleRecovery = function() {
        //     $scope.recoveryEnabled = !$scope.recoveryEnabled;
        //     $scope.recoveryEnabled === true ? tools.showToast('실시간 조회가 시작되었습니다.') : tools.showToast('실시간 조회가 종료되었습니다.')
        //     // 토글 상태가 변경되었을 때 수행할 작업 추가
        // };
        }
    }
}

export default ['$injector', '$scope', '$window', 'tools', '$http', '$timeout', '$mdDialog', '$mdToast', '$q', '$mdSelect',  SelfProcessCtrl];
