/**
 * @ngdoc directive
 * @name processingComboboxGroup
 * @module rca.processing
 *
 * @restrict E
 *
 * @description
 * `<processing-combobox-group>` 티켓 처리 페이지 > 분류 선택 콤보박스 그룹 모듈화
 *
 * @param ng-model 콤보박스 그룹 데이터 모델 (Array)
 * ng-model > Object > action    호출 서비스 ID
 * ng-model > Object > selected  선택값
 * ng-model > Object > values    구성값
 *
 * @usage
 * <processing-combobox-group ng-model="reason"></processing-combobox-group>
 *
 * $scope.reason = [
 *    {
 *        action: 'SELECT_FAULTREASON_LV1_LIST',
 *        selected: '',
 *        values: []
 *    },
 *    {
 *        action: 'SELECT_FAULTREASON_LV2_LIST',
 *        selected: '',
 *        values: []
 *    },
 *    {
 *        action: 'SELECT_FAULTREASON_LV3_LIST',
 *        selected: '',
 *        values: []
 *    },
 * ]
 *
 */
let processingComboboxGroup = function () {
    function checkValidation(model) {
        if (!(model instanceof Array) || model.length <= 0) {
            console.error('모델 형식 오류');
            return false;
        }
        if (model.every(obj => (obj.action == undefined || obj.selected == undefined || obj.values == undefined))) {
            console.error('모델 객체 형식 오류');
            return false;
        }
        return true;
    }

    return {
        restrict: 'E',
        scope: {
            ngModel: '=',
            defaultValue: '='
        },
        template: '<md-select ng-repeat="combobox in ngModel" ng-disabled="($index != 0) && ngModel[getParent($index)].selected == \'\'" ' +
            '          ng-model="combobox.selected" ng-change="changeSelected($index)">' +
            '          <md-option ng-value="\'\'">분류표{{getLevel($index)}}</md-option>' +
            '          <md-option ng-repeat="item in combobox.values" ng-value="item">' +
            '              {{item}}' +
            '          </md-option>' +
            '      </md-select>',
        compile: function (element, attrs) {
            element.addClass('layout-row');
            return function ($scope) {
                if (!checkValidation($scope.ngModel)) {
                    return;
                }

                const LAST_IDX = $scope.ngModel.length - 1;
                let tools = $scope.$parent.tools || $tools;

                // 초기로드
                tools.http({
                    service: tools.constants.Service.rca,
                    action: $scope.ngModel[0].action
                }, function (result) {
                    if (!result) return;
                    $scope.ngModel[0].values = result.map(val => val.item);
                });

                $scope.changeSelected = (index) => {
                    if (index == LAST_IDX) { return; }

                    const CHILD_IDX = getChild(index);
                    if ($scope.ngModel[index].selected) {
                        let params = {
                            service: tools.constants.Service.rca,
                            action: $scope.ngModel[CHILD_IDX].action
                        };
                        for (let i = 0; i <= index; i++) {
                            params['LEVEL' + getLevel(i)] = $scope.ngModel[i].selected;
                        }
                        tools.http(params, function (result) {
                            if (!result) return;
                            resetCombobox(CHILD_IDX, result.map(val => val.item));
                        });
                    } else {
                        resetCombobox(CHILD_IDX);
                    }
                };

                // 디폴트값 세팅시 옵션값(values)
                $scope.applyValues = (defaultValue) => {
                    $scope.ngModel.forEach((item, idx, arr) => {
                        if(idx != 0) { item.values = [item.selected]; }     // For preload
                        if(idx == arr.length - 1) { return }
                        if(!defaultValue[idx]) { return }

                        const CHILD_IDX = getChild(idx);
                        let params = {
                            service: tools.constants.Service.rca,
                            action: $scope.ngModel[CHILD_IDX].action
                        };
                        for (let i = 0; i <= idx; i++) {
                            params['LEVEL' + getLevel(i)] = $scope.ngModel[i].selected;
                        }
                        tools.http(params, function (result) {
                            if (!result) return;
                            resetCombobox(CHILD_IDX, result.map(val => val.item), null);
                        });
                    });
                };

                /** @return Index or Number  */
                $scope.getLevel = idx => getLevel(idx);
                $scope.getParent = idx => getParent(idx);
                $scope.getChild = idx => getChild(idx);
                function getLevel(index) {
                    return index + 1;
                }
                function getParent(index) {
                    return (index > 0) ? index - 1 : undefined;
                }
                function getChild(index) {
                    return (index < LAST_IDX) ? index + 1 : undefined;
                }

                /**
                 * @param values, selected
                 *  undefined : default value
                 *  null : do not set  */
                function resetCombobox(index, values = [], selected = '') {
                    if(index == undefined) { return }

                    if(values != null) {
                        $scope.ngModel[index].values = values;
                    }
                    if (selected != null) {
                        $scope.ngModel[index].selected = selected;
                        $scope.changeSelected(index);
                    }
                }

                /** For Set Default Value(selected) */
                $scope.$watch(() => $scope.defaultValue, (n, o) => {
                    if (n === o){ return; }

                    n.forEach((val, idx) => { $scope.ngModel[idx].selected = val; });
                    $scope.applyValues(n);
                })

            };
        }
    };
}

export default [processingComboboxGroup]
