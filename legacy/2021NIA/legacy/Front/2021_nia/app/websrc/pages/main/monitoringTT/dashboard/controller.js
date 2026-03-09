import BaseController from 'scripts/controller/baseController'

class DashboardCtrl extends BaseController {
    constructor($injector, $scope, $window, tools, $http, $timeout, $mdDialog, $mdToast, $q, $mdSelect) {
        $scope.config = tools.store.viewType.dashboard;
        super($injector, $scope, tools, $http, $timeout, $mdDialog, $mdToast, $q);
        {
            $scope.dataType = 'DAY'
            $scope.dataTypeAll = [
                {text:'월데이터', value:'MONTH'},
                {text:'일데이터', value:'DAY'}
            ]
            
            $scope.tableData1 = [
                {name: '장애 티켓', value: 'ticket_rt_cnt'},
                {name: '광레벨 저하 티켓', value: 'ticket_pf_cnt'},
                {name: '이상 트래픽 티켓', value: 'ticket_att_cnt'},
                {name: '유해 트래픽 티켓', value: 'ticket_ntt_cnt'},
            ]
            $scope.tableData2 = [
                {name: '광레벨 수집', value: 'trans_perf_cnt'},
                {name: '전송 경보 수집', value: 'trans_alarm_cnt'},
                {name: 'IP 시설 연동', value: 'ip_resource_cnt'},
                {name: 'IP 경보 연동', value: 'ip_alarm_cnt'},
                {name: 'IP 트래픽 연동', value: 'ip_perf_cnt'},
                {name: 'IP SFlow 연동', value: 'ip_sflow_cnt'},
            ]
            $scope.tableData3 = [
                {name: '시설 연동', value: 'link_total_resource_cnt'},
                {name: '경보 연동', value: 'link_total_alarm_cnt'},
                {name: '트래픽 연동', value: 'link_ip_perf_cnt'},
                {name: '광레벨 연동', value: 'link_trans_perf_cnt'},
            ]

            $scope.dashboardOpinion = {
                dataType : '',
                startDateTime: '',
                endDateTime: '',
                month: ''
            };

            $scope.fromDateTime = ''
            $scope.toDateTime = ''
            $scope.month = ''

            $scope.searchClear = () => {
                $scope.dashboardOpinion.dataType = '';
                $scope.dashboardOpinion.startDateTime = '';
                $scope.dashboardOpinion.endDateTime = '';
                $scope.dashboardOpinion.month = '';

                $scope.handleSearch();
            };

            $scope.dashboardData = null;

                $scope.drawChart1= () => {
                    return c3.generate({
                        bindto: '#ticketChart',
                        title:{
                            display: true,
                            text:'티켓 발생량'
                        },
                        data: {
                            x: 'x',
                            columns:  $scope.getChart1Data(),
                            type: 'bar'
                        },
                        axis: {
                            x: {
                                type: 'category',
                                categories: ['장애 티켓', '광레벨 저하 티켓', '이상 트래픽 티켓', '유해 트래픽 티켓'],
                            },
                            y: {
                                padding: { top: 0, bottom: 0 },
                                label: {
                                  },
                                tick: {
                                    values:function(){
                                        return [0,1,2,3,4,5]
                                    },
                                    format: function (d) { return Math.pow(10,d).toFixed(0); }
                                }
                            }
                        },
                        tooltip: {
                            show: true
                        }
                    });
                };
            

            $scope.drawChart2= () => {
                return c3.generate({
                    bindto: '#dataCollectChart',
                    title:{
                        display: true,
                        text:'데이터 수집량'
                    },
                    data: {
                        x: 'x',
                        columns:  $scope.getChart2Data(),
                        type: 'bar'
                    },
                    axis: {
                        x: {
                            type: 'category',
                            categories: ['광레벨 수집', '전송 경보 수집', 'IP 시설 연동', 'IP 경보 연동', 'IP 트래픽 연동', 'IP Sfolw 연동'],
                        },
                        y: {
                            padding: { top: 0, bottom: 0 },
                            label: {
                              },
                            tick: {
                                values:function(){
                                    return [0,1,2,3,4,5,6]
                                },
                                format: function (d) { return Math.pow(10,d).toFixed(0); }
                            }
                        }
                    },
                    tooltip: {
                        show: true
                    }
                });
            };

            $scope.drawChart3 = () => {
                return c3.generate({
                    bindto: '#dataOfferChart',
                    title:{
                        display: true,
                        text:'데이터 제공량(데이터레이크 연계량)'
                    },
                    data: {
                        x: 'x',
                        columns:  $scope.getChart3Data(),
                        type: 'bar'
                    },
                    axis: {
                        x: {
                            type: 'category',
                            categories: ['시설 연동', '경보 연동', '트래픽 연동', '광레벨 연동'],
                        },
                        y: {
                            padding: { top: 0, bottom: 0 },
                            label: {
                              },
                            tick: {
                                values:function(){
                                    return [0,1,2,3,4,5,6]
                                },
                                format: function (d) { return Math.pow(10,d).toFixed(0); }
                            }
                        }
                    },
                    tooltip: {
                        show: true
                    }
                });
            };

            $scope.handleSearch= () => {
                $scope.dashboardOpinion.dataType === 'MONTH' ? (($scope.dashboardOpinion.startDateTime = ''),($scope.dashboardOpinion.endDateTime = '')) : $scope.dashboardOpinion.month = '' ; 
            tools.http({
                service: tools.constants.Service.rca,
                action: "SELECT_NIA_DASHBOARD_ONE",
                START_DATE : $scope.dashboardOpinion.startDateTime != '' ? $scope.dashboardOpinion.startDateTime : null,
                END_DATE : $scope.dashboardOpinion.endDateTime != '' ? $scope.dashboardOpinion.endDateTime : null,
                DATA_TYPE : $scope.dashboardOpinion.dataType != '' ? $scope.dashboardOpinion.dataType : null,
                MONTH : $scope.dashboardOpinion.month != '' ? $scope.dashboardOpinion.month : null,
            }, function (result) {
                // debugger
                if (!result) return;
                $scope.dashboardData = result;
                $scope.drawChart();
            });
        };

            $scope.getChart1Data = ()  => {
                if(!$scope.dashboardData ) return []

                const result = $scope.dashboardData 

                function toLogVlaue(value) {
                    const logVlaue = Math.log(value) / Math.LN10
                    if(Number.isFinite(logVlaue) === false ){
                        return 0
                    }
                    else if(Number.isNaN(logVlaue)){
                        return 0
                    }
                    return logVlaue
                }

                return  [
                    ['x','장애 티켓','광레벨 저하 티켓','이상 트래픽 티켓','유해 트래픽 티켓'],
                    ['DATA', toLogVlaue(result.ticket_rt_cnt), toLogVlaue(result.ticket_pf_cnt), toLogVlaue(result.ticket_att_cnt), toLogVlaue(result.ticket_ntt_cnt) ]
                ]
            }
            $scope.getChart2Data = ()  => {
                if(!$scope.dashboardData ) return []

                const result = $scope.dashboardData 

                function toLogVlaue(value) {
                    const logVlaue = Math.log(value) / Math.LN10
                    if(Number.isFinite(logVlaue) === false ){
                        return 0
                    }
                    else if(Number.isNaN(logVlaue)){
                        return 0
                    }
                    return logVlaue
                }

                return  [
                    ['x','광레벨 수집', '전송 경보 수집', 'IP 시설 연동', 'IP 경보 연동', 'IP 트래픽 연동', 'IP Sfolw 연동'],
                    ['DATA', toLogVlaue(result.trans_perf_cnt), toLogVlaue(result.trans_alarm_cnt), toLogVlaue(result.ip_resource_cnt), toLogVlaue(result.ip_alarm_cnt), toLogVlaue(result.ip_perf_cnt) , toLogVlaue(result.ip_sflow_cnt)  ]
                ]
            }
            $scope.getChart3Data = ()  => {
                if(!$scope.dashboardData ) return []

                const result = $scope.dashboardData 

                function toLogVlaue(value) {
                    const logVlaue = Math.log(value) / Math.LN10
                    if(Number.isFinite(logVlaue) === false ){
                        return 0
                    }
                    else if(Number.isNaN(logVlaue)){
                        return 0
                    }
                    return logVlaue
                }

                return  [
                    ['x','시설 연동', '경보 연동', '트래픽 연동', '광레벨 연동'],
                    ['DATA', toLogVlaue(result.link_total_resource_cnt), toLogVlaue(result.link_total_alarm_cnt), toLogVlaue(result.link_ip_perf_cnt), toLogVlaue(result.link_trans_perf_cnt) ]
                ]
            }
 
            $scope.onInit = () => {
                $scope.drawChart();
                $scope.handleSearch();
            }

            $scope.drawChart = () => {
                setTimeout(function () {
                    $scope.drawChart1();
                    $scope.drawChart2();
                    $scope.drawChart3()
                }, 500);
            }

            $scope.reload = () => { 
                alert(1)
            }

            // 저번달 구하기 함수
            $scope.lastMonth = () => {
                const now = new Date();
                const sel_month = -1;
                    now.setMonth(now.getMonth() + sel_month ); 
                const year    = now.getFullYear();
                const month   = ('0' + (now.getMonth() +  1 )).slice(-2);
                
                const lastMonth = year+'-'+month
                return lastMonth
            }

            $scope.onChangeMonth = (m) => {
                const {month} = $scope
                if(m){
                    $scope.dashboardOpinion.month = moment(month, 'YYYY-MM').add(0, 'months').format('YYYY-MM')
                    
                    // 임시방편(input type="month" max에 값이 안들어가는 관계로...) 
                    if($scope.dashboardOpinion.month > $scope.lastMonth()){
                        $tools.showAlert($scope.lastMonth()+'까지 선택가능합니다.')
                    }
                }
            }

            $scope.fromChangeDate = (from) => {
                const {fromDateTime} = $scope
                if(from) {
                        $scope.dashboardOpinion.startDateTime = moment(fromDateTime, 'YYYY-MM-DD').add(0, 'months').format('YYYY-MM-DD')
                } 
            }
            $scope.toChangeDate = (to) => {
                const {toDateTime} = $scope
                if(to) {
                        $scope.dashboardOpinion.endDateTime = moment(toDateTime, 'YYYY-MM-DD').subtract(0, 'months').format('YYYY-MM-DD')
                } 
            }
        }
    }
}

export default ['$injector', '$scope', '$window', 'tools', '$http', '$timeout', '$mdDialog', '$mdToast', '$q', '$mdSelect',  DashboardCtrl];
