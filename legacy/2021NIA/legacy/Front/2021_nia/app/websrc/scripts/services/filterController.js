class FilterController {
    /**
     * constructor
     * @param object 필터생성의 기반이 될 Object 혹은 Array 데이터
     * @param strObjectName 필터생성의 기반이 될 Object 혹은 Array name
     * 필터항목의 자식요소는 `{display: "표기라벨", value: "값", selected: "선택여부"}` 형식으로 구성 되어야함
     * @param scope view와 view 컨트롤러의 접근에 필요한 scope 객체
     */
    constructor(strObjectName, scope, type = 1) {
        let {$scope, tools} = scope;
        // let object = eval('tools.store.filterStorage.filter.' + strObjectName) || eval('tools.store.' + strObjectName);
        let object = null;
        if(type == 2) {
            object = eval('tools.store.' + strObjectName);
        } else {
            object = eval('tools.store.filterStorage.filter.' + strObjectName)
        }

        if (!(Array.isArray(object) || typeof object === 'object') || !scope) {
            throw new Error('parameter error');
        }

        let isArray = Array.isArray(object);
        let defaultAllObj = {
            display: '전체', value: 'ALL', selected:
                (isArray ? object.every(item => item.selected) : Object.keys(object).every(item => object[item].selected))
        };
        let _object = isArray ? [defaultAllObj] : {ALL: defaultAllObj};

        angular.forEach(object, (item, key) => {
            if(isArray) {
                _object.push(item);
            } else {
                _object[key] = item;
            }
        });

        $scope.$watchCollection(() => tools.store.filterStorage.filter, (n, o) => {
            let converted_n = JSON.stringify(n, (key, value) => (key === "$$hashKey" ? undefined : value));
            let converted_o = JSON.stringify(o, (key, value) => (key === "$$hashKey" ? undefined : value));
            if (converted_n === converted_o) {
                return;
            }
            methods().loadObject(strObjectName);
        });

        function methods() {
            return {
                get obj() {
                    return _object;
                },
                getObj() {
                    return _object;
                },

                loadObject(strObjectName) {
                    object = eval('tools.store.filterStorage.filter.' + strObjectName);
                    isArray = Array.isArray(object);
                    defaultAllObj = {
                        display: '전체', value: 'ALL', selected:
                            (isArray ? object.every(item => item.selected) : Object.keys(object).every(item => data[item].selected))
                    };
                    angular.copy((isArray ? [defaultAllObj] : {ALL: defaultAllObj}), _object);
                    //_object = isArray ? [defaultAllObj] : {ALL: defaultAllObj};

                    angular.forEach(object, (item, key) => {
                        if(isArray) {
                            _object.push(item);
                        } else {
                            _object[key] = item;
                        }
                    });
                },
                itemClick(index) {
                    (index == 0 || index == 'ALL') ? this.allClick() : this.filterClick(index);
                },
                allClick() {
                    let val = !_object[isArray ? 0 : 'ALL'].selected;
                    if (Array.isArray(_object)) {
                        _object.forEach(item => {
                            item.selected = val;
                        });
                    } else {
                        Object.keys(_object).forEach(item => {
                            _object[item].selected = val;
                        });
                    }

                    this.saveFilter(_object);
                },
                filterClick(index) {
                    _object[index].selected = !_object[index].selected;
                    _object[isArray ? 0 : 'ALL'].selected = this.getIsAllChecked();
                    this.saveFilter();
                },

                getIsAllChecked(data = _object) {
                    if (Array.isArray(data)) {
                        return data.filter(item => item.value != 'ALL').every(item => item.selected);
                    } else {
                        return Object.keys(data).filter(item => item != 'ALL')
                            .every(item => data[item].selected);
                    }
                },

                saveFilter() {
                    let storage = tools.store.filterStorage;

                    if (storage.filter) {
                        storage.saveFilter(storage.filter);
                    } else {
                        console.log('Failed saveFilter');
                    }
                },
            };
        }

        Object.assign(this, methods());
    }
}

export default FilterController
