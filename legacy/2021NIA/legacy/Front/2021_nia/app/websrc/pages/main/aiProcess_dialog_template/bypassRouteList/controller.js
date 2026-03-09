/**
 * Created by HYS on 2019-07-12.
 */
import BaseController from 'scripts/controller/baseController'

class BypassRouteListDialogCtrl extends BaseController {
    constructor($injector, $scope, tools, $http, $timeout, $mdDialog, $element, $mdSelect, param) {
        $scope.config = tools.store.viewType.bypass_route_list;
        super($injector, $scope, tools, $http, $timeout);

        $scope.param = param;
        $scope.nodeGridOptions = {} ; //DB 정보 조회
        $scope.selectRouteAllList = []; //IP SDN 정보 조회(특정 E2E 경로 조회)
        $scope.selectRouteE2enodeIdList = [];
        $scope.selectOptimalRouteList = []; //IP SDN 정보 조회(산정된 특정 E2E 최적경로 조회)
        $scope.e2enode_id = null

       //Date === today
       $scope.getToday = () => {
           var date = new Date();
           var year = date.getFullYear();
           var month = ("0" + (1 + date.getMonth())).slice(-2);
           var day = ("0" + date.getDate()).slice(-2);
       
           return year + "-" + month + "-" +day;
       }
       
       $scope.handleSearch = () => {
           tools.http({
               service: tools.constants.Service.rca,
               action: "SELECT_NIA_E2E_NODE_ONE",
               IS_ALL: true,
               // ROOT_CAUSE_SYSNAMEA: 'seoul-5812',
               // ROOT_CAUSE_SYSNAMEZ: 'jeonju-5812'
               ROOT_CAUSE_SYSNAMEA: $scope.param.data.root_cause_sysnamea,
               ROOT_CAUSE_SYSNAMEZ: $scope.param.data.root_cause_sysnamez 
           }, function (result) {
               if (!result) return true;
               $scope.nodeGridOptions = result;
               $scope.onLoadRouteList($scope.nodeGridOptions.id);
           });
       }
       
       $scope.onLoadRouteList = (e2enode_id) => {
           // IP_SDN result(특정 E2E 경로 조회)
           tools.sdnRequest('GET', 'ipsdn/opt/cur_route/'+e2enode_id).then(result => {
               if(result.data.length > 0){
                   result.data.forEach(d => {
                       if (
                           $scope.nodeGridOptions.snode_nm.toLowerCase() === d.current_paths[0].send_node_name.toLowerCase() &&
                           $scope.nodeGridOptions.rnode_nm.toLowerCase() === d.current_paths[d.current_paths.length - 1].receive_node_name.toLowerCase()
                       ){
                           $scope.selectRouteE2enodeIdList = d
                       }
                   });
               }else{
                   return false;
               }
           $scope.e2enode_id = $scope.selectRouteE2enodeIdList.e2enode_id

           $scope.selectRouteAllList = result.data.find(nodeId => nodeId.e2enode_id == $scope.e2enode_id)
       }).then(function(){
                tools.sdnRequest('GET', 'ipsdn/opt/optimization?sourcedate='+$scope.getToday()).then(result => {
                    if(result){
                        // IP_SDN result(산정된 특정 E2E 최적경로 조회)
                        tools.sdnRequest('GET', 'ipsdn/opt/opt_route/'+e2enode_id+'?sourcedate='+$scope.getToday()).then(result => {
                            if(result.data.length > 0){
                                result.data[0].optimized_paths.forEach((optimizedPath) => {
                                    $scope.selectOptimalRouteList = optimizedPath.paths
                                });
                            }else{
                                return false;
                            }
                        })
                    }else{
                        return false;
                    }
                })   
           })
       }
       
        $scope.openOptimalRouteCount = () => { // 전체 경로 가능 리스트 팝업
            $mdDialog.show({
                controller: tools.store.viewType.optimal_route_count.controller,
                templateUrl: tools.store.viewType.optimal_route_count.path,
                locals: { param: { ticket: $scope.ticket, test: 'test' } },
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true,
                fullscreen: false,
                multiple: true
            })
        };

        $scope.openOptimalRouteSetting = () => { //이력 조회 리스트 팝업
            $mdDialog.show({
                controller: tools.store.viewType.optimal_route_setting.controller,
                templateUrl: tools.store.viewType.optimal_route_setting.path,
                locals: { param: { ticketInfo: $scope.nodeGridOptions, optimalRouteInfo: $scope.selectOptimalRouteList } },
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: true,
                fullscreen: false,
                multiple: true
            })
        };

        $scope.routeAddDataList = {
            userId: 'test',
            result : '성공',
            handlingType : '우회'
        };


        $scope.applyBypassRoute = () => {
            // IP_SDN result(우회 경로 적용)
            // sourcedate = 현재날짜 yyyy-mm-dd
            return tools.sdnRequest('POST', 'ipsdn/opt/optimization/config?sourcedate='+$scope.getToday()).then(result => {
                if (!result.status) {
                    tools.showAlert("우회 경로 대상이 아닙니다.")
                } else {
                    tools.createConfirmDlg(null, "경로를 우회 하시겠습니까?", null).then(() => {
                        const data = $scope.routeAddDataList;
                        tools.http({
                            service: tools.constants.Service.rca,
                            action: "INSERT_NIA_OPTIMAL_ROUTE_LIST",
                            TICKET_ID: $scope.nodeGridOptions.id,
                            USER_ID: data.userId,
                            RESULT: data.result,
                            HANDLING_TYPE: data.handlingType,
                            LINK_ID: $scope.selectOptimalRouteList[0].link_id,
                        }, function (result) {
                            if (result)
                                tools.showToast('적용되었습니다.');
                        });
                    })
                }
            })
        }

        $scope.onMoveDialog = () => {
           const element = document.querySelector('#bypassRouteList');
           const toolbar = element.querySelector('md-toolbar');
           tools.draggableDialog(element, toolbar);
       };

       angular.element(document).ready(() => {
           $scope.handleSearch();
       });

        $scope.gridOptions = {};
        tools.http({
            service: tools.constants.Service.rca,
            action: "SELECT_USER_LIST",
            IS_ALL: true
        }, function (result) {
            if (!result) return true;

            $scope.gridOptions.data = result;

            $scope.isCheck = (item, value) => {
                return !!(item.lvl & value);
            }
            $scope.lvlTable.totalCount = result.count;

            angular.forEach(tools.constants.UserGrant, function (key) {
                if ((item.lvl & key.value) != 0) {
                    item.chanagedGrantArray.push(key)
                }
            });
        });

    }
}

BypassRouteListDialogCtrl.$inject = ['$injector', '$scope', 'tools', '$http', '$timeout', '$mdDialog', '$element', '$mdSelect' , 'param'];
export default BypassRouteListDialogCtrl;
