import {guid, pushToArr, setProp} from "./util";

/**
 * This class simulates a RESTful resource, but the API calls fetch data from
 * Session Storage instead of an HTTP call.
 *
 * Once configured, it loads the initial (pristine) data from the URL provided (using HTTP).
 * It exposes GET/PUT/POST/DELETE-like API that operates on the data.  All the data is also
 * stored in Session Storage.  If any data is modified in memory, session storage is updated.
 * If the browser is refreshed, the SessionStorage object will try to fetch the existing data from
 * the session, before falling back to re-fetching the initial data using HTTP.
 *
 * For an example, please see dataSources.js
 */
export class Storage {
    /**
     * Creates a new SessionStorage object
     *
     * @param $http Pass in the $http service
     * @param $timeout Pass in the $timeout service
     * @param $q Pass in the $q service
     * @param storageKey The session storage key. The data will be stored in browser's session storage under this key.
     * @param sourceUrl The url that contains the initial data.
     */
    constructor($injector, storage, storageKey, sourceUrl, defaultValue, idProp = '_id', eqFn) {

        if (!storage) {
            return;
        }

        Object.assign(this, {
            storage, storageKey, sourceUrl,
            $http: $injector.get('$http'),
            $timeout: $injector.get('$timeout'),
            $q: $injector.get('$q'),
            fromStroage : storage.getItem(storageKey),
            _idProp: idProp, // For each data object, the _idProp defines which property has that object's unique identifier
            _eqFn: eqFn ? eqFn : (l, r) => l[this._idProp] === r[this._idProp],    // A basic triple-equals equality checker for two values
            compare_contains: (search, original) => ("" + original).indexOf("" + search) !== -1,
            compare_equals: (search, original) => search == original
        });

        // var isClear = this._getAutoClearFlag('Filter', 'maxDays');
        // var isClear = this._getAutoClearFlag('Filter', 'equipType', 3);
        var isClear = false;
        this._load(isClear, sourceUrl, defaultValue, eqFn);
    }

    /** 신규 로컬스토리지 추가후 배포시 스토리지 자동 클리어 */
    /** 수정 날짜 2019/07/26 */
    /** childFieldIndex : 기존 필터의 항목만 추가시 체크할 추가항목 인덱스값 [ex) ticketType에 새로운 타입이 추가됬을 경우]*/
    _getAutoClearFlag(storageKey, checkFieldName, childFieldIndex = null) {
        if(!this.storage[storageKey]) return false;

        var fieldData;
        try{
            if(childFieldIndex){
                fieldData = JSON.parse(this.storage.Filter)[0][checkFieldName][childFieldIndex];
            } else {
                fieldData = JSON.parse(this.storage.Filter)[0][checkFieldName];
            }
        } catch (e) {
            return true;
        }

        if(this.storageKey == storageKey && !(fieldData)){
            return true;
        }
        return false;
    }

    /** Saves all the data back to the session storage */
    _commit(data) {

        let {storage} = this;

        if (data.length > 4000) {
            data.splice(4000, data.length - 4000);
        }

        storage.setItem(this.storageKey, JSON.stringify(data));
        return this.$q.resolve(data);
    }

    _load(isClear, sourceUrl, defaultValue, eqFn){

        if(isClear){
            this.removeAll();
            this.fromStroage = undefined;
        }

        sourceUrl = sourceUrl || this.sourceUrl;
        defaultValue = defaultValue || this.defaultValue;
        eqFn = eqFn || this._eqFn;

        Object.assign(this, { sourceUrl,  defaultValue, eqFn });

        let {$q, storage, storageKey, $http, fromStroage} = this;

        let data;

        // A promise for *all* of the data.

        if (storage != sessionStorage && fromStroage) {
            try {
                // Try to parse the existing data from the Session Storage API
                data =  JSON.parse(fromStroage);

                if (data.length == 1) {
                    assignDefault(data[0], defaultValue);
                }

            } catch (e) {
                console.log("Unable to parse session messages, retrieving intial data.");
            }
        } else if (defaultValue) {
            data = [].concat(defaultValue);
        } else if (sourceUrl === undefined) {
            data = new Array();
        }

        this._data = undefined;

        let stripHashKey = (obj) =>
            setProp(obj, '$$hashKey', undefined);

        /***
         * targetValue 와 defaultValue 를 비교해서  defaultValue 만 있으면 targetValue 에 카피하고,  targetValue 만 있으면 targetValue 에서 삭제한다.
         * 즉 targetValue 를 defaultValue 와 같은 키값만 남기도록 한다.
         * @param targetValue
         * @param defaultValue
         */
        function assignDefault(targetValue, defaultValue) {

            function clone(source) {
                return JSON.parse(JSON.stringify(source))
            }

            function euqals(a, b) {
                return JSON.stringify(a) == JSON.stringify(b);
            }

            if (defaultValue) {

                if (!targetValue) {
                    targetValue = clone(defaultValue);
                    return;
                }

                if (targetValue instanceof Array && defaultValue instanceof Array) {

                    if (targetValue.length > defaultValue.length  && targetValue[defaultValue.length] instanceof Object) {
                        targetValue.splice(defaultValue.length);
                    }

                    for (var i = 0; i < defaultValue.length; i++) {

                        if (i >= targetValue.length) targetValue.push(null);

                        JSON.stringify(defaultValue[i])
                        if (!targetValue[i]) {
                            targetValue[i] = clone(defaultValue[i]);
                        } else {
                            var v1 = clone(targetValue[i]);
                            var v2 = clone(defaultValue[i]);
                            delete (v1.selected);
                            delete (v2.selected);

                            if (!euqals(v1, v2)) {
                                targetValue[i] = clone(defaultValue[i]);
                            }
                        }
                    }

                } else if (targetValue instanceof Object && defaultValue instanceof Object) {
                    var keys1 = Object.keys(targetValue);
                    var keys2 = Object.keys(defaultValue);
                    var deletedKeys = keys1.filter(n => !keys2.includes(n));

                    for (var key in defaultValue) {

                        if (!targetValue[key]) {
                            targetValue[key] = clone(defaultValue[key]);
                        } else {
                            // if (targetValue[key] instanceof Object && defaultValue[key] instanceof Object) {
                            assignDefault(targetValue[key], defaultValue[key]);
                            // }
                        }
                    }

                    for (let i = 0; i < deletedKeys.length; i++) {
                        delete targetValue[deletedKeys[i]];
                    }
                }
            }
        }

        function requestPost() {

            return $http.post(sourceUrl.path || 'service', sourceUrl)
                .then(
                    resp => {

                        // if(sourceUrl.action =="GET_TICKET_ALL")
                        //     debugger;

                        if ((resp.data || {}).error) {

                            throw "데이터조회실패";
                        }

                        if (sourceUrl.callback) {
                            return sourceUrl.callback.call(this, resp);
                        } else {
                            return resp.data.result != null && Array.isArray(resp.data.result) ? resp.data.result : resp.data
                        }
                    })/*.catch()*/;
        }

        function queryAndPostRequest() {
            return (sourceUrl.query ? sourceUrl.query().then(param => {
                    Object.assign(sourceUrl, param);
                    return requestPost()
                })
                : requestPost());
        }

        function requestGet() {
            return $http.get(sourceUrl)
                .then(resp => {
                    if (!((resp || {}).data)) throw "데이터조회실패";
                    return resp.data
                }).catch();
        }

        function request() {
            return (sourceUrl !== null && typeof sourceUrl === 'object') ?
                queryAndPostRequest()
                : requestGet();
        }

// Create a promise for the data; Either the existing data from session storage, or the initial data via $http request
        this._data = (data ? $q.resolve(data) : request())
            .then(this._commit.bind(this))
            .then(() => JSON.parse(storage.getItem(storageKey)))
            .then(
                array => array.map(stripHashKey));
    }

    setData(defaultValue)
    {
        this._load(true, null, defaultValue, null);
    }

    clear()
    {
        this._load(true, null, null, null);
    }

    reload(thenFn) {
        this._load(true, this.sourceUrl, this.defaultValue, this._eqFn);
        thenFn = thenFn || function () {};
        this.all(thenFn);
    }

    /**
     * Helper which simulates a delay, then provides the `thenFn` with the data
     * 모든 데이터를 조회한다. 조회 우선순위는 아래와 같다
     * 1. localStorage or sessionStorage 키 값이 존재하는 데이터
     * 2. defaultValue 에 준 데이터
     * 3. sourceUrl 에 의해 조회된 데이터
     **/
    all(thenFn) {

        // if (this.storageKey == 'Ticket') {
        //     debugger;
        // }
        return this.$timeout(() => this._data).then(thenFn);
    }

    log() {

        return this.all( items => {
            console.log(items);
            debugger;
            return items;
        })
    }

    /**
     * Given a sample item, returns a promise for all the data for items which have the same properties as the sample
     * 조건에 맞는 데이터를 검색한다.
     **/
    search(exampleItem, fn_compare) {
        fn_compare = fn_compare || this.compare_equals;
        let matchesExample = (example, item) =>
            Object.keys(example).reduce((memo, key) => memo && fn_compare(example[key], item[key]), true);
        return this.all(items => {
            return items.filter(matchesExample.bind(null, exampleItem));
        });
    }

    /**
     * Returns a promise for the item with the given identifier
     * _id 값으로 데이터를 조회한다.
     **/
    get(id) {
        let searchObject = {};
        searchObject[this._idProp] = id;
        return this.search(searchObject);
    }

    save(item) {

        if(!item[this._idProp]){
            item[this._idProp] = guid();
        }

        return this.insert(item, true);
    }

    insert(newItem, allowUpdate = false, allowMerge = false, extend = null,  eqFn = this._eqFn) {

        if (!newItem) return this.$q.reject('param is null or undefined');

        if (Array.isArray(newItem)) {

            if (newItem.length == 0){
                return this.$q.resolve();
            }

            return this.all(items => {

                var array = [];
                var inserted = [];
                newItem.forEach((v, i, a) => {

                    if (extend) {
                        Object.assign(v, extend);
                    }

                    let idx = items.findIndex(eqFn.bind(null, v));
                    if (idx === -1) {
                        items = pushToArr(items, v);
                        inserted.push[items];
                    } else if (allowUpdate) {
                        pushToArr(array, v);
                    }
                });

                return this._commit(items).then(this.update(array, false, allowMerge)).then((updateItem) => updateItem.concat(inserted));
            });

        } else if (typeof newItem === 'object') {

            if (extend) {
                Object.assign(newItem, extend);
            }

            return this.all(items => {
                let idx = items.findIndex(eqFn.bind(null, newItem));
                if (idx === -1) {
                    items = pushToArr(items, newItem);
                } else if (allowUpdate) {
                    return this.update(newItem, false, allowMerge);
                }
                return this._commit(items).then(() => [newItem]);
            });
        }
    }

    update(newItem, allowInsert = false, allowMerge = false, extend = null, eqFn = this._eqFn) {

        if (!newItem) return this.$q.reject('param is null or undefined');

        if (Array.isArray(newItem)) {

            if (newItem.length == 0) {
                return this.$q.resolve();
            }

            return this.all(items => {

                var array = [];
                var updated = [];
                newItem.forEach((v, i, a) => {

                    if (extend) {
                        Object.assign(v, extend);
                    }

                    let idx = items.findIndex(eqFn.bind(null, v));
                    if (idx === -1) {
                        if (allowInsert) {
                            pushToArr(array, v);
                        } else {
                            throw Error(`${v} not found in ${items}`);
                        }
                        return true;
                    }

                    if (allowMerge) {
                        Object.assign(items[idx], v);
                    } else {
                        items[idx] = v;
                    }

                    updated.push[items[idx]];
                });
                return this._commit(items).then(this.insert(array, false, false)).then((addItem) => addItem.concat(updated));
            });

        } else if (typeof newItem === 'object') {

            if (extend) {
                Object.assign(newItem, extend);
            }

            return this.all(items => {
                let idx = items.findIndex(eqFn.bind(null, newItem));
                if (idx === -1) {
                    if (allowInsert) {
                        return this.insert(newItem, false, false);
                    } else {
                        throw Error(`${newItem} not found in ${items}`);
                    }
                }

                if (allowMerge) {
                    Object.assign(items[idx], newItem);
                } else {
                    items[idx] = newItem;
                }

                return this._commit(items).then(() => [items[idx]]);
            });
        }
    }

    /**
     * Returns a promise to remove (DELETE) an item.
     *  _id 혹은 eqFn 으로 찾은 데이터를 삭제한다.
     **/
    remove(item, eqFn = this._eqFn) {
        return this.all(items => {
            let idx = items.findIndex(eqFn.bind(null, item));
            if (idx === -1) throw Error(`${item} not found in ${this}`);
            items.splice(idx, 1);
            return this._commit(items).then(() => item);
        });
    }

    /**
     * 스토리지에서 키에 해당하는 모든 데이터를 삭제한다.
     **/
    removeAll() {
        this.storage.removeItem(this.storageKey);
    }
}
Storage.$inject = ['$http', '$timeout', '$q', 'storage', 'storageKey', 'sourceUrl'];
